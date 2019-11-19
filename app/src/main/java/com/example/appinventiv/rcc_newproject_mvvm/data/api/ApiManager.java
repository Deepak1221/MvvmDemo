package com.example.appinventiv.rcc_newproject_mvvm.data.api;


import android.util.Log;


import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.refreshtoken.RefreshTokenResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;
import com.example.appinventiv.rcc_newproject_mvvm.util.CustomAuthenticator;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by appinventiv on 27/3/18.
 */

public class ApiManager {

    private static final ApiManager instance = new ApiManager();
    private ApiInterface apiClient, authenticatedApiClient;
    private OkHttpClient.Builder httpClient;

    private ApiManager() {
        apiClient = getRetrofitService();
        httpClient = getHttpClient();
        authenticatedApiClient = getAuthenticatedRetrofitService();
    }

    public static ApiManager getInstance() {
        return instance;
    }

    private static ApiInterface getRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiInterface.ENDPOINT)
                .build();

        return retrofit.create(ApiInterface.class);
    }

    private ApiInterface getAuthenticatedRetrofitService() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ApiInterface.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        return retrofit.create(ApiInterface.class);
    }

    /**
     * Method to create {@link OkHttpClient} builder by adding required headers in the {@link Request}
     *
     * @return OkHttpClient object
     */
    private OkHttpClient.Builder getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder;
                        requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " +
                                        DataManager.getInstance().getAccessToken())
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        Response response = chain.proceed(request);
                        Log.e("Response =", response.message());
                        return response;
                    }
                })
                .authenticator(new CustomAuthenticator())
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(20000, TimeUnit.MILLISECONDS);
    }


    public Call<UserResponse> hitLoginApi(User user) {
        return apiClient.login(user);
    }

    public Call<RefreshTokenResponse> refreshToken(HashMap<String, String> params) {
        return authenticatedApiClient.refreshToken(params);
    }

    public Call<UserResponse> hitSignUpApi(User user) {
        return apiClient.signUp(user);
    }

    public Call<CommonResponse> hitForgotPasswordApi(String email) {
        return authenticatedApiClient.forgotPassword(email);
    }

    public Call<CommonResponse> hitResetPasswordApi(Reset reset) {
        return authenticatedApiClient.resetPassword(reset);
    }

    public Call<CommonResponse> hitLogOutPassword() {
        return authenticatedApiClient.logOut();
    }

    public Call<CommonResponse> hitChangePasswordApi(User user) {
        return authenticatedApiClient.changePassword(user);
    }
}

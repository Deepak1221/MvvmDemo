package com.example.appinventiv.rcc_newproject_mvvm.data.api;




import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.refreshtoken.RefreshTokenResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by appinventiv on 27/3/18.
 */

public interface ApiInterface {

    String ENDPOINT = "http://rccapi.appinventive.com/";


    @POST("login")
    Call<UserResponse> login(@Body User user);


    @POST("signup")
    Call<UserResponse> signUp(@Body User user);

    @POST("change-password")
    Call<CommonResponse> changePassword(@Body User user);

    @POST("reset-password")
    Call<CommonResponse> resetPassword(@Body Reset reset);

    @FormUrlEncoded
    @POST("refresh")
    Call<RefreshTokenResponse> refreshToken(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("forget-password")
    Call<CommonResponse> forgotPassword(@Field("email") String email);

    @PUT("logout")
    Call<CommonResponse> logOut();
}

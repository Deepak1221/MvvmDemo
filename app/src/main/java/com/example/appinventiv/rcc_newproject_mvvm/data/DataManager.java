package com.example.appinventiv.rcc_newproject_mvvm.data;

import android.content.Context;

import com.example.appinventiv.rcc_newproject_mvvm.BaseMVVMSample;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.data.api.ApiManager;
import com.example.appinventiv.rcc_newproject_mvvm.data.preferences.PreferenceManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;
import com.google.gson.Gson;

import retrofit2.Call;

public class DataManager {

    private static DataManager instance;
    private ApiManager apiManager;
    private PreferenceManager mPrefManager;


    private DataManager(Context context) {
        //Initializing SharedPreference object
        mPrefManager = PreferenceManager.getInstance(context);


    }

    /**
     * Returns the single instance of {@link DataManager} if
     * {@link #init(Context)} is called first
     *
     * @return instance
     */
    public static DataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call init() before getInstance()");
        }
        return instance;
    }

    /**
     * Method used to create an instance of {@link DataManager}
     *
     * @param context of the application passed from the {@link BaseMVVMSample}
     * @return instance if it is null
     */
    public synchronized static DataManager init(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    /**
     * Method to initialize {@link ApiManager} class
     */
    public void initApiManager() {
        apiManager = ApiManager.getInstance();
    }


    public Call<UserResponse> hitLoginApi(User user) {
        return apiManager.hitLoginApi(user);
    }

    public String getRefreshToken() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.REFRESH_TOKEN);
    }

    public String getUserName() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.USER_NAME);
    }

    public void saveAccessToken(String accessToken) {
        mPrefManager.putString(AppConstants.PreferenceConstants.ACCESS_TOKEN, accessToken);
    }

    public void saveRefreshToken(String refreshToken) {
        mPrefManager.putString(AppConstants.PreferenceConstants.REFRESH_TOKEN, refreshToken);
    }

    public String getAccessToken() {
        return mPrefManager.getString(AppConstants.PreferenceConstants.ACCESS_TOKEN);
    }

    public Call<UserResponse> hitSignUpApi(User user) {
        return apiManager.hitSignUpApi(user);
    }

    public void saveUserDetails(User user) {
        //save user name differently
        mPrefManager.putString(AppConstants.PreferenceConstants.USER_NAME, user.getFirstName());
        String userDetail = new Gson().toJson(user);
        mPrefManager.putString(AppConstants.PreferenceConstants.USER_DETAILS, userDetail);
    }

    public Call<CommonResponse> hitForgotPasswordApi(String email) {
        return apiManager.hitForgotPasswordApi(email);
    }

    public Call<CommonResponse> hitResetPasswordApi(Reset reset) {
        return apiManager.hitResetPasswordApi(reset);
    }

    public Call<CommonResponse> hitLogOutApi() {
        return apiManager.hitLogOutPassword();
    }

    public void clearPreferences() {
        mPrefManager.clearAllPrefs();
    }

    public Call<CommonResponse> hitChangePasswordApi(User user) {
        return apiManager.hitChangePasswordApi(user);
    }
}

package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.login;

import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;

public class LoginRepo {

    public void hitLoginApi(final RichMediatorLiveData<UserResponse> liveData, User user) {
        DataManager.getInstance().hitLoginApi(user).enqueue(new NetworkCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                saveUserToPreference(userResponse.getRESULT());
                liveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                liveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                liveData.setError(t);
            }
        });

    }

    private void saveUserToPreference(User user) {
        if (user != null) {
            DataManager.getInstance().saveAccessToken(user.getAccesstoken());
            DataManager.getInstance().saveRefreshToken(user.getRefreshToken());
            DataManager.getInstance().saveUserDetails(user);
        }
    }

    public void saveDeviceToken(String deviceToken) {
        //save device token to shared preference using data manager

    }
}

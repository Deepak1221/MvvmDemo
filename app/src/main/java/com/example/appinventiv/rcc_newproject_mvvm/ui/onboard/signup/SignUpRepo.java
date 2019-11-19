package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.signup;

import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;

public class SignUpRepo {

    public void hitSignUpApi(final RichMediatorLiveData<UserResponse> signUpLiveData, User user) {
        DataManager.getInstance().hitSignUpApi(user).enqueue(new NetworkCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                //save data in preference
                saveUserToPreference(userResponse.getRESULT());
                signUpLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                signUpLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                signUpLiveData.setError(t);
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
}

package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.forgotpassword;


import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;

public class ForgotPasswordRepo {

    public void hitForgotPassword(final RichMediatorLiveData<CommonResponse> forgotLiveData, String email) {
        DataManager.getInstance().hitForgotPasswordApi(email).enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                forgotLiveData.setValue(successResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                forgotLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                forgotLiveData.setError(t);
            }
        });
    }
}

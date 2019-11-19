package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.reset;


import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;

public class ResetPasswordRepo {


    public void resetPassword(final RichMediatorLiveData<CommonResponse> resetPasswordLiveData, Reset reset) {
        DataManager.getInstance().hitResetPasswordApi(reset).enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                resetPasswordLiveData.setValue(successResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                resetPasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                resetPasswordLiveData.setError(t);
            }
        });

    }
}

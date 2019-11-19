package com.example.appinventiv.rcc_newproject_mvvm.ui.home.changepassword;


import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;

public class ChangePasswordRepo {

    /**
     * This method is used to hit the change password api
     *
     * @param changePasswordLiveData live data object
     * @param user                   contains all the request params
     */
    public void changePassword(final RichMediatorLiveData<CommonResponse> changePasswordLiveData, User user) {
        DataManager.getInstance().hitChangePasswordApi(user).enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                changePasswordLiveData.setValue(successResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                changePasswordLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                changePasswordLiveData.setError(t);
            }
        });
    }
}

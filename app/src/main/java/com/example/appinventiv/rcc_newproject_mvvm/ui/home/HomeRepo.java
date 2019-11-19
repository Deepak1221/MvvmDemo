package com.example.appinventiv.rcc_newproject_mvvm.ui.home;

import com.example.appinventiv.rcc_newproject_mvvm.base.NetworkCallback;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;

public class HomeRepo {

    /**
     * This method is used to hit log out api
     *
     * @param logOutLiveData live data object
     */
    public void userLogOut(final RichMediatorLiveData<CommonResponse> logOutLiveData) {
        DataManager.getInstance().hitLogOutApi().enqueue(new NetworkCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse successResponse) {
                if (successResponse != null) {
                    logOutLiveData.setValue(successResponse);
                    DataManager.getInstance().clearPreferences();
                }
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                logOutLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                logOutLiveData.setError(t);
            }
        });

    }
}

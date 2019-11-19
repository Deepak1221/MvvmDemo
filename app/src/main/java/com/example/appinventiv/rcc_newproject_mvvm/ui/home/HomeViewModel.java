package com.example.appinventiv.rcc_newproject_mvvm.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;

public class HomeViewModel extends ViewModel {

    private Observer<Throwable> mErrorObserver;

    private Observer<FailureResponse> mFailureObserver;

    private RichMediatorLiveData<CommonResponse> mLogOutLiveData;

    //Initializing repository class
    private HomeRepo mHomeRepo = new HomeRepo();

    //saving error & failure observers instance
    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();
    }

    /**
     * Method is used to initialize live data objects
     */
    private void initLiveData() {
        if (mLogOutLiveData == null) {
            mLogOutLiveData = new RichMediatorLiveData<CommonResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
    }

    /**
     * This method gives the log out live data object to {@link HomeFragment}
     *
     * @return {@link #mLogOutLiveData}
     */
    public RichMediatorLiveData<CommonResponse> getLogOutLiveData() {
        return mLogOutLiveData;
    }

    /**
     * This method is used on the click of log out button
     */
    public void loginButtonClicked() {
        mHomeRepo.userLogOut(mLogOutLiveData);
    }
}

package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;
import com.example.appinventiv.rcc_newproject_mvvm.util.ResourceUtils;

public class LoginViewModel extends ViewModel {

    private RichMediatorLiveData<UserResponse> mLoginLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;


    private LoginRepo mLoginRepo = new LoginRepo();


    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mLoginLiveData == null) {
            mLoginLiveData = new RichMediatorLiveData<UserResponse>() {
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

        if (mValidateLiveData == null) {
            mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the login live data object to {@link LoginFragment}
     *
     * @return {@link #mLoginLiveData}
     */
    public RichMediatorLiveData<UserResponse> getLoginLiveData() {
        return mLoginLiveData;
    }

    /**
     * Method used to hit login api after checking validations
     *
     * @param user contains all the params of the request
     */
    public void loginButtonClicked(User user) {
        if (checkValidation(user)) {
//            showProgress
            mLoginRepo.hitLoginApi(mLoginLiveData, user);
        }
    }

    /**
     * Method to check validation
     *
     * @param user
     * @return
     */
    private boolean checkValidation(User user) {
        if (user.getEmail().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_email)));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_EMAIL, ResourceUtils.getInstance().getString(R.string.enter_valid_email)
            ));
            return false;
        } else if (user.getPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_password)
            ));
            return false;
        } else if (user.getPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance().getString(R.string.enter_valid_password)
            ));
            return false;
        }
        return true;
    }

    /**
     * This method gives the validation live data object to {@link LoginFragment}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

    public void saveDeviceToken(String deviceToken) {
        mLoginRepo.saveDeviceToken(deviceToken);
    }
}

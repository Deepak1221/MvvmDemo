package com.example.appinventiv.rcc_newproject_mvvm.ui.home.changepassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.util.ResourceUtils;

public class ChangePasswordViewModel extends ViewModel {


    private Observer<FailureResponse> mFailureObserver;

    private Observer<Throwable> mErrorObserver;

    private RichMediatorLiveData<CommonResponse> mChangePasswordLiveData;

    private MutableLiveData<FailureResponse> mValidateLiveData;

    //initializing repository class
    private ChangePasswordRepo mChangePasswordRepo = new ChangePasswordRepo();


    //set error observer & failure observer instances
    public void setGenericListeners(Observer<Throwable> errorObserver, Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();
    }

    /**
     * Initializing all the live data objects
     */
    private void initLiveData() {
        if (mChangePasswordLiveData == null) {
            mChangePasswordLiveData = new RichMediatorLiveData<CommonResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };

            if (mValidateLiveData == null)
                mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the live data object to {@link ChangePasswordFragment}
     *
     * @return {@link #mChangePasswordLiveData}
     */
    public RichMediatorLiveData<CommonResponse> getChangePasswordLiveData() {
        return mChangePasswordLiveData;
    }


    /**
     * This method is used on the submit button action
     *
     * @param user contains all the params of the api request
     */
    public void onSubmitClicked(User user) {
        if (validate(user)) {
            mChangePasswordRepo.changePassword(mChangePasswordLiveData, user);
        }
    }

    /**
     * This method is used to check validation
     *
     * @return false if any validation fails otherwise true
     */
    private boolean validate(User user) {
        if (user.getOldPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OLD_PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.please_enter_old_pass)
            ));
            return false;
        } else if (user.getOldPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;
        } else if (user.getPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_password)
            ));
            return false;
        } else if (user.getPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;
        } else if (user.getConfirmPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.CONFIRM_PASSWORD_EMPTY, ResourceUtils.getInstance()
                    .getString(R.string.confirm_password_empty)
            ));
            return false;
        } else if (user.getConfirmPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;

        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_NOT_MATCHED, ResourceUtils.getInstance()
                    .getString(R.string.password_not_matched)
            ));
            return false;
        }
        return true;
    }

    /**
     * This method gives the validation live data to {@link ChangePasswordFragment}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidateLiveData() {
        return mValidateLiveData;
    }
}

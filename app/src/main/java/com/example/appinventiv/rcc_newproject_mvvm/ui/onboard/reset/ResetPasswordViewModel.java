package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.reset;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.util.ResourceUtils;

public class ResetPasswordViewModel extends ViewModel {

    private RichMediatorLiveData<CommonResponse> mResetPasswordLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;

    private ResetPasswordRepo mResetPasswordRepo = new ResetPasswordRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureResponseObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureResponseObserver;

        initLiveData();

    }

    private void initLiveData() {
        if (mResetPasswordLiveData == null) {
            mResetPasswordLiveData = new RichMediatorLiveData<CommonResponse>() {
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

        if (mValidateLiveData == null)
            mValidateLiveData = new MutableLiveData<>();
    }

    public RichMediatorLiveData<CommonResponse> getmResetPasswordLiveData() {
        return mResetPasswordLiveData;
    }

    /**
     * This method is used to check the validations and pass the data to the
     * {@link ResetPasswordRepo} to get the response
     *
     * @param reset contains the params of the request
     */
    public void onSubmitClicked(Reset reset) {
        if (checkValidation(reset)) {
            mResetPasswordRepo.resetPassword(mResetPasswordLiveData, reset);
        }
    }

    /**
     * This method is used to check the validations
     *
     * @return false if any validation fails otherwise true
     */
    private boolean checkValidation(Reset reset) {
        if (reset.getPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NEW_PASSWORD_EMPTY, ResourceUtils.getInstance()
                    .getString(R.string.new_password_empty)
            ));
            return false;
        } else if (reset.getPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;
        } else if (reset.getConfirmPassword().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.CONFIRM_PASSWORD_EMPTY, ResourceUtils.getInstance()
                    .getString(R.string.confirm_password_empty)
            ));
            return false;
        } else if (reset.getConfirmPassword().length() < 6) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance()
                    .getString(R.string.enter_valid_password)
            ));
            return false;

        } else if (!reset.getPassword().equals(reset.getConfirmPassword())) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_NOT_MATCHED, ResourceUtils.getInstance()
                    .getString(R.string.password_not_matched)
            ));
            return false;
        }
        return true;
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;

    }
}

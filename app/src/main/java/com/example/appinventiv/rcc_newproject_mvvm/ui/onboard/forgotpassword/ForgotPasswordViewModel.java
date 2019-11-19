package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.forgotpassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.RichMediatorLiveData;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.util.ResourceUtils;

public class ForgotPasswordViewModel extends ViewModel {

    private RichMediatorLiveData<CommonResponse> forgotLiveData;
    private Observer<FailureResponse> failureObserver;
    private Observer<Throwable> errorObserver;
    private MutableLiveData<FailureResponse> validateLiveData;

    private ForgotPasswordRepo forgotPasswordRepo = new ForgotPasswordRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.errorObserver = errorObserver;
        this.failureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (forgotLiveData == null) {
            forgotLiveData = new RichMediatorLiveData<CommonResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };

            if (validateLiveData == null)
                validateLiveData = new MutableLiveData<>();
        }
    }


    public RichMediatorLiveData<CommonResponse> getForgotPasswordLiveData() {
        return forgotLiveData;
    }


    public void onSubmitClicked(String email) {
        if (checkValidation(email)) {
            forgotPasswordRepo.hitForgotPassword(forgotLiveData, email);
        }

    }


    private boolean checkValidation(String email) {
        if (email.isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_email)
            ));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_EMAIL, ResourceUtils.getInstance().getString(R.string.enter_valid_email)
            ));
            return false;
        }
        return true;
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return validateLiveData;
    }
}

package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.signup;

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

public class SignUpViewModel extends ViewModel {

    private RichMediatorLiveData<UserResponse> signUpLiveData;
    private Observer<FailureResponse> failureResponseObserver;
    private Observer<Throwable> errorObserver;
    private MutableLiveData<FailureResponse> validateLiveData;

    private SignUpRepo signUpRepo = new SignUpRepo();


    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.errorObserver = errorObserver;
        this.failureResponseObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (signUpLiveData == null) {
            signUpLiveData = new RichMediatorLiveData<UserResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return failureResponseObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return errorObserver;
                }
            };
        }

        if (validateLiveData == null)
            validateLiveData = new MutableLiveData<>();
    }


    public RichMediatorLiveData<UserResponse> getSignUpLiveData() {
        return signUpLiveData;
    }

    /**
     * Method used to hit sign up api after checking validations
     *
     * @param user contains all the params of the request
     */
    public void userSignUp(User user) {
        if (checkValidation(user)) {
            signUpRepo.hitSignUpApi(signUpLiveData, user);
        }
    }

    private boolean checkValidation(User user) {
        if (user.getFirstName().isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NAME_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_name)
            ));
            return false;
        } else if (!user.getFirstName().matches("[a-z A-Z]*")) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_NAME, ResourceUtils.getInstance().getString(R.string.enter_valid_name)
            ));
            return false;
        } else if (user.getEmail().isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.EMAIL_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_email)
            ));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_EMAIL, ResourceUtils.getInstance().getString(R.string.enter_valid_email)
            ));
            return false;
        } else if (user.getPassword().isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PASSWORD_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_password)
            ));
            return false;
        } else if (user.getPassword().length() < 6) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PASSWORD, ResourceUtils.getInstance().getString(R.string.enter_valid_password)
            ));
            return false;
        } else if (user.getPhone().isEmpty()) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.PHONE_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_phone)
            ));
            return false;
        } else if (user.getPhone().length() < 10) {
            validateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_PHONE, ResourceUtils.getInstance().getString(R.string.enter_valid_phone)
            ));
        }
        return true;
    }

    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return validateLiveData;
    }
}

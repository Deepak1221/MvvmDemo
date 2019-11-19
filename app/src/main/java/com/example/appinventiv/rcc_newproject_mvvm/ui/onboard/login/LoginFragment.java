package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.login;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseFragment;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_sign_up)
    Button btSignUp;
    Unbinder unbinder;

    /**
     * A {@link ILoginHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private ILoginHost mLoginHost;

    /**
     * A {@link LoginViewModel} object to handle all the actions and business logic of login
     */
    private LoginViewModel mLoginViewModel;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginHost) {
            mLoginHost = (ILoginHost) context;
        } else
            throw new IllegalStateException("host must implement ILoginHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //check for device token from repo, if it is not present then ask for token
        //getDeviceToken();

        //observing login live data
        mLoginViewModel.getLoginLiveData().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(@Nullable UserResponse userResponse) {
                if (userResponse != null) {
                    hideProgressDialog();
                    showToastLong(getString(R.string.login_success));
                    mLoginHost.steerToHomeActivity();
                }
            }
        });

        //observing validation live data
        mLoginViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
                //You can also handle validations differently on the basis of the codes here
                /*switch (failureResponse.getErrorCode()){
                    case AppConstants.UIVALIDATIONS.EMAIL_EMPTY:
                        showToastLong(failureResponse.getErrorMessage());
                        break;
                }*/
            }
        });


    }

    /**
     * This method is used to get device token from fire base
     */
    private void getDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                mLoginViewModel.saveDeviceToken(deviceToken);
            }
        });
    }

    @Override
    protected void onErrorOccurred(Throwable throwable) {
        super.onErrorOccurred(throwable);

    }

    @OnClick({R.id.bt_login, R.id.bt_sign_up, R.id.tv_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                showProgressDialog();
                mLoginViewModel.loginButtonClicked(new User(etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim(), getDeviceId(), "1", "12345"));
                break;
            case R.id.bt_sign_up:
                mLoginHost.showSignUpFragment();
                break;
            case R.id.tv_forgot_password:
                mLoginHost.showForgotPasswordFragment();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface ILoginHost {
        void showSignUpFragment();

        void steerToHomeActivity();

        void showForgotPasswordFragment();
    }
}

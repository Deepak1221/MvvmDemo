package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.signup;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseFragment;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.User;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.UserResponse;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class SignUpFragment extends BaseFragment {


    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.et_fname)
    EditText etFname;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;
    Unbinder unbinder;


    /**
     * A {@link SignUpViewModel} object to handle all the actions and business logic of sign up
     */
    private SignUpViewModel mSignUpViewModel;

    /**
     * A {@link ISignUpHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private ISignUpHost mSignUpHost;

    /**
     * This method is used to get the instance of this fragment
     *
     * @return new instance of {@link SignUpFragment}
     */
    public static SignUpFragment getInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISignUpHost) {
            mSignUpHost = (ISignUpHost) context;
        } else
            throw new IllegalStateException("Host must implement ISignUpHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSignUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        mSignUpViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mSignUpViewModel.getSignUpLiveData().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(@Nullable UserResponse userResponse) {
                hideProgressDialog();
                //on success
                mSignUpHost.steerToHomeActivity();
            }
        });
        mSignUpViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    @OnClick(R.id.bt_signup)
    public void onViewClicked() {
        showProgressDialog();
        mSignUpViewModel.userSignUp(new User(etFname.getText().toString().trim(),
                etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), etPhone.getText().toString(),
                getDeviceId(), "12345", "1"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface ISignUpHost {

        void steerToHomeActivity();
    }
}

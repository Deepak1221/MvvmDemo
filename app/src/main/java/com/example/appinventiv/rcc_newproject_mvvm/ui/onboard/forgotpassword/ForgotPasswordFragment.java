package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.forgotpassword;


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

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseFragment;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class ForgotPasswordFragment extends BaseFragment {


    @BindView(R.id.et_email)
    EditText etEmail;
    Unbinder unbinder;

    /**
     * A {@link ForgotPasswordViewModel} object to handle all the actions and business logic
     */
    private ForgotPasswordViewModel mForgotPasswordViewModel;

    /**
     * A {@link IForgotPasswordHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private IForgotPasswordHost mForgotPasswordHost;

    /**
     * This method returns the instance of this fragment
     *
     * @return instance of {@link ForgotPasswordFragment}
     */
    public static ForgotPasswordFragment getInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IForgotPasswordHost) {
            mForgotPasswordHost = (IForgotPasswordHost) context;
        } else
            throw new IllegalStateException("host must implement ILoginHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mForgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
        mForgotPasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mForgotPasswordViewModel.getForgotPasswordLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse successResponse) {
                if (successResponse != null) {
                    hideProgressDialog();
                    showToastLong(getString(R.string.reset_password_link_sent));
                    mForgotPasswordHost.navigateToLoginFragment();
                }
            }
        });
        mForgotPasswordViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked() {
        showProgressDialog();
        mForgotPasswordViewModel.onSubmitClicked(etEmail.getText().toString().trim());
    }


    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface IForgotPasswordHost {

        void navigateToLoginFragment();
    }
}

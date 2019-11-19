package com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.reset;


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
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.model.FailureResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;
import com.example.appinventiv.rcc_newproject_mvvm.model.request.Reset;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class ResetPasswordFragment extends BaseFragment {


    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_confirm)
    EditText etConfirm;
    Unbinder unbinder;


    /**
     * A {@link IResetPasswordHost} object to interact with the host{@link OnBoardActivity}
     * if any action has to be performed from the host.
     */
    private IResetPasswordHost mResetPasswordHost;

    /**
     * A {@link ResetPasswordViewModel} object to handle all the actions and business logic of reset password
     */
    private ResetPasswordViewModel mResetPasswordViewModel;

    /**
     * This id is coming from deep linking url to reset the user's password.We will save it to send it
     * in api call
     */
    private String mUserId;

    /**
     * This method gives the instance of this fragment
     *
     * @param userId coming from the host {@link OnBoardActivity}
     * @return new instance of {@link ResetPasswordFragment}
     */
    public static ResetPasswordFragment getInstance(String userId) {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TOKEN, userId);
        resetPasswordFragment.setArguments(bundle);
        return resetPasswordFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IResetPasswordHost) {
            mResetPasswordHost = (IResetPasswordHost) context;
        } else
            throw new IllegalStateException("Host must implement IResetPasswordHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getting user id from the arguments
        if (getArguments() != null) {
            mUserId = getArguments().getString(AppConstants.TOKEN);
        }
        //initializing view model & setting listeners
        mResetPasswordViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel.class);
        mResetPasswordViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mResetPasswordViewModel.getmResetPasswordLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse successResponse) {
                //password is reset successfully
                if (successResponse != null) {
                    hideProgressDialog();
                    showToastLong(getString(R.string.password_reset_success));
                    mResetPasswordHost.navigateToLoginFragment();
                }
            }
        });
        mResetPasswordViewModel.getValidationLiveData().observe(this, new Observer<FailureResponse>() {
            @Override
            public void onChanged(@Nullable FailureResponse failureResponse) {
                hideProgressDialog();
                if (failureResponse != null)
                    showToastLong(failureResponse.getErrorMessage());
            }
        });
    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked() {
        showProgressDialog();
        Reset reset = new Reset(mUserId, etNew.getText().toString().trim(), etConfirm.getText().toString().trim());
        mResetPasswordViewModel.onSubmitClicked(reset);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface is used to interact with the host {@link OnBoardActivity}
     */
    public interface IResetPasswordHost {

        void navigateToLoginFragment();
    }
}

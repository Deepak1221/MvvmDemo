package com.example.appinventiv.rcc_newproject_mvvm.ui.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseFragment;
import com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse.CommonResponse;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    Unbinder unbinder;
    /**
     * A {@link HomeViewModel} object to handle all the actions and business logic
     */
    private HomeViewModel mHomeViewModel;

    /**
     * A {@link IHomeHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     */
    private IHomeHost mHomeHost;

    /**
     * This method is used to return the instance of this fragment
     *
     * @return new instance of {@link HomeFragment}
     */
    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IHomeHost) {
            mHomeHost = (IHomeHost) context;
        } else
            throw new IllegalStateException("Host must implement IHomeHost");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initializing view model
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        //observing log out live data
        mHomeViewModel.getLogOutLiveData().observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(@Nullable CommonResponse successResponse) {
                hideProgressDialog();
                if (successResponse != null) {
                    //host send to login
                    showToastLong(getString(R.string.log_out_success));
                    mHomeHost.logOutSuccess();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_log_out, R.id.btn_change_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_log_out:
                showProgressDialog();
                mHomeViewModel.loginButtonClicked();
                break;
            case R.id.btn_change_password:
                mHomeHost.openChangePasswordFragment();
                break;
        }
    }

    /**
     * This interface is used to interact with the host {@link HomeActivity}
     */
    public interface IHomeHost {

        void openChangePasswordFragment();

        void logOutSuccess();
    }
}

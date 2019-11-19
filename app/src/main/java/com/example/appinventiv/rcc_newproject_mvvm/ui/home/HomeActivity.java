package com.example.appinventiv.rcc_newproject_mvvm.ui.home;

import android.content.Intent;
import android.os.Bundle;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseActivity;
import com.example.appinventiv.rcc_newproject_mvvm.ui.home.changepassword.ChangePasswordFragment;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;

public class HomeActivity extends BaseActivity implements HomeFragment.IHomeHost, ChangePasswordFragment.IChangePasswordHost {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addInitialFragment();
    }

    private void addInitialFragment() {
        addFragment(R.id.home_container, HomeFragment.getInstance(), HomeFragment.class.getSimpleName());
    }


    @Override
    public void openChangePasswordFragment() {
        addFragmentWithBackstack(R.id.home_container, ChangePasswordFragment.getInstance(),
                ChangePasswordFragment.class.getSimpleName());
    }

    @Override
    public void logOutSuccess() {
        showToastLong(getString(R.string.log_out_success));
        startActivity(new Intent(HomeActivity.this, OnBoardActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_home;
    }

    @Override
    public void navigateToHomeFragment() {
        getSupportFragmentManager().popBackStack();
    }
}

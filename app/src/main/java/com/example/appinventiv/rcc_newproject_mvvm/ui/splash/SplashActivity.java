package com.example.appinventiv.rcc_newproject_mvvm.ui.splash;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseActivity;
import com.example.appinventiv.rcc_newproject_mvvm.ui.home.HomeActivity;
import com.example.appinventiv.rcc_newproject_mvvm.ui.walkthrough.WalkThroughActivity;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME_OUT = 2000;
    /**
     * A {@link SplashViewModel} object to handle all the actions and business logic of splash
     */
    private SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        showSplashScreen();

    }

    private void showSplashScreen() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSplashViewModel.getAccessTokenFromPref() != null) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    protected int getResourceId() {
        return R.layout.activity_splash;
    }
}

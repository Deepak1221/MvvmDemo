package com.example.appinventiv.rcc_newproject_mvvm.ui.splash;

import android.arch.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {

    private SplashRepo mSplashRepo = new SplashRepo();


    public String getAccessTokenFromPref() {
        return mSplashRepo.getAccessToken();
    }
}

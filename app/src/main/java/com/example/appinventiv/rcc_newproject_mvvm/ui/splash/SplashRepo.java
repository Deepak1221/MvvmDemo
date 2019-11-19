package com.example.appinventiv.rcc_newproject_mvvm.ui.splash;


import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;

public class SplashRepo {


    public String getAccessToken() {
        return DataManager.getInstance().getAccessToken();
    }
}

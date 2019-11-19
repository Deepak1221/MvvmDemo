package com.example.appinventiv.rcc_newproject_mvvm;

import android.app.Application;
import android.content.Context;

import com.example.appinventiv.rcc_newproject_mvvm.data.DataManager;
import com.example.appinventiv.rcc_newproject_mvvm.util.ResourceUtils;


public class BaseMVVMSample extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ResourceUtils.init(this);
        DataManager dataManager = DataManager.init(context);
        dataManager.initApiManager();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}

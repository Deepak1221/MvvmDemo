package com.example.appinventiv.rcc_newproject_mvvm.ui.walkthrough;

import android.content.Intent;
import android.os.Bundle;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.base.BaseActivity;
import com.example.appinventiv.rcc_newproject_mvvm.ui.onboard.OnBoardActivity;

public class WalkThroughActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOnBoardActivity();
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_walk_through;
    }

    private void openOnBoardActivity() {
        startActivity(new Intent(this, OnBoardActivity.class));
        finish();
    }
}

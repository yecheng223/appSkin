package com.cy.skin;

import android.app.Application;

import com.cy.mylibrary.SkinManager;

/**
 * Created by chuyang on 2021/3/3.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}

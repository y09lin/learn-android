package com.huim_lin.learn;

import android.app.Application;

import com.huim_lin.learn.util.ToastUtil;

public class LearnApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
    }
}

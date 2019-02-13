package com.haier.demo.testflippablestackview;

import android.app.Application;

/**
 * Created by 01438511 on 2019/2/13.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static MyApplication getApp() {
        return mInstance;
    }



}

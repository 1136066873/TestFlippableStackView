package com.haier.demo.testflippablestackview.observed;

import android.util.Log;

/**
 * Created by 01438511 on 2019/1/25.
 */

public class MyBannerObserver implements BannerObserver {

    private MyBannerObserver(){}

    private static class LazyHolder{
        private static final MyBannerObserver INSTANCE = new MyBannerObserver();
    }

    public static final MyBannerObserver getInstance(){
        return LazyHolder.INSTANCE;
    }

    @Override
    public void updateUnauthorisedStatus() {
        Log.e("heguodong","被通知未被授权");
    }

    @Override
    public void updateAuthorisedStatus() {
        Log.e("heguodong","被通知已被授权");

    }
}

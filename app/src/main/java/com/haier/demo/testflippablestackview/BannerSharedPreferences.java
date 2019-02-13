package com.haier.demo.testflippablestackview;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 01438511 on 2019/2/13.
 * https://blog.csdn.net/weixin_39409141/article/details/81583243
 * https://blog.csdn.net/Zackratos/article/details/72695799
 */

public class BannerSharedPreferences {

    private SharedPreferences spManager;
    private SharedPreferences.Editor editor;
    private static String SHARED_NAME = "banner_sp";
    public static final String BANNER_VERSION = "banner_version";


    private BannerSharedPreferences(){
        spManager = MyApplication.getApp().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = spManager.edit();
    }

    private static class LazyHolder{
        private static final BannerSharedPreferences INSTANCE = new BannerSharedPreferences();
    }

    public static BannerSharedPreferences getSingleInstance(){
        return LazyHolder.INSTANCE;
    }

    public BannerSharedPreferences put(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.commit();
        return this;
    }

    public <T> T get(String key, Object defValue) {
        if (defValue instanceof String) {
            return (T) spManager.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return (T) Integer.valueOf(spManager.getInt(key, (Integer) defValue));
        } else if (defValue instanceof Boolean) {
            return (T) Boolean.valueOf(spManager.getBoolean(key, (Boolean) defValue));
        } else if (defValue instanceof Long) {
            return (T) Long.valueOf(spManager.getLong(key, (Long) defValue));
        } else if (defValue instanceof Float) {
            return (T) Float.valueOf(spManager.getFloat(key, (Float) defValue));
        }
        return null;
    }

    public String getBannerVersion() {
        return get(BANNER_VERSION, "");
    }

    public BannerSharedPreferences putBannerVersion(String versionCode) {
        return put(BANNER_VERSION, versionCode);
    }

}

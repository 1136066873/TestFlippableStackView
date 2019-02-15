package com.haier.demo.testflippablestackview;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

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
        int tempBannerVersionCode = -1;
        String targetBannerVersionCode = get(BANNER_VERSION, "");
        if (!TextUtils.isEmpty(targetBannerVersionCode)){
            return targetBannerVersionCode;
        }else {
            //查找 smartspeaker 目录下二级目录，获取文件夹名，把文件夹名字转换成数字，取出数字最大的那个文件夹
            //并作为targetBannerVersionCode,如果只有一个文件夹，则就返回这个文件夹名字。
            String targetFolderPath = BannerPathManager.getInstance().getBannerRootDirectory();
            File targetFile = new File(targetFolderPath);
            if (targetFile.exists() && targetFile.isDirectory()) {
                File[] files = targetFile.listFiles();
                for (File f : files) {
                    Log.d("fileName --", f.getName()); //打印文件名
                    String name = f.getName();
                    int tempCode = Integer.parseInt(name);//maybe crash.???
                    if (tempCode > tempBannerVersionCode){
                        tempBannerVersionCode = tempCode;
                    }
                }
                //判断tempBannerVersionCode!
                if (tempBannerVersionCode == -1){
                    //没有子文件夹,则去启动检查（以及后续下载 zip 包的流程）???
                    return null;
                }else {
                    //至少有一个文件夹 //得到tempBannerVersionCode!
                    targetBannerVersionCode = String.valueOf(tempBannerVersionCode);
                    if (targetBannerVersionCode.length() < 4){
                        String temp = "0000" + targetBannerVersionCode;
                        targetBannerVersionCode = temp.substring(temp.length()-4 ,temp.length());
                    }
                    putBannerVersion(targetBannerVersionCode);
                }
            }else {
                //连根文件夹都没有，直接去启动检查（以及后续下载 zip 包的流程）???
                return null;
            }
            return targetBannerVersionCode;
        }
    }

    public BannerSharedPreferences putBannerVersion(String versionCode) {
        return put(BANNER_VERSION, versionCode);
    }

}

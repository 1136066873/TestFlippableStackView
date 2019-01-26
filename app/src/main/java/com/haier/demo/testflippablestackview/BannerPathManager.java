package com.haier.demo.testflippablestackview;

import android.os.Environment;

/**
 * Created by 01438511 on 2019/1/26.
 */

public class BannerPathManager {

    private BannerPathManager(){}

    private static class LazyHolder{
        private static final BannerPathManager INSTANCE = new BannerPathManager();
    }

    public static final BannerPathManager getInstance(){
        return LazyHolder.INSTANCE;
    }

    public boolean isThisDeviceHasSDCard(){
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public String getDeviceRootDirectory(){
        String path ;
        if (isThisDeviceHasSDCard()){
            path = Environment.getExternalStorageDirectory().getPath() + "/";
        }else {
            path = Environment.getRootDirectory().getPath()  + "/";
        }
        return path;
    }

    public String getBannerRootDirectory(){
        return getDeviceRootDirectory() + Constant.rootFolderNameOfBanner + "/";
    }

    public String getBannerDirectory(){
        return getBannerRootDirectory() + Constant.bannerFolderName + "/" ;
    }

    public String getBannerJsonFilePath(){
        return getBannerDirectory() + Constant.bannerJaonFileName ;
    }

    public String getBannerImageDirectory(){
        return getBannerDirectory()  + Constant.bannerImageFolderName + "/" ;
    }

    public String getBannerAdvertisingResourcesDirectory(){
        return getBannerDirectory()  + Constant.bannerAdvertisingResourcesFolderName + "/" ;
    }

}

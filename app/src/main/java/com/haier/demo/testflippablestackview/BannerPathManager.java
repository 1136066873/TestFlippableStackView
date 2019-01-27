package com.haier.demo.testflippablestackview;

import android.os.Environment;

import java.io.File;

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
            path = Environment.getExternalStorageDirectory().getPath() + File.separator;
        }else {
            path = Environment.getRootDirectory().getPath()  + File.separator;
        }
        return path;
    }

    public String getBannerRootDirectory(){
        return getDeviceRootDirectory() + Constant.rootFolderNameOfBanner + File.separator;
    }

    public String getBannerDirectory(){
        return getBannerRootDirectory() + Constant.bannerFolderName + File.separator ;
    }

    public String getBannerJsonFilePath(){
        return getBannerDirectory() + Constant.bannerJaonFileName ;
    }

    public String getBannerImageDirectory(){
        return getBannerDirectory()  + Constant.bannerImageFolderName + File.separator ;
    }

    public String getBannerAdvertisingResourcesDirectory(){
        return getBannerDirectory()  + Constant.bannerAdvertisingResourcesFolderName + File.separator ;
    }

}

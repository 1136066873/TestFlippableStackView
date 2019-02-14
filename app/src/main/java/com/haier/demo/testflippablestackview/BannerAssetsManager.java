package com.haier.demo.testflippablestackview;

import java.io.File;

/**
 * Created by 01438511 on 2019/2/14.
 */

public class BannerAssetsManager {

    private BannerAssetsManager(){
    }

    private static class LazyHolder{
        private static final BannerAssetsManager INSTANCE = new BannerAssetsManager();
    }

    public static BannerAssetsManager getSingleInstance(){
        return LazyHolder.INSTANCE;
    }

    /**
     * 遍历删除文件夹
     */
    public void deleteOldAssetsFolder(String oldBannerAssetsVersionCode) {
        String targetFolderPath = BannerPathManager.getInstance().getBannerRootDirectory()
                + oldBannerAssetsVersionCode + File.separator;
        File targetFile = new File(targetFolderPath);
        if (targetFile.exists()) {
            if (targetFile.isFile()) {
                targetFile.delete();
            } else if (targetFile.isDirectory()) {
                File[] files = targetFile.listFiles();
                for (File f : files) {
                    deleteOldAssetsFolder(f); //递归删除
                    //Log.d("fileName", f.getName()); //打印文件名
                }
            }
            targetFile.delete(); //删除文件夹
        }
    }

        /**
         * 遍历删除文件夹
         */
    public void deleteOldAssetsFolder(File targetFile){
        if (targetFile.exists()) {
            if (targetFile.isFile()) {
                targetFile.delete();
            } else if (targetFile.isDirectory()) {
                File[] files = targetFile.listFiles();
                for (File f : files) {
                    deleteOldAssetsFolder(f); //递归删除
                    //Log.d("fileName", f.getName()); //打印文件名
                }
            }
            targetFile.delete();
        }
    }
}

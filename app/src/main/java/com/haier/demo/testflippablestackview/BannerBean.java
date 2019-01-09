package com.haier.demo.testflippablestackview;

import java.io.Serializable;

/**
 * Created by 01438511 on 2019/1/5.
 */

public class BannerBean implements Serializable {

    private String mCurrentBannerUrl;

    private String mCurrentBannerAdvertisingLink;

    public BannerBean(String currentBannerUrl, String currentBannerAdvertisingLink){
        mCurrentBannerUrl = currentBannerUrl;
        mCurrentBannerAdvertisingLink = currentBannerAdvertisingLink;
    }

    public String getmCurrentBannerUrl() {
        return mCurrentBannerUrl;
    }

    public String getmCurrentBannerAdvertisingLink() {
        return mCurrentBannerAdvertisingLink;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "mCurrentBannerUrl='" + mCurrentBannerUrl + '\'' +
                ", mCurrentBannerAdvertisingLink='" + mCurrentBannerAdvertisingLink + '\'' +
                '}';
    }
}

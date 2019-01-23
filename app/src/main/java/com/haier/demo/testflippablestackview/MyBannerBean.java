package com.haier.demo.testflippablestackview;

import java.util.List;

/**
 * Created by 01438511 on 2019/1/23.
 */

public class MyBannerBean {

    private List<BannerItemsBean> BannerItems;

    public List<BannerItemsBean> getBannerItems() {
        return BannerItems;
    }

    public void setBannerItems(List<BannerItemsBean> BannerItems) {
        this.BannerItems = BannerItems;
    }

    public static class BannerItemsBean {
        /**
         * BannerPicPath : pic_1_l.png
         * BannerADResourcePath : pic_1_l.png
         */

        private String BannerPicPath;
        private String BannerADResourcePath;

        public String getBannerPicPath() {
            return BannerPicPath;
        }

        public void setBannerPicPath(String BannerPicPath) {
            this.BannerPicPath = BannerPicPath;
        }

        public String getBannerADResourcePath() {
            return BannerADResourcePath;
        }

        public void setBannerADResourcePath(String BannerADResourcePath) {
            this.BannerADResourcePath = BannerADResourcePath;
        }
    }
}

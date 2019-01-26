package com.haier.demo.testflippablestackview.observed;

/**
 * Created by 01438511 on 2019/1/25.
 */

public interface BannerObserved {

    void addWatcher(BannerObserver observer);

    void removeWatcher(BannerObserver observer);

    void onNotAuthorized();

    void onAuthorized();
}

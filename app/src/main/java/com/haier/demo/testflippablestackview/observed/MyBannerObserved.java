package com.haier.demo.testflippablestackview.observed;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 01438511 on 2019/1/25.
 */

public class MyBannerObserved implements BannerObserved {

    private List<BannerObserver> list = new ArrayList<>();

    private MyBannerObserved(){}

    private static class LazyHolder{
        private static final MyBannerObserved INSTANCE = new MyBannerObserved();
    }

    public static final MyBannerObserved getInstance(){
        return LazyHolder.INSTANCE;
    }

    @Override
    public void addWatcher(BannerObserver observer) {
        if (!list.contains(observer)){
            list.add(observer);
        }
    }

    @Override
    public void removeWatcher(BannerObserver observer) {
        if (list.contains(observer)){
            list.remove(observer);
        }
    }

    @Override
    public void onNotAuthorized() {
        for (BannerObserver observer : list){
            observer.updateUnauthorisedStatus();
        }
    }

    @Override
    public void onAuthorized() {
        for (BannerObserver observer : list){
            observer.updateAuthorisedStatus();
        }
    }
}

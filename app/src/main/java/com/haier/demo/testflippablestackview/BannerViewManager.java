package com.haier.demo.testflippablestackview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haier.demo.testflippablestackview.helper.viewpagerhelper.FlippableStackView;
import com.haier.demo.testflippablestackview.helper.viewpagerhelper.StackPageTransformer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by 01438511 on 2019/2/14.
 */

public class BannerViewManager {

    private Context mContext;
    private ArrayList<Fragment> mViewPagerFragments;
    private CardFragmentAdapter mPageAdapter;

    private BannerViewManager(){
    }

    private static class LazyHolder{
        private static final BannerViewManager INSTANCE = new BannerViewManager();
    }

    public static BannerViewManager getSingleInstance(){
        return LazyHolder.INSTANCE;
    }

    public void updateBannerView(FragmentActivity activity,FlippableStackView mFlippableStack){
        mContext = activity;
        createViewPagerFragmentsBasedOnSdCardData();
        mPageAdapter = new CardFragmentAdapter(activity.getSupportFragmentManager(), mViewPagerFragments);
        mFlippableStack.initStack(2, StackPageTransformer.Orientation.VERTICAL );
        mFlippableStack.setAdapter(mPageAdapter);

    }

    private void createViewPagerFragmentsBasedOnSdCardData() {
        try {
            mViewPagerFragments = new ArrayList<>();
            Gson gson = new Gson();
            Reader reader = new FileReader(BannerPathManager.getInstance().getBannerJsonFilePath());//注意，json 文件名字与后台约定好保持不变
            MyBannerBean bannerBean = gson.fromJson(reader,MyBannerBean.class);

            //制造数据
            for (int i = 0; i < bannerBean.getBannerItems().size(); i++) {
                mViewPagerFragments.add(CardFragment.newInstance((i + 1),
                        BannerPathManager.getInstance().getBannerImageDirectory() + bannerBean.getBannerItems().get(i).getBannerPicPath(),
                        BannerPathManager.getInstance().getBannerAdvertisingResourcesDirectory() + bannerBean.getBannerItems().get(i).getBannerADResourcePath()));
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "FileNotFoundException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

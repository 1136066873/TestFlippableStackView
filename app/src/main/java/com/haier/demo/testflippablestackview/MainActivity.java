package com.haier.demo.testflippablestackview;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.haier.demo.testflippablestackview.helper.viewpagerhelper.FlippableStackView;
import com.haier.demo.testflippablestackview.helper.viewpagerhelper.StackPageTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FlippableStackView mFlippableStack;

    private CardFragmentAdapter mPageAdapter;

    private List<Fragment> mViewPagerFragments;

    private ScheduledExecutorService executor;

    private int currentPosition;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mFlippableStack.setCurrentItem(currentPosition,true);
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if ( null == executor){
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(new ViewPagerTask(), 4, 4, TimeUnit.SECONDS);
        }
    }

    class ViewPagerTask implements Runnable {
        public void run() {
            // TODO Auto-generated method stub
            currentPosition = (currentPosition + 1 )%Constant.bannerList.length;
            handler.obtainMessage().sendToTarget();//获取当前消息 发送给handler
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlippableStack = findViewById(R.id.flippable_stack_view);


        mFlippableStack.setOnPageChangeListener(listener);

/*
        //把资产目录下文件推到sd卡中,
        Util.copyDataFromAssetsToSDcard(this,"/mnt/sdcard/smartspeaker","assets.zip");

        //解压数据并删除zip包
        try {
            File file = new File("/mnt/sdcard/smartspeaker/assets.zip");
            Util.upZipFile(file,"/mnt/sdcard/smartspeaker/banner/");//后期需要遍历这个文件夹
            boolean isDeleteSuccess = file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("heguodong","Exception ->" + e.getLocalizedMessage());
        }

        //TODO:初始化数据，并返回两个list


*/



        //初始化数据
        createViewPagerFragments();

        mPageAdapter = new CardFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
        mFlippableStack.initStack(2,StackPageTransformer.Orientation.VERTICAL );
        mFlippableStack.setAdapter(mPageAdapter);



        //点击按钮的时候，去加载sd卡中的资源以及文件

        //点击图片的时候，加载对应的资源
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    private void createViewPagerFragments() {
        mViewPagerFragments = new ArrayList<>();

/*        //制造数据
        for (int i = 0; i < 10; i++) {
            BannerBean bean = new BannerBean("https://image.haier.com/cn/xbsy_37860/sybanner_27044/201812/P020181229474858160551.jpg",
                    "https://www.haier.com/cn/ehaier/");
            mViewPagerFragments.add(CardFragment.newInstance((i + 1),bean.getmCurrentBannerUrl(),bean.getmCurrentBannerAdvertisingLink()));
        }*/

        //制造数据
        for (int i = 0; i < Constant.bannerList.length; i++) {
            BannerBean bean = new BannerBean("https://image.haier.com/cn/xbsy_37860/sybanner_27044/201812/P020181229474858160551.jpg",
                    "https://www.haier.com/cn/ehaier/");
            mViewPagerFragments.add(CardFragment.newInstance((i + 1),bean.getmCurrentBannerUrl(),bean.getmCurrentBannerAdvertisingLink()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}

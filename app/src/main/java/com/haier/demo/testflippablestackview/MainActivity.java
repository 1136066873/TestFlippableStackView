package com.haier.demo.testflippablestackview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haier.demo.testflippablestackview.helper.viewpagerhelper.FlippableStackView;
import com.haier.demo.testflippablestackview.helper.viewpagerhelper.StackPageTransformer;
import com.haier.demo.testflippablestackview.observed.MyBannerObserved;
import com.haier.demo.testflippablestackview.observed.MyBannerObserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
    
    public static final String TAG = "heguodong";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mFlippableStack.setCurrentItem(currentPosition,true);
            Log.e(TAG,"----------------------" + BannerPathManager.getInstance().getDeviceRootDirectory());

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if ( null == executor){
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(new ViewPagerTask(), 3, 3, TimeUnit.SECONDS);
        }
    }

    class ViewPagerTask implements Runnable {
        public void run() {
            if ( 0 != currentPosition ){
                currentPosition = (currentPosition - 1 ) % Constant.bannerList.length;
            }else {
                currentPosition = Constant.bannerList.length;
            }
            handler.obtainMessage().sendToTarget();//获取当前消息 发送给handler
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlippableStack = findViewById(R.id.flippable_stack_view);

        //init default views.
        initDefaultViews();

        //add watcher.
        MyBannerObserved.getInstance().addWatcher(MyBannerObserver.getInstance());

        requestPermission();

        mFlippableStack.setOnPageChangeListener(listener);

    }

    private void initDefaultViews() {
        createViewPagerFragments();
        mPageAdapter = new CardFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
        mFlippableStack.initStack(2,StackPageTransformer.Orientation.VERTICAL );
        mFlippableStack.setAdapter(mPageAdapter);
    }

    private void initViewsAccordingDataInSDCard(){
        createViewPagerFragmentsBasedOnSdCardData();
        mPageAdapter = new CardFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
        mFlippableStack.initStack(2,StackPageTransformer.Orientation.VERTICAL );
        mFlippableStack.setAdapter(mPageAdapter);
    }

    private final int REQUST_PERMISSION_TAG = 1001;

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUST_PERMISSION_TAG);
        } else {
            //TODO:说明用户之前已经授权应用访问网络和访问sd卡
            initViewsAccordingDataInSDCard();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUST_PERMISSION_TAG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "用户已授权该应用访问 SD 卡", Toast.LENGTH_SHORT).show();

                    //MyBannerObserved.getInstance().onAuthorized();

                    //把资产目录下文件推到sd卡中,
                    Util.copyDataFromAssetsToSDcard(this,
                            BannerPathManager.getInstance().getBannerRootDirectory(), "banner_assets.zip",
                            new CallBackWhenCopyDataFromAssetsToSDcard() {
                                @Override
                                public void onCopyDataFailed(String msg) {
                                    Log.e(TAG,"onCopyDataFailed,msg is ->" + msg);
                                }

                                @Override
                                public void onCopyDataSuccessful() {
                                    Log.i(TAG,"onCopyDataSuccessful.");

                                }
                            });

                    //解压数据并删除zip包
                    final File file = new File(BannerPathManager.getInstance().getBannerRootDirectory() + "banner_assets.zip");
                    if (file.exists()){
                        Util.upZipFile(file, BannerPathManager.getInstance().getBannerRootDirectory(), new CallBackWhenUpZipFile() {
                            @Override
                            public void onUpZipFileFailed(String msg) {
                                Log.e(TAG,"onUpZipFileFailed,msg is ->" + msg);
                            }

                            @Override
                            public void onUpZipFileSuccessful() {
                                Log.i(TAG,"onUpZipFileSuccessful.");
                                if (file.exists()){
                                    boolean isDeleteSuccess = file.delete();
                                    if (isDeleteSuccess){
                                        Log.i(TAG, "file delete success");
                                    } else {
                                        Log.e(TAG, "file delete failed");
                                    }
                                    initViewsAccordingDataInSDCard();
                                }
                            }
                        });//这行得需要注意：这是把 banner_assets.zip 包解压在了 BannerRootDirectory 目录下

                    } else {
                        //file do not exist
                        Log.e(TAG, "file do not exist");
                    }
                } else {
                    Toast.makeText(this, "用户未授权该应用访问 SD 卡", Toast.LENGTH_SHORT).show();

//                    MyBannerObserved.getInstance().onNotAuthorized();
//                    requestPermission();
                }
            }
        }
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
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

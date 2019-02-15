package com.haier.demo.testflippablestackview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.text.TextUtils;
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

    String[] PERMISSIONS =  new String[]{Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
        mFlippableStack.setOnPageChangeListener(listener);

        //init default views.
        initDefaultViews();

        //add watcher.
        MyBannerObserved.getInstance().addWatcher(MyBannerObserver.getInstance());

        requestPermission();


/*        BannerSharedPreferences.getSingleInstance().putBannerVersion("0001");
        Object object = BannerSharedPreferences.getSingleInstance().getBannerVersion();
        Log.e("heguodong","------------------------------------" + object);*/
    }

    private void initDefaultViews() {
        createViewPagerFragments();
        mPageAdapter = new CardFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);
        mFlippableStack.initStack(2,StackPageTransformer.Orientation.VERTICAL );
        mFlippableStack.setAdapter(mPageAdapter);
    }

    private final int REQUST_PERMISSION_TAG = 1001;

    private void requestPermission() {
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this,PERMISSIONS,REQUST_PERMISSION_TAG);
        }else {
            //TODO:说明用户之前已经授权应用访问网络和访问sd卡
            //initViewsAccordingDataInSDCard();
            BannerViewManager.getSingleInstance().updateBannerView(MainActivity.this,mFlippableStack);
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
                    copyData(this);

                } else {
                    Toast.makeText(this, "用户未授权该应用访问 SD 卡", Toast.LENGTH_SHORT).show();

//                    MyBannerObserved.getInstance().onNotAuthorized();
//                    requestPermission();
                }
            }
        }
    }

    private void copyData(Context context) {
        final File jsonFile = new File(BannerPathManager.getInstance().getBannerDirectory());
        if (!jsonFile.exists()){
            //copy zip to sdcard(when in Production: begin check and download zip file)
            Util.copyDataFromAssetsToSDcard(context,
                    BannerPathManager.getInstance().getBannerRootDirectory(), "banner_assets.zip",
                    new CallBackWhenCopyDataFromAssetsToSDcard() {
                        @Override
                        public void onCopyDataFailed(String msg) {
                            //Do Nothing.
                            Log.e(TAG,"onCopyDataFailed,msg is ->" + msg);
                        }

                        @Override
                        public void onCopyDataSuccessful() {
                            Log.i(TAG,"onCopyDataSuccessful.");
                            unZipFile();
                        }
                    });
        }else {
            //TODO:说明 Zip 包在此之前已经被拷贝到 sd卡
            //load assets from sdcard
            BannerViewManager.getSingleInstance().updateBannerView(MainActivity.this,mFlippableStack);
        }

    }

    private void unZipFile() {
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
                    //发送广播出去，用来通知、关闭关闭掉其他正在加载旧资源的组件
                    //sendBroadcast(new Intent("现在已经解压文件成功，需要关闭不必要的窗口"));

                    //删除旧的banner 文件夾资源
                    String oldBannerVersionCode = BannerSharedPreferences.getSingleInstance().getBannerVersion();
                    if (!TextUtils.isEmpty(oldBannerVersionCode)){
                        BannerAssetsManager.getSingleInstance().deleteOldAssetsFolder(oldBannerVersionCode);
                    }

                    //使用新的banner 资源：解压zip 包成功即认为最新版本的banner文件夹在本地SD卡上建立了，这个地方把最新的版本信息写入到 SP 存储中
                    BannerSharedPreferences.getSingleInstance().putBannerVersion("0002");
                    String newBannerVersionCode = BannerSharedPreferences.getSingleInstance().getBannerVersion();
                    Log.e("heguodong", "BannerVersion ----------" + newBannerVersionCode);
                    if (file.exists()){
                        boolean isDeleteSuccess = file.delete();
                        if (isDeleteSuccess){
                            Log.i(TAG, "file delete success");
                        } else {
                            Log.e(TAG, "file delete failed");
                        }
                        //initViewsAccordingDataInSDCard();
                        BannerViewManager.getSingleInstance().updateBannerView(MainActivity.this,mFlippableStack);
                    }
                }
            });//这行得需要注意：这是把 banner_assets.zip 包解压在了 BannerRootDirectory 目录下

        } else {
            //file do not exist
            Log.e(TAG, "Zip file do not exist");
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

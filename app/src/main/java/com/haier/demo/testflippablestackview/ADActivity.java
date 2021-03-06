package com.haier.demo.testflippablestackview;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.haier.demo.testflippablestackview.helper.imageviewhelper.RCImageView;

/**
 * Created by 01438511 on 2019/1/10.
 */

public class ADActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String INDEX_KEY = "index_key";
    private int index ;
    private RCImageView iv_ad;
    private static final String CURRENT_BANNER_ADVERTISING_LINK = "current_banner_advertising_link";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        findViewById(R.id.iv_close_ad).setOnClickListener(this);
        iv_ad = findViewById(R.id.iv_ad);
        if (getIntent().getExtras() != null) {
            String imagePath = getIntent().getExtras().getString(CURRENT_BANNER_ADVERTISING_LINK);
            int tempIndex = getIntent().getExtras().getInt(INDEX_KEY);
            if (!TextUtils.isEmpty(imagePath)){
                iv_ad.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            }else if (0 != tempIndex){
                index = tempIndex;
                iv_ad.setImageResource(Constant.bannerLinkList[index -1]);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_close_ad:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}

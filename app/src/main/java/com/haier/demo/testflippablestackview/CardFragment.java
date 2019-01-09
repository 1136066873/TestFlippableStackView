/**
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haier.demo.testflippablestackview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.haier.demo.testflippablestackview.helper.imageviewhelper.RCImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Bartosz Lipinski
 * 28.01.15
 */
public class CardFragment extends Fragment {

    private static final String INDEX_KEY = "index_key";
    private static final String CURRENT_BANNER_URL = "current_banner_url";
    private static final String CURRENT_BANNER_ADVERTISING_LINK = "current_banner_advertising_link";

    RCImageView mMainLayout;

    public static CardFragment newInstance(int index,String CurrentBinnerUrl ,String CurrentBinnerAdvertisingLink) {
        CardFragment fragment = new CardFragment();
        Bundle bdl = new Bundle();
        bdl.putInt(INDEX_KEY, index);
        bdl.putString(CURRENT_BANNER_URL, CurrentBinnerUrl);
        bdl.putString(CURRENT_BANNER_ADVERTISING_LINK, CurrentBinnerAdvertisingLink);
        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dummy, container, false);
        ImageView cardIv = v.findViewById(R.id.card_iv);
        final Bundle bundle = getArguments();
        if (bundle != null && (!TextUtils.isEmpty(bundle.getString(CURRENT_BANNER_URL)))&&
                (!TextUtils.isEmpty(bundle.getString(CURRENT_BANNER_ADVERTISING_LINK)))) {
            Picasso.with(getActivity()).load(bundle.getString(CURRENT_BANNER_URL)).into(cardIv);
        }
        cardIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null && (!TextUtils.isEmpty(bundle.getString(CURRENT_BANNER_ADVERTISING_LINK)))){
                    Intent intent = new Intent();
                    intent.setClass(getContext(), WebViewActivity.class);
                    intent.putExtra(CURRENT_BANNER_ADVERTISING_LINK,bundle.getString(CURRENT_BANNER_ADVERTISING_LINK));
                    startActivity(intent);
                }
            }
        });
        return v;
    }
}

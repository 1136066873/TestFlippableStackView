package com.haier.demo.testflippablestackview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by 01438511 on 2019/1/9.
 */

public class CardFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private int itemPosition= PagerAdapter.POSITION_UNCHANGED;

    public CardFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return  getItemPosition();
    }

    public int getItemPosition() {
        return itemPosition;
    }
    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }
}

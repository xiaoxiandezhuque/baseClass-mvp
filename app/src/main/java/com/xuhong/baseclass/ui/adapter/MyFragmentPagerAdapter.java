package com.xuhong.baseclass.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by BHKJ on 2016/8/19.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MyFragmentPagerAdapter(List<Fragment> fragments , FragmentManager fm){
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

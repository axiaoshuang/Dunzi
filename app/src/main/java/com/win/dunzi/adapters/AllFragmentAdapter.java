package com.win.dunzi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author：WangShuang
 * Date: 2016/1/25 11:53
 * email：m15046658245_1@163.com
 */
public class AllFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> titleList;


    public AllFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        titleList = new ArrayList<>();
        titleList.add("专享");
        titleList.add("视频");
        titleList.add("纯文");
        titleList.add("纯图");
        titleList.add("最新");
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

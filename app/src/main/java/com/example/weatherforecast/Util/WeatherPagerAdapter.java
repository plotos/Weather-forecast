package com.example.weatherforecast.Util;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weatherforecast.Activity.RecommendFragment;
import com.example.weatherforecast.Activity.TodayFragment;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "WeatherPagerAdapter";

    //Tab Title 列表
    String [] tabLists={"今日","推荐"};

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Log.d(TAG, "getItem: "+position);
        switch (position){
            case 1:
                fragment=new RecommendFragment();
                return fragment;
            default:
                fragment=new TodayFragment();
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return tabLists.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabLists[position];
    }
}

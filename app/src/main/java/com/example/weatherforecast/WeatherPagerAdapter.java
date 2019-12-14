package com.example.weatherforecast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    //Tab Title 列表
    String [] tabLists={"今日","推荐"};

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment=new TodayFragment();
                return fragment;
            case 1:
                fragment=new RecommendFragment();
                return fragment;
        }
        return null;
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

package com.example.weatherforecast.Util;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weatherforecast.Activity.HistoryFragment;
import com.example.weatherforecast.Activity.MapFragment;
import com.example.weatherforecast.Activity.SettingFragment;
import com.example.weatherforecast.Activity.WeatherFragment;

public class NavigationPagerAdapter extends FragmentPagerAdapter {


    //Navigation Title列表
    private String[] listTitle={"地图","天气","历史","设置"};
    public NavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return listTitle.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment=new MapFragment();
                return fragment;
            case 1:
                fragment=new WeatherFragment();
                return fragment;
            case 2:
                fragment=new HistoryFragment();
                return fragment;
            case 3:
                fragment=new SettingFragment();
                return fragment;
        }
        return null;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle[position];
    }
}

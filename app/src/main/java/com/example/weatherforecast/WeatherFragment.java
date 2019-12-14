package com.example.weatherforecast;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    //控件声明
    private TabLayout tlWeather;
    private ViewPager vpWeather;


    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置上方Tab页面
        vpWeather=getActivity().findViewById(R.id.vpWeather);
        tlWeather=getActivity().findViewById(R.id.tlWeather);
        vpWeather.setAdapter(new WeatherPagerAdapter(getChildFragmentManager()));
        tlWeather.setupWithViewPager(vpWeather);
        //设置Tab图标
        tlWeather.getTabAt(0).setIcon(R.drawable.ic_star_black_24dp);
        tlWeather.getTabAt(1).setIcon(R.drawable.ic_star_black_24dp);

    }
}

package com.example.weatherforecast.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.weatherforecast.Service.WeatherService;
import com.example.weatherforecast.Util.NavigationPagerAdapter;
import com.example.weatherforecast.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private ViewPager vpNavigation;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //启动存储历史天气功能
        Intent intent=new Intent(this, WeatherService.class);
        startService(intent);


        //设置底部导航
        navView = findViewById(R.id.nav_view);
        vpNavigation=findViewById(R.id.vpNavigation);
        vpNavigation.setAdapter(new NavigationPagerAdapter(getSupportFragmentManager()));
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId=menuItem.getItemId();
                switch (itemId){
                    case R.id.navigation_news:
                        vpNavigation.setCurrentItem(0);
                        break;
                    case R.id.navigation_Entertainment:
                        vpNavigation.setCurrentItem(1);
                        break;
                    case R.id.navigation_Sport:
                        vpNavigation.setCurrentItem(2);
                        break;
                    case R.id.navigation_adb:
                        vpNavigation.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}

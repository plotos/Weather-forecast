package com.example.weatherforecast.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.weatherforecast.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    //常量声明
    private static String SP_NAME="com.example.weatherforecast_preferences";
    private static final String TAG = "MapFragment";

    //声明locationClient对象
    public AMapLocationClient locationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption locationOption = null;


    //地图容器声明
    private MapView mapView;
    //声明地图控制器对象
    private AMap aMap;
    //声明定位蓝点样式类
    private MyLocationStyle myLocationStyle;

    //其他控件声明
    private TextView tvMap;//显示定位信息
    private Button btnLocate;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //显示地图
        mapView=getActivity().findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
        }

        //显示定位蓝点
        myLocationStyle=new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        //当前城市显示
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
        String defcity=sharedPreferences.getString("defCity","杭州");
        String locationMessage="默认城市："+defcity;
        tvMap=getActivity().findViewById(R.id.tvMap);
        tvMap.setText(locationMessage);

        locationClient = new AMapLocationClient(getContext());
        //初始化定位参数
        locationOption = new AMapLocationClientOption();

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        locationOption.setOnceLocation(true);
        //设置返回地址
        locationOption.setNeedAddress(true);
        //设置使用模拟器
        locationOption.setMockEnable(true);

        //设置定位参数
        locationClient.setLocationOption(locationOption);
        //启动定位
        locationClient.startLocation();

        //重新定位
        btnLocate=getActivity().findViewById(R.id.btnLocate);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("isDefCity",true);
                editor.commit();
                locationClient.startLocation();
            }
        });

        //设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getAddress();//地址
                        amapLocation.getCountry();//国家信息
                        amapLocation.getProvince();//省信息
                        String city = amapLocation.getCity();//城市信息

                        //将当前城市写入SP文件
                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
                        if(sharedPreferences.getBoolean("isDefCity",true)){
                            Log.d(TAG, "onLocationChanged: "+city);
                            String locationMessage="默认城市："+city;
                            tvMap.setText(locationMessage);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("defCity",city);
                            editor.putBoolean("isDefCity",false);
                            editor.commit();
                        }



                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e(TAG,"location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

package com.example.weatherforecast.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.weatherforecast.JSON.Forecast;
import com.example.weatherforecast.Util.MyToast;
import com.example.weatherforecast.R;
import com.example.weatherforecast.JSON.WeatherData;
import com.example.weatherforecast.JSON.Yesterday;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    //常量声明
    private static String SP_NAME="com.example.weatherforecast_preferences";
    private static final String TAG = "TodayFragment";

    //修改的Toast
    private MyToast myToast=new MyToast();

    //控件声明
    private TextView tvCity;


    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //获得当前城市
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
        String defCity=sharedPreferences.getString("defCity","温州");
        tvCity=getActivity().findViewById(R.id.tvCity);
        tvCity.setText(defCity);

        //启动访问天气网站的线程
        final Thread t = new Thread(MyThread);
        t.start();

    }


    private Handler handler =new Handler(){

        //控件声明
        private ListView lvToday;
        private TextView tvTemperature;
        private TextView tvType;
        private TextView tvHint;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String str=bundle.getString("str");


            try {
                //天气数据获取
                Gson gson=new GsonBuilder()
                        .serializeNulls()
                        .create();

                WeatherData weatherData=gson.fromJson(str,new TypeToken<WeatherData>(){}.getType());

                List<Forecast> forecastList=weatherData.getData().getForecast();
                Forecast today=forecastList.get(0);
                //天气
                tvType=getActivity().findViewById(R.id.tvType);
                tvType.setText(today.getType());
                //气温
                tvTemperature=getActivity().findViewById(R.id.tvTemperature);
                tvTemperature.setText(weatherData.getData().getWendu()+"℃");
                //感冒提示
                tvHint=getActivity().findViewById(R.id.tvHint);
                tvHint.setText(weatherData.getData().getGanmao());

                //列表数据
                String[] elementList={"title","content"};
                String[] titleList={"日期","高温","低温","风向","风力"};
                String fengLi=today.getFengli().split("\\[|]")[2];
                String[] dataList={today.getDate(),today.getHigh(),today.getLow(),today.getFengxiang(),fengLi};
                List<Map<String,Object>> mapList=new ArrayList<>();
                for (int i=0;i<titleList.length;i++){
                    Map<String,Object> map=new HashMap<>();
                    map.put(elementList[0],titleList[i]);
                    map.put(elementList[1],dataList[i]);
                    mapList.add(map);
                }
                //列表数据填充
                SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),mapList,R.layout.weather_list,elementList,new int[]{R.id.tvTitle,R.id.tvContent});

                lvToday=getActivity().findViewById(R.id.lvToday);
                lvToday.setAdapter(simpleAdapter);

            } catch (Exception e) {
                myToast.sendToast(getContext(),"请输入正确的城市信息");
                e.printStackTrace();
            }


        }
    };

    private void sendHttpRequest(String strURL,okhttp3.Callback callback){
        try {
            OkHttpClient client=new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .build();
            Request request=new Request.Builder()
                    .url(strURL)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //访问天气信息网站线程
    final Runnable MyThread=new Runnable() {
        @Override
        public void run() {
            //获得当前城市
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
            String defCity=sharedPreferences.getString("defCity","温州");
            sendHttpRequest(" http://wthrcdn.etouch.cn/weather_mini?city="+defCity,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: "+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str=response.body().string();
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("str",str);
                    message.setData(bundle);
                    handler.sendMessage(message);

                }
            });
        }
    };
}

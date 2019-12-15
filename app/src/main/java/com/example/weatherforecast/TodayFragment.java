package com.example.weatherforecast;


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
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    private static final String TAG = "TodayFragment";

    //控制连接线程
    private boolean flag=false;
    //修改的Toast
    private MyToast myToast=new MyToast();



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

        //启动访问天气网站的线程
        final Thread t=new Thread(MyThread);
        t.start();


    }

    private Handler handler =new Handler(){

        //控件声明
        private ListView lvToday;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String str=bundle.getString("str");


            try {
                Gson gson=new GsonBuilder()
                        .serializeNulls()
                        .create();

                WeatherData weatherData=gson.fromJson(str,new TypeToken<WeatherData>(){}.getType());

                //列表数据获取
                Yesterday yesterday=weatherData.getData().getYesterday();
                String[] dataList={yesterday.getDate(),yesterday.getHigh(),yesterday.getLow(),yesterday.getFx(),yesterday.getFl(),yesterday.getType()};
                //列表数据填充
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,dataList);
                lvToday=getActivity().findViewById(R.id.lvToday);
                lvToday.setAdapter(adapter);

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
            while(!flag){
                sendHttpRequest(" http://wthrcdn.etouch.cn/weather_mini?city=温州",new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str=response.body().string();
                        Log.d(TAG, "onResponse: "+str);
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("str",str);
                        message.setData(bundle);
                        handler.sendMessage(message);

                    }
                });
                flag=true;
            }
        }
    };
}

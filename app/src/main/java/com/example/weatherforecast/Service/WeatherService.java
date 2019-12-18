package com.example.weatherforecast.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.example.weatherforecast.JSON.WeatherData;
import com.example.weatherforecast.JSON.Yesterday;
import com.example.weatherforecast.Receiver.WeatherReceiver;
import com.example.weatherforecast.db.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService extends Service {

    private boolean flag=false;

    private static final String TAG = "WeatherService";
    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+new Date().toString());

        //启动查询线程
        sendHttpRequest(" http://wthrcdn.etouch.cn/weather_mini?city=杭州", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                Gson gson=new GsonBuilder()
                        .serializeNulls()
                        .create();

                WeatherData weatherData=gson.fromJson(str,new TypeToken<WeatherData>(){}.getType());
                Yesterday yesterday=weatherData.getData().getYesterday();
                Weather weather=new Weather("杭州市",yesterday.getDate(),yesterday.getHigh(),yesterday.getLow(),yesterday.getFx(),yesterday.getFl(),yesterday.getType());
                if(LitePal.where("date = ? and city = ?",yesterday.getDate(),"杭州市").find(Weather.class).size() == 0){
                    weather.save();
                }
            }
        });

        sendHttpRequest(" http://wthrcdn.etouch.cn/weather_mini?city=温州", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                Gson gson=new GsonBuilder()
                        .serializeNulls()
                        .create();

                WeatherData weatherData=gson.fromJson(str,new TypeToken<WeatherData>(){}.getType());
                Yesterday yesterday=weatherData.getData().getYesterday();
                Weather weather=new Weather("温州市",yesterday.getDate(),yesterday.getHigh(),yesterday.getLow(),yesterday.getFx(),yesterday.getFl(),yesterday.getType());
                if(LitePal.where("date = ? and city = ?",yesterday.getDate(),"温州市").find(Weather.class).size() == 0){
                    weather.save();
                }
            }
        });


        //定时启动服务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime= SystemClock.elapsedRealtime()+14400000;
        Intent intent1 = new Intent(this, WeatherReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent1,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

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

}

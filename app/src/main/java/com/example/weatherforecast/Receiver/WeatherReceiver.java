package com.example.weatherforecast.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.weatherforecast.Service.WeatherService;

public class WeatherReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context, WeatherService.class);
        context.startService(intent1);
    }
}

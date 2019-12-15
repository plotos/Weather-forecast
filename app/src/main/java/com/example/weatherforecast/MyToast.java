package com.example.weatherforecast;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    private Toast toast=null;
    public void sendToast(Context context,CharSequence text){
        if(toast==null){
            toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            toast.setText(text);
            toast.show();
        }

    }
}

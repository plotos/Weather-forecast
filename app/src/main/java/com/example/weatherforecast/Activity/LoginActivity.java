package com.example.weatherforecast.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.weatherforecast.R;

public class LoginActivity extends AppCompatActivity {

    //常量声明
    private static String SP_NAME="com.example.weatherforecast_preferences";

    //控件声明
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //自动登录判断
        final SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences(SP_NAME,MODE_PRIVATE);
        if(sharedPreferences.getBoolean("autoLogin",false)){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

        //登录判断
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认密码
                String defPassword="123456";
                //用户输入密码
                String enterPassword=etPassword.getText().toString();
                //密码校验
                SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences(SP_NAME,MODE_PRIVATE);
                if(sharedPreferences.getString("password",defPassword).equals(enterPassword)){
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

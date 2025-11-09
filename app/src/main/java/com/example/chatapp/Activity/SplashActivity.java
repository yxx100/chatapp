package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.example.chatapp.R;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private static final int SPLASH_DURATION = 2000;
    //创建Handler，显式绑定主线程 Looper
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //两秒后跳转
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //判断是否登录过
                if (EMClient.getInstance().isLoggedInBefore()){
                    //跳转到已登陆
                    Log.d(TAG, "已登录跳转");
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else {
                    //跳转到未登录
                    Log.d(TAG, "未登录跳转");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        },SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null); // 移除所有回调
        super.onDestroy();
    }
}
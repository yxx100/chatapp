package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void logout(View view) {
        EMClient.getInstance().logout(true);
        User user= ChatApplication.getUser();
        com.example.chatapp.Bean.User.logOut();
        finish();
    }


    public void tonewfriend(View view) {
        startActivity(new Intent(MainActivity.this,NewFriendActivity.class));
    }

    public void toaddfriend(View view) {
        startActivity(new Intent(MainActivity.this,AddFriendActivity.class));
    }

    public void tocontact(View view) {
        startActivity(new Intent(MainActivity.this,TestActivity.class));
    }
}
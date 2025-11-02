package com.example.chatapp;

import android.app.Application;

import com.example.chatapp.Bean.User;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class ChatApplication extends Application {
    private static ChatApplication instance;
    private static User user;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Bmob.initialize(this, "73908d02b0bb7742c10dcf49d3768650");
        EMOptions options = new EMOptions();
        options.setAppKey("1152251022193156#test");
        EMClient.getInstance().init(this, options);
        user= BmobUser.getCurrentUser(User.class);
    }

    public static ChatApplication getInstance(){
        return instance;
    }

    public static void setInstance(ChatApplication instance) {
        ChatApplication.instance = instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ChatApplication.user = user;
    }
}

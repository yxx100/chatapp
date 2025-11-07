package com.example.chatapp;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatapp.Bean.User;
import com.hyphenate.EMMessageListener;
import com.hyphenate.callkit.CallKitClient;
import com.hyphenate.callkit.CallKitConfig;
import com.hyphenate.callkit.bean.CallEndReason;
import com.hyphenate.callkit.bean.CallInfo;
import com.hyphenate.callkit.bean.CallType;
import com.hyphenate.callkit.interfaces.CallKitListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.ai.ChatClient;
import io.agora.rtc2.RtcEngine;

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
        CallKitConfig config=new CallKitConfig();
        CallKitClient.INSTANCE.init(this,config);
        user= BmobUser.getCurrentUser(User.class);
        initHuanXinListener();
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
    private void initHuanXinListener() {
        //环信实时接收
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage msg:list){
                    String from= msg.getFrom();
                    String content=((EMTextMessageBody)msg.getBody()).getMessage();
                    Intent intent=new Intent("NEW_MESSAGE_RECEIEVED");
                    intent.putExtra("from",from);
                    intent.putExtra("content",content);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            }
        });
        CallKitClient.INSTANCE.setCallKitListener(new CallKitListener() {
            @Override
            public void onEndCallWithReason(@NonNull CallEndReason callEndReason, @Nullable CallInfo callInfo) {

            }

            @Override
            public void onCallError(@NonNull CallKitClient.CallErrorType callErrorType, int i, @Nullable String s) {

            }

            @Override
            public void onReceivedCall(@NonNull String s, @NonNull CallType callType, @Nullable JSONObject jsonObject) {

            }

            @Override
            public void onRemoteUserJoined(@NonNull String s, @NonNull CallType callType, @NonNull String s1) {

            }

            @Override
            public void onRemoteUserLeft(@NonNull String s, @NonNull CallType callType, @NonNull String s1) {

            }

            @Override
            public void onRtcEngineCreated(@NonNull RtcEngine rtcEngine) {

            }
        });
    }
}

package com.example.chatapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ChatAdapter;
import com.example.chatapp.Bean.Chat;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.hyphenate.callkit.CallKitClient;
import com.hyphenate.callkit.bean.CallType;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private BroadcastReceiver messagereceiever;
    private TextView textViewChatUser;
    private ImageButton imageButtonChatBack;
    private Button buttonSend;
    private ImageView imageViewmore;
    private EditText editTextChat;
    private RecyclerView recyclerView;
    private List<Chat> chats = new ArrayList<>();
    private List<BmobObject> updateChats = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private User me;
    private User friend;
    private String friendName;
    private String friendAvatar;
    private LinearLayout plus;
    private Button yuyin;
    private Button shipin;
    private boolean isVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        initView();
        setEvent();
        updateChat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messagereceiever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String from = intent.getStringExtra("from");
                String content = intent.getStringExtra("content");
                if (from.equals(friendName)) {
                    updateChat();
                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messagereceiever, new IntentFilter("NEW_MESSAGE_RECEIEVED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messagereceiever);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 彻底清理
        if (messagereceiever != null) {
            try {
                {
                    LocalBroadcastManager.getInstance(this).unregisterReceiver(messagereceiever);
                }

                if (recyclerView != null) {
                    recyclerView.setAdapter(null);
                }

                if (chats != null) {
                    chats.clear();
                }

                if (updateChats != null) {
                    updateChats.clear();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateChat() {
        int previousSize = chats.size();
        //遍历聊天记录数据库
        Log.d(TAG, "进入updateChat");
        String myName = me.getUsername();
        BmobQuery<Chat> bmobQuery = new BmobQuery<>();
        bmobQuery.include("sender,receiver");
        bmobQuery.findObjects(new FindListener<Chat>() {
            @Override
            public void done(List<Chat> list, BmobException e) {
                //chats.clear();
                if (list != null) {
                    for (Chat chat : list) {
                        if (myName.contentEquals(chat.getSender().getUsername()) && friendName.contentEquals(chat.getReceiver().getUsername())) {
                            chats.add(chat);
                            Log.d(TAG, "我" + myName);
                            Log.d(TAG, "朋友接收者" + friendName);
                            Log.d(TAG, "我是发送者的一条消息被添加");
                        }
                        if (myName.contentEquals(chat.getReceiver().getUsername()) && friendName.contentEquals(chat.getSender().getUsername())) {
                            chats.add(chat);
                            Log.d(TAG, "我" + myName);
                            Log.d(TAG, "朋友发送者" + friendName);
                            Log.d(TAG, "我是接收者的一条消息被添加");

                            if (!chat.isRead()) {
                                chat.setRead(true);
                                Chat chat1 = new Chat(chat.getSender(), chat.getReceiver(), chat.getContent(), chat.isRead(), chat.getTime());
                                chat1.setObjectId(chat.getObjectId());
                                updateChats.add(chat1);
                                Log.d(TAG, "updateChats新加一条");
                            }
                        }
                    }
                }
                Collections.sort(chats, new Comparator<Chat>() {
                    @Override
                    public int compare(Chat o1, Chat o2) {
                        return Long.compare(o1.getTime(), o2.getTime());
                    }
                });
                Log.d(TAG, "聊天记录排序完成");
                if (chats.size() != previousSize || previousSize == 0) {
                    chatAdapter.updateData(chats);
                    if (chats.size() > 0)
                        recyclerView.scrollToPosition(chats.size() - 1);
                }
                makeMessageRead();
            }
        });
    }

    private void makeMessageRead() {
        //批量更新已读状态
        if (!updateChats.isEmpty()) {
            new BmobBatch().updateBatch(updateChats).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> results, BmobException e) {
                    if (e != null) {
                        Snackbar.make(textViewChatUser, "失败：" + e.getMessage() + "," + e.getErrorCode(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setEvent() {
        //返回按钮的点击事件
        imageButtonChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //发送按钮的点击事件
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入框获取输入的内容
                String content = editTextChat.getText().toString();
                //输入为空时返回，不往下执行
                if (content.contentEquals(""))
                    return;
                //获取发送时间
                Long time = System.currentTimeMillis();
                Chat chat = new Chat(friend, me, content, true, time);
                chats.add(chat);
                Log.d(TAG, "Chats添加一条");
                //保存到数据库
                chat.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //recyclerView更新数据并滚动到最新位置
                            recyclerView.getAdapter().notifyItemInserted(chats.size() - 1);
                            recyclerView.scrollToPosition(chats.size() - 1);
                            editTextChat.setText("");
                            Log.d(TAG, "聊天记录保存成功");
                            //环信推送
                            EMMessage emMessage = EMMessage.createTextSendMessage(content, friend.getUsername());
                            EMClient.getInstance().chatManager().sendMessage(emMessage);
                            Log.d(TAG, "推送成功");
                        }
                        if (e != null) {
                            Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //更多按钮点击事件
        imageViewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisible){
                plus.setVisibility(View.VISIBLE);
                isVisible=true;}
                else {
                   plus.setVisibility(View.GONE);
                   isVisible=false;
                }

            }
        });
        //语音通话点击
        yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "点击了语音通话", Toast.LENGTH_SHORT).show();
                startVoiceCall();
            }
        });
        //视频通话点击
        shipin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "点击了视频通话", Toast.LENGTH_SHORT).show();
                startVideoCall();
            }
        });
    }

    private void startVoiceCall() {
        String targetUserId="";
        CallKitClient.INSTANCE.startSingleCall(
                CallType.SINGLE_VOICE_CALL,
                targetUserId,
                null // 可传 null
        );
    }

    private void startVideoCall() {
        String targetUserId="";
        CallKitClient.INSTANCE.startSingleCall(
                CallType.SINGLE_VIDEO_CALL,
                targetUserId,
                null // 可传 null
        );
    }

    private void initView() {
        me = ChatApplication.getUser();
        Log.d(TAG, "me" + me.getUsername());
        // 获取传递过来的 Intent
        Intent intent = getIntent();
//        if (intent.hasExtra("friend")) {
//            // 获取序列化的 User 对象
//            Serializable serializable = intent.getSerializableExtra("friend");
//            if (serializable instanceof User) {
//                friend = (User) serializable;
//                Log.d(TAG, "通过 friend 对象获取数据");
//            }
//        }
//        // 情况：没有传递任何数据（错误处理）
//        else {
//            Log.e(TAG, "错误：没有传递任何好友数据");
//            Toast.makeText(this, "无法获取聊天对象信息", Toast.LENGTH_SHORT).show();
//            finish(); // 关闭当前 Activity
//            return;
//        }
        friend=(User)intent.getSerializableExtra("friend");
        if (friend!=null){
        friendName = friend.getUsername();
        friendAvatar = friend.getAvatar();
        Log.d(TAG, "friendname:" + friendName + "avatar:" + friendAvatar);
        }else {
            Log.d(TAG,"传递的friend为空");
        }
        textViewChatUser = (TextView) findViewById(R.id.textViewChatUser);
        editTextChat = (EditText) findViewById(R.id.editTextChat);
        imageButtonChatBack = (ImageButton) findViewById(R.id.imageButtonChatBack);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        imageViewmore = (ImageView) findViewById(R.id.imageViewmore);
        plus=(LinearLayout) findViewById(R.id.plus);
        yuyin=(Button)findViewById(R.id.yuyin);
        shipin=(Button)findViewById(R.id.shipin);
        textViewChatUser.setText(friendName);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(friendName, friendAvatar, chats, this);
        recyclerView.setAdapter(chatAdapter);
    }

}
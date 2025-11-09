package com.example.chatapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Bean.Chat;
import com.example.chatapp.Bean.Message;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;
import com.example.chatapp.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    View view;
    private RecyclerView recyclerView;
    private User me;
    private List<Chat> chats = new ArrayList<>();
    private List<User> users;
    private List<Message> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        initview();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        Log.d(TAG,"开始更新数据");
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                users = new ArrayList<>(list);
                updateMessage();
            }
        });
    }
    private void updateMessage() {
        //遍历消息列表数据库
        Log.d(TAG,"进入updateMessage");
        BmobQuery<Chat> bmobQuery = new BmobQuery<>();
        bmobQuery.include("sender,receiver");
        bmobQuery.findObjects(new FindListener<Chat>() {
            @Override
            public void done(List<Chat> list, BmobException e) {
                //记录先前的message,便于分辨后续是否更新ui
                List<Message> previousMessages = new ArrayList<>(messages);
                messages.clear();
                Log.d("MessageFragment", String.valueOf(list == null));

                if (list != null) {
                    //开始遍历消息列表，分两种情况
                    for (Chat chat : list) {
                        processChatRecord(chat);
                    }
                }
                if (!compareLists(previousMessages, messages)||previousMessages.isEmpty())
                { recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(TAG,"recycle更新");}
            }
        });
    }

    private boolean compareLists(List<Message> list1, List<Message> list2) {
        if (list1.size() != list2.size())
            return false;
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getTitle().contentEquals(list2.get(i).getTitle()))
                return false;
            if (!list1.get(i).getContent().contentEquals(list2.get(i).getContent()))
                return false;
            if (list1.get(i).getPrompt() != list2.get(i).getPrompt())
                return false;
        }
        return true;
    }

    // 处理单条聊天记录
    private void processChatRecord(Chat chat){
        Log.d(TAG,"进入processChatRecord");
        boolean find = false;
        //第一种我是发最后一条消息的人
        if (chat.getSender().getUsername().contentEquals(me.getUsername())) {
            //遍历所有消息
            for (Message message : messages) {
                if (message.getTitle().contentEquals(chat.getReceiver().getUsername())) {
                    //时间大的内容会覆盖掉旧内容
                    if (chat.getTime() > message.getTime())
                        message.setContent(chat.getContent());
                    find = true;
                    break;
                }
            }
            //如果消息列表里没有就添加上
            if (!find)
            {  messages.add(new Message(chat.getReceiver().getUsername(), chat.getContent(), 0, chat.getTime(), chat.getReceiver().getAvatar()));
            Log.d(TAG,"添加了一条我是发送者");}
        } else if (chat.getReceiver().getUsername().contentEquals(me.getUsername())) {
            //第二种情况我是接收者
            for (Message message : messages) {
                //分别给每条消息设置未读数
                if (message.getTitle().contentEquals(chat.getSender().getUsername())) {
                    if (!chat.isRead()) {
                        message.increasePrompt();
                        //未读消息和当前消息列表的第零个交换，即置顶消息
                        Collections.swap(messages, messages.indexOf(message), 0);
                    }
                    if (chat.getTime() > message.getTime())
                        message.setContent(chat.getContent());
                    find = true;
                    break;
                }
            }
            if (!find) {
                Log.d(TAG,"添加了一条我是接收者");
                if (chat.isRead())
                    messages.add(new Message(chat.getSender().getUsername(), chat.getContent(), 0, chat.getTime(), chat.getSender().getAvatar()));
                else {
                    messages.add(0, new Message(chat.getSender().getUsername(), chat.getContent(), 1, chat.getTime(), chat.getSender().getAvatar()));
                }
            }
        }
    }
    private void initview() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMessage);
        me = ChatApplication.getUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messages,getContext());
        recyclerView.setAdapter(messageAdapter);
    }
}
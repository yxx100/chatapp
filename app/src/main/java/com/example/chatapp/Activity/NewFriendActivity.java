package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.NewFriendAdapter;
import com.example.chatapp.Bean.FriendRelation;
import com.example.chatapp.Bean.FriendRequest;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class NewFriendActivity extends AppCompatActivity implements NewFriendAdapter.OnFriendRequestActionListener {
    private static final String TAG = "NewFriendActivity";
    private ImageView back;
    private ImageView addnewfriend;
    private RecyclerView recyclerViewNewFriend;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private NewFriendAdapter newFriendAdapter;
    private  User  me;
    private List<FriendRequest> requestlist = new ArrayList<>();
    private List<FriendRequest> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_new_friend);
        initdata();
        initview();
        setevent();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setAdapter();
//            }
//        }, 1000);

    }

    private void setAdapter() {
        newFriendAdapter = new NewFriendAdapter(requestlist, NewFriendActivity.this);
        newFriendAdapter.setOnFriendRequestActionListener(this);
        recyclerViewNewFriend.setAdapter(newFriendAdapter);

        LinearLayoutManager linearLayout = new LinearLayoutManager(NewFriendActivity.this);
        recyclerViewNewFriend.setLayoutManager(linearLayout);
        Log.d(TAG, "设置适配器与布局");
    }

    private void setRequestlist(List<FriendRequest> list) {
        Log.d(TAG,"setRequestlist");
        for (FriendRequest req : list) {
            Log.d(TAG,"循环");
            Log.d(TAG,req.getReceiver().getUsername());
            Log.d(TAG,me.getUsername());
            if (req.getReceiver().getUsername().contentEquals(me.getUsername())) {
                requestlist.add(req);
                Log.d(TAG, "req添加了一条");
                Log.d(TAG, "请求者" + requestlist.get(0).getRequester().getUsername());
                Log.d(TAG, "接收者" + requestlist.get(0).getReceiver().getUsername());
            }
        }
        setAdapter();
    }

    private void initdata() {
        me = ChatApplication.getUser();
        BmobQuery<FriendRequest> query = new BmobQuery<>();
        Log.d(TAG, "开始查找好友请求");
        query.include("requester,receiver");
        query.findObjects(new FindListener<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> list, BmobException e) {
                Log.d(TAG, "进入done");
                if (e == null) {
                    if (list != null) {
                        Log.d(TAG, "好友请求数据表不为空");
                        temp = list;
                        Log.d(TAG, "temp添加了一条");
                        Log.d(TAG, "请求者" + temp.get(0).getRequester().getUsername());
                        Log.d(TAG, "接收者" + temp.get(0).getReceiver().getUsername());
                        setRequestlist(temp);
                    }
                } else {
                    Log.d(TAG, e.getMessage() + e.getErrorCode());
                }
            }
        });
    }


    private void setevent() {
        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //跳转到添加好友页面
        addnewfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendActivity.this, AddFriendActivity.class));
            }
        });
    }

    private void initview() {
        Log.d(TAG, "新的朋友页面视图初始化");
        back = findViewById(R.id.newfriendback);
        addnewfriend = findViewById(R.id.addnewfriend);
        recyclerViewNewFriend = findViewById(R.id.recyclerViewNewFriend);
    }

    @Override
    public void onAcceptClicked(int position, FriendRequest request) {
        BmobQuery<FriendRequest> bmobQuery = new BmobQuery<>();
        bmobQuery.include("requester,receiver");
        //删除本条请求
        FriendRequest request1 = new FriendRequest();
        Log.d(TAG, "id是" + request.getObjectId());
        request1.delete(request.getObjectId(), new UpdateListener() {
            public void done(BmobException e) {
                Log.d(TAG, "进入done");
                if (e == null) {
                    Log.d(TAG, request.getRequester().getUsername() + "的好友请求已删除");
                    //删除成功后隐藏消息
                    recyclerViewNewFriend.setVisibility(View.INVISIBLE);

                } else {
                    Log.d(TAG, "接收" + e.getErrorCode() + e.getMessage());
                    Toast.makeText(NewFriendActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                }
            }
        });

        User me = new User();
        User friend = new User();
        me = ChatApplication.getUser();
        friend = request.getRequester();
        addFriendRelation(me, friend);
        addFriendRelation(friend, me);
        Toast.makeText(this, "好友添加成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefuseClicked(int position, FriendRequest request) {
        BmobQuery<FriendRequest> bmobQuery = new BmobQuery<>();
        bmobQuery.include("requester,receiver");
        //删除本条请求
        FriendRequest request1 = new FriendRequest();
        Log.d(TAG, "id是" + request.getObjectId());
        request1.delete(request.getObjectId(), new UpdateListener() {
            public void done(BmobException e) {
                Log.d(TAG, "进入done");
                if (e == null) {
                    Log.d(TAG, request.getRequester().getUsername() + "的好友请求已删除");
                    //删除成功后隐藏消息
                    recyclerViewNewFriend.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "接收" + e.getErrorCode() + e.getMessage());
                    Toast.makeText(NewFriendActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(this, "您已拒绝好友请求", Toast.LENGTH_SHORT).show();
    }

    private void addFriendRelation(User user, User friend) {
        FriendRelation relation = new FriendRelation();
        relation.setUser(user);
        relation.setFriend(friend);
        relation.setUsername(user.getUsername());
        relation.setFriendname(friend.getUsername());
        relation.save(new SaveListener<FriendRelation>() {
            @Override
            public void done(FriendRelation friendRelation, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "成为了好友");
                }
            }
        });
    }
}
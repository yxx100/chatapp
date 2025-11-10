package com.example.chatapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Activity.NewFriendActivity;
import com.example.chatapp.Adapter.ContactAdapter;
import com.example.chatapp.Bean.FriendRelation;
import com.example.chatapp.Bean.FriendRequest;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ContactFragment extends Fragment {
    private static final String TAG = "contactFragment";

    private LinearLayout linearLayout;
    private View view;
    private List<User> users= new ArrayList<>();
    private List<User> temp = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textViewAddPrompt;
    private TextView textViewContactNumber;
    private ContactAdapter contactAdapter;
    private static User me;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = ChatApplication.getUser();
        Log.d(TAG,"me"+me.getUsername());
        //加载联系人数据
        updateContactUser();
        //更新新好友添加请求数据
        updateContactPrompt();
    }

    private void updateContactPrompt() {
        //遍历好友请求的数据表
        BmobQuery<FriendRequest> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> list, BmobException e) {
                //获取当前用户的唯一id
                String userId = ChatApplication.getUser().getObjectId();
                //遍历数据表
                if (list != null) {
                    int count = 0;
                    for (FriendRequest request : list) {
                        //如果当前账号出现在好友请求接收者列表里，消息数计数加一，即红点数
                        if (request.getReceiver().getObjectId().contentEquals(userId)) {
                            count++;
                        }
                    }
                    //设置红点数据
                    if (count == 0) {
                        textViewAddPrompt.setVisibility(View.INVISIBLE);
                    } else {
                        textViewAddPrompt.setVisibility(View.VISIBLE);
                        textViewAddPrompt.setText(count + "");
                    }
                }
            }
        });
    }

    private void updateContactUser() {
        //记录当前好友数便于更新
        int previousSize = users.size();
        Log.d(TAG,"好友数"+previousSize);
        //遍历好友关系数据表
        BmobQuery<FriendRelation> bmobQuery1 = new BmobQuery<>();
        bmobQuery1.include("user,friend");
        bmobQuery1.findObjects(new FindListener<FriendRelation>() {
            @Override
            public void done(List<FriendRelation> list, BmobException e) {
                    if (e == null) {
                        if (list != null) {
                            Log.d(TAG,"list不是零");
                            Log.d(TAG, "正在遍历数据库，寻找好友");
                            for (FriendRelation relation : list) {
                                Log.d(TAG, "进入for");
                                //数据库中的user和当前用户me匹配时
                                Log.d(TAG,relation.getUser().getObjectId());
                                Log.d(TAG,"-------------");
                                Log.d(TAG,me.getObjectId());
                                if (relation.getUser().getObjectId().contentEquals(me.getObjectId())) {
                                    //把user关联的friend添加到temp中
                                    Log.d(TAG, "找到" + relation.getFriend().getUsername());
                                    temp.add(relation.getFriend());
                                }
                            }
                            //将拿到的联系人赋给当前页面的集合
                            users = temp;
                            //排序
                            Collections.sort(users, new Comparator<User>() {
                                @Override
                                public int compare(User o1, User o2) {
                                    String s1 = o1.getUsername().toLowerCase();
                                    String s2 = o2.getUsername().toLowerCase();
                                    return s1.compareTo(s2);
                                }
                            });

                            Log.d(TAG, "好友排序已完成");
                            setAdapter();
                            //好友数有变化时更新
                            if (users.size() != previousSize) {
                                recyclerView.setAdapter(new ContactAdapter(users));
                                Log.d(TAG, "联系人列表已更新");
                            }
                            Log.d(TAG, "好友数没有变化");
                        }
                    } else {
                        Log.d(TAG, "联系人列表数据获取失败");
                        Toast.makeText(getActivity(), "加载失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                //根据得到的好友数显示在列表下方
                switch (users.size()) {
                    case 0:
                        textViewContactNumber.setText("你还没有好友 ");
                        break;
                    case 1:
                        textViewContactNumber.setText("你只有一个好友");
                        break;
                    default:
                        textViewContactNumber.setText("好友总数" + users.size());
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        initview();
        setEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initview();
        setEvent();
    }

    private void setEvent() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initview() {
        Log.d(TAG,"联系人列表初始化视图");
        linearLayout=view.findViewById(R.id.addfriend);
        recyclerView=view.findViewById(R.id.recyclerViewContact);
        textViewContactNumber=view.findViewById(R.id.textViewContactNumber);
        textViewAddPrompt=view.findViewById(R.id.textViewAddPrompt);
    }
    private void setAdapter(){
        //设置适配器
        contactAdapter=new ContactAdapter(users,getContext());
        recyclerView.setAdapter(contactAdapter);
        //设置显示方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }
}
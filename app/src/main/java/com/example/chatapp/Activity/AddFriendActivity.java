package com.example.chatapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.APPUtil;
import com.example.chatapp.Bean.FriendRelation;
import com.example.chatapp.Bean.FriendRequest;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddFriendActivity extends AppCompatActivity {
    private static final String TAG = "AddFriendActivity";
    private ImageView imageViewAddBack;
    private EditText editTextAddFriend;
    private ImageView imageViewSearch;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewSearchavatar;
    private TextView textViewSearchname;
    private Button buttonAddFriend;
    private User newFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);
        initview();
        setevent();
    }

    private void initview() {
        imageViewAddBack = findViewById(R.id.addback);
        editTextAddFriend = findViewById(R.id.et_query);
        imageViewSearch = findViewById(R.id.iv_search);
        imageViewSearchavatar = findViewById(R.id.imageViewSearchavatar);
        textViewSearchname = findViewById(R.id.textViewSearchname);
        buttonAddFriend = findViewById(R.id.buttonAddFriend);
        constraintLayout=findViewById(R.id.addlayout);
    }

    private void setevent() {
        //返回按钮点击事件
        imageViewAddBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索按钮的点击事件
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框的信息
                String username = editTextAddFriend.getText().toString().trim();

                //遍历数据库查找要搜索的用户
                BmobQuery<User> bmobQuery = new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            Boolean flag=false;
                            Log.d(TAG,"正在遍历数据库查找"+username);
                            for (User user : list) {
                                if (user.getUsername().contentEquals(username)) {
                                    flag=true;
                                    //查找到之后显示出来
                                    Log.d(TAG,"找到了用户"+username);
                                    Log.d(TAG,"开始赋值中");
                                    newFriend = user;
                                    Log.d(TAG,"newfriend已经被赋值");
                                    constraintLayout.setVisibility(View.VISIBLE);
                                    textViewSearchname.setText(username);
                                    APPUtil util=new APPUtil(AddFriendActivity.this);
                                    util.loadImage(imageViewSearch,user.getAvatar());
                                    imageViewSearchavatar.setVisibility(View.VISIBLE);
                                    buttonAddFriend.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                            if (flag==false)
                            Toast.makeText(AddFriendActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddFriendActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //添加按钮的点击事件
        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myId = ChatApplication.getUser().getObjectId();
                Log.d(TAG,myId);
                if (ChatApplication.getUser().getObjectId()==null){
                    Log.d(TAG,"用户id为空");
                }

                if (newFriend==null){
                    Log.d(TAG,"搜到的好友未正确赋值");
                }
                //搜索到自己并添加自己时
                if (myId.equals(newFriend.getObjectId())) {
                    Toast.makeText(AddFriendActivity.this, "你不能添加你自己", Toast.LENGTH_SHORT).show();
                    return;
                }
                //遍历数据库检查是否是好友

                BmobQuery<FriendRelation> bmobQuery = new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<FriendRelation>() {
                    @Override
                    public void done(List<FriendRelation> list, BmobException e) {
                        if (list != null) {
                            for (FriendRelation relation : list) {
                                //如果是好友
                                if (relation.getFriend().getObjectId()==newFriend.getObjectId()){
                                    Log.d(TAG,"和"+newFriend.getUsername()+"已经是好友");
                                    Toast.makeText(AddFriendActivity.this, newFriend.getUsername()+"已经是你的好友了", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        //创建一个请求，发送者是我，接收者是搜到的人
                        User me=ChatApplication.getUser();
                        Log.d(TAG, me.getObjectId());
                        String mename=me.getUsername();
                        String newfriendname=newFriend.getUsername();

                        FriendRequest request = new FriendRequest(newFriend,newfriendname,me,mename);
                        request.setRequestername(me.getUsername());
                        request.setReceivername(newFriend.getUsername());
                        //不是好友就发送请求同时保存到数据库
                        request.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Log.d(TAG,"好友请求已发送"+"发送人："+ChatApplication.getInstance().getUser().getUsername()+"接收人："+newFriend.getUsername());
                                    Toast.makeText(AddFriendActivity.this, "好友请求已发送", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddFriendActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
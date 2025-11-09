package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.Bean.User;
import com.example.chatapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText et_username;
    private EditText et_password;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initview();
        initevent();
    }

    private void initevent() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                // 输入验证
                if (!validateInput(username, password)) {
                    return;
                }
                // 检查用户是否存在
                checkUserExistence(username, password);
            }
        });
    }

    private void initview() {
        et_username = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
    }

    public void back(View view) {
        finish();
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            et_username.setError("用户名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            et_password.setError("密码不能为空");
            return false;
        }
//正式使用时取消注释
//        if (username.length() < 4 || username.length() > 20) {
//            et_username.setError("用户名长度需在4-20字符之间");
//            return false;
//        }
//
//        if (password.length() < 6) {
//            et_password.setError("密码至少6位");
//            return false;
//        }

        return true;
    }

    private void checkUserExistence(String username, String password) {
        Log.d(TAG, "检查用户是否存在: " + username);
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e== null) {
                    if (list!=null) {
                        for (User u : list) {
                            if (u != null && u.getUsername().equals(username)) {
                                showToast("该用户已存在");
                                return;
                            }
                        }
                    }
                    registerUserOnBmob(username, password);
                }else {
                    Log.d(TAG,e.getErrorCode()+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("注册失败，请检查网络设置");
                        }
                    });
                }
            }
        });
    }

    private void registerUserOnBmob(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar("123");
        Log.d(TAG, "正在注册Bmob用户: " + username);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "用户Bmob注册成功");
                    registerUserOnHuanxin(username, password);
                } else {
                    Log.d(TAG,"Bmob注册失败");
                    showToast("注册失败，请检查网络设置");
                }
            }
        });
    }

    private void registerUserOnHuanxin(String username, String password) {
        Log.d(TAG, "正在注册环信用户: " + username);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username,password);
                    Log.d(TAG,"用户环信注册成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //注册成功后跳转到登录页面
                            showToast("注册成功");
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }
                    });
                } catch (HyphenateException ex) {
                    Log.d(TAG,"与环信连接失败");
                }
            }
        }).start();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
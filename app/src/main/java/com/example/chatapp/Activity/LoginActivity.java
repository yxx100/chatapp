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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatapp.Bean.User;
import com.example.chatapp.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText et_username;
    private EditText et_password;
    private TextView tv_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
        setevent();
    }

    private void initview() {
        et_username = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
    }

    private void setevent() {
        //跳转到注册页面
        tv_register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (inputIsValid(username,password)) {
                    final User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMClient.getInstance().login(username, password, new EMCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onError(int i, String s) {
                                                Toast.makeText(LoginActivity.this, "登录失败，检查网络设置", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean inputIsValid(String u,String p){

        if (TextUtils.isEmpty(u)) {
            et_username.setError("用户名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(p)) {
            et_password.setError("密码不能为空");
            return false;
        }
        return true;
    }
}

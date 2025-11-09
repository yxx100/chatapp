package com.example.chatapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.Activity.LoginActivity;
import com.example.chatapp.Activity.MainActivity;
import com.example.chatapp.Activity.RegisterActivity;
import com.example.chatapp.Bean.User;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class demo {}
//
//                final User user = new User();
//                user.setUsername(username);
//                user.setPassword(password);
//                user.login(new SaveListener<User>() {
//                    @Override
//                    public void done(User user, BmobException e) {
//                        if (e == null) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    EMClient.getInstance().login(username, password, new EMCallBack() {
//                                        @Override
//                                        public void onSuccess() {
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                    finish();
//                                                }
//                                            });
//                                        }
//                                        @Override
//                                        public void onError(int i, String s) {
//                                        }
//                                    });
//                                }
//                            }).start();
//
//
//                        } else {
//                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }
//    });
//
//}
//
//================================================
//                btn_register.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        String username = et_username.getText().toString().trim();
//        String password = et_password.getText().toString().trim();
//
//        BmobQuery<User> bmobQuery = new BmobQuery<>();
//        bmobQuery.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> list, BmobException e) {
//
//                User user=new User();
//                user.setUsername(username);
//                user.setPassword(password);
//                Log.d(TAG,"信息准备");
//                user.signUp(new SaveListener<User>() {
//                    @Override
//                    public void done(User user, BmobException e) {
//                        Log.d(TAG,"正在注册Bmob");
//                        if (e == null) {
//                            //注册成功
//                            Log.d(TAG,"用户Bmob注册成功");
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        EMClient.getInstance().createAccount(username,password);
//                                        Log.d(TAG,"用户环信注册成功");
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                //注册成功后跳转到登录页面
//                                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//                                                finish();
//                                            }
//                                        });
//
//                                    } catch (HyphenateException ex) {
//                                        Log.d(TAG,"与环信连接失败");
//                                    }
//                                }
//                            }).start();
//                        } else {
//                            Log.d(TAG, e.getErrorCode()+e.getMessage());
//                            Toast.makeText(RegisterActivity.this,"注册失败，请检查网络",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//    }
//});
//        }

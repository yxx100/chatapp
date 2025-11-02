package com.example.chatapp.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

package com.example.chatapp.Bean;

import cn.bmob.v3.BmobObject;

public class FriendRelation extends BmobObject {
    private User user;
    private User friend;

    private String username;
    private String friendname;

    public FriendRelation() {
    }

    public FriendRelation(User friend, String friendname, User user, String username) {
        this.friend = friend;
        this.friendname = friendname;
        this.user = user;
        this.username = username;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

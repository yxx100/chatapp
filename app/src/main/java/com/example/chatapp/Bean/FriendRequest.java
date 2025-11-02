package com.example.chatapp.Bean;

import cn.bmob.v3.BmobObject;

public class FriendRequest extends BmobObject {
    private User requester;
    private User receiver;
    private String requestername;
    private String receivername;

    public FriendRequest(User receiver, String receivername, User requester, String requestername) {
        this.receiver = receiver;
        this.receivername = receivername;
        this.requester = requester;
        this.requestername = requestername;
    }

    public FriendRequest() {
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public String getRequestername() {
        return requestername;
    }

    public void setRequestername(String requestername) {
        this.requestername = requestername;
    }
}

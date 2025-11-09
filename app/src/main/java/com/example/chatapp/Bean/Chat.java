package com.example.chatapp.Bean;

import cn.bmob.v3.BmobObject;

public class Chat extends BmobObject {
    private User sender;
    private User receiver;
    private String content;
    private boolean isRead;
    private Long time;

    public Chat() {
    }

    public Chat(User receiver, User sender,String content, boolean isRead, Long time) {
        this.content = content;
        this.isRead = isRead;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

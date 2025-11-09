package com.example.chatapp.Bean;

public class Message {
    private String title;
    private String content;
    private int prompt;
    private long time;
    private String avatar;

    public Message(String title, String content, int prompt, long time, String avatar) {
        this.title = title;
        this.content = content;
        this.prompt = prompt;
        this.time=time;
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrompt() {
        return prompt;
    }

    public void setPrompt(int prompt) {
        this.prompt = prompt;
    }

    public void increasePrompt() {
        this.prompt++;
    }

    public long getTime() {
        return this.time;
    }

    public void setOrder(long time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

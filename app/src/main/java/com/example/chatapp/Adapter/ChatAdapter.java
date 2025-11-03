package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Bean.Chat;
import com.example.chatapp.Bean.User;
import com.example.chatapp.ChatApplication;
import com.example.chatapp.R;
import com.example.chatapp.APPUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private List<Chat> chats = new ArrayList<>();

    private User me= ChatApplication.getUser();

    private String friendName;
    private String friendAvatar;
    private Context context;

    public ChatAdapter(String friendName, String friendAvatar){
        this.friendName=friendName;
        this.friendAvatar=friendAvatar;
    }
    public ChatAdapter(String friendName, String friendAvatar, List<Chat> chats,Context context){
        this.friendName=friendName;
        this.friendAvatar=friendAvatar;
        this.chats = chats;
        this.context = context;
    }
    public ChatAdapter(String friendName, String friendAvatar,Context context){
        this.friendName=friendName;
        this.friendAvatar=friendAvatar;
        this.context = context;
    }

    public ChatAdapter() {
    }
    public ChatAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.textView.setText(chat.getContent());

        APPUtil util=new APPUtil(context);
        if (chat.getSender().getUsername().contentEquals(me.getUsername())) {
            util.loadImage(holder.imageView, me.getAvatar());
        } else if (chat.getSender().getUsername().contentEquals(friendName)) {
            util.loadImage(holder.imageView, friendAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    @Override
    public int getItemViewType(int position) {
        Chat chat = chats.get(position);
        if (chat.getSender().getUsername().contentEquals(me.getUsername())) {
            return R.layout.chat_right_item;
        } else {
            return R.layout.chat_left_item;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewcontent);
            imageView = view.findViewById(R.id.imageViewavatar);
        }
    }
}

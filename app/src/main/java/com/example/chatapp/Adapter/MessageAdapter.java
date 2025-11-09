package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.APPUtil;
import com.example.chatapp.Activity.ChatActivity;
import com.example.chatapp.Bean.Message;
import com.example.chatapp.Bean.User;
import com.example.chatapp.R;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private static final String TAG = "MessageAdapter";
    private List<Message> messages = new ArrayList<>();
    private Context context;
    private User user =new User();
    public MessageAdapter() {
    }

    public MessageAdapter(List<Message> messages,Context context){
        this.messages=messages;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = messages.get(viewHolder.getAdapterPosition());

                Intent intent = new Intent(context, ChatActivity.class);
                BmobQuery<User> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("username", message.getTitle()); // 关键优化
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        for (User u:list) {
                            if (u.getUsername().equals(message.getTitle())){
                                user=u;
                                Log.d(TAG,user.getUsername()+user.getAvatar());
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("friend",user);
                                bundle.putString("username", user.getUsername());
                                bundle.putString("avatar", user.getAvatar());
                                intent.putExtras(bundle);
                                Log.d(TAG,"跳转chat");
                                context.startActivity(intent);
                                break;
                            }
                        }
                    }
                });
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = messages.get(position);
        holder.textViewMessageTitle.setText(message.getTitle());
        holder.textViewMessageContent.setText(message.getContent());
        APPUtil util=new APPUtil(context);
        util.loadImage(holder.imageViewMessage, message.getAvatar());

        if (message.getPrompt() != 0) {
            holder.textViewMessagePrompt.setVisibility(View.VISIBLE);
            holder.textViewMessagePrompt.setText(String.valueOf(message.getPrompt()));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMessage;
        TextView textViewMessageTitle;
        TextView textViewMessageContent;
        TextView textViewMessagePrompt;

        public ViewHolder(View view) {
            super(view);
            imageViewMessage = (ImageView) view.findViewById(R.id.imageViewMessage);
            textViewMessageTitle = (TextView) view.findViewById(R.id.textViewMessageTitle);
            textViewMessageContent = (TextView) view.findViewById(R.id.textViewMessageContent);
            textViewMessagePrompt = (TextView) view.findViewById(R.id.textViewMessagePrompt);
        }

    }
}

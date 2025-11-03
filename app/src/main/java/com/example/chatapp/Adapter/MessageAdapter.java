package com.example.chatapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.APPUtil;
import com.example.chatapp.Bean.Message;
import com.example.chatapp.R;


import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private APPUtil util=new APPUtil();
    private List<Message> messages = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = messages.get(position);
        holder.textViewMessageTitle.setText(message.getTitle());
        holder.textViewMessageContent.setText(message.getContent());
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

package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.APPUtil;
import com.example.chatapp.Activity.ChatActivity;
import com.example.chatapp.Bean.User;
import com.example.chatapp.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    private List<User> list;
    private Context context;
    private LayoutInflater layoutInflater;
    public ContactAdapter(List<User> list, Context context){
        this.list=list;
        this.context=context;
    }
    public ContactAdapter(List<User> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.contact_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        //点击好友一栏跳转到聊天页面
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = list.get(viewHolder.getAdapterPosition());

                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("friend",user);
                bundle.putString("username", user.getUsername());
                bundle.putString("avatar", user.getAvatar());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//给好友联系人绑定数据
        User user = list.get(position);
        String username=user.getUsername();
        //好友名称
        holder.username.setText(username);
        //好友头像
        APPUtil util=new APPUtil(context);
        util.loadImage(holder.iv_photo,user.getAvatar());
        //首字母显示
        //第一个好友显示首字母索引
        if (position==0){
            holder.tvletters.setText(String.valueOf(getFirstLetter(user)));
            holder.tvletters.setVisibility(View.VISIBLE);
        }
        //其他位置的情况
        if (position >= 1) {
            //如果与前一个好友的首字母相同，就隐藏首字母索引
            if (list.get(position - 1).getUsername().toLowerCase().charAt(0) == username.toLowerCase().charAt(0)) {
                holder.tvletters.setVisibility(View.GONE);
            } else {
                //不同就正常显示首字母的大写形式
                holder.tvletters.setText(username.toUpperCase().substring(0, 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private char getFirstLetter(User user) {
        char firstChar = user.getUsername().charAt(0);
        // 处理字母,第一个字符是字母就返回其大写形式，其余返回#
        if (Character.isLetter(firstChar)) {
            return Character.toUpperCase(firstChar);
        }else {
            return '#';
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView tvletters;
        ImageView iv_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.username=itemView.findViewById(R.id.name);
            this.iv_photo=itemView.findViewById(R.id.iv_photo);
            this.tvletters=itemView.findViewById(R.id.tvLetters);
        }
    }
}

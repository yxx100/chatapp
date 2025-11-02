package com.example.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.APPUtil;
import com.example.chatapp.Bean.FriendRequest;
import com.example.chatapp.R;

import java.util.List;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder>{
    private static final String TAG = "NewFriendAdapter";
    private List<FriendRequest> list;
    private Context context;

    public NewFriendAdapter(List<FriendRequest> list,Context context){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_friend_item,parent,false);
        ViewHolder newfriendviewholder=new ViewHolder(view);
        return newfriendviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest request=list.get(position);
        //设置请求者的名称
        holder.newfriendname.setText(request.getRequester().getUsername());
        //设置请求者的头像
        APPUtil util=new APPUtil(context);
        util.loadImage(holder.newfriendavatar,"123");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView newfriendavatar;
        TextView newfriendname;
        ImageButton refuse;
        ImageButton accept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newfriendavatar=itemView.findViewById(R.id.imageViewNewFriend);
            newfriendname=itemView.findViewById(R.id.textViewNewFriend);
            refuse=itemView.findViewById(R.id.imageButtonRefuse);
            accept=itemView.findViewById(R.id.imageButtonAccept);
            //设置点击事件，点击后回调onAcceptClicked方法
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"点击了接收");
                    if (listener != null) {
                        Log.d(TAG,"listener不为空");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Log.d(TAG,"调用点击接受方法");
                            listener.onAcceptClicked(position, list.get(position));
                        }
                    }
                    Log.d(TAG,"liatener为空");
                }
            });
            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"点击了拒绝");
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRefuseClicked(position, list.get(position));
                        }
                    }
                }
            });
        }
    }
    // 定义回调接口,包含同意和拒绝的回调方法
    public interface OnFriendRequestActionListener {
        void onAcceptClicked(int position, FriendRequest request);
        void onRefuseClicked(int position, FriendRequest request);
    }

    //该变量可以存储任何实现了该接口的对象实例
    private OnFriendRequestActionListener listener;

    // 设置监听器的方法
    public void setOnFriendRequestActionListener(OnFriendRequestActionListener listener) {
        this.listener = listener;
    }
}

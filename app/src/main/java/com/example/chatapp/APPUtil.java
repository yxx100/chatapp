package com.example.chatapp;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class APPUtil {
    private Context context;
    public APPUtil(Context context){
        this.context=context;
    }
    public APPUtil(){}
    public void loadImage(ImageView imageView, String avatar) {
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(20));
        if (avatar.contentEquals("123")) {
            Glide.with(context)
                    .load(R.drawable.default_avatar)
                    .apply(options)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .apply(options)
                    .into(imageView);
        }
    }
}

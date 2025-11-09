package com.example.chatapp2.ui.theme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp2.R;


public class TextNavFragment extends Fragment {


    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private TextView tvContent;

    public TextNavFragment() {

    }

    public static TextNavFragment newInstance(String param1, String param2) {
        TextNavFragment fragment = new TextNavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_nav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvContent=view.findViewById(R.id.tv_content);

        if(!TextUtils.isEmpty(mParam1)){
            tvContent.setText(mParam1);
        }
    }
}
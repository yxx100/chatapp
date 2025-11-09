package com.example.chatapp2.ui.theme;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatapp2.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llHome,llConnect,llMine;
    private ImageView ivHome,ivConnect,ivMine;
    private TextView tvHome,tvConnect,tvMine;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_bottom_nav1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();

        initEvent();
    }

    private void initEvent() {
        //添加fragment

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        TextNavFragment fragment =TextNavFragment.newInstance("这是首页","");
        fragmentTransaction.replace(R.id.fcv_fragment,fragment).commit();

        llHome.setOnClickListener(this);
        llConnect.setOnClickListener(this);
        llMine.setOnClickListener(this);


    }

    private void initView() {
        llHome=findViewById(R.id.ll_home);
        llConnect=findViewById(R.id.ll_connect);
        llMine=findViewById(R.id.ll_mine);

        ivHome=findViewById(R.id.iv_home);
        ivConnect=findViewById(R.id.iv_connect);
        ivMine=findViewById(R.id.iv_mine);

        tvHome=findViewById(R.id.tv_home);
        tvConnect=findViewById(R.id.tv_connect);
        tvMine=findViewById(R.id.tv_mine);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.ll_home:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                TextNavFragment fragment =TextNavFragment.newInstance("这是首页","");
                fragmentTransaction.replace(R.id.fcv_fragment,fragment).commit();
                ivHome.setSelected(true);
                tvHome.setTextColor(Color.BLUE);

                break;
            case R.id.ll_connect:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                TextNavFragment connectfragment =TextNavFragment.newInstance("这是联系人","");
                fragmentTransaction.replace(R.id.fcv_fragment,connectfragment).commit();
                ivConnect.setSelected(true);
                tvConnect.setTextColor(Color.BLUE);

                break;
            case R.id.ll_mine:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                TextNavFragment minefragment =TextNavFragment.newInstance("这是我的","");
                fragmentTransaction.replace(R.id.fcv_fragment,minefragment).commit();
                ivMine.setSelected(true);
                tvMine.setTextColor(Color.BLUE);

                break;
            default:
                break;
        }

    }
}
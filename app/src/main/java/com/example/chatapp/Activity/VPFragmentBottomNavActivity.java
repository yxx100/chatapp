package com.example.chatapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.Adapter.MyFragmentStateVPAdapter;
import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class VPFragmentBottomNavActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MyFragmentStateVPAdapter mStateVPAdapter;
    private List<Fragment> mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vpfragment_botoom_nav);
        mViewPager= findViewById(R.id.vp);
        mBottomNavigationView=findViewById(R.id.bottom_menu);

        initData();
        mStateVPAdapter =new MyFragmentStateVPAdapter(getSupportFragmentManager(),mFragmentList);

        mViewPager.setAdapter(mStateVPAdapter);


    }

    private void initData() {
        mFragmentList =new ArrayList<>();

        VPFragment fragmentHome =VPFragment.newInstance("首页","");
        VPFragment fragmentConnect=VPFragment.newInstance("联系人","");
        VPFragment fragmentMine=VPFragment.newInstance("我的","");

        mFragmentList.add(fragmentHome);
        mFragmentList.add(fragmentConnect);
        mFragmentList.add(fragmentMine);
    }
}
package com.example.chatapp.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.Adapter.MyFragmentStateVPAdapter;
import com.example.chatapp.Fragment.ContactFragment;
import com.example.chatapp.Fragment.MessageFragment;
import com.example.chatapp.Fragment.MineFragment;
import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class VPFragmentBottomNavActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MyFragmentStateVPAdapter mStateVPAdapter;
    private List<Fragment> mFragmentList;
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(VPFragmentBottomNavActivity.this, "页面" + position, Toast.LENGTH_SHORT).show();

                onPagerSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.menu_home)
                    mViewPager.setCurrentItem(0);
                else if (menuItem.getItemId()==R.id.menu_connect)
                    mViewPager.setCurrentItem(1);
                else if (menuItem.getItemId()==R.id.menu_mine)
                    mViewPager.setCurrentItem(2);



                return true;
            }
        });
    }


    private void onPagerSelected(int position) {
        switch (position){
            case 0:
               mBottomNavigationView.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.menu_connect);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.menu_mine);
                break;
            default:
                break;
        }

    }

    private void initData() {
        mFragmentList =new ArrayList<>();

        MessageFragment fragmentHome =MessageFragment.newInstance("首页","");
        ContactFragment fragmentConnect=ContactFragment.newInstance("联系人","");
        MineFragment fragmentMine=MineFragment.newInstance("我的","");

        mFragmentList.add(fragmentHome);
        mFragmentList.add(fragmentConnect);
        mFragmentList.add(fragmentMine);
    }
}
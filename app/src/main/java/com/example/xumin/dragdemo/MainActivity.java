package com.example.xumin.dragdemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.xumin.dragdemo.fragment.RoomDevice;
import com.example.xumin.dragdemo.fragment.RoomDeviceFragment;
import com.example.xumin.dragdemo.helper.ItemTouchHelperCallBack;
import com.example.xumin.dragdemo.widget.SmartFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabs;
    private ViewPager viewpager;
    private List<String> titles = new ArrayList<>();
    private SmartFragmentAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTab();
        initData();
    }

    /**
     * 初始控件
     */
    private void initView() {
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
    }

    /**
     * 初始化底部导航栏
     */
    private void initTab() {
        pagerAdapter = new SmartFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < 3; i++) {
            String roomName = "房间" + i;
            titles.add(roomName);
            RoomDeviceFragment fragment = new RoomDeviceFragment();
            fragment.setMyonItemStartScroll(new ItemTouchHelperCallBack.OnItemStartScroll() {
                @Override
                public void startScroll(RoomDevice roomDevice) {
                    ((RoomDeviceFragment) fragments.get(1)).addRoomDevice(roomDevice);
                    //tabs.setScrollPosition(0, 1, true);
                    tabs.getTabAt(1).select();
                   // pagerAdapter.notifyDataSetChanged();
                }
            });
            fragments.add(fragment);
        }
        initFragment();
    }

    /**
     * 初始化adapter
     */
    public void initFragment() {
        Log.i(TAG, "initFragment fragments size=" + fragments.size());
        tabs.setupWithViewPager(viewpager,true);
        pagerAdapter.updateFragments(fragments);
        // pagerAdapter.notifyDataSetChanged();
        viewpager.setOffscreenPageLimit(fragments.size());
        if (fragments.size() > 5) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabs.setTabMode(TabLayout.MODE_FIXED);
        }


    }


}

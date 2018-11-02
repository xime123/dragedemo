package com.example.xumin.dragdemo.fragment;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xumin.dragdemo.R;
import com.example.xumin.dragdemo.adapter.CommonAdapter;
import com.example.xumin.dragdemo.adapter.base.ViewHolder;
import com.example.xumin.dragdemo.helper.DividerGridItemDecoration;
import com.example.xumin.dragdemo.helper.ItemTouchHelperCallBack;
import com.example.xumin.dragdemo.helper.OnRecyclerItemClickListener;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomDeviceFragment extends Fragment {
    XRecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private List<RoomDevice> datas = new ArrayList<>();
    private CommonAdapter<RoomDevice> adapter;
    private ItemTouchHelperCallBack.OnItemStartScroll myonItemStartScroll;
    private ItemTouchHelperCallBack callBack;

    public void setMyonItemStartScroll(ItemTouchHelperCallBack.OnItemStartScroll myonItemStartScroll) {
        this.myonItemStartScroll = myonItemStartScroll;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("callback", "setUserVisibleHint=" + isVisibleToUser);
        if (isVisibleToUser && callBack != null) {
            callBack.setHasScrolled(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_room, null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recycle_view);
        initRecyclerView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            RoomDevice roomDevice = new RoomDevice();
            roomDevice.setName("设备" + i);
            datas.add(roomDevice);
        }
        adapter.notifyDataSetChanged();
        initTouchHelp();
    }

    private void initTouchHelp() {
        //长按拖动跟下拉刷新事件冲突

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

                //判断被拖拽的是否是头部，如果不是则执行拖拽

                mItemTouchHelper.startDrag(vh);

                //获取系统震动服务
                Vibrator vib = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                vib.vibrate(70);


            }
        });

        callBack = new ItemTouchHelperCallBack(mRecyclerView, datas, adapter);
        callBack.setOnItemStartScroll(myonItemStartScroll);
        callBack.setColNum(2);
        mItemTouchHelper = new ItemTouchHelper(callBack);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initRecyclerView() {
        mRecyclerView.setPullRefreshEnabled(false);
        List<? extends RecyclerView.ItemDecoration> decorations = buildDefaultItemDecorations();
        if (decorations != null && decorations.size() > 0) {
            for (RecyclerView.ItemDecoration itemDecoration : buildDefaultItemDecorations()) {
                mRecyclerView.addItemDecoration(itemDecoration);
            }
        }
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new CommonAdapter<RoomDevice>(getContext(), R.layout.item_room_device, datas) {
            @Override
            protected void convert(ViewHolder holder, RoomDevice roomDevice, int position) {
                holder.setText(R.id.name, roomDevice.getName());
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    protected List<? extends RecyclerView.ItemDecoration> buildDefaultItemDecorations() {
        int color = getResources().getColor(R.color.headerbg);
        return Collections.singletonList(new DividerGridItemDecoration(getContext(), 3, color) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
                //顺序:left, top, right, bottom
                boolean[] booleans = {false, false, false, false};
                if (itemPosition == 0) {
                    //因为给 RecyclerView 添加了 header，所以原本的 position 发生了变化
                    //position 为 0 的地方实际上是 header，真正的列表 position 从 1 开始
                } else {
                    switch (itemPosition % 2) {
                        case 0:
                            //每一行第三个只显示左边距和下边距
                            //  booleans[0] = true;
                            booleans[3] = true;
                            break;
                        case 1:
                            //每一行第一个显示右边距和下边距
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                    }
                }
                return booleans;
            }
        });
    }

    public void addRoomDevice(RoomDevice roomDevice) {
        Log.i("callback", "addRoomDevice  ==新增一个roomdevice " + roomDevice.getName());
        datas.add(0, roomDevice);
        adapter.notifyDataSetChanged();
    }

}

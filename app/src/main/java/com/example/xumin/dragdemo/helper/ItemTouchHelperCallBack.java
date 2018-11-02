package com.example.xumin.dragdemo.helper;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.xumin.dragdemo.adapter.MultiItemTypeAdapter;
import com.example.xumin.dragdemo.fragment.RoomDevice;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.Collections;
import java.util.List;


/**
 * author:     xumin
 * date:       2018/5/17
 * email:      xumin2@evergrande.cn
 */
public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private OnItemStartScroll onItemStartScroll;
    private final static String TAG = "ItemTouchHelperCallBack";
    private XRecyclerView mRecyclerVIew;
    private List<RoomDevice> datas;
    private MultiItemTypeAdapter<RoomDevice> adapter;
    private int colNum;
    private boolean onMove = false;
    private boolean hasScrolled = false;

    public ItemTouchHelperCallBack(XRecyclerView mRecyclerVIew, List<RoomDevice> datas, MultiItemTypeAdapter<RoomDevice> adapter) {
        this.mRecyclerVIew = mRecyclerVIew;
        this.datas = datas;
        this.adapter = adapter;
    }

    /**
     * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView == null) {
            return -1;
        }

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.i(TAG, "onMove");
        onMove = true;
        if (datas.size() <= 1) {
            return false;
        }
        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();

        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();

        int headerCount = mRecyclerVIew.getHeaders_includingRefreshCount();
        Log.i(TAG, "fromPosition=" + fromPosition + "   toPosition=" + toPosition);
        int fromIndex = fromPosition - headerCount;
        int toIndex = toPosition - headerCount;
        Log.i(TAG, "fromIndex=" + fromIndex + "   toIndex=" + toIndex);
        if (fromIndex < 0 || toIndex < 0) {
            return false;
        }


        Collections.swap(datas, fromIndex, toIndex);


        adapter.notifyItemMoved(fromPosition, toPosition);
        //adapter.notifyDataSetChanged();
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i(TAG, "onSwiped");

    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float itemWidth = viewHolder.itemView.getMeasuredWidth();
        int position = viewHolder.getLayoutPosition();
        int index = position % colNum;
        Log.e(TAG, "onChildDrawOver  dx=" + dX + "   dy=" + dY + "  itemWidth=" + itemWidth + "  position=" + position + "   index=" + index);

        float needScrollGap = index * itemWidth + itemWidth / 2 - 50;

        int dataSize = datas.size();
        Log.e(TAG, "onChildDrawOver ================================>needScrollGap=" + needScrollGap + " hasScrolled=" + hasScrolled + "   dataSize=" + dataSize + "    \nonItemStartScroll=" + onItemStartScroll);
        if (dX >= needScrollGap && onItemStartScroll != null) {
            if (dataSize > position - 1 && !hasScrolled) {
                onItemStartScroll.startScroll(datas.remove(position - 1));
                adapter.notifyDataSetChanged();
                hasScrolled = true;
            }
        }
    }


    /**
     * 重写拖拽可用
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        Log.i(TAG, "isLongPressDragEnabled");
        return false;
    }


    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.i(TAG, "onSelectedChanged  actionState=" + actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            Log.i(TAG, "onSelectedChanged  actionState=" + actionState);
            // viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 手指松开的时候还原
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.i(TAG, "clearView");
        super.clearView(recyclerView, viewHolder);
        adapter.notifyDataSetChanged();
        //  viewHolder.itemView.setBackgroundColor(0);
        //item长按放开的时候，这个下拉刷新要设置为true
        // mRecyclerVIew.setPullRefreshEnabled(true);
    }


    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public void setOnItemStartScroll(OnItemStartScroll onItemStartScroll) {
        this.onItemStartScroll = onItemStartScroll;
    }

    public void setHasScrolled(boolean hasScrolled) {
        this.hasScrolled = hasScrolled;
    }

    public interface OnItemStartScroll {
        void startScroll(RoomDevice roomDevice);
    }
}

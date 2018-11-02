package com.example.xumin.dragdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.xumin.dragdemo.adapter.base.ItemViewDelegate;
import com.example.xumin.dragdemo.adapter.base.ViewHolder;

import java.util.List;



/**
 * author:     xumin
 * date:       2018/5/16
 * email:      xumin2@evergrande.cn
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}

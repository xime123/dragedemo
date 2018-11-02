package com.example.xumin.dragdemo.adapter.base;


/**
 * author:     xumin
 * date:       2018/5/16
 * email:      xumin2@evergrande.cn
 */
public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}

package com.gzhealthy.health.base;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdap<T> extends android.widget.BaseAdapter {
    protected List<T> datas = new ArrayList<T>();
    protected Context mContext;
    protected LayoutInflater layoutInflater;

    public BaseAdap(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return (datas == null) ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        if (datas == null || position < 0 || position > datas.size() - 1)
            return null;
        return datas.get(position);
    }

    public void removeItem(int position) {
        if (datas == null || position < 0 || position > datas.size())
            return;
        datas.remove(position);
        notifyDataSetChanged();
        return;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return (datas == null) ? true : datas.isEmpty();
    }

    public void setGroup(List<T> g) {
        if (datas != null)
            datas.clear();
        datas.addAll(g);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        datas.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加指定位置的item
     */
    public void addItem(int position, T item) {
        datas.add(position, item);
        notifyDataSetChanged();
    }

    public void addItemNoNotify(T item) {
        datas.add(item);
    }

    public void addItems(List<T> items) {
        if (items != null) {
            datas.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clearGroup(boolean notify) {
        if (datas != null) {
            datas.clear();
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

}

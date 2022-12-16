package com.gzhealthy.health.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.holder.CompositeContentHolder;
import com.gzhealthy.health.model.CompositeModel;

import java.util.ArrayList;
import java.util.List;


public class CompositeAdapter extends RecyclerView.Adapter<CompositeContentHolder> {

    private static final String TAG = "CompositeAdapter";
    private View.OnClickListener mOnClickListener;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public List<CompositeModel> getData() {
        return data;
    }

    private List<CompositeModel> data;

    public CompositeAdapter(List<CompositeModel> mData) {
        if (mData != null) {
            this.data = mData;
        } else {
            this.data = new ArrayList<>();
        }
    }

    @Override
    public CompositeContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composite_content_item, parent, false);
        return new CompositeContentHolder(root, this);
    }

    @Override
    public void onBindViewHolder(CompositeContentHolder holder, int position) {
        holder.bindData(data.get(position), this, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

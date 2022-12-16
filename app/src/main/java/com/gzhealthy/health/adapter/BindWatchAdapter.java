package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aitsuki.swipe.SwipeItemLayout;
import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.WatchOperationDetailActivity;
import com.gzhealthy.health.callback.OnUnBindWatchCallBack;
import com.gzhealthy.health.model.WatchDeviceModel;
import com.gzhealthy.health.widget.BatteryView;

import java.util.ArrayList;
import java.util.List;

public class BindWatchAdapter extends SwipeMenuRecyclerView.Adapter<BindWatchAdapter.ViewHolder> {

    private Context mContext;
    private List<WatchDeviceModel.DataBean> mList = new ArrayList<>();
    private OnUnBindWatchCallBack onUnBindWatchCallBack;

    public BindWatchAdapter(Context context, OnUnBindWatchCallBack onUnBindWatchCallBack) {
        this.mContext = context;
        this.onUnBindWatchCallBack = onUnBindWatchCallBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device_watch_bind, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        WatchDeviceModel.DataBean bean = mList.get(position);
        holder.swipe_layout.setSwipeEnable(false);
        holder.swipe_layout.setOnClickListener(v -> {
            WatchOperationDetailActivity.instance(mContext, bean);
        });
        holder.dev_imei.setText(mContext.getResources().getString(R.string.swipe_desc, mList.get(position).imei));
//        Glide.with(mContext).load(bean.photoUrl).into(holder.img_watch);
        holder.unbind.setOnClickListener(v -> {
            onUnBindWatchCallBack.unBind();
            holder.swipe_layout.close();
        });
        if (bean.electricity != null) {
            holder.batteryView.setVisibility(View.VISIBLE);
            try {
                holder.batteryView.setBatterySize(Integer.valueOf(bean.electricity));
            } catch (Exception e) {
                holder.batteryView.setVisibility(View.GONE);
            }
        } else {
            holder.batteryView.setVisibility(View.GONE);
        }
    }

    public void refreshData(List<WatchDeviceModel.DataBean> mList) {
        this.mList.clear();
        if (mList != null && !mList.isEmpty()) {
            this.mList.addAll(mList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dev_imei, unbind;
        private ImageView img_watch;
        private SwipeItemLayout swipe_layout;
        private BatteryView batteryView;


        public ViewHolder(View itemView) {
            super(itemView);
            dev_imei = itemView.findViewById(R.id.dev_imei);
            img_watch = itemView.findViewById(R.id.img_watch);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
            unbind = itemView.findViewById(R.id.unbind);
            batteryView = itemView.findViewById(R.id.batteryView);
        }
    }


//    public interface OnItemClikListener {
//        void onItemClik(View view, int position);
//
//        void onItemLongClik(View view, int position);
//    }
//
//    public void setItemClikListener(OnItemClikListener mOnItemClikListener) {
//        this.mOnItemClikListener = mOnItemClikListener;
//    }

}

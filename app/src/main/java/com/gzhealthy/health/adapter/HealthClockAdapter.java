package com.gzhealthy.health.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
//import com.gzhealthy.health.activity.ECGHtmlActivity;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.CollectListModel;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.model.HealthClockInfo;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.utils.BaseUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 收藏适配器
 */
public class HealthClockAdapter extends RecyclerView.Adapter<HealthClockAdapter.ClockViewHolder> {
    private Context mContext;
    public List<HealthClockInfo> mList = new ArrayList<>();
    public int type;
    public View.OnClickListener onClickListener;


    public HealthClockAdapter(Context mContext, int type, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.type = type;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClockViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_health_clock, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClockViewHolder holder, int position) {
        holder.timeName.setText(mList.get(position).time);
        holder.timeName.setTag(mList.get(position).type);
        holder.timeName.setOnClickListener(onClickListener);
        if (mList.get(position).type == type){
            holder.timeTips.setVisibility(View.VISIBLE);
            holder.timeName.setTextColor(mContext.getColor(R.color.colorPrimary));
        }else {
            holder.timeTips.setVisibility(View.GONE);
            holder.timeName.setTextColor(mContext.getColor(R.color.global_333333));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ClockViewHolder extends RecyclerView.ViewHolder{
        private TextView timeTips,timeName;

        public ClockViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTips = itemView.findViewById(R.id.time_tips);
            timeName = itemView.findViewById(R.id.time_name);
        }
    }


    public void refreshData(List<HealthClockInfo> mList){
        this.mList.clear();
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
        }
        this.notifyDataSetChanged();
    }
}

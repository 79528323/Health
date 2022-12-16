package com.gzhealthy.health.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gzhealthy.health.R;
//import com.gzhealthy.health.activity.ECGHtmlActivity;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.model.HealthCard;
import com.gzhealthy.health.model.HealthyReportHistory;
import com.gzhealthy.health.tool.DateUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HealthyReportAdapter extends RecyclerView.Adapter<HealthyReportAdapter.ViewHolder> {

    private Context mContext;
    private List<HealthyReportHistory.DataBean.ListBean> mList = new ArrayList<>();
    private OnSelectHistoryClickListener onSelectHistoryClickListener;

    public HealthyReportAdapter(Context mContext,OnSelectHistoryClickListener onSelectHistoryClickListener) {
        this.mContext = mContext;
        this.onSelectHistoryClickListener = onSelectHistoryClickListener;
    }

    public void refreshData(List<HealthyReportHistory.DataBean.ListBean> mList){
        this.mList.clear();
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
        }
        this.notifyDataSetChanged();
    }

    public void appendData(List<HealthyReportHistory.DataBean.ListBean> mList){
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_healthy_report_history, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HealthyReportHistory.DataBean.ListBean bean = mList.get(position);
        holder.date.setText(DateUtil.getStringDate3(bean.createTime));
        holder.score.setText(String.valueOf(bean.score));
        holder.clicker.setOnClickListener(v -> {
            onSelectHistoryClickListener.onSelect(bean.id);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private XAxis xAxis;                //X轴
        private YAxis leftYAxis;            //左侧Y轴
        private YAxis rightYaxis;           //右侧Y轴
        private Legend legend;              //图例

        private RelativeLayout clicker;
        private TextView date, score;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            score = (TextView) itemView.findViewById(R.id.tv_score);
//            lineChart = (LineChart) itemView.findViewById(R.id.rate_linechart);
            clicker = itemView.findViewById(R.id.clicker);
//            initChart(lineChart);
        }
    }


    public interface OnSelectHistoryClickListener{
        void onSelect(int id);
    }
}

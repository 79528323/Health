package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.model.HealthyNewReport;
import com.gzhealthy.health.model.HealthyReportHistory;
import com.gzhealthy.health.model.SosListModel;

import java.util.List;

public class HealthNewReportAdapter extends BaseQuickAdapter<HealthyNewReport.DataBean, BaseViewHolder> {
    private Context context;

    public HealthNewReportAdapter(Context context) {
        super(R.layout.item_healthy_report_history);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HealthyNewReport.DataBean item) {
        helper.setText(R.id.tv_score,String.valueOf(item.score));
        helper.setText(R.id.tv_date,item.reportTime);
        helper.addOnClickListener(R.id.tv_item_linear);
    }


//    public void refreshData(List<HealthyNewReport.DataBean> mList){
//        this.mData.clear();
//        if (mList!=null && !mList.isEmpty()){
//            this.mData.addAll(mList);
//        }
//        this.notifyDataSetChanged();
//    }
//
//    public void appendData(List<HealthyNewReport.DataBean> mList){
//        if (mList!=null && !mList.isEmpty()){
//            this.mData.addAll(mList);
//            notifyDataSetChanged();
//        }
//    }
}

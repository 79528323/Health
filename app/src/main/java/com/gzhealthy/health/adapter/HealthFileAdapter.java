package com.gzhealthy.health.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.HealthRecordModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class HealthFileAdapter extends BaseQuickAdapter<HealthRecordModel.DataBean, BaseViewHolder> {

    public HealthFileAdapter() {
        super(R.layout.item_health_file);
    }

    @Override
    protected void convert(BaseViewHolder helper, HealthRecordModel.DataBean item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvDate, TimeUtils.millis2String(item.getCreateTime(), "yyyy-MM-dd HH:mm"));
        helper.setText(R.id.tvAddress, item.getStore());
    }
}

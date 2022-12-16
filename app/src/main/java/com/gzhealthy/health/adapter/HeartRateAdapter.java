package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.SosListModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class HeartRateAdapter extends BaseQuickAdapter<SosListModel.DataModel.RateWarnListModel, BaseViewHolder> {

    public HeartRateAdapter() {
        super(R.layout.item_heart_rate);
    }

    @Override
    protected void convert(BaseViewHolder helper, SosListModel.DataModel.RateWarnListModel item) {
        helper.setText(R.id.tvFrequency, item.getRate() + "次/分");
        helper.setText(R.id.tvTime, item.getCheckTime());
    }
}

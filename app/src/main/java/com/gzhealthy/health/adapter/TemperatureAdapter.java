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
public class TemperatureAdapter extends BaseQuickAdapter<SosListModel.DataModel.TemperatureWarnListModel, BaseViewHolder> {

    public TemperatureAdapter() {
        super(R.layout.item_tempera_ture);
    }

    @Override
    protected void convert(BaseViewHolder helper, SosListModel.DataModel.TemperatureWarnListModel item) {
        helper.setText(R.id.tvTemperaTure, item.getTemperature() + "℃");
        helper.setText(R.id.tvTime, item.getCheckTime());
    }
}

package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.ChineseMedicineBodyModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class ChineseMedicineBodyAdapter extends BaseQuickAdapter<ChineseMedicineBodyModel.DataBean, BaseViewHolder> {

    public ChineseMedicineBodyAdapter() {
        super(R.layout.item_chinese_medicine_body);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChineseMedicineBodyModel.DataBean item) {
        helper.setText(R.id.tvTitle, item.getPropertyName());
        helper.setText(R.id.tvDate, "分析时间：" + item.getCreateTime());
    }
}

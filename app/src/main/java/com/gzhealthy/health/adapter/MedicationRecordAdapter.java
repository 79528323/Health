package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.MedicationRecordModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MedicationRecordAdapter extends BaseQuickAdapter<MedicationRecordModel.DataBean, BaseViewHolder> {

    public MedicationRecordAdapter() {
        super(R.layout.item_medication_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicationRecordModel.DataBean item) {
        helper.setText(R.id.tvTitle, item.getName());
        helper.setText(R.id.tvDate, item.getTime());
        helper.addOnClickListener(R.id.ivEdit);
    }
}

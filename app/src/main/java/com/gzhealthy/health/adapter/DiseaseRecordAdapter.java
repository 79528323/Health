package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.DiseaseRecord;
import com.gzhealthy.health.model.MedicationRecordModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class DiseaseRecordAdapter extends BaseQuickAdapter<DiseaseRecord.DataBean, BaseViewHolder> {

    public DiseaseRecordAdapter() {
        super(R.layout.item_disease_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiseaseRecord.DataBean item) {
        helper.setText(R.id.tvType, item.type);
        helper.setText(R.id.tvDate, item.seeDate);
        helper.setText(R.id.tvHospital,item.hospital);
        helper.setText(R.id.tvDiagnosis,item.diagnosis);
        helper.addOnClickListener(R.id.ivEdit);
    }
}

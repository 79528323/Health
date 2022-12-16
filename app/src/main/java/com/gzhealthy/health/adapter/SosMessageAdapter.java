package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.SosListModel;

public class SosMessageAdapter extends BaseQuickAdapter<SosListModel.DataModel, BaseViewHolder> {


    public SosMessageAdapter() {
        super(R.layout.item_sos_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, SosListModel.DataModel item) {
        helper.setText(R.id.tvDate, item.getCreateTime());
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvDec, item.getExtraContent());
        helper.addOnClickListener(R.id.llLinearLayout);
        helper.setVisible(R.id.vDot, item.getIfRead() == 0);
    }
}

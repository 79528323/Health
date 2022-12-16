package com.gzhealthy.health.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.MedicationRecordModel;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class BlueToothListAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {
    public BlueToothListAdapter() {
        super(R.layout.item_bluetooth);
    }

    @Override
    protected void convert(BaseViewHolder helper, BleDevice item) {
        String device = item.getName();
        helper.setText(R.id.name, device);
        helper.addOnClickListener(R.id.name);
    }
}

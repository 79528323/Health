package com.gzhealthy.health.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class ModifyRemarkNicknameAdapter extends BaseQuickAdapter<String
        , BaseViewHolder> {

    public ModifyRemarkNicknameAdapter() {
        super(R.layout.item_modify_remark_nickname);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tvRemark, item);
    }
}

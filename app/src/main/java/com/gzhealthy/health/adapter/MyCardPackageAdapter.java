package com.gzhealthy.health.adapter;

import com.blankj.utilcode.util.ClipboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.CardPackageModel;
import com.gzhealthy.health.utils.ToastUtil;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MyCardPackageAdapter extends BaseQuickAdapter<CardPackageModel.DataBean, BaseViewHolder> {

    public MyCardPackageAdapter() {
        super(R.layout.item_my_card_package);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardPackageModel.DataBean item) {
        if (item.getType() == 1) {
            helper.setImageResource(R.id.ivLogo, R.mipmap.my_card_package_logo);
            helper.setVisible(R.id.ivLogo, true);
        } else {
            helper.setVisible(R.id.ivLogo, false);
        }
        helper.setText(R.id.tvTitle, item.getName());
        helper.setText(R.id.tvDate, "有效期至：" + item.getValidPeriod());
        helper.setText(R.id.tvCode, item.getContent());
        helper.getView(R.id.tvCopy).setOnClickListener(v -> {
            try {
                ClipboardUtils.copyText(item.getContent());
                ToastUtil.showToast("已复制");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

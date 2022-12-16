package com.gzhealthy.health.share;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;

import java.util.List;

public class ShareIconAdapter extends BaseQuickAdapter<ShareIconBean, BaseViewHolder> {

    public ShareIconAdapter(@Nullable List<ShareIconBean> data) {
        super(R.layout.item_share_icon, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ShareIconBean item) {
        helper.setText(R.id.tv_icon, item.getTitle());
        Glide.with(mContext)
                .load(item.getPic())
                .into((ImageView) helper.getView(R.id.iv_icon));
    }
}

package com.gzhealthy.health.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class FeedBackDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean isDeleteGone=false;

    public FeedBackDetailAdapter() {
        super(R.layout.item_feed_back_detail);
    }

    public FeedBackDetailAdapter(int layoutResId, boolean isDeleteGone) {
        super(layoutResId);
        this.isDeleteGone = isDeleteGone;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (TextUtils.isEmpty(item)) {
            helper.setGone(R.id.cvAdd, true);
            helper.setGone(R.id.cvImage, false);
            helper.setGone(R.id.ivDel, false);

        } else {
            helper.setGone(R.id.cvAdd, false);
            helper.setGone(R.id.cvImage, true);
            helper.setGone(R.id.ivDel, true);

            Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.ivImage));
            helper.addOnClickListener(R.id.ivDel);
            helper.addOnClickListener(R.id.ivImage);
        }

        helper.addOnClickListener(R.id.cvAdd);
        helper.addOnClickListener(R.id.ivDel);
        if (isDeleteGone)
            helper.setGone(R.id.ivDel,false);
    }
}

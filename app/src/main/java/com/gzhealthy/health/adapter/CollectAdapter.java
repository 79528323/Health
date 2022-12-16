package com.gzhealthy.health.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.CollectListModel;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.utils.BaseUtil;

/**
 * 收藏适配器
 */
public class CollectAdapter extends BaseQuickAdapter<CollectListModel.DataBean, com.chad.library.adapter.base.BaseViewHolder> {

    private View.OnClickListener onItemListener;

    public View.OnClickListener getOnItemContentListener() {
        return onItemContentListener;
    }

    public void setOnItemContentListener(View.OnClickListener onItemContentListener) {
        this.onItemContentListener = onItemContentListener;
    }

    private View.OnClickListener onItemContentListener;

    public View.OnClickListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(View.OnClickListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public CollectAdapter() {
        super(R.layout.collect_item);
    }


    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, CollectListModel.DataBean item) {
        if (item.isEditMode()) {
            helper.setGone(R.id.iv_choose, true);
            if (item.isChoose()) {
                helper.setImageResource(R.id.iv_choose, R.mipmap.icon_hook_b);
            } else {
                helper.setImageResource(R.id.iv_choose, R.mipmap.icon_hook_a);
            }
        } else {
            helper.setGone(R.id.iv_choose, false);

        }

        View tagView = helper.getView(R.id.ll_item_collect);
        Object tag = tagView.getTag();
        if (tag == null || !TextUtils.equals(((String) tag), item.getPhoto())) {
            tagView.setTag(item.getPhoto());
            GlideUtils.DrawableTransformCop(mContext, helper.getView(R.id.iv_guide), Constants.Api.ossPicPre + item.getPhoto(),
                    8, false, false, false, false);
        }

        helper.setText(R.id.des_tv, "" + item.getName());
        helper.setText(R.id.cat_tv, "" + item.getTypeName() + "    " + BaseUtil.getDateToString(item.getCreateTime()));
        helper.getView(R.id.iv_choose).setTag(helper.getAdapterPosition());
        helper.getView(R.id.message_ll).setTag(item);
        helper.getView(R.id.iv_choose).setOnClickListener(mItemListener);
        helper.getView(R.id.message_ll).setOnClickListener(mItemContentListener);
    }

    private View.OnClickListener mItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getOnItemListener() != null) {
                getOnItemListener().onClick(view);
            }
        }
    };

    private View.OnClickListener mItemContentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getOnItemContentListener() != null) {
                getOnItemContentListener().onClick(view);
            }
        }
    };

}

package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.holder.ImageHolder;
import com.gzhealthy.health.model.Admodel;
import com.gzhealthy.health.tool.GlideUtils;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

/**
 * 自定义布局,多个不同UI切换
 */
public class MultipleTypesAdapter extends BannerAdapter<Admodel.DataBean.AdVoListBean, RecyclerView.ViewHolder> {

    private Context context;

    public MultipleTypesAdapter(Context context, List<Admodel.DataBean.AdVoListBean> mDatas) {
        super(mDatas);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(BannerUtils.getView(parent, R.layout.banner_image));
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, Admodel.DataBean.AdVoListBean data, int position, int size) {
        ImageHolder imageHolder = (ImageHolder) holder;
        GlideUtils.DrawableTransformCop(context, imageHolder.imageView, Constants.Api.ossPicPre + data.getAdImg(),
                6, false, false, false, false);
    }


}

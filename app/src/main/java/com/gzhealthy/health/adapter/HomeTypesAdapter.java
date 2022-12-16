package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.holder.ImageHolder;
import com.gzhealthy.health.model.Admodel;
import com.gzhealthy.health.model.HomeTypesModel;
import com.gzhealthy.health.tool.GlideUtils;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义布局,多个不同UI切换
 */
public class HomeTypesAdapter extends BannerAdapter<HomeTypesModel, RecyclerView.ViewHolder> {

    private Context context;

    public HomeTypesAdapter(Context context, List<HomeTypesModel> mDatas) {
        super(mDatas);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(BannerUtils.getView(parent, R.layout.banner_image));
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, HomeTypesModel data, int position, int size) {
        ImageHolder imageHolder = (ImageHolder) holder;
        GlideUtils.DrawableTransformCop(context, imageHolder.imageView, data.resId,
                6, false, false, false, false);
    }


}

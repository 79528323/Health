package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.holder.ImageHolder;
import com.gzhealthy.health.model.Admodel;
import com.gzhealthy.health.tool.GlideUtils;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义布局,多个不同UI切换
 */
public class WelcomeBannerAdapter extends BannerAdapter<Integer, RecyclerView.ViewHolder> {

    private Context context;

    public WelcomeBannerAdapter(Context context, List<Integer> mDatas) {
        super(mDatas);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        if (params!=null){
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        parent.setLayoutParams(params);
        return new ImageHolder(BannerUtils.getView(parent, R.layout.welcome_banner_image));
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, Integer integer, int position, int size) {
        ImageHolder imageHolder = (ImageHolder) holder;
        Glide.with(context).load(integer).into(imageHolder.imageView);
//        loadIntoUseFitWidth(context,integer,integer,imageHolder.imageView);
//        GlideUtils.DrawableTransformCop(context, imageHolder.imageView, Constants.Api.ossPicPre + data.getAdImg(),
//                6, false, false, false, false);
    }


    public void loadIntoUseFitWidth(Context context, final int resId, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }
}

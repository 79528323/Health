package com.gzhealthy.health.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.glide.CornerTransform;
import com.gzhealthy.health.tool.glide.GlideCircleTransform;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.annotation.Nonnull;

public class GlideUtils {
    public static void loadCustomeImg(ImageView imageView, int width, double height, String url) {
        Glide.with(imageView.getContext()).load(url).centerCrop().crossFade().placeholder(R.color.window_background_color).override(width, (int) height).into(imageView);
    }

    public static DrawableRequestBuilder loadHolderImg(Context context, int width, double height, String url) {
        return Glide.with(context).load(url).centerCrop().crossFade().placeholder(R.color.window_background_color).override(width, (int) height);
    }

    public static void loadCustomeImg(ImageView imageView, String url) {
        if (url.endsWith("gif") || url.endsWith("GIF")) {
            Glide.with(imageView.getContext()).load(url).asGif().centerCrop().crossFade().into(imageView);
        } else {
            Glide.with(imageView.getContext()).load(url).centerCrop().crossFade().into(imageView);
        }
    }

    public static void loadCustomeImgNoCache(ImageView imageView, String url) {
        DrawableTypeRequest request = Glide.with(imageView.getContext()).load(url);
        request.skipMemoryCache(true);
        request.diskCacheStrategy(DiskCacheStrategy.NONE);
        request.signature(new StringSignature(UUID.randomUUID().toString()));
        if (url.endsWith("gif") || url.endsWith("GIF")) {
            request.asGif().centerCrop().crossFade().into(imageView);
        } else {
            request.centerCrop().crossFade().into(imageView);
        }
    }

    public static void loadCustomeImgGif(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).asGif().
                centerCrop().crossFade().into(imageView);
    }

    public static void loadCustomeImgGif(ImageView imageView, int resId) {
        Glide.with(imageView.getContext()).load(resId).asGif().
                centerCrop().crossFade().into(imageView);
    }

    public static void loadCustomeImg(ImageView imageView, String url, int defaultid) {
        Glide.with(imageView.getContext()).load(url).placeholder(defaultid).centerCrop().crossFade().into(imageView);
    }

    public static void DrawableTransformCop(@Nonnull Context context, ImageView imageView, @Nonnull String url, @Nonnull float coppx, boolean lefttop,
                                            boolean leftbuttom, boolean righttop, boolean rightbottom) {
        CornerTransform transformation = new CornerTransform(context, TDevice.dip2px(context, coppx));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(lefttop, righttop, leftbuttom, rightbottom);
        Glide.with(context).
                load(url).asBitmap().placeholder(R.color.window_background_color).
//                diskCacheStrategy(DiskCacheStrategy.ALL).
        transform(transformation).
                into(imageView);
//        return Glide.with(context).load(url).centerCrop().crossFade().placeholder(R.color.window_background_color).override(width, (int)height);
    }

    public static void DrawableTransformCop(@Nonnull Context context, ImageView imageView, @Nonnull int resId, @Nonnull float coppx, boolean lefttop,
                                            boolean leftbuttom, boolean righttop, boolean rightbottom) {
        CornerTransform transformation = new CornerTransform(context, TDevice.dip2px(context, coppx));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(lefttop, righttop, leftbuttom, rightbottom);
        Glide.with(context).
                load(resId).asBitmap().placeholder(R.color.window_background_color).
//                diskCacheStrategy(DiskCacheStrategy.ALL).
        transform(transformation).
                into(imageView);
//        return Glide.with(context).load(url).centerCrop().crossFade().placeholder(R.color.window_background_color).override(width, (int)height);
    }

    public static void DrawableTransformCop1(@Nonnull Context context, ImageView imageView, @Nonnull Bitmap mBitmap, @Nonnull float coppx,
                                             boolean lefttop, boolean leftbuttom, boolean righttop, boolean rightbottom) {
        CornerTransform transformation = new CornerTransform(context, TDevice.dip2px(context, coppx));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(lefttop, righttop, leftbuttom, rightbottom);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        Glide.with(context).
                load(bytes).
                asBitmap().placeholder(R.color.window_background_color).
                transform(transformation).
                into(imageView);
    }

    public static void CircleImage(Context context, String url, ImageView imageView) {
        RequestManager glideRequest;
        glideRequest = Glide.with(context);
        glideRequest.load(url).asBitmap().placeholder(R.mipmap.ic_details_map_bg)
                .transform(new GlideCircleTransform(context)).into(imageView);
    }
}

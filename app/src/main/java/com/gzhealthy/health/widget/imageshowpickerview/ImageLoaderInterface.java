package com.gzhealthy.health.widget.imageshowpickerview;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Description: 加载图片接口
 */
public interface ImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(Context context, String path, T imageView);

    void displayImage(Context context, @DrawableRes Integer resId, T imageView);

    T createImageView(Context context);
}

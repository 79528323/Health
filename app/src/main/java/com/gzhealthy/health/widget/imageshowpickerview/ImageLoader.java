package com.gzhealthy.health.widget.imageshowpickerview;

import android.content.Context;
import android.widget.ImageView;

/**
 * Description: 加载图片方式
 */
public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }

}

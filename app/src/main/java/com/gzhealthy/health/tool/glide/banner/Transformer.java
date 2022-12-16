package com.gzhealthy.health.tool.glide.banner;

import androidx.viewpager.widget.ViewPager;

import com.gzhealthy.health.tool.glide.banner.transformer.AccordionTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.BackgroundToForegroundTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.CubeInTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.CubeOutTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.DefaultTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.DepthPageTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.FlipHorizontalTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.FlipVerticalTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.ForegroundToBackgroundTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.RotateDownTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.RotateUpTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.ScaleInOutTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.StackTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.TabletTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.ZoomInTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.ZoomOutSlideTransformer;
import com.gzhealthy.health.tool.glide.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}

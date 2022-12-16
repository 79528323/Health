package com.gzhealthy.health.utils;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class WebViewUtils {
    /**
     * 对富文本的图片和视频进行屏幕适配
     *
     * @param str 富文本
     */
    public static String screenAdapter(String str) {
        return str
                .replace("<img", "<img style='max-width:100%;height:auto;'")
                .replace("<video", "<video style='max-width:100%;height:auto;'")
                .replace("<table", "<table style='width:100%;'");
    }
}

package com.gzhealthy.health.widget.imageshowpickerview;

/**
 * Description: 显示数据类的父类，必须继承于该类
 */

public abstract class ImageShowPickerBean {

    public String getImageShowPickerUrl() {
        return setImageShowPickerUrl();
    }

    public int getImageShowPickerDelRes() {
        return setImageShowPickerDelRes();
    }

    /**
     * 为URL赋值，必须重写方法
     *
     * @return
     */
    public abstract String setImageShowPickerUrl();

    /**
     * 为删除label赋值，必须重写方法
     *
     * @return
     */
    public abstract int setImageShowPickerDelRes();


}

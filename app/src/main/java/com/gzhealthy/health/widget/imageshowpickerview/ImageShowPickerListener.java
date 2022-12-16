package com.gzhealthy.health.widget.imageshowpickerview;

import java.util.List;

/**
 * Description: picker点击事件监听
 */

public interface ImageShowPickerListener {

    void addOnClickListener(int remainNum);

    void picOnClickListener(List<ImageShowPickerBean> list, int position, int remainNum);

    void delOnClickListener(int position, int remainNum);
}

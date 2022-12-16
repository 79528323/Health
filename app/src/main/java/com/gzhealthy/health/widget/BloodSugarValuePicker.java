package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;

/**
 * Created by Justin_Liu
 * on 2021/9/22
 */
public class BloodSugarValuePicker {
    OptionsPickerView optionsPickerView;

    ArrayList<String> firstItemList = new ArrayList<>();
    ArrayList<ArrayList<String>> secondItemList = new ArrayList<>();

    public BloodSugarValuePicker(Context context,OnValueSelectedCallBack onValueSelectedCallBack) {
        initdata();
        optionsPickerView = new OptionsPickerView.Builder(context, (options1, options2, options3, v) -> {
            if (onValueSelectedCallBack!=null){
                String value = firstItemList.get(options1) + "." + secondItemList.get(options1).get(options2);
                onValueSelectedCallBack.onSelected(value);
            }

        }).setTitleText("选择血糖")
                .setLabels("•","mmol/L","")
                .isCenterLabel(true)
                .setTitleBgColor(Color.parseColor("#FFFFFFFF"))
                .setBgColor(Color.parseColor("#FFFFFFFF"))
                .setLineSpacingMultiplier(2.0f)
                .setCancelText("取消")
                .setCancelColor(Color.parseColor("#FF999999"))
                .setSubmitText("确定")
                .setSubmitColor(Color.parseColor("#FF333333"))
                .setDividerColor(Color.parseColor("#fff6f6f6"))
                .setContentTextSize(18)
                .setTextColorCenter(Color.parseColor("#ff202020"))
                .setSelectOptions(4,0)
                .build();
        optionsPickerView.setDialogOutSideCancelable(true);
        optionsPickerView.setPicker(firstItemList,secondItemList);
    }

    public void initdata(){
        for (int first=0; first < 19; first++){
            ArrayList<String> itemList = new ArrayList<>();
            for (int second=0; second < 10; second++){
                itemList.add(String.valueOf(second));
            }

            firstItemList.add(String.valueOf(first+1));
            secondItemList.add(itemList);
        }
    }


    public void showPicker(){
        if (optionsPickerView!=null){
            optionsPickerView.show();
        }
    }


    public interface OnValueSelectedCallBack{
        void onSelected(String value);
    }
}

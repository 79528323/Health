package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;

/**
 * Created by Justin_Liu
 * on 2021/9/22
 */
public class BloodPressureValuePicker {
    /**
     * 收缩压
     */
    public static final int TYPE_PRESSURE_SYSTOLIC = 1;

    /**
     * 舒张压
     */
    public static final int TYPE_PRESSURE_DIASTOLIC = 2;

    OptionsPickerView optionsPickerView;

    ArrayList<Integer> firstItemList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> secondItemList = new ArrayList<>();

    int type;

    public BloodPressureValuePicker(Context context, OnValueSelectedCallBack onValueSelectedCallBack,int type) {
        initdata();
        this.type = type;
        optionsPickerView = new OptionsPickerView.Builder(context, (options1, options2, options3, v) -> {
            if (onValueSelectedCallBack!=null){
                String value = String.valueOf(firstItemList.get(options1)  + secondItemList.get(options1).get(options2));//firstItemList.get(options1);
                onValueSelectedCallBack.onSelected(value);
            }

        }).setTitleText(this.type == TYPE_PRESSURE_SYSTOLIC?"选择收缩压":"选择舒张压")
                .setLabels("","mmHg","")
                .isCenterLabel(true)
                .setSelectOptions(this.type == TYPE_PRESSURE_SYSTOLIC?8:4)
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
                .build();
        optionsPickerView.setDialogOutSideCancelable(true);
        optionsPickerView.setPicker(firstItemList,secondItemList);
    }

    public void initdata(){
        for (int position=20; position < 301; position++){
            if (position % 10 == 0){
                ArrayList<Integer> itemList = new ArrayList<>();
                for (int second=0; second < 10; second++){
                    itemList.add(second);
                }
                firstItemList.add(position);
                secondItemList.add(itemList);
            }
        }
//        optionsPickerView.setPicker(firstItemList);
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

package com.gzhealthy.health.utils;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.gzhealthy.health.model.RegionBean;
import com.gzhealthy.health.model.RegionJsonBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 三级联动工具类
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class PickerViewUtils {

    /**
     * 获取省市区数据，需要开启子线程
     */
    public static RegionBean getRegionData() throws IOException {

        ArrayList<String> optionsProvinces = new ArrayList<>();
        ArrayList<ArrayList<String>> optionsCitys = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> optionsAreas = new ArrayList<>();


        String regionStr = ConvertUtils.inputStream2String(
                Utils.getApp().getAssets().open("province.json"),
                "UTF-8"
        );
        ArrayList<RegionJsonBean> regions = GsonUtils.fromJson(regionStr, new TypeToken<ArrayList<RegionJsonBean>>() {
        }.getType());
        for (RegionJsonBean provice : regions) {
            optionsProvinces.add(provice.getName());//添加省份
            ArrayList<String> cityList = new ArrayList<>(); //该省的城市列表
            ArrayList<ArrayList<String>> areaList = new ArrayList<>(); //该省的所有地区列表
            for (RegionJsonBean.CityBean city : provice.getCityList()) { //遍历该省份的所有城市
                cityList.add(city.getName()); //添加城市

                ArrayList<String> cityAreaList = new ArrayList<>();
                cityAreaList.addAll(city.getArea());
                areaList.add(cityAreaList); //添加该市所有地区
            }
            // 添加城市数据
            optionsCitys.add(cityList);
            // 添加地区数据
            optionsAreas.add(areaList);
        }

        RegionBean bean = new RegionBean();
        bean.setProvince(optionsProvinces);
        bean.setCity(optionsCitys);
        bean.setArea(optionsAreas);
        return bean;
    }

    /**
     * 获取三级联动控件
     *
     * @return
     */
    public static OptionsPickerView OptionsPickerView(Context context, String title, OptionsPickerView.OnOptionsSelectListener listener) {
        return new OptionsPickerView.Builder(context, listener)
                .setTitleText(title)
                .setTitleSize(16)
                .setTitleColor(Color.parseColor("#FF333333"))
                .setSubCalSize(15)
                .setCancelColor(Color.parseColor("#FF999999"))
                .setSubmitColor(Color.parseColor("#FF333333"))
                .setTitleBgColor(Color.WHITE)
                .setContentTextSize(20)
                .isCenterLabel(false)
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .build();
    }

    /**
     * 获取三级联动控件
     *
     * @return
     */
    public static OptionsPickerView OptionsPickerView(Context context, String title, String[] label, OptionsPickerView.OnOptionsSelectListener listener) {
        return new OptionsPickerView.Builder(context, listener)
                .setTitleText(title)
                .setTitleSize(16)
                .setTitleColor(Color.parseColor("#FF333333"))
                .setSubCalSize(15)
                .setCancelColor(Color.parseColor("#FF999999"))
                .setSubmitColor(Color.parseColor("#FF333333"))
                .setTitleBgColor(Color.WHITE)
                .setContentTextSize(20)
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .isCenterLabel(false)
                .setLabels(label[0], label[1], label[2])
                .build();
    }

    /**
     * 获取日期控件
     */
    public static TimePickerView getTimePickerView(
            Context context,
            Calendar startDate,
            Calendar endDate,
            Calendar selectData,
            String title,
            TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false)
                .setRangDate(startDate, endDate)
                .setDate(selectData)
                .setTitleText(title)
                .setTitleSize(16)
                .setTitleColor(Color.parseColor("#FF333333"))
                .setSubCalSize(15)
                .setCancelColor(Color.parseColor("#FF999999"))
                .setSubmitColor(Color.parseColor("#FF333333"))
                .setTitleBgColor(Color.WHITE)
                .setContentSize(20)
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .build();
    }
}

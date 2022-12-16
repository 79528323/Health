package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.tool.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/7/28
 */
public class BloodSugarMarkerView extends MarkerView {
    public static final int TYPE_BLOODPRESSURE = 2;
    public static final int TYPE_RATE = 1;
    public static final int TYPE_SPO2 = 3;
    public static final int TYPE_BLOODSUGAR = 5;
    public static final int TYPE_BLOODSUGAR_HUNGER = 6;
    public static final int TYPE_BLOODSUGAR_MEAL = 7;


    TextView date;

    TextView data;

    TextView meal;

    TextView hunger;

    List<HealthyListDataModel.DataBeanX.DataBean> list;

    HealthyListDataModel.DataBeanX model;

//    int type;

    double dif;

    float screenWidth;

    public BloodSugarMarkerView(Context context, HealthyListDataModel.DataBeanX model) {
        super(context, R.layout.blood_sugar_marker_view);
        data = findViewById(R.id.data);
        date = findViewById(R.id.date);
        meal = findViewById(R.id.meal);
        hunger = findViewById(R.id.hunger);
        this.list = model.data;
        this.model = model;
//        this.type = type;
        dif = model.gluDif;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void setOffset(MPPointF offset) {
        super.setOffset(offset);
    }

    @Override
    public void setOffset(float offsetX, float offsetY) {
//        Log.e("222","setOffset offsetX="+offsetX+"   offsetY="+offsetY);
        super.setOffset(offsetX, offsetY);
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
//        Log.e("222","getOffsetForDrawingAtPoint posX="+posX+"   posY="+posY);
        return super.getOffsetForDrawingAtPoint(posX, posY);
    }

    @Override
    public void setChartView(Chart chart) {
        super.setChartView(chart);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
//        Log.e("222","draw posX="+posX+"   posY="+posY);
        float temp = 0;
        if (getWidth() + posX > screenWidth){
            temp = getWidth() + posX;
            temp -= screenWidth;
        }
        temp = Math.abs(temp);
        posX -= temp;
        super.draw(canvas, posX, posY);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth()/2) , -getHeight());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        HealthyListDataModel.DataBeanX.HungerDataBean hungerDataBean = this.model.hungerData.get((int) e.getX());
        hunger.setText(String.valueOf(hungerDataBean.glu));

        HealthyListDataModel.DataBeanX.SugarDataBean sugarDataBean = this.model.sugarData.get((int) e.getX());
        data.setText(String.valueOf(sugarDataBean.glu));

        HealthyListDataModel.DataBeanX.MealDataBean mealDataBean = this.model.mealData.get((int) e.getX());
        meal.setText(String.valueOf(mealDataBean.glu));
//
//        if (this.type == TYPE_BLOODSUGAR){
//
//        }else if (this.type == TYPE_BLOODSUGAR_HUNGER){
//            hungerDataBean = this.model.hungerData.get((int) e.getX());
//        }else if (this.type >= TYPE_BLOODSUGAR_MEAL){
//            mealDataBean = this.model.mealData.get((int) e.getX());
//
//        }
        String time = DateUtil.getStringDate4(sugarDataBean.createTime);
        date.setText(time);


//        switch (this.type){
//            case TYPE_RATE://心率
//                data.setText(String.valueOf(bean.rate));
//                unit.setText("次/分");
//                break;
//
//            case TYPE_BLOODPRESSURE://血压
//                data.setText(bean.high+"/"+bean.low);
//                unit.setText("mmHg");
//                break;
//
//            case TYPE_SPO2://血氧
//                data.setText(String.valueOf(bean.spo2));
//                unit.setText("%/分");
//                break;
//
//            case TYPE_BLOODSUGAR://血糖
//                data.setText((sugarDataBean.glu-dif)+"-"+(sugarDataBean.glu+dif));
//                unit.setText("mmol/L");
//                break;
//
//            case TYPE_BLOODSUGAR_HUNGER://餐前血糖
//                data.setText((hungerDataBean.glu-dif)+"-"+(hungerDataBean.glu+dif));
//                unit.setText("mmol/L");
//                break;
//
//            case TYPE_BLOODSUGAR_MEAL://餐后血糖
//
//                break;
//        }
        super.refreshContent(e, highlight);
    }

    private String convertGLu(double glu){
        String min = null, max = null;
        if (glu <= 0 )
            return String.valueOf(glu);
        else if (glu <= dif){
            min= String.valueOf(glu);
            max= formatDouble(glu + dif);
        }else {
            min= formatDouble(glu - dif);
            max= formatDouble(glu + dif);
        }

        return min+" - "+max;
    }


    public String formatDouble(double d){
        return String.format("%.2f",d);
    }
}

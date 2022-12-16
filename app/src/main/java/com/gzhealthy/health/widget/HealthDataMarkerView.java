package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
public class HealthDataMarkerView  extends MarkerView {
    public static final int TYPE_BLOODPRESSURE = 2;
    public static final int TYPE_RATE = 1;
    public static final int TYPE_SPO2 = 3;
    public static final int TYPE_BLOODSUGAR = 5;


    TextView date;

    TextView data;

    TextView unit;

    List<HealthyListDataModel.DataBeanX.DataBean> list;

    int type;

    double dif;

    float screenWidth;

    public HealthDataMarkerView(Context context, HealthyListDataModel.DataBeanX model,int type) {
        super(context, R.layout.health_data_marker_view);
        data = findViewById(R.id.data);
        date = findViewById(R.id.date);
        unit = findViewById(R.id.unit);
        this.list = model.data;
        this.type = type;
        dif = model.gluDif;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void setOffset(MPPointF offset) {
        super.setOffset(offset);
    }

    @Override
    public void setOffset(float offsetX, float offsetY) {
        Log.e("222","setOffset offsetX="+offsetX+"   offsetY="+offsetY);
        super.setOffset(offsetX, offsetY);
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        Log.e("222","getOffsetForDrawingAtPoint posX="+posX+"   posY="+posY);
        return super.getOffsetForDrawingAtPoint(posX, posY);
    }

    @Override
    public void setChartView(Chart chart) {
        super.setChartView(chart);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Log.e("222","draw posX="+posX+"   posY="+posY);
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
        int position;
        if (list.size() <= 1){
            position = 0;
        }else {
            position = (int) e.getX();
        }
        HealthyListDataModel.DataBeanX.DataBean bean = list.get(position);
        String time = DateUtil.getStringDate4(bean.createTime);
        date.setText(time);
        switch (this.type){
            case 1://心率
                data.setText(String.valueOf(bean.rate));
                unit.setText("次/分");
                break;

            case 2://血压
                data.setText(bean.high+"/"+bean.low);
                unit.setText("mmHg");
                break;

            case 3://血氧
                data.setText(String.valueOf(bean.spo2));
                unit.setText("%/分");
                break;

            case 5://血糖
                data.setText((bean.glu-dif)+"-"+(bean.glu+dif));
                unit.setText("mmol/L");
                break;
        }
        super.refreshContent(e, highlight);
    }


    private String converDate(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(time);
        return format.format(date);
    }
}

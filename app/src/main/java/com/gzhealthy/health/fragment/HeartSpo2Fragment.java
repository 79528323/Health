package com.gzhealthy.health.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.contract.HealthyDataContract;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.presenter.HealthyDataPresenter;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.HealthDataMarkerView;
import com.gzhealthy.health.widget.HealthDateChoiceView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;


/**
 * 血氧
 */
public class HeartSpo2Fragment extends BaseFra implements HealthyDataContract.View{

    Map<String,String> param = new HashMap<>();
    TextView oxy;
    TextView oxy_highest;
    TextView oxy_lowest;
//    LineChart linechart;
    private HealthyDataPresenter presenter;
//    private HealthDateChoiceView healthDateChoiceView;
//    private CalendarPopWindow popWindow;
    private String hour = "";
    boolean isValues = false;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_oxygen_layout;
    }

    @Override
    protected void widgetClick(View view) {

    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        presenter = new HealthyDataPresenter(this,this,this);
        oxy_highest = (TextView) $(R.id.oxy_highest);
        oxy_lowest = (TextView) $(R.id.oxy_lowest);
        oxy = (TextView) $(R.id.min_oxy);
        lineChart = (LineChart) $(R.id.linechart);
        initChart(lineChart);
        lineChart.setDragEnabled(true);//拖动
        lineChart.setTouchEnabled(true);//触摸

//        healthDateChoiceView=(HealthDateChoiceView) $(R.id.healthChoiceView);

        rxManager.onRxEvent(
                RxEvent.ON_SELECT_HEALTHY_DATE)
                .subscribe(o -> {
                    String day = (String) o;
                    isValues = false;
                    param.put("dateTime" , day);
                    presenter.getHealthyInfo(param);
                });

        param.put("dateTime" , DateUtil.getStringDate());
        param.put("token", SPCache.getString("token",""));
        param.put("type","4");
        presenter.getHealthyInfo(param);

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (!menuVisible){
//            popWindow.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-血氧详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-血氧详情");
    }

    /**
     * 抛物线
     * @param model
     */
    public void showLineChart(HealthyListDataModel.DataBeanX model,LineChart lineChart) {
        /**
         * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
         * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
         */
        List<Entry> entries = new ArrayList<>();
        int size = 0;
        float hight = 0;
        if (model !=null){
            size = model.data.size();
            for (int i = 0; i < size; i++) {
                HealthyListDataModel.DataBeanX.DataBean bean =  model.data.get(i);
                float y = (float)(bean.spo2);
                if (y>hight)
                    hight = y;
                Entry entry = new Entry(i, y);
                entries.add(entry);
            }

            HealthDataMarkerView markerView = new HealthDataMarkerView(getActivity(),model,HealthDataMarkerView.TYPE_SPO2);
            lineChart.setMarker(markerView);
        }else {

            Entry entry = new Entry(0, 0);
            entries.add(entry);
        }


        // 每一个LineDataSet代表一条线
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initLineDataSet(highlineDataSet,
                Color.parseColor("#00DDCB"),
                LineDataSet.Mode.CUBIC_BEZIER);
        highlineDataSet.setLineWidth(2f);
        highlineDataSet.setDrawCircles(false);
        highlineDataSet.setValueFormatter(new MyIValueFormatter((int) hight));
        LineData highlineData = new LineData(highlineDataSet);
        lineChart.setData(highlineData);


        //设置最大值，避免图线最高点显示不完整
        mHightOffset =8;
        float axisMaximum = 100f;
        lineChart.getAxisLeft().setAxisMaximum(axisMaximum);
        lineChart.getAxisLeft().setAxisMinimum(50);
        lineChart.getAxisLeft().setLabelCount(5);
//        lineChart.getAxisLeft().setXOffset(XOffset);
        lineChart.setScaleMinima(size>9?scaleXOffset:0f,0f);
        lineChart.moveViewToX(size-1);

        //Y轴值
        lineChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int count = (int)value;
                if (count > 100)//血氧含量最大率为100%，超过不显示
                    return "";

                return count % 10==0 ? (int)value+"%":"";
            }
        });
    }

    @Override
    public void getInfoSuccess(HealthyListDataModel model) {
        oxy.setText("血氧" +model.data.avgSpo2 + "%");
        oxy_highest.setText( model.data.highestSpo2+"%");
        oxy_lowest.setText( model.data.lowestSpo2+"%");

        cleanLineCharts();
        xAxis.setLabelCount(model.data.data.size());
        xAxis.setValueFormatter(new MyXaisValueFormatter(model.data.data));
        showLineChart(model.data,lineChart);
        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.shape_oxy_gradient);
        setChartFillDrawable(drawable);
    }

    @Override
    public void getInfoFail(String msg) {
        oxy.setText("血氧");
        oxy_highest.setText( "%");
        oxy_lowest.setText( "%");
        hour = "";
        cleanLineCharts();
        showLineChart(null,lineChart);
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }

    @Override
    public void showLoading() {
        showWaitDialog();
    }

    @Override
    public void dimissLoading() {
        hideWaitDialog();
    }


    private class MyIValueFormatter implements IValueFormatter {
        int highValue;

        public MyIValueFormatter(int highValue) {
            this.highValue = highValue;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1 && !isValues){
                Drawable drawable = getActivity().getResources().getDrawable(R.drawable.rate_circle_big_bg);
                entry.setIcon(drawable);
                isValues = true;
                return String.valueOf((int)value);
            }
            return "";
        }
    }
}

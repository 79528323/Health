package com.gzhealthy.health.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 心率
 */
public class HeartRateFragment extends BaseFra implements HealthyDataContract.View{

    Map<String,String> param = new HashMap<>();
    TextView start_date;
    TextView end_date;
    TextView rate_hight;
    TextView rate_low;
    TextView avgs;
    TextView rates;
//    LineChart linechart;
    private HealthyDataPresenter presenter;
    private String hour = "";
    boolean isValues = false;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_heart_rate_layout;
    }

    @Override
    protected void widgetClick(View view) {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        presenter = new HealthyDataPresenter(this,this,this);
        start_date = (TextView) $(R.id.start_date);
        end_date = (TextView) $(R.id.end_date);
        rate_hight = (TextView) $(R.id.rate_hight);
        rate_low = (TextView) $(R.id.rate_low);
        avgs = (TextView) $(R.id.rate_avgs);
        rates = (TextView) $(R.id.min_rates);
        lineChart = (LineChart) $(R.id.linechart);
        initChart(lineChart);
        lineChart.setDragEnabled(true);//拖动
        lineChart.setTouchEnabled(true);//触摸

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
        param.put("type","2");
        presenter.getHealthyInfo(param);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (!menuVisible){
//            if (popWindow!=null)
//                popWindow.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-心率详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-心率详情");
    }

    /**
     * 抛物线
     * @param model
     */
    public void showLineChart(HealthyListDataModel.DataBeanX model,LineChart lineChart) {
        float highest = 0;
        float lowest = 0;
        int avg = 0;
        int size = 0;
        List<Entry> entries = new ArrayList<>();

        if (model!=null){
            size = model.data.size();
            Drawable drawable = getActivity().getResources().getDrawable(R.drawable.rate_circle_bg);
            for (int i = 0; i < size; i++) {
                HealthyListDataModel.DataBeanX.DataBean bean =  model.data.get(i);
                /**
                 * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
                 * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
                 */
                float y = (float)(bean.rate);
                if (y > highest)
                    highest = y;

                if (lowest == 0)
                    lowest = y;

                if (y < lowest){
                    lowest = y;
                }

                avg += (int)y;

                Entry entry = new Entry(i, y);
                entries.add(entry);
            }
            avg = avg/entries.size();//平均数

            HealthDataMarkerView markerView = new HealthDataMarkerView(getActivity(),model,HealthDataMarkerView.TYPE_RATE);
            lineChart.setMarker(markerView);

        }else {
            //假数据
            Entry entry = new Entry(0, -1);
            entries.add(entry);
        }


        //设置最大值，避免图线最高点显示不完整
//        int axisMax = Math.round(highest + mHightOffset);
//        while (axisMax % 20 !=0){
//            axisMax+=1;
//        }

        // 每一个LineDataSet代表一条线
        int color = Color.parseColor("#00DDCB");//ContextCompat.getColor(getActivity(),R.color.linechart_line_color);
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initLineDataSet(highlineDataSet, color, LineDataSet.Mode.HORIZONTAL_BEZIER);
        highlineDataSet.setValueTextSize(17f);
        highlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        highlineDataSet.setLineWidth(4f);
        highlineDataSet.setCircleRadius(0f);
        highlineDataSet.setDrawValues(true);

        highlineDataSet.setValueFormatter(new MyIValueFormatter((int) highest));
        LineData highlineData = new LineData(highlineDataSet);
        lineChart.setData(highlineData);



        //Y轴值
        lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
            int count = (int)value;
            return count% 50==0?String.valueOf(count):"";
        });

        //心率峰顶值
        float axisMaximum = 200f;
        int count = 4;//axisMax/20;
        lineChart.getAxisLeft().setAxisMaximum(axisMaximum);
        lineChart.getAxisLeft().setLabelCount(count);
//        lineChart.getAxisLeft().setXOffset(XOffset);

        lineChart.setScaleMinima(size>9?scaleXOffset:0f,0f);
        if (size < 5){
            lineChart.zoomOut();
        }
        lineChart.moveViewToX(size-1);

        avgs.setText(avg+"次/分");
        rate_hight.setText((int)highest+"次/分");
        rate_low.setText((int)lowest+"次/分");
    }

    @Override
    public void getInfoSuccess(HealthyListDataModel model) {
        cleanLineCharts();
        xAxis.setLabelCount(model.data.data.size());
        xAxis.setValueFormatter(new MyXaisValueFormatter(model.data.data));
        showLineChart(model.data,lineChart);
        rates.setText("心率"+model.data.avgRate+"次/分");
    }

    @Override
    public void getInfoFail(String msg) {
        rates.setText("心率 -- 次/分");
        hour = "";
        avgs.setText( "次/分");
        rate_hight.setText( "次/分");
        rate_low.setText( "次/分");
        cleanLineCharts();
        showLineChart(null,lineChart);
//        ToastUtil.showToast(msg);
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

    private class MyIValueFormatter implements IValueFormatter{
        int highValue;

        public MyIValueFormatter(int highValue) {
            this.highValue = highValue;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1 && !isValues){
                Drawable drawable = getActivity().getDrawable(R.drawable.rate_circle_big_bg);//getActivity().getResources().getDrawable(R.drawable.circle_low_big_legend);
                entry.setIcon(drawable);
                isValues = true;
                return String.valueOf((int)value);
            }
            return "";
        }
    }
}

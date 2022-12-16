package com.gzhealthy.health.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
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
import com.gzhealthy.health.activity.BloodPressureRecordAddActivity;
import com.gzhealthy.health.activity.report.HealthyReportResultActivity;
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
 * 血压
 */
public class HealthBloodPressureFragment extends BaseFra implements HealthyDataContract.View {

    private TextView tx_high,tx_low;
//    @BindView(R.id.healthChoiceView)
//    HealthDateChoiceView healthDateChoiceView;
    private HealthyDataPresenter presenter;

    @BindView(R.id.highest)
    TextView tv_highest;
    @BindView(R.id.lowest)
    TextView tv_lowest;
    @BindView(R.id.rercord_add)
    ImageView rercord_add;

    @BindView(R.id.title)
    TextView tv_title;
//    private CalendarPopWindow popWindow;
    boolean isHightValues = false;
    boolean isLowValues = false;

    public HealthBloodPressureFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        presenter = new HealthyDataPresenter(this,this,this);
        Map<String,String> param = new HashMap<>();
        param.put("dateTime" , DateUtil.getStringDate());
        param.put("token", SPCache.getString("token",""));
        param.put("type","1");
        presenter.getHealthyInfo(param);

        tx_high = (TextView) $(R.id.tx_high);
        tx_low = (TextView) $(R.id.tx_low);
        rercord_add.setOnClickListener(this::widgetClick);
        lineChart = (LineChart) $(R.id.linechart);
        initChart(lineChart);
        lineChart.setDragEnabled(true);//拖动
        lineChart.setTouchEnabled(true);//触摸

        rxManager.onRxEvent(
                RxEvent.ON_SELECT_HEALTHY_DATE)
                .subscribe(o -> {
                    String day = (String) o;
                    isHightValues = false;
                    isLowValues = false;
                    param.put("dateTime" , day);
                    presenter.getHealthyInfo(param);
                });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_health_bloodpressure_layout;
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()){
            case R.id.rercord_add:
                BloodPressureRecordAddActivity.instance(getActivity());
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
        if (!menuVisible){
//            popWindow.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-血压详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-血压详情");
    }


    /**
     * 构造曲线展示
     * @param model
     */
    public void showLineChart(HealthyListDataModel model,LineChart lineChart) {
        List<Entry> entriesHigh = new ArrayList<>();
        List<Entry> entrieslow = new ArrayList<>();
        int size =  0;
        float high = 0,low = 0;

        if (model !=null){
            List<HealthyListDataModel.DataBeanX.DataBean> dataBeanList = model.data.data;
            size = dataBeanList.size();
            for (int i = 0; i < size; i++) {
                HealthyListDataModel.DataBeanX.DataBean bean =  dataBeanList.get(i);
                /**
                 * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
                 * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
                 */
                if (dataBeanList.size() <= 1){
                    //当数据只有一个时
                    entriesHigh.add(new Entry(i+1, (float)(bean.high)));
                    entrieslow.add(new Entry(i+1, (float)(bean.low)));
                }else {
                    entriesHigh.add(new Entry(i, (float)(bean.high)));
                    entrieslow.add(new Entry(i, (float)(bean.low)));
                }

                float y = (float)(bean.high);
                if (y > high)
                    high = y;

                float y1 = (float)bean.low;
                if (y1 > low)
                    low = y1;
            }


            HealthDataMarkerView markerView = new HealthDataMarkerView(getActivity(),model.data,
                    HealthDataMarkerView.TYPE_BLOODPRESSURE);
            lineChart.setMarker(markerView);
        }else {

            entriesHigh.add(new Entry(0, 0));
            entrieslow.add(new Entry(0, 0));
        }


        tv_title.setText("血压"+(int)high+"/"+(int)low+"mmHg");
        //设置最大值，避免图线最高点显示不完整
//        int axisMax = Math.round(high + mHightOffset);
//        while (axisMax % 20 !=0){
//            axisMax+=1;
//        }
        lineChart.getAxisLeft().setAxisMaximum(300f);
        lineChart.getAxisLeft().setAxisMinimum(30f);
        lineChart.getAxisLeft().setLabelCount(10);

        LineDataSet highlineDataSet = new LineDataSet(entriesHigh, "收缩压");
        highlineDataSet.setHighLightColor(Color.parseColor("#00000000"));
        initPressureLineDataSet(highlineDataSet, Color.parseColor("#00A7FF"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        LineData highlineData = new LineData(highlineDataSet);
        highlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        highlineDataSet.setDrawValues(true);
        highlineData.setValueFormatter(new PressureHighIValueFormatter((int) high,R.drawable.circle_high_big_legend));
        lineChart.setData(highlineData);



        LineDataSet lowlineDataSet = new LineDataSet(entrieslow, "舒张压");
        lowlineDataSet.setHighLightColor(Color.parseColor("#00000000"));
        initPressureLineDataSet(lowlineDataSet, Color.parseColor("#00DDCB"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        lowlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        lowlineDataSet.setDrawValues(true);
        lowlineDataSet.setValueFormatter(new PressureLowIValueFormatter((int) low,R.drawable.circle_low_big_legend));
        lineChart.getLineData().addDataSet(lowlineDataSet);
        lineChart.invalidate();

        //Y轴值
        lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
            int count = (int)value;
            return count% 10==0?String.valueOf(count):"";
        });


        lineChart.moveViewToX(size-1);
//        int count = axisMax/20;
//        lineChart.getAxisLeft().setLabelCount(count);
//        lineChart.getAxisLeft().setXOffset(10f);

        lineChart.setScaleMinima(size>9?scaleXOffset:0f,0f);
    }


    @Override
    public void getInfoSuccess(HealthyListDataModel model) {

//        check_pre.setText("测量血压 "+model.data.time+"小时 "+model.data.num+"次");
//        avg_pre.setText("平均 "+model.data.avgHigh+"/"+model.data.avgLow+" mmHg");

        tv_highest.setText(model.data.highestHigh+"/"+model.data.highestLow+"mmHg");
        tv_lowest.setText(model.data.lowestHigh+"/"+model.data.lowestLow+"mmHg");
        cleanLineCharts();
        xAxis.setLabelCount(model.data.data.size());
        xAxis.setValueFormatter(new MyXaisValueFormatter(model.data.data));
        showLineChart(model,lineChart);
    }

    @Override
    public void getInfoFail(String msg) {
        tv_title.setText("血压 -- mmHg");
        tx_low.setText(getResources().getString(R.string.lowmmHg,0,0));
        tx_high.setText(getResources().getString(R.string.highmmHg,0,0));
        tv_highest.setText(0+"mmHg");
        tv_lowest.setText(0+"mmHg");
//        xAxis.setLabelCount(0);
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


    private class PressureHighIValueFormatter implements IValueFormatter {
        int highValue;
        int drawResId;

        public PressureHighIValueFormatter(int highValue,int drawResId) {
            this.highValue = highValue;
            this.drawResId = drawResId;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1 && !isHightValues){
                Drawable drawable = getResources().getDrawable(drawResId,null);
                entry.setIcon(drawable);
                isHightValues = true;
                return String.valueOf((int)value);
            }
            return "";
        }
    }

    private class PressureLowIValueFormatter implements IValueFormatter {
        int highValue;
        int drawResId;

        public PressureLowIValueFormatter(int highValue,int drawResId) {
            this.highValue = highValue;
            this.drawResId = drawResId;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1 && !isLowValues){
                Drawable drawable = getResources().getDrawable(drawResId,null);
                entry.setIcon(drawable);
                isLowValues = true;
                return String.valueOf((int)value);
            }
            return "";
        }
    }


    protected void initPressureLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(4f);
        lineDataSet.setCircleRadius(2f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(3.5f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setDrawCircleHole(false);
        //值显示
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                Log.e("111","value="+value+"  entry.x="+entry.getX()+"  dataSetIndex="+dataSetIndex);
                return null;
            }
        });
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

}







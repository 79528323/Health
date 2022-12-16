package com.gzhealthy.health.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gzhealthy.health.activity.BloodSugarRecordAddActivity;
import com.gzhealthy.health.activity.HealthBodyInfoActivity;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.contract.HealthyDataContract;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.presenter.HealthyDataPresenter;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.widget.BloodSugarMarkerView;
import com.gzhealthy.health.widget.HealthDataMarkerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;


/**
 * 血糖
 */
public class BloodSugarFragment extends BaseFra implements HealthyDataContract.GluView{

    Map<String,String> param = new HashMap<>();
    TextView start_date;
    TextView end_date;
    TextView sugar_hight;
    TextView sugar_low;
    TextView avg_sugar;
    TextView sugar_desc;
    TextView sugar_reference;
//    TextView glu_flag;
//    LinearLayout glu_flag_lay;
    private HealthyDataPresenter presenter;
    private String hour = "";
    boolean isValues = false;
    ImageView rercord_add;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_blood_sugar_layout;
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()){
            case R.id.rercord_add:
                BloodSugarRecordAddActivity.instance(getActivity());
                break;
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        presenter = new HealthyDataPresenter(this,this,this);
        start_date = (TextView) $(R.id.start_date);
        end_date = (TextView) $(R.id.end_date);
        sugar_hight = (TextView) $(R.id.sugar_hight);
        sugar_low = (TextView) $(R.id.sugar_low);
        avg_sugar = (TextView) $(R.id.avg_sugar);
        sugar_desc = (TextView) $(R.id.sugar_desc);
        sugar_reference = (TextView) $(R.id.sugar_reference);
//        glu_flag = (TextView) $(R.id.glu_flag);
////        glu_flag_lay = (LinearLayout) $(R.id.glu_flag_lay);
//        glu_flag.setOnClickListener(v -> {
//            HealthBodyInfoActivity.instance(getActivity());
//        });
        rercord_add = (ImageView) $(R.id.rercord_add);
        rercord_add.setOnClickListener(this::widgetClick);

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
                    presenter.getGluInfo(param);
                });

        param.put("dateTime" , DateUtil.getStringDate());
        param.put("token", SPCache.getString("token",""));
        param.put("type","5");
        presenter.getGluInfo(param);

//        rxManager.onRxEvent(RxEvent.ON_GLU_RECORD_IS_VISIABLE)
//                .subscribe(o -> {
//                    boolean isVisiable = (Boolean) o;
////                    glu_flag_lay.setVisibility(isVisiable?View.VISIBLE:View.GONE);
//                });
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
        MobclickAgent.onPageStart("首页-血糖详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-血糖详情");
    }

    /**
     * 抛物线
     * @param model
     */
    public void showLineChart(HealthyListDataModel.DataBeanX model,LineChart lineChart) {
        float highest = 0;
        List<Entry> entryArrayList = null;
        int size = 0;
        HealthyListDataModel.DataBeanX.HungerDataBean hungerDataBean;
        HealthyListDataModel.DataBeanX.SugarDataBean sugarDataBean;
        HealthyListDataModel.DataBeanX.MealDataBean mealDataBean;
        Drawable drawable = null;

        if (model != null){
            size = model.sugarData.size();
            for (int index = 0; index < 3; index++){
                entryArrayList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    float y = 0;
                    if (index == 0){
                        hungerDataBean = model.hungerData.get(i);
                        y = (float)(hungerDataBean.glu);
                        drawable = getActivity().getResources().getDrawable(R.drawable.shape_indicator_hunger_big);
                    }else if (index == 1){
                        mealDataBean = model.mealData.get(i);
                        drawable = getActivity().getResources().getDrawable(R.drawable.shape_indicator_meal_big);
                        y = (float)(mealDataBean.glu);
                    }else {
                        sugarDataBean = model.sugarData.get(i);
                        y = (float)(sugarDataBean.glu);
                        drawable = getActivity().getResources().getDrawable(R.drawable.shape_indicator_sugar_big);
                    }

                    if (y > highest)
                        highest = y;

                    /**
                     * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
                     * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
                     */
                    Entry entry = new Entry(i, y,drawable);
                    entryArrayList.add(entry);
                }

                int color = 0;
                Drawable fill=null;
                if (index == 0){
                    color = ContextCompat.getColor(getActivity(),R.color.glu_hunger);
                    fill = ContextCompat.getDrawable(getActivity(),R.drawable.shape_hunger_gradient);
                }else if (index == 1){
                    color = ContextCompat.getColor(getActivity(),R.color.glu_meal);
                    fill = ContextCompat.getDrawable(getActivity(),R.drawable.shape_meal_gradient);
                }else {
                    color = ContextCompat.getColor(getActivity(),R.color.glu_sugar);
                    fill = ContextCompat.getDrawable(getActivity(),R.drawable.shape_sugar_gradient);
                }
                LineDataSet dataSet = getLine(entryArrayList, color,LineDataSet.Mode.LINEAR,fill);
                dataSet.setValueFormatter(new MyIValueFormatter((int) highest));
                LineData lineData = new LineData(dataSet);
                if (index == 0)
                    lineChart.setData(lineData);
                else {
                    lineChart.getLineData().addDataSet(dataSet);
                }
                lineChart.invalidate();
            }

            //血糖浮窗
            BloodSugarMarkerView markerView = new BloodSugarMarkerView(getActivity(),model);
            lineChart.setMarker(markerView);

        }else {
            //没数据时
            entryArrayList = new ArrayList<>();
            Entry entry = new Entry(0, 0);
            entryArrayList.add(entry);

            LineDataSet dataSet = getLine(entryArrayList, ContextCompat.getColor(getActivity(),R.color.glu_hunger)
                    ,LineDataSet.Mode.LINEAR,ContextCompat.getDrawable(getActivity(),R.drawable.shape_meal_gradient));
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate();
        }

        //Y轴值
        lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
            int count = (int)value;
            if (count < 0)
                return "";
            return count% 5==0?String.valueOf(count):"";
        });


//        int count = axisMax/20;
        lineChart.getAxisLeft().setLabelCount(5);
        lineChart.getAxisLeft().setAxisMaximum(25);
        lineChart.getAxisLeft().setAxisMinimum(0);
//        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisLeft().setDrawZeroLine(true);
        lineChart.moveViewToX(size-1);

        lineChart.setScaleMinima(size>9?scaleXOffset:0f,0f);
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }

    @Override
    public void getInfoRespone(HealthyListDataModel model) {
        cleanLineCharts();
        if (model!=null){
            if (model.code == 1){
                xAxis.setLabelCount(model.data.sugarData.size());
                xAxis.setValueFormatter(new MyXaisValueFormatter(model.data.sugarData));
                showLineChart(model.data,lineChart);
                avg_sugar.setText("血糖"+model.data.avgGlu+"mmol/L");
                sugar_hight.setText(String.valueOf(model.data.highestGlu));
                sugar_low.setText(String.valueOf(model.data.lowestGlu));

                sugar_desc.setText(model.data.description);
                sugar_reference.setText(
                        model.data.reference.get(0).condition+":  "+model.data.reference.get(0).min+"-"+model.data.reference.get(0).max+"mmol/L");
            }
        } else {
            avg_sugar.setText("血糖 -- mmol/L");
            hour = "";
            sugar_hight.setText( "0");
            sugar_low.setText( "0");
            lineChart.setMarker(null);
            xAxis.setLabelCount(0);
            xAxis.setValueFormatter(new MyXaisValueFormatter(null));
            showLineChart(null,lineChart);
        }

//        glu_flag_lay.setVisibility(model.data.gluFlag==1?View.VISIBLE:View.GONE);
    }

    @Override
    public void getInfoFail(String msg) {

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
//            if (highValue - (int)value < 0.1 && !isValues){
//                Drawable drawable = getResources().getDrawable(R.drawable.rate_circle_big_bg,null);
//                entry.setIcon(drawable);
//                isValues = true;
//                return String.valueOf((int)value);
//            }
            return "";
        }
    }


    protected class MyXaisValueFormatter implements IAxisValueFormatter {
        List<HealthyListDataModel.DataBeanX.SugarDataBean> dataBeanList;
        String hour ="";

        public MyXaisValueFormatter(List<HealthyListDataModel.DataBeanX.SugarDataBean> dataBeanList) {
            this.dataBeanList = dataBeanList;
            hour ="";
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (dataBeanList == null){
                return "";
            }
            int index = (int)value;
            if (index < 0 || index >= dataBeanList.size()) {
                //TODO 防止value为-1
                return "";
            }

            if (index == 0){
                //TODO 防止这个回调多次刷新导致hour初始值判断失效引发刻度时间为空
                hour = "";
            }

            if (dataBeanList!=null && !dataBeanList.isEmpty()){
                String time = DateUtil.getStringDateHour(dataBeanList.get(index).createTime);
                if (!TextUtils.equals(time, hour)){
                    //防止一小时内多个刻度重复显示
                    hour = time;
                    return getTimeDesc(hour);
                }
            }

            return "";
        }
    }


    public LineDataSet getLine(List<Entry> entries,int color,LineDataSet.Mode mode,Drawable filled){
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        initLineDataSet(lineDataSet, color, mode);
        lineDataSet.setValueTextSize(17f);
        lineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(0f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(filled);
        return lineDataSet;
    }
}

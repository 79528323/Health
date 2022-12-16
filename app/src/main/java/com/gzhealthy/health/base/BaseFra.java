package com.gzhealthy.health.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.widget.HealthDateChoiceView;

import java.util.List;

public abstract class BaseFra extends BaseFragment implements View.OnClickListener, LifeSubscription, ResponseState {

    protected LineChart lineChart;
    protected XAxis xAxis;                //X轴
    protected YAxis leftYAxis;            //左侧Y轴
    protected YAxis rightYaxis;           //右侧Y轴
    protected Legend legend;              //图例
    /**
     * 最高值添加的偏移量，撑起图表，防止最高值无法显示完整
     */
    protected float mHightOffset = 30f;
    protected float scaleXOffset = 1.5f;
    protected float XOffset = 3f;
    @Override
    public void setState(int state) {
        loadingPageView.state = state;
        loadingPageView.showPage();

    }

    public void setDateView(HealthDateChoiceView healthDateChoiceView,HealthDateChoiceView.OnDateChoiceListener onDateChoiceListener){
//        if (healthDateChoiceView!=null){
//            healthDateChoiceView.setOnDateChoiceListener(onDateChoiceListener);
//        }
    }


    /**
     * 初始化图表
     */
    protected void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //是否双击放大
        lineChart.setDoubleTapToZoomEnabled(false);
        //是否竖向缩放
        lineChart.setScaleYEnabled(false);
        //是否横向缩放
        lineChart.setScaleEnabled(false);
        //设置XY轴动画效果
        lineChart.animateY(0);
        lineChart.animateX(1500);

        //隐藏点击时高亮的十字线
//        lineChart.setHighlightPerDragEnabled(false);
//        lineChart.setHighlightPerTapEnabled(false);


        //隐藏label
        Description description= new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        //开启x轴刻度线
        xAxis.setmIsDrawScale(true);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);//设置X轴坐标之间的最小间隔
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);//是否开启在X轴上的竖线



        leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setDrawGridLines(true);//是否开启Y轴上的横线
        leftYAxis.setGridLineWidth(0.5f);
        leftYAxis.setGridColor(ContextCompat.getColor(getActivity(),R.color.gray_line));
        leftYAxis.setDrawZeroLine(true);
//        leftYAxis.setZeroLineWidth(81f);
//        leftYAxis.setAxisLineWidth(51f);
        leftYAxis.setDrawAxisLine(false);//Y轴边线
        leftYAxis.setmIsDrawScale(false);
//        leftYAxis.enableAxisLineDashedLine(10f,10f,10f);



        rightYaxis = lineChart.getAxisRight();
//        //保证Y轴从0开始，不然会上移一点
//        rightYaxis.setAxisMinimum(0f);
////        xAxis.setDrawGridLines(false);
//        rightYaxis.setDrawGridLines(false);

        rightYaxis.setEnabled(false);



        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        legend.setEnabled(false);
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }



    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    protected void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(2f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        //设置高亮十字线颜色
        lineDataSet.setHighLightColor(Color.parseColor("#00000000"));
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


    protected class MyXaisValueFormatter implements IAxisValueFormatter {
        List<HealthyListDataModel.DataBeanX.DataBean> dataBeanList;
        String hour ="";

        public MyXaisValueFormatter(List<HealthyListDataModel.DataBeanX.DataBean> dataBeanList) {
            this.dataBeanList = dataBeanList;
            hour ="";
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (dataBeanList == null)
                return "";

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


    protected class MyYaisValueFormatter implements IAxisValueFormatter {
        String flag;

        public MyYaisValueFormatter(String flag) {
            this.flag = flag;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int count = (int)value;
            return count % 20==0?String.valueOf((int)value+flag):"";
        }
    }


    protected String getTimeDesc(String str){
        return getActivity().getResources().getString(R.string.times,str);
    }


    /**
     * 曲线内填充背景
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable){
        if (lineChart.getData()!=null && lineChart.getData().getDataSetCount() > 0){
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在initLineDataSet()方法中设置了LineDataSet.setDrawFilled(false)；而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 须每次刷新数据时调用，清除上一次Entry值,
     *  否则IAxisValue循环数据列表时会重用上一次的值
     */
    protected void cleanLineCharts(){
        if (!lineChart.isEmpty())
            lineChart.clearValues();
        lineChart.clearAllViewportJobs();
        lineChart.clear();
        lineChart.removeAllViews();
//        lineChart.getAxisLeft().removeAllLimitLines();
        lineChart.getXAxis().removeAllLimitLines();
        lineChart.getXAxis().setmIsDrawScale(false);
        lineChart.getXAxis().setmEntries(new float[]{});
        lineChart.getXAxis().setmEntryCount(0);
//        lineChart.getXAxis().setmLabelList(null);

        xAxis.setLabelCount(0);
        xAxis.setValueFormatter(new MyXaisValueFormatter(null));
    }



    protected boolean isUserLogin(){
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        return !TextUtils.isEmpty(token);
    }

    protected String getUserToken(){
        return SPCache.getString(SPCache.KEY_TOKEN,"");
    }
}

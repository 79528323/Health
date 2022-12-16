package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.tool.DateUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 心电内页
 */
public class ECGActivity extends BaseAct {
    @BindView(R.id.ekg_linechart)
    public LineChart lineChart;
    @BindView(R.id.date)
    public TextView date;
    @BindView(R.id.avg_rate)
    public TextView avg_rate;
    @BindView(R.id.tx_af)
    public TextView tx_af;
    @BindView(R.id.tx_apb)
    public TextView tx_apb;
    @BindView(R.id.tx_atrialFlutter)
    public TextView tx_atrialFlutter;
    @BindView(R.id.tx_heartEcg)
    public TextView tx_heartEcg;
    @BindView(R.id.tx_vpb)
    public TextView tx_vpb;

    private float mHightOffset = 100;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_ekg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("心电报告");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        EKGModel.DataBean model = (EKGModel.DataBean) getIntent().getSerializableExtra("bean");
        showLineChart(model,lineChart);

        date.setText(DateUtil.getStringDate5(model.createTime));
        avg_rate.setText("平均心率: "+model.avgRate+"次/分钟");
        tx_af.setText(getString(R.string.seconds,model.af));
        tx_apb.setText(getString(R.string.seconds,model.apb));
        tx_atrialFlutter.setText(getString(R.string.seconds,model.atrialFlutter));
        tx_heartEcg.setText(getString(R.string.seconds,model.heartEcg));
        tx_vpb.setText(getString(R.string.seconds,model.vpb));
//        avg_rate.setText(getString(R.string.seconds,model.avgRate));
//        avg_rate.setText(getString(R.string.seconds,model.avgRate));
//        avg_rate.setText(getString(R.string.seconds,model.avgRate));
//        avg_rate.setText(getString(R.string.seconds,model.avgRate));
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, ECGActivity.class);
        context.startActivity(intent);
    }



    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //设置自定义边界
        lineChart.setDrawCustomBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置X轴缩放，Y轴禁止缩放
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        //隐藏点击数据时显示的十字线
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        //设置XY轴动画效果
        lineChart.animateY(0);
        lineChart.animateX(0);


        //隐藏label
        Description description= new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        /***XY轴的设置***/
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawCustomRateLine(false);
        xAxis.setGridLineWidth(1f);
        xAxis.setGridColor(Color.parseColor("#e1e1e1"));
//        //开启x轴刻度线
        xAxis.setmIsDrawScale(false);
//        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);



        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYaxis = lineChart.getAxisRight();
        leftYAxis.setEnabled(true);
//        //保证Y轴从0开始，不然会上移一点
//        leftYAxis.setAxisMinimum(0f);
//        rightYaxis.setAxisMinimum(0f);

        leftYAxis.setDrawGridLines(true);
        leftYAxis.setDrawLabels(false);
//        leftYAxis.setmIsDrawScale(false);
//
//        leftYAxis.setDrawGridLines(false);
        leftYAxis.setGridLineWidth(1f);
        leftYAxis.setGridColor(Color.parseColor("#e1e1e1"));
        leftYAxis.setDrawZeroLine(false);
        leftYAxis.setAxisLineWidth(1f);
        leftYAxis.setAxisLineColor(Color.parseColor("#e1e1e1"));
//        leftYAxis.setZeroLineWidth(1f);
//
//        leftYAxis.setAxisLineWidth(1f);
        rightYaxis.setEnabled(true);
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setDrawLabels(false);
        rightYaxis.setGridColor(Color.parseColor("#e1e1e1"));
        rightYaxis.setAxisLineWidth(1f);
        rightYaxis.setAxisLineColor(Color.parseColor("#e1e1e1"));


        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);



//            //count为只显示个数，force为是否平均
//            xAxis.setLabelCount(5,true);

//            //Y轴值
//            leftYAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    int v = (int)value;
////                if (v % 20 == 0)
//                    return String.valueOf(v);
////                else
////                    return "";
//                }
//            });
//            leftYAxis.setLabelCount(8);
//    }
    }






    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircles(false);//取消标点
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        //值显示
//        lineDataSet.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                Log.e("111","value="+value+"  entry.x="+entry.getX()+"  dataSetIndex="+dataSetIndex);
//                return "null";
//            }
//        });
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }


    /**
     * 抛物线
     * @param model
     */
    public void showLineChart(EKGModel.DataBean model, LineChart lineChart) {
        List<Entry> entries = new ArrayList<>();
        //改造心电数据
        float hight = 0f;
        String[] datas = model.ecgData.split(",");
        if (datas.length>0){
            for (int j = 0; j < datas.length; j++) {
                float y = Float.valueOf((datas[j]));
                Entry entry = new Entry(j, y);
                entries.add(entry);
                if (y > hight)
                    hight = y;
            }
        }

        initChart(lineChart);

        int count = (datas.length/30) * 3;
        //设置一页最大显示数，超出部分就滑动
        //显示时候是按照多大的比率缩放显示，1f表示不放大缩小
        float ratio = 1f;
        if (datas.length >= count){
            ratio = (float)datas.length/(float)count;
        }

        lineChart.zoom(0,1f,0,0);
        lineChart.zoom(ratio,1f,0,0);

        mHightOffset += (float)(hight*0.4);
//        lineChart.getAxisLeft().setAxisMaximum(hight + mHightOffset);

        lineChart.getXAxis().setValueFormatter((value, axis) -> {
            int index = (int)value;
            if (index %100 ==0){
                return (index/100)+"秒";
            }

            return index+"";
        });
//        lineChart.getXAxis().setLabelCount(datas.length);
        // 每一个LineDataSet代表一条线
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initLineDataSet(highlineDataSet, ContextCompat.getColor(this,R.color.red), LineDataSet.Mode.CUBIC_BEZIER);
        LineData highlineData = new LineData(highlineDataSet);
        lineChart.setData(highlineData);



//        lineChart.getAxisLeft().setValueFormatter(((value, axis) -> {
//            return "";
//        }));
        lineChart.getAxisLeft().setLabelCount(8,true);
    }
}

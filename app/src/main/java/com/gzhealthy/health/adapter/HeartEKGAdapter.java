package com.gzhealthy.health.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
//import com.gzhealthy.health.activity.ECGHtmlActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.tool.DateUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HeartEKGAdapter extends RecyclerView.Adapter<HeartEKGAdapter.ViewHolder> {

    private Context mContext;
    private List<EKGModel.DataBean> mList = new ArrayList<>();
    private float mHightOffset;

    private float eventOffset = 20;//点击偏移值
    private float downRawX = 0,downRawY = 0;

    public HeartEKGAdapter(Context context,float mHightOffset) {
        this.mContext = context;
        this.mHightOffset = mHightOffset;
    }

    public void refreshData(List<EKGModel.DataBean> mList){
        this.mList.clear();
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_heart_ekg, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        EKGModel.DataBean model = mList.get(position);
        showLineChart(model,holder.lineChart);
        holder.date.setText(DateUtil.getStringDate3(model.createTime));
        holder.avg.setText("平均"+model.avgRate+"次/分钟");
        holder.clicker.setOnClickListener(v -> {
            WebViewActivity.startLoadLink(mContext,model.ecgUrl,"");
        });
//        setLineChartActionMotion(holder,model);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private XAxis xAxis;                //X轴
        private YAxis leftYAxis;            //左侧Y轴
        private YAxis rightYaxis;           //右侧Y轴
        private Legend legend;              //图例

        private LinearLayout clicker;
        private TextView date,avg;
        private LineChart lineChart;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            avg = (TextView) itemView.findViewById(R.id.avg);
            lineChart = (LineChart) itemView.findViewById(R.id.rate_linechart);
            clicker = itemView.findViewById(R.id.clicker);
            initChart(lineChart);
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
            lineChart.setDrawBorders(true);
            //设置自定义边界
            lineChart.setDrawCustomBorders(true);
            lineChart.setRadius(false);
            //是否可以拖动
            lineChart.setDragEnabled(true);
            //是否有触摸事件
            lineChart.setTouchEnabled(true);
            //设置X轴缩放，Y轴禁止缩放
            lineChart.setScaleXEnabled(false);
            lineChart.setScaleYEnabled(false);
            //设置XY轴动画效果
            lineChart.animateY(0);
            lineChart.animateX(0);

            //隐藏点击数据时显示的十字线
            lineChart.setHighlightPerDragEnabled(false);
            lineChart.setHighlightPerTapEnabled(false);

            //隐藏label
            Description description= new Description();
            description.setEnabled(false);
            lineChart.setDescription(description);

            /***XY轴的设置***/
            xAxis = lineChart.getXAxis();
            leftYAxis = lineChart.getAxisLeft();
            rightYaxis = lineChart.getAxisRight();

            xAxis.setEnabled(true);
            xAxis.setDrawGridLines(true);
            xAxis.setDrawCustomRateLine(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGridColor(Color.parseColor("#e1e1e1"));
            xAxis.setGridLineWidth(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter((value, axis) -> "");

            leftYAxis.setEnabled(false);
//        //X轴设置显示位置在底部
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);
//        //保证Y轴从0开始，不然会上移一点
//        leftYAxis.setAxisMinimum(0f);
//        rightYaxis.setAxisMinimum(0f);
            leftYAxis.setDrawGridLines(false);
//        //开启x轴刻度线
//        xAxis.setmIsDrawScale(false);
//        leftYAxis.setmIsDrawScale(false);
//
            rightYaxis.setDrawGridLines(false);
//        leftYAxis.setDrawGridLines(false);
//        leftYAxis.setGridLineWidth(1f);
//        leftYAxis.setGridColor(Color.LTGRAY);
            leftYAxis.setDrawZeroLine(false);
//        leftYAxis.setZeroLineWidth(1f);
//
//        leftYAxis.setAxisLineWidth(1f);
            rightYaxis.setEnabled(false);
            leftYAxis.setDrawLabels(false);
            rightYaxis.setDrawLabels(false);



            /***折线图例 标签 设置***/
            legend = lineChart.getLegend();
            legend.setEnabled(false);
//        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setTextSize(12f);
//        //显示位置 左下方
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        //是否绘制在图表里面
//        legend.setDrawInside(false);


//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
////                    int index = (int)value;
////                    String time = DateUtil.getStringDate5(model.getData().data.get(index).createTime);
////                    if (time!=null)
////                        return time;
////                    else
//                        return "";
//                }
//            });
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
        }



    }


//    /**
//     * 判断分发心电图点击事件
//     * @param holder
//     * @param model
//     */
//    @SuppressLint("ClickableViewAccessibility")
//    public void setLineChartActionMotion(final ViewHolder holder,final EKGModel.DataBean model){
//        if (holder.lineChart instanceof View){
//            Log.e("111","lineChart 是View");
//        }else {
//            Log.e("111","lineChart 是ViewGroup");
//        }
//
//        holder.lineChart.setOnTouchListener((v, event) -> {
//
//            switch (event.getAction()){
//                case  MotionEvent.ACTION_DOWN:
//                    downRawX = event.getRawX();//获取按下X坐标
//                    downRawY = event.getRawY();//获取按下Y坐标
//                    break;
//
//                case  MotionEvent.ACTION_UP:
//                    float downLocate = downRawX + downRawY;
//                    float result = Math.abs(downLocate - (event.getRawX() + event.getRawY()));
//                    if (result < eventOffset){
//                        //抬起坐标总和与按下坐标总和相减 绝对值在1以内  判断为点击事件
////                        ToastUtil.showToast("点击事件");
//                        mContext.startActivity(
//                                new Intent(mContext, ECGHtmlActivity.class).putExtra("bean",model));
//                        return true;
//                    }
//                    break;
//
//                default:
//                    break;
//            }
////            ToastUtil.showToast("事件执行完毕");
//            return false;
//        });
//    }


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
//        if (lineChart!=null){
//
//            lineChart.clear();
//            lineChart.getXAxis().setmEntries(new float[]{});
//            lineChart.getXAxis().setmEntryCount(0);
//        }



        float hight = 0f;
        List<Entry> entries = new ArrayList<>();
        //改造心电数据
        String[] datas = model.ecgData.split(",");
        if (datas.length>0){
            for (int j = 0; j < datas.length; j++) {
                float y = Float.valueOf((datas[j]));
                if (y > hight)
                    hight = y;

                Entry entry = new Entry(j, y);
                entries.add(entry);
            }
        }

        int count = (datas.length/30) * 3;
        //设置一页最大显示数，超出部分就滑动
        //显示时候是按照多大的比率缩放显示，1f表示不放大缩小
        float ratio = 1f;
        if (datas.length >= count){
            ratio = (float)datas.length/(float)count;
//            lineChart.moveViewToX(datas.length - 1);
        }

        lineChart.zoom(0,1f,0,0);
        lineChart.zoom(ratio,1f,0,0);

        mHightOffset = 200;
        lineChart.getAxisLeft().setAxisMaximum(hight + mHightOffset);

        // 每一个LineDataSet代表一条线
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initLineDataSet(highlineDataSet, ContextCompat.getColor(mContext,R.color.red), LineDataSet.Mode.CUBIC_BEZIER);
        LineData highlineData = new LineData(highlineDataSet);
        lineChart.setData(highlineData);
    }
}

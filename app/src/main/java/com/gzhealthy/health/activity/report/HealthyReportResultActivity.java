package com.gzhealthy.health.activity.report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.flexbox.FlexboxLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.HealthyReportScoreView;
import com.gzhealthy.health.widget.MoreTextView;
import com.gzhealthy.health.widget.ReportTipsPopupWindow;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告
 */
public class HealthyReportResultActivity extends BaseAct {
    String[] weekDay = {"周一","周二","周三","周四","周五","周六","周日",};

    @BindView(R.id.name)
    TextView tv_name;

    @BindView(R.id.date)
    TextView tv_date;

    @BindView(R.id.protype_name)
    TextView tv_protype_name;

    @BindView(R.id.protype_note)
    TextView tv_protype_note;

    @BindView(R.id.sport_kcal)
    TextView tv_sport_kcal;

    @BindView(R.id.sport_step)
    TextView tv_sport_step;

    @BindView(R.id.bmi_score)
    TextView tv_bmi_score;

    @BindView(R.id.bmi_score_range)
    TextView tv_bmi_score_range;

    @BindView(R.id.bmi_type)
    TextView tv_bmi_type;

    @BindView(R.id.waist_size)
    TextView tv_waist_size;

    @BindView(R.id.waist_type)
    TextView tv_waist_type;

    @BindView(R.id.waist_over)
    TextView tv_waist_over;

    @BindView(R.id.last_week_pressure)
    TextView tv_last_week_pressure;

    @BindView(R.id.routine_pressure)
    TextView tv_routine_pressure;

    @BindView(R.id.last_week_spo2)
    TextView tv_last_week_spo2;

    @BindView(R.id.last_week_rate)
    TextView tv_last_week_rate;

    @BindView(R.id.routine_rate)
    TextView tv_routine_rate;

    @BindView(R.id.routine_spo2)
    TextView tv_routine_spo2;

    @BindView(R.id.food)
    TextView tv_food;

    @BindView(R.id.living)
    TextView tv_living;

    @BindView(R.id.sport)
    TextView tv_sport;

    @BindView(R.id.pressure_status)
    TextView tv_pressure_status;

    @BindView(R.id.rate_status)
    TextView tv_rate_status;

    @BindView(R.id.spo2_status)
    TextView tv_spo2_status;

    @BindView(R.id.ecg_status)
    TextView tv_ecg_status;

    @BindView(R.id.ecg_times)
    TextView tv_ecg_times;

    @BindView(R.id.health_status)
    TextView health_status;

    @BindView(R.id.health_desc1)
    TextView health_desc1;

    @BindView(R.id.health_desc2)
    TextView health_desc2;

    @BindView(R.id.health_status_img)
    ImageView health_status_img;

    @BindView(R.id.spo2Chart)
    LineChart spo2Chart;

    @BindView(R.id.radarChart)
    RadarChart radarChart;

    @BindView(R.id.report_lineChart)
    LineChart report_lineChart;

    @BindView(R.id.rateChart)
    LineChart rateChart;

    @BindView(R.id.walk_lineChart)
    BarChart walk_lineChart;

    @BindView(R.id.score_view)
    HealthyReportScoreView score_view;

    @BindView(R.id.ecg_result_container)
    FlexboxLayout ecg_result_container;

    @BindView(R.id.kcal_linear)
    LinearLayout kcal_linear;

    @BindView(R.id.bmi_linear)
    LinearLayout bmi_linear;

    @BindView(R.id.health_data_linear)
    LinearLayout health_data_linear;

    @BindView(R.id.health_weeked)
    LinearLayout health_weeked;

    @BindView(R.id.tips_linear)
    LinearLayout tips_linear;

    @BindView(R.id.health_status_lay)
    LinearLayout health_status_lay;

    @BindView(R.id.desc_title)
    TextView desc_title;

    @BindView(R.id.bttom_desc)
    TextView bttom_desc;

    @BindView(R.id.app_bar_title)
    TextView app_bar_title;

    @BindView(R.id.back)
    LinearLayout back;

    @BindView(R.id.ecg_tips_dialog)
    LinearLayout ecg_tips_dialog;

    @BindView(R.id.bloodpressure_more)
    MoreTextView bloodpressure_more;

    @BindView(R.id.rate_more)
    MoreTextView rate_more;

    @BindView(R.id.bloodpressure_toggle)
    CheckBox bloodpressure_toggle;

    @BindView(R.id.rate_toggle)
    CheckBox rate_toggle;

    @BindView(R.id.spo2_more)
    MoreTextView spo2_more;

    @BindView(R.id.spo2_toggle)
    CheckBox spo2_toggle;

    @BindView(R.id.reAnalysis)
    TextView reAnalysis;


    ReportTipsPopupWindow popupWindow;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        hideOrShowToolbar(true);
        hideOrShowAppbar(true);
        mImmersionBar.transparentStatusBar().statusBarDarkFont(false).init();

        popupWindow = new ReportTipsPopupWindow(this);
        HealthyReport report = (HealthyReport) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        app_bar_title.setText(report.data.type.equals("0")?"健康报告":"中医体质分析");

        setupReportView(report.data);
        back.setOnClickListener(v -> {
            goBack();
        });

        ecg_tips_dialog.setOnClickListener(v -> {
            popupWindow.showAsDropDown(v);
        });

        reAnalysis.setOnClickListener(v -> {
            String url = Constants.HealthDataValue.HEALTH_QUETION_URL+ getUserToken()+"&agent=Android";
            WebViewActivity.startLoadLink(this,url,"中医体质问卷");
            goBack();
        });
    }



    /**
     * 组装报告view数据
     * @param beanXXXX
     */
    public void setupReportView(HealthyReport.DataBeanXXX beanXXXX){
        try {
            UserInfo.DataBean userInfo = DataSupport.findFirst(UserInfo.DataBean.class);
            if (userInfo!=null){
                tv_name.setText(userInfo.getNickName()+(beanXXXX.type.equals("0")?"的健康报告":"的中医体质分析"));
            }
            tv_date.setText(DateUtil.getStringDate3(beanXXXX.createTime));

            //体质结果
            tv_protype_name.setText(beanXXXX.property.name);
            tv_protype_note.setText(beanXXXX.property.note);
            //雷达图数据
            initRadar(beanXXXX);
        }catch (Exception e){
            e.printStackTrace();
            tv_name.setText("的健康报告");
        }

        bttom_desc.setVisibility(TextUtils.equals(beanXXXX.type ,"1")?View.VISIBLE:View.GONE);

        //1：主动生成  自动生成报告才是完整报告
        if (TextUtils.equals(beanXXXX.type ,"0")) {
            kcal_linear.setVisibility(View.VISIBLE);
            bmi_linear.setVisibility(View.VISIBLE);
            health_data_linear.setVisibility(View.VISIBLE);
            health_weeked.setVisibility(View.VISIBLE);
            tips_linear.setVisibility(View.VISIBLE);
            desc_title.setVisibility(View.VISIBLE);

            //健康评分
            score_view.setScore(Integer.valueOf(beanXXXX.score));
            score_view.setVisibility(View.VISIBLE);

            //步数 卡路里
            tv_sport_kcal.setText(String.valueOf(beanXXXX.sport.kcal));
            tv_sport_step.setText(String.valueOf(beanXXXX.sport.step));

            //BMI 腰围
            tv_bmi_score.setText(getSpannableText(beanXXXX.body.bmi.score));
            /**
             * 类型，thin-偏瘦，fit-标准，minFat-微胖，fat-肥胖，overFat-过度肥胖
             */
            tv_bmi_type.setText("你属于"+bodyType(beanXXXX.body.bmi.type)+"身材");
            tv_bmi_score_range.setText(getString(R.string.bmi_ranges,beanXXXX.body.bmi.miniScore,beanXXXX.body.bmi.miniScore));
            //腰围
            tv_waist_size.setText(getSpannableText(beanXXXX.body.waist.size));
            tv_waist_over.setText(
                    "正常值:"
                            +(TextUtils.equals(beanXXXX.body.waist.sex,"1")?"男性":"女性")
                            +"小于"
                            +beanXXXX.body.waist.overSize+"cm");
            tv_waist_type.setText("你的腰围属于"+bodyType(beanXXXX.body.waist.type));

            //血压图
            //1-正常,2-低血压,3-高血压
            if (TextUtils.equals(beanXXXX.ehr.bloodPress.status,"1")){
                tv_pressure_status.setText("正常");
                tv_rate_status.setTextColor(Color.parseColor("#30C99A"));
            }else if (TextUtils.equals(beanXXXX.ehr.bloodPress.status,"2")){
                tv_pressure_status.setText("低血压");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }else {
                tv_pressure_status.setText("高血压");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }
            initBooldPressure(beanXXXX.ehr.bloodPress);
            if (beanXXXX.ehr.bloodPress.description!=null){
                StringBuilder builder = new StringBuilder();
                for (int i=0; i<beanXXXX.ehr.bloodPress.description.length;i++){
                    builder.append(beanXXXX.ehr.bloodPress.description[i]);
                    builder.append("\n");
                }
                bloodpressure_more.setText(builder.toString(),bloodpressure_toggle);
                bloodpressure_more.setVisibility(builder.length()>0?View.VISIBLE:View.GONE);
            }


            //心率图
            //1-正常,2-偏慢,3-偏快
            if (TextUtils.equals(beanXXXX.ehr.rate.status,"1")){
                tv_rate_status.setText("正常");
                tv_rate_status.setTextColor(Color.parseColor("#30C99A"));
            }else if (TextUtils.equals(beanXXXX.ehr.rate.status,"2")){
                tv_rate_status.setText("偏慢");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }else {
                tv_rate_status.setText("偏快");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }
            initRateHeart(beanXXXX.ehr.rate);
            if (beanXXXX.ehr.rate.description!=null){
                StringBuilder builder = new StringBuilder();
                for (int i=0; i<beanXXXX.ehr.rate.description.length;i++){
                    builder.append(beanXXXX.ehr.rate.description[i]);
                    builder.append("\n");
                }
                rate_more.setText(builder.toString(),rate_toggle);
                rate_more.setVisibility(builder.length()>0?View.VISIBLE:View.GONE);
            }

            //血氧图
            //1-正常,2-偏低
            if (TextUtils.equals(beanXXXX.ehr.rate.status,"1")){
                tv_spo2_status.setText("正常");
                tv_spo2_status.setTextColor(Color.parseColor("#30C99A"));
            }else {
                tv_spo2_status.setText("偏低");
                tv_spo2_status.setTextColor(Color.parseColor("#FF5B58"));
            }
            initSpo2(beanXXXX.ehr.spo2);
            if (beanXXXX.ehr.spo2.description!=null){
                StringBuilder builder = new StringBuilder();
                for (int i=0; i<beanXXXX.ehr.spo2.description.length;i++){
                    builder.append(beanXXXX.ehr.spo2.description[i]);
                    builder.append("\n");
                }
                spo2_more.setVisibility(builder.length()>0?View.VISIBLE:View.GONE);
                spo2_more.setText(builder.toString(),spo2_toggle);
            }

            //心电
            initEcg(beanXXXX.ehr.ecg);

            //提示建议
            tv_food.setText(beanXXXX.tips.food);
            tv_living.setText(beanXXXX.tips.living);
            tv_sport.setText(beanXXXX.tips.sport);

            //步数图
            if (beanXXXX.sport.walkInfoList !=null){
                initwalk_lineChart(beanXXXX);
            }else {
                walk_lineChart.setVisibility(View.GONE);
            }

            //健康状态分析
            if (beanXXXX.healthStatus == null){
                health_status_lay.setVisibility(View.GONE);
            }else {
                int status = beanXXXX.healthStatus.status;
                switch (status){
                    case 1:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_heath);
                        health_status.setText("健康");
                        health_status.setTextColor(Color.parseColor("#30C99A"));
                        health_desc2.setText(beanXXXX.healthStatus.description[0]);
                        health_desc1.setVisibility(View.GONE);
                        break;

                    case 2:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_low);
                        health_status.setText("健康低风险");
                        health_status.setTextColor(Color.parseColor("#4983F4"));
                        health_desc1.setText(beanXXXX.healthStatus.description[0]);
                        health_desc2.setText(beanXXXX.healthStatus.description[1]);
                        break;

                    case 3:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_medium);
                        health_status.setText("健康中风险");
                        health_status.setTextColor(Color.parseColor("#FFBE0F"));
                        health_desc1.setText(beanXXXX.healthStatus.description[0]);
                        health_desc2.setText(beanXXXX.healthStatus.description[1]);
                        break;

                    case 4:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_highest);
                        health_status.setText("健康高风险");
                        health_status.setTextColor(Color.parseColor("#D92C2C"));
                        health_desc1.setText(beanXXXX.healthStatus.description[0]);
                        health_desc2.setText(beanXXXX.healthStatus.description[1]);
                        break;
                }
            }
        }
    }



    public String bodyType(String type){
        String types;
        if (TextUtils.equals(type,"thin")){
            types = "偏瘦";
        }else if (TextUtils.equals(type,"fit")){
            types = "标准";
        }else if (TextUtils.equals(type,"minFat")){
            types = "微胖";
        }else if (TextUtils.equals(type,"fat")){
            types = "肥胖";
        }else {
            types = "过度肥胖";
        }
        return types;
    }


    private void initRadar(HealthyReport.DataBeanXXX dataBeanXXX){
        /* RadarData的List类型参数的构造方法 */
        List<IRadarDataSet> dataSets = new ArrayList<>();
        List<HealthyReport.DataBeanXXX.PropertyBean.DataBeanXX> dataBeanXXList = dataBeanXXX.property.data;
        List<RadarEntry> yVals = new ArrayList<>();
        for (int i = 0; i < dataBeanXXList.size(); i++) {
            float value = dataBeanXXList.get(i).convertScore;// - Tools.getRandomInt(10,18);//weekStrs.get(i) + (Tools.getRandomInt(10000,99999)/10000) ;// (float) (Math.random() * 100);
//            Log.e("111","random ="+value);
            yVals.add(new RadarEntry(value
            )); // 生成1-100的随机数
        }

        RadarDataSet ds = new RadarDataSet(yVals, "lable");
//        ds.setHighlightCircleInnerRadius(5f);
//        ds.setHighlightCircleOuterRadius(5f);
//        ds.setHighlightEnabled(true);
//        ds.setHighLightColor(Color.YELLOW);
        ds.setColor(Color.parseColor("#00D0B8"));
        ds.setDrawFilled(true); // 绘制填充，默认为false
        ds.setFillColor(Color.parseColor("#33DECB")); // 填充颜色
        ds.setFillAlpha(51); // 填充内容透明度
        ds.setDrawValues(false); // 指定那组数据（RadarDataSet对象）不显示标签
        ds.setDrawIcons(true);
        ds.setLineWidth(2f);
//        ds.setDrawHighlightCircleEnabled(true);
//        ds.setHighlightCircleStrokeWidth(2f);
//        ds.setHighlightCircleFillColor(Color.BLUE);
//        ds.setHighlightCircleStrokeColor(Color.RED);
        dataSets.add(ds);


        RadarData data = new RadarData(dataSets);
//        data.setValueTextSize(8f);
//        data.setValueTextColor(Color.RED);
        radarChart.animateXY(1000,1000);
        radarChart.setDragDecelerationEnabled(false);
        radarChart.setTouchEnabled(false);
        radarChart.setRadarCustomStyle(true);//使用自定义样式
        radarChart.setNoRenderXAisLine(true);//网格的纵轴线
        radarChart.setRenderXAisCircle(false);//网格的画小圆点
        radarChart.setData(data);
        radarChart.setRotationEnabled(false);
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);
        Description description = radarChart.getDescription();
        description.setEnabled(false);

        XAxis xAxis = radarChart.getXAxis();
        // X轴标签数据（图外的文字）
        xAxis.setTextColor(Color.parseColor("#666666")); // 文本颜色为灰色
        xAxis.setTextSize(12f); //
        xAxis.setDrawLabels(true);
        xAxis.setDrawRadarLabelValue(true);//边角值

        // 设置标签的显示格式
        xAxis.setValueFormatter((value, axis) -> {
            int index = (int) value;
            if (index >= dataBeanXXList.size())
                return "";
            return dataBeanXXList.get(index).propertyName;
        });

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMaximum(90f); // 要达到100需要把该值设为80，至于原因可以试着向下调小和向上调大看看效果就
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(6);//写死层数
        yAxis.setDrawLabels(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setZeroLineColor(Color.GREEN);
        yAxis.setTextColor(Color.parseColor("#999999"));
    }



    /**
     * 血压图
     * @param bean
     */
    public void initBooldPressure(HealthyReport.DataBeanXXX.EhrBean.BloodPressBean bean){
        tv_last_week_pressure.setText(bean.avgHigh+"/"+bean.avgLow);//上周均值
        tv_routine_pressure.setText(bean.maxHigh+"-"+bean.minHigh+"/"+bean.minHigh+"-"+bean.minLow);//常规值

        initLineChart(report_lineChart);

        List<Entry> entriesHigh = new ArrayList<>();
        List<Entry> entrieslow = new ArrayList<>();

        List<HealthyReport.DataBeanXXX.EhrBean.BloodPressBean.HighListBean> highListBeanList = bean.highList;
        List<HealthyReport.DataBeanXXX.EhrBean.BloodPressBean.LowListBean> lowListBeanList = bean.lowList;

        int size =  highListBeanList.size();
        float high1 = 0,high2 = 0;
        for (int i = 0; i < size; i++) {
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            int h = highListBeanList.get(i).high;
            int l = lowListBeanList.get(i).low;
            entriesHigh.add(new Entry(i, (float)( h )));
            entrieslow.add(new Entry(i, (float)( l )));

            float h1 = (float)(h);
            if (h1 > high1)
                high1 = h1;

            float l1 = (float)(l);
            if (l1 > high2)
                high2 = l1;
        }

        // 每一个LineDataSet代表一条线
        LineDataSet highlineDataSet = new LineDataSet(entriesHigh, "收缩压");
        initPressureLineDataSet(highlineDataSet, Color.parseColor("#00A7FF"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        LineData highlineData = new LineData(highlineDataSet);
        highlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        highlineDataSet.setDrawValues(true);
        highlineData.setValueFormatter(new PressureIValueFormatter((int) high1,R.drawable.circle_high_big_legend));
        report_lineChart.setData(highlineData);

        LineDataSet lowlineDataSet = new LineDataSet(entrieslow, "舒张压");
        initPressureLineDataSet(lowlineDataSet, Color.parseColor("#00DDCB"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        lowlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        lowlineDataSet.setDrawValues(true);
        lowlineDataSet.setValueFormatter(new PressureIValueFormatter((int) high2,R.drawable.circle_low_big_legend));
        report_lineChart.getLineData().addDataSet(lowlineDataSet);
        report_lineChart.invalidate();

        report_lineChart.getAxisLeft().setAxisMaximum(180f);
        report_lineChart.getAxisLeft().setAxisMinimum(30f);
        //Y轴值
        report_lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
            int count = (int)value;
            return count% 30==0?String.valueOf(count):"";
        });

        //X轴
        report_lineChart.getXAxis().setValueFormatter(((value, axis) -> {
            int index = (int) value;
            if (index < 0 || index == highListBeanList.size()) {
                //TODO 防止value为-1
                return "";
            }
            return weekDay[index];
        }));
        report_lineChart.getXAxis().setYOffset(8); // 设置标签对x轴的偏移量，垂直方向
        report_lineChart.getXAxis().setXOffset(10);
        report_lineChart.setExtraBottomOffset(10f);
//        int count = axisMax/20;
//        report_lineChart.getAxisLeft().setLabelCount(count);
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

    private class PressureIValueFormatter implements IValueFormatter{
        int highValue;
        int drawResId;

        public PressureIValueFormatter(int highValue,int drawResId) {
            this.highValue = highValue;
            this.drawResId = drawResId;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1){
                Drawable drawable = getDrawable(drawResId);
                entry.setIcon(drawable);
                return String.valueOf((int)value);
            }
            return "";
        }
    }



    protected void initRateLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(4f);
//        lineDataSet.setCircleRadius(2f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(3.5f);
        lineDataSet.setFormSize(15.f);
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
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }


    private void initLineChart(LineChart lineChart){
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(false);
        //设置XY轴动画效果
        lineChart.animateY(0);
        lineChart.animateX(1500);


        //隐藏label
        Description description= new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        /***XY轴的设置***/
        XAxis xAxis = lineChart.getXAxis();
        //开启x轴刻度线
        xAxis.setmIsDrawScale(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.parseColor("#9CA5AC"));
        xAxis.setGranularity(1f);//设置X轴坐标之间的最小间隔
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);//是否开启在X轴上的竖线
        xAxis.setXOffset(10);
        xAxis.setYOffset(8);

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawGridLines(true);//是否开启Y轴上的横线
        leftYAxis.setGridLineWidth(0.5f);
        leftYAxis.setGridColor(ContextCompat.getColor(this,R.color.gray_line));
        leftYAxis.setTextColor(Color.parseColor("#9CA5AC"));
        leftYAxis.setDrawZeroLine(false);
        leftYAxis.setDrawAxisLine(false);//Y轴边线
        leftYAxis.setmIsDrawScale(false);

        YAxis rightYaxis = lineChart.getAxisRight();
        rightYaxis.setEnabled(false);

        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
    }


    /**
     * 心率图
     * @param bean
     */
    public void initRateHeart(HealthyReport.DataBeanXXX.EhrBean.RateBean bean){
        tv_last_week_rate.setText(bean.avgRate);
        tv_routine_rate.setText(bean.minRate+"-"+bean.maxRate);

        initLineChart(rateChart);
        float highest = 0,lowest = 0;
        int avg = 0;
        List<Entry> entries = new ArrayList<>();
        int size = bean.data.size();
        Drawable drawable = getResources().getDrawable(R.drawable.circle_rate_legend);
        for (int i = 0; i < size; i++) {
            HealthyReport.DataBeanXXX.EhrBean.RateBean.DataBean rate =  bean.data.get(i);
            float y = (float)(rate.rate);
            if (y > highest)
                highest = y;

            if (lowest == 0)
                lowest = y;

            if (y < lowest){
                lowest = y;
            }

            avg += (int)y;

            Entry entry = new Entry(i, y,drawable);
            entries.add(entry);
        }

        // 每一个LineDataSet代表一条线
        int color = Color.parseColor("#00DDCB");//ContextCompat.getColor(this,R.color.linechart_line_color);
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initRateLineDataSet(highlineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
        highlineDataSet.setValueTextSize(17f);
        highlineDataSet.setValueTextColor(color);
        highlineDataSet.setLineWidth(4f);
        highlineDataSet.setCircleRadius(3f);
        highlineDataSet.setDrawValues(true);

        highlineDataSet.setValueFormatter(new MyIValueFormatter((int) highest));
        LineData highlineData = new LineData(highlineDataSet);
        rateChart.setData(highlineData);

        //X轴
        rateChart.getXAxis().setValueFormatter(((value, axis) -> {
            int index = (int) value;
            if (index < 0 || index == bean.data.size()) {
                //TODO 防止value为-1
                return "";
            }
            return weekDay[index];
        }));
        rateChart.getXAxis().setYOffset(8);
        rateChart.getXAxis().setXOffset(10);

        rateChart.getAxisLeft().setAxisMaximum(120f);
        rateChart.getAxisLeft().setAxisMinimum(50f);

        rateChart.setExtraBottomOffset(10f);

//        int count = axisMax/20;
//        rate_linechart.getAxisLeft().setLabelCount(count);
    }

    private class MyIValueFormatter implements IValueFormatter{
        int highValue;

        public MyIValueFormatter(int highValue) {
            this.highValue = highValue;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (highValue - (int)value < 0.1){
                Drawable drawable = getDrawable(R.drawable.rate_circle_big_bg);
                entry.setIcon(drawable);
                return String.valueOf((int)value);
            }
            return "";
        }
    }


    /**
     * 血氧
     * @param bean
     */
    private void initSpo2(HealthyReport.DataBeanXXX.EhrBean.Spo2Bean bean){
        tv_last_week_spo2.setText(bean.avgSpo2+"%");
        tv_routine_spo2.setText(">"+bean.minSpo2+"%");

        initLineChart(spo2Chart);
        List<Entry> entries = new ArrayList<>();
        int size = bean.data.size();
        for (int i = 0; i < size; i++) {
            HealthyReport.DataBeanXXX.EhrBean.Spo2Bean.DataBeanX spo2Bean =  bean.data.get(i);
            float y = (float)(spo2Bean.spo2);
            Log.e("111","initSpo2  "+y);
            Entry entry = new Entry(i, y);
            entries.add(entry);
        }


        // 每一个LineDataSet代表一条线
        LineDataSet highlineDataSet = new LineDataSet(entries, "");
        initRateLineDataSet(highlineDataSet,
                Color.parseColor("#00DDCB"),
                LineDataSet.Mode.HORIZONTAL_BEZIER);
        highlineDataSet.setLineWidth(2f);
        highlineDataSet.setDrawCircles(false);
        LineData highlineData = new LineData(highlineDataSet);
        spo2Chart.setData(highlineData);


        Drawable drawable = getDrawable(R.drawable.shape_oxy_gradient);
        if (spo2Chart.getData()!=null && spo2Chart.getData().getDataSetCount() > 0){
            LineDataSet lineDataSet = (LineDataSet) spo2Chart.getData().getDataSetByIndex(0);
            //避免在initLineDataSet()方法中设置了LineDataSet.setDrawFilled(false)；而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            spo2Chart.invalidate();
        }


        spo2Chart.getAxisLeft().setAxisMaximum(102f);
        spo2Chart.getAxisLeft().setAxisMinimum(75f);

        //Y轴值
        spo2Chart.getAxisLeft().setLabelCount(6);
        spo2Chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int count = (int)value;
                switch (count){
                    case 75:
                    case 80:
                    case 85:
                    case 90:
                    case 95:
                    case 100:
                        return count + "%";
                }
                return "";
            }
        });

        //X轴值
        spo2Chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if (index < 0 || index == bean.data.size()) {
                    //TODO 防止value为-1
                    return "";
                }

                return weekDay[index];
            }
        });

        spo2Chart.getXAxis().setYOffset(8);
        spo2Chart.getXAxis().setXOffset(10);
        spo2Chart.setExtraBottomOffset(10f);
    }


    /**
     * 心电
     * @param bean
     */
    private void initEcg(HealthyReport.DataBeanXXX.EhrBean.EcgBean bean){
        //状态，0-异常，1-正常
        if (TextUtils.equals(bean.status,"0")){
            tv_ecg_status.setText("异常");
            tv_ecg_status.setTextColor(Color.parseColor("#FF5B58"));
        }else {
            tv_ecg_status.setText("正常");
            tv_ecg_status.setTextColor(Color.parseColor("#30C99A"));
        }

        tv_ecg_times.setText("上周共测量"+bean.times+"次");

        for (int index=0; index < bean.data.size(); index++){
            TextView text = new TextView(this);
            text.setTextColor(ContextCompat.getColor(this,R.color.global_333333));
            text.setText(bean.data.get(index));
            text.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.shape_report_gray_circle),null,null,null);
            text.setCompoundDrawablePadding(DispUtil.dipToPx(this,10));
            FlexboxLayout.LayoutParams lp =new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = DispUtil.dipToPx(this,17);
            lp.rightMargin = getResources().getDisplayMetrics().widthPixels/9;
            text.setLayoutParams(lp);
            ecg_result_container.addView(text);
        }

        ecg_result_container.setVisibility(bean.data.size()<=0?View.GONE:View.VISIBLE);
    }


    public static void inStance(Context context,HealthyReport report){
        context.startActivity(new Intent(context, HealthyReportResultActivity.class)
                .putExtra(IntentParam.DATA_BEAN,report));
    }



    //跑步数 柱状图
    private void initwalk_lineChart(HealthyReport.DataBeanXXX beanXXXX) {
        walk_lineChart.getDescription().setEnabled(false); // 不显示描述
        walk_lineChart.setExtraOffsets(0, 0, 0, 22f); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小
        walk_lineChart.setDrawBorders(false);
        walk_lineChart.setDrawGridBackground(false);
        walk_lineChart.setDragEnabled(false);
        walk_lineChart.setDoubleTapToZoomEnabled(false);
        walk_lineChart.setTouchEnabled(false);
        Legend legend = walk_lineChart.getLegend();
        legend.setEnabled(false);
        setAxis(beanXXXX); // 设置坐标轴
//        setLegend(); // 设置图例
        setData(beanXXXX);  // 设置数据
    }

    private void setData(HealthyReport.DataBeanXXX bean) {
        List<IBarDataSet> sets = new ArrayList<>();
        // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
        // x是横坐标，表示位置，y是纵坐标，表示高度
        List<BarEntry> barEntries1 = new ArrayList<>();
        for (int index=0; index < bean.sport.walkInfoList.size(); index++){
            int step = bean.sport.walkInfoList.get(index).step;
//            if (step < 10)
//                step += 2100;
            barEntries1.add(new BarEntry(index, (float)step));
        }

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
        barDataSet1.setValueTextColor(Color.RED); // 值的颜色
        barDataSet1.setValueTextSize(0f); // 值的大小
        barDataSet1.setColor(Color.parseColor("#00C9B4")); // 柱子的颜色
//        barDataSet1.setLabel("蔬菜"); // 设置标签之后，图例的内容默认会以设置的标签显示
        // 设置柱子上数据显示的格式
        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // 此处的value默认保存一位小数
                return value + "步";
            }
        });

        sets.add(barDataSet1);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f); // 设置柱子的宽度
        barData.setBarCharRaduis(true);//设置柱体圆角
        walk_lineChart.setData(barData);
    }

//    private void setLegend() {
//        Legend legend = walk_lineChart.getLegend();
//        legend.setEnabled(false);
//        legend.setFormSize(12f); // 图例的图形大小
//        legend.setTextSize(15f); // 图例的文字大小
//        legend.setDrawInside(true); // 设置图例在图中
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例的方向为垂直
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //显示位置，水平右对齐
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 显示位置，垂直上对齐
//        // 设置水平与垂直方向的偏移量
//        legend.setYOffset(55f);
//        legend.setXOffset(30f);
//    }

    private void setAxis(HealthyReport.DataBeanXXX bean) {
        // 设置x轴
        XAxis xAxis = walk_lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(bean.sport.walkInfoList.size());  // 设置x轴上的标签个数
        xAxis.setTextColor(getColor(R.color.global_999999));
        xAxis.setTextSize(10f); // x轴上标签的大小
        xAxis.setDrawXaxisCustomValues(true);

        List<String> list = new ArrayList<>();
        for (int index=0; index < bean.sport.walkInfoList.size(); index++){
            list.add(bean.sport.walkInfoList.get(index).date);
        }
        xAxis.setmLabelList(list);
        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if (index < 0 || index == weekDay.length) {
                    //TODO 防止value为-1
                    return "";
                }

                return weekDay[index];
            }
        });
        xAxis.setYOffset(8); // 设置标签对x轴的偏移量，垂直方向
        xAxis.setXOffset(10);

        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = walk_lineChart.getAxisRight();
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = walk_lineChart.getAxisLeft();
        yAxis_left.setTextColor(getColor(R.color.global_999999));
        yAxis_left.setAxisMaximum(10000f);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setLabelCount(5);
        yAxis_left.setTextSize(10f); // 设置y轴的标签大小
        yAxis_left.setDrawAxisLine(false);
        yAxis_left.setDrawGridLines(false);
    }


    public SpannableString getSpannableText(String str){
        int textSize = DispUtil.sp2px(this,26);
        SpannableString string = new SpannableString(str);
        string.setSpan(new AbsoluteSizeSpan(textSize),0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
}

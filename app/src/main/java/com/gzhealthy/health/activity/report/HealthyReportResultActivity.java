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
 * ????????????
 */
public class HealthyReportResultActivity extends BaseAct {
    String[] weekDay = {"??????","??????","??????","??????","??????","??????","??????",};

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
        app_bar_title.setText(report.data.type.equals("0")?"????????????":"??????????????????");

        setupReportView(report.data);
        back.setOnClickListener(v -> {
            goBack();
        });

        ecg_tips_dialog.setOnClickListener(v -> {
            popupWindow.showAsDropDown(v);
        });

        reAnalysis.setOnClickListener(v -> {
            String url = Constants.HealthDataValue.HEALTH_QUETION_URL+ getUserToken()+"&agent=Android";
            WebViewActivity.startLoadLink(this,url,"??????????????????");
            goBack();
        });
    }



    /**
     * ????????????view??????
     * @param beanXXXX
     */
    public void setupReportView(HealthyReport.DataBeanXXX beanXXXX){
        try {
            UserInfo.DataBean userInfo = DataSupport.findFirst(UserInfo.DataBean.class);
            if (userInfo!=null){
                tv_name.setText(userInfo.getNickName()+(beanXXXX.type.equals("0")?"???????????????":"?????????????????????"));
            }
            tv_date.setText(DateUtil.getStringDate3(beanXXXX.createTime));

            //????????????
            tv_protype_name.setText(beanXXXX.property.name);
            tv_protype_note.setText(beanXXXX.property.note);
            //???????????????
            initRadar(beanXXXX);
        }catch (Exception e){
            e.printStackTrace();
            tv_name.setText("???????????????");
        }

        bttom_desc.setVisibility(TextUtils.equals(beanXXXX.type ,"1")?View.VISIBLE:View.GONE);

        //1???????????????  ????????????????????????????????????
        if (TextUtils.equals(beanXXXX.type ,"0")) {
            kcal_linear.setVisibility(View.VISIBLE);
            bmi_linear.setVisibility(View.VISIBLE);
            health_data_linear.setVisibility(View.VISIBLE);
            health_weeked.setVisibility(View.VISIBLE);
            tips_linear.setVisibility(View.VISIBLE);
            desc_title.setVisibility(View.VISIBLE);

            //????????????
            score_view.setScore(Integer.valueOf(beanXXXX.score));
            score_view.setVisibility(View.VISIBLE);

            //?????? ?????????
            tv_sport_kcal.setText(String.valueOf(beanXXXX.sport.kcal));
            tv_sport_step.setText(String.valueOf(beanXXXX.sport.step));

            //BMI ??????
            tv_bmi_score.setText(getSpannableText(beanXXXX.body.bmi.score));
            /**
             * ?????????thin-?????????fit-?????????minFat-?????????fat-?????????overFat-????????????
             */
            tv_bmi_type.setText("?????????"+bodyType(beanXXXX.body.bmi.type)+"??????");
            tv_bmi_score_range.setText(getString(R.string.bmi_ranges,beanXXXX.body.bmi.miniScore,beanXXXX.body.bmi.miniScore));
            //??????
            tv_waist_size.setText(getSpannableText(beanXXXX.body.waist.size));
            tv_waist_over.setText(
                    "?????????:"
                            +(TextUtils.equals(beanXXXX.body.waist.sex,"1")?"??????":"??????")
                            +"??????"
                            +beanXXXX.body.waist.overSize+"cm");
            tv_waist_type.setText("??????????????????"+bodyType(beanXXXX.body.waist.type));

            //?????????
            //1-??????,2-?????????,3-?????????
            if (TextUtils.equals(beanXXXX.ehr.bloodPress.status,"1")){
                tv_pressure_status.setText("??????");
                tv_rate_status.setTextColor(Color.parseColor("#30C99A"));
            }else if (TextUtils.equals(beanXXXX.ehr.bloodPress.status,"2")){
                tv_pressure_status.setText("?????????");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }else {
                tv_pressure_status.setText("?????????");
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


            //?????????
            //1-??????,2-??????,3-??????
            if (TextUtils.equals(beanXXXX.ehr.rate.status,"1")){
                tv_rate_status.setText("??????");
                tv_rate_status.setTextColor(Color.parseColor("#30C99A"));
            }else if (TextUtils.equals(beanXXXX.ehr.rate.status,"2")){
                tv_rate_status.setText("??????");
                tv_rate_status.setTextColor(Color.parseColor("#FF5B58"));
            }else {
                tv_rate_status.setText("??????");
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

            //?????????
            //1-??????,2-??????
            if (TextUtils.equals(beanXXXX.ehr.rate.status,"1")){
                tv_spo2_status.setText("??????");
                tv_spo2_status.setTextColor(Color.parseColor("#30C99A"));
            }else {
                tv_spo2_status.setText("??????");
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

            //??????
            initEcg(beanXXXX.ehr.ecg);

            //????????????
            tv_food.setText(beanXXXX.tips.food);
            tv_living.setText(beanXXXX.tips.living);
            tv_sport.setText(beanXXXX.tips.sport);

            //?????????
            if (beanXXXX.sport.walkInfoList !=null){
                initwalk_lineChart(beanXXXX);
            }else {
                walk_lineChart.setVisibility(View.GONE);
            }

            //??????????????????
            if (beanXXXX.healthStatus == null){
                health_status_lay.setVisibility(View.GONE);
            }else {
                int status = beanXXXX.healthStatus.status;
                switch (status){
                    case 1:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_heath);
                        health_status.setText("??????");
                        health_status.setTextColor(Color.parseColor("#30C99A"));
                        health_desc2.setText(beanXXXX.healthStatus.description[0]);
                        health_desc1.setVisibility(View.GONE);
                        break;

                    case 2:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_low);
                        health_status.setText("???????????????");
                        health_status.setTextColor(Color.parseColor("#4983F4"));
                        health_desc1.setText(beanXXXX.healthStatus.description[0]);
                        health_desc2.setText(beanXXXX.healthStatus.description[1]);
                        break;

                    case 3:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_medium);
                        health_status.setText("???????????????");
                        health_status.setTextColor(Color.parseColor("#FFBE0F"));
                        health_desc1.setText(beanXXXX.healthStatus.description[0]);
                        health_desc2.setText(beanXXXX.healthStatus.description[1]);
                        break;

                    case 4:
                        health_status_img.setImageResource(R.mipmap.icon_report_heal_status_highest);
                        health_status.setText("???????????????");
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
            types = "??????";
        }else if (TextUtils.equals(type,"fit")){
            types = "??????";
        }else if (TextUtils.equals(type,"minFat")){
            types = "??????";
        }else if (TextUtils.equals(type,"fat")){
            types = "??????";
        }else {
            types = "????????????";
        }
        return types;
    }


    private void initRadar(HealthyReport.DataBeanXXX dataBeanXXX){
        /* RadarData???List??????????????????????????? */
        List<IRadarDataSet> dataSets = new ArrayList<>();
        List<HealthyReport.DataBeanXXX.PropertyBean.DataBeanXX> dataBeanXXList = dataBeanXXX.property.data;
        List<RadarEntry> yVals = new ArrayList<>();
        for (int i = 0; i < dataBeanXXList.size(); i++) {
            float value = dataBeanXXList.get(i).convertScore;// - Tools.getRandomInt(10,18);//weekStrs.get(i) + (Tools.getRandomInt(10000,99999)/10000) ;// (float) (Math.random() * 100);
//            Log.e("111","random ="+value);
            yVals.add(new RadarEntry(value
            )); // ??????1-100????????????
        }

        RadarDataSet ds = new RadarDataSet(yVals, "lable");
//        ds.setHighlightCircleInnerRadius(5f);
//        ds.setHighlightCircleOuterRadius(5f);
//        ds.setHighlightEnabled(true);
//        ds.setHighLightColor(Color.YELLOW);
        ds.setColor(Color.parseColor("#00D0B8"));
        ds.setDrawFilled(true); // ????????????????????????false
        ds.setFillColor(Color.parseColor("#33DECB")); // ????????????
        ds.setFillAlpha(51); // ?????????????????????
        ds.setDrawValues(false); // ?????????????????????RadarDataSet????????????????????????
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
        radarChart.setRadarCustomStyle(true);//?????????????????????
        radarChart.setNoRenderXAisLine(true);//??????????????????
        radarChart.setRenderXAisCircle(false);//?????????????????????
        radarChart.setData(data);
        radarChart.setRotationEnabled(false);
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);
        Description description = radarChart.getDescription();
        description.setEnabled(false);

        XAxis xAxis = radarChart.getXAxis();
        // X????????????????????????????????????
        xAxis.setTextColor(Color.parseColor("#666666")); // ?????????????????????
        xAxis.setTextSize(12f); //
        xAxis.setDrawLabels(true);
        xAxis.setDrawRadarLabelValue(true);//?????????

        // ???????????????????????????
        xAxis.setValueFormatter((value, axis) -> {
            int index = (int) value;
            if (index >= dataBeanXXList.size())
                return "";
            return dataBeanXXList.get(index).propertyName;
        });

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMaximum(90f); // ?????????100?????????????????????80?????????????????????????????????????????????????????????????????????
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(6);//????????????
        yAxis.setDrawLabels(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setZeroLineColor(Color.GREEN);
        yAxis.setTextColor(Color.parseColor("#999999"));
    }



    /**
     * ?????????
     * @param bean
     */
    public void initBooldPressure(HealthyReport.DataBeanXXX.EhrBean.BloodPressBean bean){
        tv_last_week_pressure.setText(bean.avgHigh+"/"+bean.avgLow);//????????????
        tv_routine_pressure.setText(bean.maxHigh+"-"+bean.minHigh+"/"+bean.minHigh+"-"+bean.minLow);//?????????

        initLineChart(report_lineChart);

        List<Entry> entriesHigh = new ArrayList<>();
        List<Entry> entrieslow = new ArrayList<>();

        List<HealthyReport.DataBeanXXX.EhrBean.BloodPressBean.HighListBean> highListBeanList = bean.highList;
        List<HealthyReport.DataBeanXXX.EhrBean.BloodPressBean.LowListBean> lowListBeanList = bean.lowList;

        int size =  highListBeanList.size();
        float high1 = 0,high2 = 0;
        for (int i = 0; i < size; i++) {
            /**
             * ??????????????? Entry???????????????????????? ??????????????? Entry(float x, float y)
             * ????????????Drawable??? Entry(float x, float y, Drawable icon) ??????XY????????? ??????Drawable????????????
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

        // ?????????LineDataSet???????????????
        LineDataSet highlineDataSet = new LineDataSet(entriesHigh, "?????????");
        initPressureLineDataSet(highlineDataSet, Color.parseColor("#00A7FF"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        LineData highlineData = new LineData(highlineDataSet);
        highlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        highlineDataSet.setDrawValues(true);
        highlineData.setValueFormatter(new PressureIValueFormatter((int) high1,R.drawable.circle_high_big_legend));
        report_lineChart.setData(highlineData);

        LineDataSet lowlineDataSet = new LineDataSet(entrieslow, "?????????");
        initPressureLineDataSet(lowlineDataSet, Color.parseColor("#00DDCB"), LineDataSet.Mode.HORIZONTAL_BEZIER);
        lowlineDataSet.setValueTextColor(Color.parseColor("#00000000"));
        lowlineDataSet.setDrawValues(true);
        lowlineDataSet.setValueFormatter(new PressureIValueFormatter((int) high2,R.drawable.circle_low_big_legend));
        report_lineChart.getLineData().addDataSet(lowlineDataSet);
        report_lineChart.invalidate();

        report_lineChart.getAxisLeft().setAxisMaximum(180f);
        report_lineChart.getAxisLeft().setAxisMinimum(30f);
        //Y??????
        report_lineChart.getAxisLeft().setValueFormatter((value, axis) -> {
            int count = (int)value;
            return count% 30==0?String.valueOf(count):"";
        });

        //X???
        report_lineChart.getXAxis().setValueFormatter(((value, axis) -> {
            int index = (int) value;
            if (index < 0 || index == highListBeanList.size()) {
                //TODO ??????value???-1
                return "";
            }
            return weekDay[index];
        }));
        report_lineChart.getXAxis().setYOffset(8); // ???????????????x??????????????????????????????
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
        //?????????????????????????????????????????????
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //?????????????????????
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(3.5f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setDrawCircleHole(false);
        //?????????
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                Log.e("111","value="+value+"  entry.x="+entry.getX()+"  dataSetIndex="+dataSetIndex);
                return null;
            }
        });
        if (mode == null) {
            //?????????????????????????????????????????????????????????????????????
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
        //?????????????????????????????????????????????
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(7f);
        lineDataSet.setDrawValues(false);
        //?????????????????????
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(3.5f);
        lineDataSet.setFormSize(15.f);
        //?????????
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                Log.e("111","value="+value+"  entry.x="+entry.getX()+"  dataSetIndex="+dataSetIndex);
                return null;
            }
        });
        if (mode == null) {
            //?????????????????????????????????????????????????????????????????????
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }


    private void initLineChart(LineChart lineChart){
        /***????????????***/
        //?????????????????????
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //??????????????????
        lineChart.setDrawBorders(false);
        //??????????????????
        lineChart.setDragEnabled(false);
        //?????????????????????
        lineChart.setTouchEnabled(false);
        //??????XY???????????????
        lineChart.animateY(0);
        lineChart.animateX(1500);


        //??????label
        Description description= new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        /***XY????????????***/
        XAxis xAxis = lineChart.getXAxis();
        //??????x????????????
        xAxis.setmIsDrawScale(false);
        //X??????????????????????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.parseColor("#9CA5AC"));
        xAxis.setGranularity(1f);//??????X??????????????????????????????
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);//???????????????X???????????????
        xAxis.setXOffset(10);
        xAxis.setYOffset(8);

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawGridLines(true);//????????????Y???????????????
        leftYAxis.setGridLineWidth(0.5f);
        leftYAxis.setGridColor(ContextCompat.getColor(this,R.color.gray_line));
        leftYAxis.setTextColor(Color.parseColor("#9CA5AC"));
        leftYAxis.setDrawZeroLine(false);
        leftYAxis.setDrawAxisLine(false);//Y?????????
        leftYAxis.setmIsDrawScale(false);

        YAxis rightYaxis = lineChart.getAxisRight();
        rightYaxis.setEnabled(false);

        /***???????????? ?????? ??????***/
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
    }


    /**
     * ?????????
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

        // ?????????LineDataSet???????????????
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

        //X???
        rateChart.getXAxis().setValueFormatter(((value, axis) -> {
            int index = (int) value;
            if (index < 0 || index == bean.data.size()) {
                //TODO ??????value???-1
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
     * ??????
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


        // ?????????LineDataSet???????????????
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
            //?????????initLineDataSet()??????????????????LineDataSet.setDrawFilled(false)????????????????????????
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            spo2Chart.invalidate();
        }


        spo2Chart.getAxisLeft().setAxisMaximum(102f);
        spo2Chart.getAxisLeft().setAxisMinimum(75f);

        //Y??????
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

        //X??????
        spo2Chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if (index < 0 || index == bean.data.size()) {
                    //TODO ??????value???-1
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
     * ??????
     * @param bean
     */
    private void initEcg(HealthyReport.DataBeanXXX.EhrBean.EcgBean bean){
        //?????????0-?????????1-??????
        if (TextUtils.equals(bean.status,"0")){
            tv_ecg_status.setText("??????");
            tv_ecg_status.setTextColor(Color.parseColor("#FF5B58"));
        }else {
            tv_ecg_status.setText("??????");
            tv_ecg_status.setTextColor(Color.parseColor("#30C99A"));
        }

        tv_ecg_times.setText("???????????????"+bean.times+"???");

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



    //????????? ?????????
    private void initwalk_lineChart(HealthyReport.DataBeanXXX beanXXXX) {
        walk_lineChart.getDescription().setEnabled(false); // ???????????????
        walk_lineChart.setExtraOffsets(0, 0, 0, 22f); // ????????????????????????????????????????????? ???????????????????????????
        walk_lineChart.setDrawBorders(false);
        walk_lineChart.setDrawGridBackground(false);
        walk_lineChart.setDragEnabled(false);
        walk_lineChart.setDoubleTapToZoomEnabled(false);
        walk_lineChart.setTouchEnabled(false);
        Legend legend = walk_lineChart.getLegend();
        legend.setEnabled(false);
        setAxis(beanXXXX); // ???????????????
//        setLegend(); // ????????????
        setData(beanXXXX);  // ????????????
    }

    private void setData(HealthyReport.DataBeanXXX bean) {
        List<IBarDataSet> sets = new ArrayList<>();
        // ???????????????DataSet???????????????????????????BarEntry????????????x???y????????????????????????????????????
        // x??????????????????????????????y???????????????????????????
        List<BarEntry> barEntries1 = new ArrayList<>();
        for (int index=0; index < bean.sport.walkInfoList.size(); index++){
            int step = bean.sport.walkInfoList.get(index).step;
//            if (step < 10)
//                step += 2100;
            barEntries1.add(new BarEntry(index, (float)step));
        }

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
        barDataSet1.setValueTextColor(Color.RED); // ????????????
        barDataSet1.setValueTextSize(0f); // ????????????
        barDataSet1.setColor(Color.parseColor("#00C9B4")); // ???????????????
//        barDataSet1.setLabel("??????"); // ?????????????????????????????????????????????????????????????????????
        // ????????????????????????????????????
        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // ?????????value????????????????????????
                return value + "???";
            }
        });

        sets.add(barDataSet1);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f); // ?????????????????????
        barData.setBarCharRaduis(true);//??????????????????
        walk_lineChart.setData(barData);
    }

//    private void setLegend() {
//        Legend legend = walk_lineChart.getLegend();
//        legend.setEnabled(false);
//        legend.setFormSize(12f); // ?????????????????????
//        legend.setTextSize(15f); // ?????????????????????
//        legend.setDrawInside(true); // ?????????????????????
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // ????????????????????????
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //??????????????????????????????
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // ??????????????????????????????
//        // ???????????????????????????????????????
//        legend.setYOffset(55f);
//        legend.setXOffset(30f);
//    }

    private void setAxis(HealthyReport.DataBeanXXX bean) {
        // ??????x???
        XAxis xAxis = walk_lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // ??????x????????????????????????????????????
        xAxis.setDrawGridLines(false); // ???????????????true??????????????????????????????
        xAxis.setLabelCount(bean.sport.walkInfoList.size());  // ??????x?????????????????????
        xAxis.setTextColor(getColor(R.color.global_999999));
        xAxis.setTextSize(10f); // x?????????????????????
        xAxis.setDrawXaxisCustomValues(true);

        List<String> list = new ArrayList<>();
        for (int index=0; index < bean.sport.walkInfoList.size(); index++){
            list.add(bean.sport.walkInfoList.get(index).date);
        }
        xAxis.setmLabelList(list);
        // ??????x????????????????????????
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if (index < 0 || index == weekDay.length) {
                    //TODO ??????value???-1
                    return "";
                }

                return weekDay[index];
            }
        });
        xAxis.setYOffset(8); // ???????????????x??????????????????????????????
        xAxis.setXOffset(10);

        // ??????y??????y?????????????????????????????????
        YAxis yAxis_right = walk_lineChart.getAxisRight();
        yAxis_right.setEnabled(false);  // ??????????????????y???

        YAxis yAxis_left = walk_lineChart.getAxisLeft();
        yAxis_left.setTextColor(getColor(R.color.global_999999));
        yAxis_left.setAxisMaximum(10000f);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setLabelCount(5);
        yAxis_left.setTextSize(10f); // ??????y??????????????????
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

package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.report.HealthyReportQuestionActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.SleepInfo;
import com.gzhealthy.health.model.WalkInfoModel;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.DateUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Tools;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.HealthDateChoiceView;
import com.gzhealthy.health.widget.RingView;
import com.gzhealthy.health.widget.SleepDateChoiceView;
import com.gzhealthy.health.widget.SleepRatioView;
import com.gzhealthy.health.widget.SleepsPopupWindow;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gzhealthy.health.widget.SleepDateChoiceView.OnDateChoiceListener;

/**
 * 睡眠监测
 */
public class SleepMonitoringActivity extends BaseAct implements PopupWindow.OnDismissListener {
    @BindView(R.id.sleep_time)
    TextView tv_sleep_time;
    @BindView(R.id.select_time)
    TextView tv_select_time;
    @BindView(R.id.deep)
    TextView tv_deep;
    @BindView(R.id.light)
    TextView tv_light;
    @BindView(R.id.deep_time)
    TextView tv_deep_time;
    @BindView(R.id.light_time)
    TextView tv_light_time;
    @BindView(R.id.paraphrase)
    TextView tv_paraphrase;
    @BindView(R.id.suggest)
    TextView tv_suggest;
    @BindView(R.id.paraphrase_desc)
    TextView tv_paraphrase_desc;
    @BindView(R.id.suggest_desc)
    TextView tv_suggest_desc;
    @BindView(R.id.no_data)
    ImageView iv_no_data;

    @BindView(R.id.sleep_container)
    LinearLayout sleep_container;
    @BindView(R.id.sleep_hour_container)
    LinearLayout sleep_hour_container;
    @BindView(R.id.top_lay)
    LinearLayout top_lay;

    @BindView(R.id.sleep_view)
    SleepRatioView sleep_view;
    @BindView(R.id.sleep_date_choice)
    SleepDateChoiceView sleepDateChoiceView;
    CalendarPopWindow popWindow;
    Map<String,String> param = new HashMap<>();
    SleepsPopupWindow window;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sleep_monitoring;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("睡眠监测");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(SleepMonitoringActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        setBarRighticon(R.mipmap.icon_right_date);
        getIvRight().setOnClickListener(v -> {
            popWindow.showAsDropDown(v);
        });

        //时间往前一天
//        sleepDateChoiceView.setDate(SleepDateChoiceView.TX_LAST_DAY);
        sleepDateChoiceView.setOnDateChoiceListener(new OnDateChoiceListener() {
            @Override
            public void onPrevious(String date) {
                String day = DateUtil.scrollCalendarDate(date,-1);
                RxBus.getInstance().post(RxEvent.ON_SCROLL_CALENDAR_DATE,day);
                getSleep(day);
                sleepDateChoiceView.setDate(day);
            }

            @Override
            public void onNext(String date) {
                if (DateUtils.compareDateCount(date) < -1){
                    //日期小于今天的继续
                    String day = DateUtil.scrollCalendarDate(date,1);
                    RxBus.getInstance().post(RxEvent.ON_SCROLL_CALENDAR_DATE,day);
                    getSleep(day);
                    sleepDateChoiceView.setDate(day);
                }
            }
        });

        popWindow = new CalendarPopWindow(this, rxManager,day -> {
            new Handler().postDelayed(() -> {
                sleepDateChoiceView.setDate(day);
                getSleep(day);
            },100);
        },true);


        getSleep(DateUtil.scrollCalendarDate(DateUtil.getStringDate(),-1));
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-睡眠详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-睡眠详情");
    }

    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        super.setState(state);
    }

    public void getSleep(String date){
        param.put("token", SPCache.getString("token",""));
        param.put("dateTime", date);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getSleepInfo(param),
                new CallBack<SleepInfo>() {
                    @Override
                    public void onResponse(SleepInfo info) {
                        if (info.code == 1){
                            iv_no_data.setVisibility(View.GONE);
//                            tv_sleep_time.setText(info.data.sleepDuration);
                            tv_deep.setText(info.data.deepProportion+"%");
                            tv_light.setText(info.data.lightProportion+"%");
//                            tv_deep_time.setText(info.data.totalDeepDuration);
//                            tv_light_time.setText(info.data.totalLightDuration);
                            tv_paraphrase.setText(info.data.paraphrase.get(0));
                            tv_paraphrase_desc.setText(info.data.paraphrase.get(1));
                            tv_suggest.setText(info.data.suggest.get(0));
                            tv_suggest_desc.setText(info.data.suggest.get(1));
                            sleep_view.setRatio(Float.valueOf(info.data.deepProportion));

                            reformSleep(info.data.sleepDuration,tv_sleep_time);
                            reformSleep(info.data.totalDeepDuration,tv_deep_time);
                            reformSleep(info.data.totalLightDuration,tv_light_time);

                            horizontalRatioView(info.data);
                            tv_select_time.setOnClickListener(v -> {
                                WatchOperationDetailActivity.instance(SleepMonitoringActivity.this,info.data.imei);
                            });
                        }else if (info.code == 800){
                            iv_no_data.setVisibility(View.VISIBLE);
//                            tv_sleep_time.setText(info.data.sleepDuration);
                            tv_deep.setText(info.data.deepProportion+"%");
                            tv_light.setText(info.data.lightProportion+"%");
//                            tv_deep_time.setText(info.data.totalDeepDuration);
//                            tv_light_time.setText(info.data.totalLightDuration);
                            tv_paraphrase.setText(info.data.paraphrase.get(0));
                            tv_paraphrase_desc.setText(info.data.paraphrase.get(1));
                            tv_suggest.setText(info.data.suggest.get(0));
                            tv_suggest_desc.setText(info.data.suggest.get(1));
                            sleep_view.setRatio(Float.valueOf(info.data.deepProportion));

                            reformSleep(info.data.sleepDuration,tv_sleep_time);
                            reformSleep(info.data.totalDeepDuration,tv_deep_time);
                            reformSleep(info.data.totalLightDuration,tv_light_time);

                            horizontalRatioView(info.data);
                            tv_select_time.setOnClickListener(v -> {
                                WatchOperationDetailActivity.instance(SleepMonitoringActivity.this,info.data.imei);
                            });
                        }else {
                            ToastUtil.showToast(info.msg);
                        }
                    }
                });
    }


    /**
     * 深浅睡眠比例图   公式：（单个分钟数 / 总长分钟）*  总宽度  =  单个分钟在屏幕中占比）
     * @param bean
     */
    public void horizontalRatioView(SleepInfo.DataBean bean) {
        View view;//填充视图
        SleepInfo.DataBean.SleepDataBean nullSleepBean;//空View数据Bean
        int totalLong = sleep_container.getMeasuredWidth();//容器宽度
        LinearLayout.LayoutParams lp = null;
        int totalMinute = Math.abs((int) (DateUtil.calulationTotalMinute(bean.startTime, bean.endTime)));

        if (bean.sleepData.isEmpty()){
            sleep_container.removeAllViews();
        }else{
            window = new SleepsPopupWindow(this);
            window.setOnDismissListener(this);
            List<SleepInfo.DataBean.SleepDataBean> sleepDataBeanList = new ArrayList<>();
            //TODO 先判断第一个数据是否跟睡眠开始时间重叠
            SleepInfo.DataBean.SleepDataBean firstbean = bean.sleepData.get(0);
            if (firstbean.startTime.equals(bean.startTime)){
                //TODO 重叠一起就获取并添加
                sleepDataBeanList.add(firstbean);
            }else {
                nullSleepBean = new SleepInfo.DataBean.SleepDataBean();
                int minute = Math.abs((int) DateUtil.calulationTotalMinute(bean.startTime,firstbean.startTime));//计算空View的分钟
                nullSleepBean.duration = minute * 60 * 1000;
                nullSleepBean.startTime = bean.startTime;
                nullSleepBean.endTime = firstbean.startTime;
                nullSleepBean.type = "3";
                sleepDataBeanList.add(nullSleepBean);
                sleepDataBeanList.add(firstbean);//添加完空View后仍然将第一个bean再次添加
            }

            String endTime = firstbean.endTime;//临时存储起最后的结束时间

            //TODO 已添加第一个Bean 所以i 要从1开始迭代
            for (int i=1; i < bean.sleepData.size(); i++){
                if (bean.sleepData.get(i).startTime.equals(endTime)){
                    //TODO 时间若前后吻合即为连续性
                    sleepDataBeanList.add(bean.sleepData.get(i));

                }else {
                    // 若前后不吻合即为中途被中断过
                    nullSleepBean = new SleepInfo.DataBean.SleepDataBean();
                    int minute = Math.abs((int) DateUtil.calulationTotalMinute(endTime,bean.sleepData.get(i).startTime));//计算空View的分钟
                    nullSleepBean.duration = minute * 60 * 1000;
                    nullSleepBean.startTime = endTime;
                    nullSleepBean.endTime = bean.sleepData.get(i).startTime;
                    nullSleepBean.type = "3";
                    sleepDataBeanList.add(nullSleepBean);
                    sleepDataBeanList.add(bean.sleepData.get(i));
                }

                //TODO 判断最后一个睡眠结束时间是否跟总结束时间吻合
                if (i == bean.sleepData.size()-1){
                    sleepDataBeanList.add(bean.sleepData.get(i));
                    if (!bean.sleepData.get(i).endTime.equals(bean.endTime)){
                        //不吻合需要再添加空视图填充
                        nullSleepBean = new SleepInfo.DataBean.SleepDataBean();
                        int minute = Math.abs((int) DateUtil.calulationTotalMinute(endTime,bean.endTime));//计算空View的分钟
                        nullSleepBean.duration = minute * 60 * 1000;
                        nullSleepBean.startTime = endTime;
                        nullSleepBean.endTime = bean.sleepData.get(i).startTime;
                        nullSleepBean.type = "3";
                        sleepDataBeanList.add(nullSleepBean);
                    }
                }


                endTime = sleepDataBeanList.get(sleepDataBeanList.size()-1).endTime;
            }

            sleep_container.removeAllViews();
            for (int j =0;  j < sleepDataBeanList.size(); j++){
                view = new View(this);
                SleepInfo.DataBean.SleepDataBean sleepDataBean = sleepDataBeanList.get(j);
                int min = sleepDataBean.duration / 60000;
                float calMin = (float) min / (float) totalMinute;
                float width = calMin * totalLong;
                if (sleepDataBean.type.equals("1")){//深睡
                    view.setBackgroundColor(Color.parseColor("#7A62D1"));
                }else if (sleepDataBean.type.equals("2")){//浅睡
                    view.setBackgroundColor(Color.parseColor("#C0B1FF"));
                }else{
                    view.setBackgroundColor(Color.parseColor("#5294FF"));
                }
                lp = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.FILL_PARENT);
                view.setLayoutParams(lp);
                view.setTag(sleepDataBean);
                view.setOnClickListener(v -> {
                    SleepInfo.DataBean.SleepDataBean sleeps = (SleepInfo.DataBean.SleepDataBean) v.getTag();
                    window.showPop(sleepDateChoiceView,sleeps);
                    top_lay.setVisibility(View.INVISIBLE);
                });
                sleep_container.addView(view);
            }
        }



        sleep_hour_container.removeAllViews();
        LinearLayout.LayoutParams params;
        TextView txTime = null;
        if (!TextUtils.isEmpty(bean.startTime)){
            txTime = new TextView(this);
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txTime.setLayoutParams(params);
            txTime.setText(bean.startTime);
            txTime.setTextSize(12f);
            txTime.setTextColor(Color.parseColor("#333333"));
            sleep_hour_container.addView(txTime);
        }

        if (!TextUtils.isEmpty(bean.endTime)){
            txTime = new TextView(this);
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            txTime.setLayoutParams(params);
            txTime.setGravity(Gravity.END);
            txTime.setText(bean.endTime);
            txTime.setTextSize(12f);
            txTime.setTextColor(Color.parseColor("#333333"));
            sleep_hour_container.addView(txTime);
        }
    }

    public void reformSleep(String str,TextView view){
        try {
            if (TextUtils.isEmpty(str)) {
                view.setText("--");
                return;
            }

            int size = DispUtil.sp2px(this,18);
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            if (str.contains("小")&&str.contains("分")){
                //有小时和分钟
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new AbsoluteSizeSpan(size),str.indexOf("时")+1,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if (str.contains("小")){
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                builder.setSpan(new AbsoluteSizeSpan(size),str.indexOf("时")+1,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            view.setText(builder);
        }catch (Exception e){
            e.printStackTrace();
            view.setText("--");
        }

    }

    public static long calulationAddHourTime(String time){
        long total = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            Date date = new Date();
            date = format.parse(time);
            total =  date.getTime();
//            total = timemillon / 60000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return total;
    }


    public static void inStance(Context context){
        context.startActivity(new Intent(context, SleepMonitoringActivity.class));
    }

    @Override
    public void onDismiss() {
        top_lay.setVisibility(View.VISIBLE);
    }
}
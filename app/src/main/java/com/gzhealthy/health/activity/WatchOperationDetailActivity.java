package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.WatchDeviceModel;
import com.gzhealthy.health.model.banner.DeviceDetail;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CommonKnowDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

public class WatchOperationDetailActivity extends BaseAct {
    static final int TYPE_SWITCH_SLEEP = 3;

    static final int TYPE_SWITCH_SPORT = 1;

    static final int TYPE_SWITCH_BREAKFAST = 2;


    static final int SELECT_TIME_START = 0x101;

    static final int SELECT_TIME_END = 0x102;

    static final int SELECT_TIME_SPORT = 0x103;

    static final int SELECT_TIME_BREAKFAST = 0x104;

    static final int SELECT_TIME_SLEEP = 0x105;

    @BindView(R.id.model)
    TextView tv_model;

    @BindView(R.id.imei)
    TextView tv_imei;

    @BindView(R.id.version)
    TextView tv_version;

    @BindView(R.id.raise_hand_lightscreen)
    TextView light_Screen;

    @BindView(R.id.long_sit_warnning)
    TextView long_sit;

    @BindView(R.id.heart_rate_warnning)
    TextView heart_rate_warnning;

    @BindView(R.id.ivTip)
    ImageView ivTip;

    @BindView(R.id.img)
    ImageView img;

    @BindView(R.id.start_time)
    TextView start_time;

    @BindView(R.id.end_time)
    TextView end_time;

    @BindView(R.id.sleep_time)
    TextView sleep_time;

    @BindView(R.id.sport_time)
    TextView sport_time;

    @BindView(R.id.breakfast_time)
    TextView breakfast_time;

    @BindView(R.id.tips_breakfast)
    TextView tips_breakfast;

    @BindView(R.id.tips_sleep)
    TextView tips_sleep;

    @BindView(R.id.tips_sport)
    TextView tips_sport;

    @BindView(R.id.safe_fence)
    TextView safe_fence;

    @BindView(R.id.sport_lay)
    LinearLayout sport_lay;

    @BindView(R.id.sleep_lay)
    LinearLayout sleep_lay;

    @BindView(R.id.breakfast_lay)
    LinearLayout breakfast_lay;

    WatchDeviceModel.DataBean model;
    TimePickerView pvTime;

    String startTime, endTime;
    int select_date_type;
    String imei = "";

    @Override
    protected int getContentLayout() {
        return R.layout.activity_watch_operation_detail;
    }


    public static void instance(Context context, WatchDeviceModel.DataBean bean) {
        Intent intent = new Intent(context, WatchOperationDetailActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN, bean);
        context.startActivity(intent);
    }

    public static void instance(Context context, String imei) {
        Intent intent = new Intent(context, WatchOperationDetailActivity.class);
        intent.putExtra("IMEI", imei);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("我的设备");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(WatchOperationDetailActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getTvRight().setText("解绑");
        getTvRight().setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getTvRight().setOnClickListener(v -> {
            showUnBindDailog();
        });
        light_Screen.setOnClickListener(v -> {
            DeviceDetail.DataBean bean = (DeviceDetail.DataBean) v.getTag();
            updateScreen(bean, light_Screen.isSelected());
        });
        long_sit.setOnClickListener(v -> {
            DeviceDetail.DataBean bean = (DeviceDetail.DataBean) v.getTag();
            updateSit(bean, long_sit.isSelected());
        });
        heart_rate_warnning.setOnClickListener(v -> {
            DeviceDetail.DataBean bean = (DeviceDetail.DataBean) v.getTag();
            updateRateWarnStatus(bean, heart_rate_warnning.isSelected());
        });
        ivTip.setOnClickListener(v -> {
            String hint = "当心率预警时，系统会弹出弹窗确认用户是否需要帮助，如10分钟内用户未能回应，系统将自动向用户拨打电话询问。";
            new CommonKnowDialog(WatchOperationDetailActivity.this).setHint(hint).show();
        });
        start_time.setOnClickListener(v -> {
            select_date_type = SELECT_TIME_START;
            initTimePicker();
        });
        end_time.setOnClickListener(v -> {
            select_date_type = SELECT_TIME_END;
            initTimePicker();
        });
        sport_time.setOnClickListener(v -> {
            select_date_type = SELECT_TIME_SPORT;
            initTimePicker();
        });
        sleep_time.setOnClickListener(v -> {
            select_date_type = SELECT_TIME_SLEEP;
            initTimePicker();
        });
        breakfast_time.setOnClickListener(v -> {
            select_date_type = SELECT_TIME_BREAKFAST;
            initTimePicker();
        });
        tips_breakfast.setOnClickListener(v -> {
            setControlSwitch(TYPE_SWITCH_BREAKFAST,tips_breakfast.isSelected()?0:1);
        });
        tips_sleep.setOnClickListener(v -> {
            setControlSwitch(TYPE_SWITCH_SLEEP,tips_sleep.isSelected()?0:1);
        });
        tips_sport.setOnClickListener(v -> {
            setControlSwitch(TYPE_SWITCH_SPORT,tips_sport.isSelected()?0:1);
        });
        safe_fence.setOnClickListener(v -> {
            SecurityFenceActivity.instance(this,imei);
        });

        imei = "";
        model = (WatchDeviceModel.DataBean) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        if (model == null) {
            imei = getIntent().getStringExtra("IMEI");
        } else
            imei = model.imei;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDetail(imei);
    }

    public void getDetail(String imei) {
        Map<String, String> params = new HashMap<>();
        params.put("imei", imei);
        params.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getDeviceDetail(params),
                new CallBack<DeviceDetail>() {
                    @Override
                    public void onResponse(DeviceDetail data) {
                        if (data.code == 1) {
                            GlideUtils.loadCustomeImg(img,data.data.photoUrl);
                            tv_model.setText(data.data.model);
                            tv_imei.setText(data.data.imei);
                            tv_version.setText(data.data.ver);
                            light_Screen.setSelected(data.data.screenStatus == 1 ? true : false);
                            light_Screen.setTag(data.data);
                            long_sit.setSelected(data.data.sedentaryStatus == 1 ? true : false);
                            long_sit.setTag(data.data);
                            heart_rate_warnning.setSelected(data.data.rateWarnStatus == 1 ? true : false);
                            heart_rate_warnning.setTag(data.data);

                            tips_breakfast.setSelected(data.data.breakfastStatus==1?true:false);
                            tips_sport.setSelected(data.data.sportStatus==1?true:false);
                            tips_sleep.setSelected(data.data.sleepStatus==1?true:false);
                            breakfast_lay.setVisibility(data.data.breakfastStatus==1?View.VISIBLE:View.GONE);
                            sport_lay.setVisibility(data.data.sportStatus==1?View.VISIBLE:View.GONE);
                            sleep_lay.setVisibility(data.data.sleepStatus==1?View.VISIBLE:View.GONE);
                            breakfast_time.setText(data.data.breakfastRemindTime);
                            sleep_time.setText(data.data.sleepRemindTime);
                            sport_time.setText(data.data.sportRemindTime);

                            startTime = data.data.sleepStartTime;
                            endTime = data.data.sleepEndTime;
                            start_time.setText(startTime);
                            end_time.setText(endTime);
                            try {
                                long start = DateUtil.dateToStamp(startTime, "HH:mm");
                                long end = DateUtil.dateToStamp(endTime, "HH:mm");
                                if (start >= end) {
                                    end_time.setText("次日 " + endTime);
                                } else {
                                    end_time.setText(endTime);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            safe_fence.setText(data.data.safetyFenceStatus==1?"已开启":"未开启");
                            safe_fence.setTextColor(data.data.safetyFenceStatus==1?
                                    ContextCompat.getColor(WatchOperationDetailActivity.this,R.color.global_333333) : Color.parseColor("#CCCCCC"));
                        }
                    }
                });
    }


    /**
     * 设置亮屏
     *
     * @param deviceDetail
     * @param isCheck
     */
    public void updateScreen(DeviceDetail.DataBean deviceDetail, boolean isCheck) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", deviceDetail.imei);
        params.put("screenStatus", isCheck ? "0" : "1");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateScreenStatus(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            light_Screen.setSelected(!isCheck);
                        }

                    }
                });
    }


    /**
     * 设置久坐
     *
     * @param deviceDetail
     * @param isCheck
     */
    public void updateSit(DeviceDetail.DataBean deviceDetail, boolean isCheck) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", deviceDetail.imei);
        params.put("sedentaryStatus", isCheck ? "0" : "1");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateSedentaryStatus(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            long_sit.setSelected(!isCheck);
                        }

                    }
                });
    }

    public void updateRateWarnStatus(DeviceDetail.DataBean deviceDetail, boolean isCheck) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", deviceDetail.imei);
        params.put("rateWarnStatus", isCheck ? "0" : "1");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateRateWarnStatus(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            heart_rate_warnning.setSelected(!isCheck);
                        }

                    }
                });
    }

    public void unbindWatch() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().watchUnBind(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
                            finish();
                        }
                        ToastUtil.showToast(data.getMsg());
                    }
                });
    }


    private void showUnBindDailog() {
        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_unbind_watch)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            unbindWatch();
                            baseLDialog.dismiss();
                        });
                    }
                })
                .show();
    }


    private void initTimePicker() {
//        if (pvTime == null) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Time t = new Time("GMT+8");
        t.setToNow();
//            selectedDate.set(t.year, t.month, t.monthDay,1,10);
//            selectedDate.set(Calendar.YEAR,t.year);
//            selectedDate.set(Calendar.MONTH,t.month);
//            selectedDate.set(Calendar.DAY_OF_MONTH,t.monthDay);
//            selectedDate.set(Calendar.HOUR_OF_DAY,1);
//            selectedDate.set(Calendar.MINUTE,33);
        startDate.set(t.year, t.month, t.monthDay);
        endDate.set(t.year, t.month, t.monthDay);
        pvTime = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String dateTime = dateFormat.format(date);
            if (select_date_type == SELECT_TIME_START) {
                startTime = dateTime;
                setSleepTime();
            } else if (select_date_type == SELECT_TIME_END) {
                endTime = dateTime;
                setSleepTime();
            }else if (select_date_type == SELECT_TIME_SPORT){
                setTime(TYPE_SWITCH_SPORT,dateTime);
            }else if (select_date_type == SELECT_TIME_BREAKFAST){
                setTime(TYPE_SWITCH_BREAKFAST,dateTime);
            }else{
                setTime(TYPE_SWITCH_SLEEP,dateTime);
            }
        })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .setDividerColor(ContextCompat.getColor(this, R.color.text1))
                .setDividerType(WheelView.DividerType.FILL)
                .setContentSize(21)
                .gravity(Gravity.CENTER)
                .setTitleText(select_date_type == 1 ? "开始时间" : "结束时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
                .setSubmitColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setCancelColor(ContextCompat.getColor(this, R.color.global_333333))
                .build();
//        }
//        pvTime.setTitleText(type == 1?"开始时间":"结束时间");
        pvTime.show();
    }


    /**
     * 设置睡眠时间
     */
    public void setSleepTime() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", model == null ? imei : model.imei);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().setSleep(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.code == 1) {
                            start_time.setText(startTime);
                            end_time.setText(endTime);

                            try {
                                long start = DateUtil.dateToStamp(startTime, "HH:mm");
                                long end = DateUtil.dateToStamp(endTime, "HH:mm");
                                if (start >= end) {
                                    end_time.setText("次日 " + endTime);
                                } else {
                                    end_time.setText(endTime);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


    /**
     * 设置运动 睡眠 早餐
     * @param type
     * @param status
     */
    public void setControlSwitch(int type,int status) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", model == null ? imei : model.imei);
        params.put("type", String.valueOf(type));
        params.put("status", String.valueOf(status));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().controlSwitch(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1) {
                            switch (type){
                                case TYPE_SWITCH_SPORT://运动
                                    tips_sport.setSelected(status==1?true:false);
                                    sport_lay.setVisibility(status==1?View.VISIBLE:View.GONE);
                                    if (status == 1){
                                        setDefaultTipsDateTime(SELECT_TIME_SPORT);
                                    }
                                    break;

                                case TYPE_SWITCH_BREAKFAST://早餐
                                    tips_breakfast.setSelected(status==1?true:false);
                                    breakfast_lay.setVisibility(status==1?View.VISIBLE:View.GONE);
                                    if (status == 1){
                                        setDefaultTipsDateTime(SELECT_TIME_BREAKFAST);
                                    }
                                    break;

                                case TYPE_SWITCH_SLEEP://睡眠
                                    tips_sleep.setSelected(status==1?true:false);
                                    sleep_lay.setVisibility(status==1?View.VISIBLE:View.GONE);
                                    if (status == 1){
                                        setDefaultTipsDateTime(SELECT_TIME_SLEEP);
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }



    public void setTime(int type,String time) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei", model == null ? imei : model.imei);
        params.put("type", String.valueOf(type));
        params.put("time", time);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().setTime(params), new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1) {
                            switch (type){
                                case TYPE_SWITCH_SPORT:
                                    sport_time.setText(time);
//                                    setLocalPushTips(TYPE_SWITCH_SPORT,time);
                                    break;

                                case TYPE_SWITCH_BREAKFAST:
                                    breakfast_time.setText(time);
                                    break;

                                case TYPE_SWITCH_SLEEP:
                                    sleep_time.setText(time);
                                    break;
                            }
                        }else
                            ToastUtil.showToast(data.getMsg());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 截取小时数值
     *
     * @param time
     * @return
     */
    public int subTime4Hour(String time) {
        if (TextUtils.isEmpty(time))
            return 0;
        return Integer.parseInt(time.substring(0, time.indexOf(":")));
    }


    /**
     * 截取分钟数值
     *
     * @param time
     * @return
     */
    public int subTime4Minute(String time) {
        if (TextUtils.isEmpty(time))
            return 0;
        return Integer.parseInt(time.substring(time.indexOf(":") + 1));
    }


    public void setDefaultTipsDateTime(int type){
        String defaultTime = "";//DateUtil.getStringDateNow("HH:mm");
        switch (type){
            case SELECT_TIME_SPORT:
                defaultTime = sport_time.getText().toString();
                if (TextUtils.isEmpty(defaultTime)){
                    defaultTime = DateUtil.getStringDateNow("HH:mm");
                    sport_time.setText(defaultTime);
                }
                setTime(TYPE_SWITCH_SPORT,defaultTime);
                break;

            case SELECT_TIME_BREAKFAST:
                defaultTime = breakfast_time.getText().toString();
                if (TextUtils.isEmpty(defaultTime)){
                    defaultTime = converDateTime(endTime,0);
                    breakfast_time.setText(defaultTime);
                }
                setTime(TYPE_SWITCH_BREAKFAST,defaultTime);
                break;

            default:
                defaultTime = sleep_time.getText().toString();
                if (TextUtils.isEmpty(defaultTime)){
                    defaultTime = converDateTime(startTime,1);
                    sleep_time.setText(defaultTime);
                }
                setTime(TYPE_SWITCH_SLEEP,defaultTime);
                break;
        }
    }


    /**
     * 加减提醒时间
     * @param time
     * @param timeType 0是+  1是-
     * @return
     */
    public String converDateTime(String time,int timeType){
        String resultTime = "";
        long TimeMillisOffset = 30 * 60 * 1000;//半小时
        try {
            long times = DateUtil.dateToStamp(time,"HH:mm");
            if (timeType == 0){
                times += TimeMillisOffset;
            }else
                times -= TimeMillisOffset;

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            resultTime = dateFormat.format(new Date(times));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTime;
    }


//    /**
//     * 本地提醒推送
//     * @param type
//     * @param remindTime (HH:mm)
//     */
//    public void setLocalPushTips(int type,String remindTime){
//        String title="";
//        String content="";
//
//        JPushLocalNotification ln = new JPushLocalNotification();
//        ln.setBuilderId(0);
//        ln.setContent("极光推送的本地通知内容提醒");
//        ln.setTitle("本地提醒推送");
//        ln.setNotificationId(10001);
//        long remindTemp = calulationTipsTime(remindTime);
//        ln.setBroadcastTime(remindTemp);
//
//        Map<String , Object> map = new HashMap<String, Object>() ;
//        map.put("name", "jpush");
//        map.put("test", "111") ;
//        JSONObject json = new JSONObject(map) ;
//        ln.setExtras(json.toString()) ;
//        JPushInterface.addLocalNotification(getApplicationContext(), ln);
//    }


//    /**
//     * 计算设定提醒时间之间的毫秒
//     * @param time
//     * @return
//     */
//    public long calulationTipsTime(String time){
//        long result = 0;
//        try {
//            //当前日期毫秒
//            long currenDayTimeMillis = System.currentTimeMillis();
//            //提醒时分
//            long remindHour = DateUtil.dateToStamp(time,"HH:mm");
//            //当前时分
//            long remindCurrenHour = DateUtil.dateToStamp(DateUtil.getStringDateNow("HH:mm"),"HH:mm");
//            if (remindCurrenHour > remindHour){
//                //大于后 设置提醒日期为明天
//                result = currenDayTimeMillis - (remindCurrenHour - remindHour) + (24 * 60 * 60 * 1000);
//            }else {
//                result = currenDayTimeMillis + (remindHour - remindCurrenHour);
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
}
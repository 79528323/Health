package com.gzhealthy.health.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.WalkInfoModel;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.HealthDateChoiceView;
import com.gzhealthy.health.widget.RingView;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.gzhealthy.health.widget.HealthDateChoiceView.*;

public class WalkAndCalorieActivity extends BaseAct {
    @BindView(R.id.rings)
    RingView ringView;
    @BindView(R.id.healthChoiceView)
    HealthDateChoiceView healthDateChoiceView;
    CalendarPopWindow popWindow;
    Map<String,String> param = new HashMap<>();
    String pattern;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_walk_and_calorie;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("运动量统计");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(WalkAndCalorieActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        setBarRighticon(R.mipmap.icon_right_date);
        getIvRight().setOnClickListener(v -> {
            popWindow.showAsDropDown(v);
        });

        pattern = "yyyy-MM-dd";
        restData();

        healthDateChoiceView.setOnDateChoiceListener(new OnDateChoiceListener() {
            @Override
            public void onPrevious(String date) {
                try {
                    String day = scrollCalendarDate(date,-1,pattern);
                    healthDateChoiceView.setDate(day);
                    getWalk(day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(String date) {
                try {
                    String day = scrollCalendarDate(date,1,pattern);
                    healthDateChoiceView.setDate(day);
                    getWalk(day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        popWindow = new CalendarPopWindow(this, rxManager,day -> {
            new Handler().postDelayed(() -> {
                healthDateChoiceView.setDate(day);
                getWalk(day);
            },100);
        });

        getWalk(DateUtil.getStringDate());
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-运动量统计");
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-运动量统计");
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        super.setState(state);
    }


    public void getWalk(String date){
        param.put("token", SPCache.getString("token",""));
        param.put("dateTime", date);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getWalkInfo(param),
                new CallBack<WalkInfoModel>() {
            @Override
            public void onResponse(WalkInfoModel data) {
                if (data.getCode() == 1){
                    ringView.setCalorie(data.getData().kcal);
                    ringView.setWalk(data.getData().walk);
                    ringView.setKilo(data.getData().distance);
                }else {
                    restData();
                }
            }
        });
    }

    /**
     * 重置数据
     */
    public void restData(){
        ringView.setWalk(0);
        ringView.setCalorie(0);
        ringView.setKilo(0);
    }


    /**
     * 向前或往后一天
     * @param date
     * @param count
     * @param pattern 格式
     */
    private String scrollCalendarDate(String date,int count,String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.add(Calendar.DAY_OF_MONTH,count);
        Date mDate = calendar.getTime();
        String d = format.format(mDate);
        RxBus.getInstance().post(RxEvent.ON_SCROLL_CALENDAR_DATE,d);
        return d;
    }
}
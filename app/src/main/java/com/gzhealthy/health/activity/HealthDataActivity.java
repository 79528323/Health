package com.gzhealthy.health.activity;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.fragment.BloodSugarFragment;
import com.gzhealthy.health.fragment.HealthBloodPressureFragment;
import com.gzhealthy.health.fragment.HeartECGFragment;
import com.gzhealthy.health.fragment.HeartSpo2Fragment;
import com.gzhealthy.health.fragment.HeartRateFragment;
import com.gzhealthy.health.model.tab.HealthTab;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.HealthTabPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.gzhealthy.health.widget.HealthDateChoiceView.*;

public class HealthDataActivity extends BaseAct {
    private CalendarPopWindow popWindow;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setBarRighticon(R.mipmap.icon_right_date);
        getIvRight().setOnClickListener(v -> {
            popWindow.showAsDropDown(v);
        });


        List<HealthTab> healthTabList = new ArrayList<>();
        HealthTab bloodPressure = new HealthTab("血压",HealthBloodPressureFragment.class);
        HealthTab heartRate = new HealthTab("心率", HeartRateFragment.class);
        HealthTab heartEkg = new HealthTab("心电", HeartECGFragment.class);
        HealthTab oxygen = new HealthTab("血氧", HeartSpo2Fragment.class);
        HealthTab sugar = new HealthTab("血糖", BloodSugarFragment.class);

        HealthTabPager tabPager = (HealthTabPager) findViewById(R.id.tab_pager);
        tabPager.setTabTextColor(getColor(R.color.global_333333),getColor(R.color.colorPrimary));
        tabPager.getChoiceView().setOnDateChoiceListener(new OnDateChoiceListener() {

            @Override
            public void onPrevious(String date) {
                try {
                    String day = scrollCalendarDate(date,-1);
                    tabPager.getChoiceView().setDate(day);
                    RxBus.getInstance().post(RxEvent.ON_SELECT_HEALTHY_DATE,day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(String date) {
                try {
                    String day = scrollCalendarDate(date,1);
                    tabPager.getChoiceView().setDate(day);
                    RxBus.getInstance().post(RxEvent.ON_SELECT_HEALTHY_DATE,day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        popWindow = new CalendarPopWindow(this, rxManager, day -> {
            RxBus.getInstance().post(RxEvent.ON_SELECT_HEALTHY_DATE,day);
            tabPager.getChoiceView().setDate(day);
            popWindow.dismiss();
        });

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt(IntentParam.HEALTH_TAB_POSITION,-1);//getIntent().getIntExtra(IntentParam.HEALTH_TAB_POSITION,-1);
        boolean all = bundle.getBoolean(IntentParam.HEALTH_TAB_IS_ALL,false);
        if (all){
            healthTabList.add(heartRate);
            healthTabList.add(bloodPressure);
            healthTabList.add(oxygen);
            healthTabList.add(heartEkg);
            healthTabList.add(sugar);
            position = position<=0 ? 0 :position;
            setTitle("健康数据");
        }else {
            if (position >= 0){
                String title = "";
                switch (position){
                    case 0:
                        title = "心率";
                        healthTabList.add(heartRate);
                        break;

                    case 1:
                        title = "血压";
                        healthTabList.add(bloodPressure);
                        break;

                    case 2:
                        title = "血氧";
                        healthTabList.add(oxygen);
                        break;

                    case 3:
                        title = "心电";
                        healthTabList.add(heartEkg);
                        break;

                    case 4:
                        title = "血糖";
                        healthTabList.add(sugar);
                        break;
                }

                setTitle(title);
            }

            position = 0;
            tabPager.setTabGone();
        }


        tabPager.setTabFragment(getSupportFragmentManager(),healthTabList,position);
        tabPager.setTabIndicatorColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tabPager.setTabIndicatorWidth(false);
        tabPager.showFragment(getSupportFragmentManager());
    }


    /**
     * 向前或往后一天
     * @param date
     * @param count
     */
    private String scrollCalendarDate(String date,int count) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.add(Calendar.DAY_OF_MONTH,count);
        Date mDate = calendar.getTime();
        String d = format.format(mDate);
        Log.e("111","days===="+d);
        RxBus.getInstance().post(RxEvent.ON_SCROLL_CALENDAR_DATE,d);
        return d;
    }

    public static void instance(Context context,int position,boolean isAll){
        Intent intent = new Intent(context,HealthDataActivity.class);
        intent.putExtra(IntentParam.HEALTH_TAB_POSITION,position);
        intent.putExtra("all",isAll);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (popWindow!=null){
            popWindow.onDestroy();
        }
        super.onDestroy();
    }
}
package com.gzhealthy.health.fragment;

import android.Manifest;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.AboutHealthContentActivity;
import com.gzhealthy.health.activity.BodyInfoActivity;
import com.gzhealthy.health.activity.DesinfectionBlueToothActivity;
import com.gzhealthy.health.activity.HealthBodyInfoActivity;
import com.gzhealthy.health.activity.HealthCardManagerActivity;
import com.gzhealthy.health.activity.HealthDataActivity;
import com.gzhealthy.health.activity.HealthyClockActivity;
import com.gzhealthy.health.activity.InsertSosContactsActivity;
import com.gzhealthy.health.activity.MapActivity;
import com.gzhealthy.health.activity.MyFamilyInviteActivity;
import com.gzhealthy.health.activity.SleepMonitoringActivity;
import com.gzhealthy.health.activity.SosMessageActivity;
import com.gzhealthy.health.activity.VideoConsultationActivity;
import com.gzhealthy.health.activity.WalkAndCalorieActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.activity.report.HealthyReportBigDataActivity;
import com.gzhealthy.health.activity.report.HealthyReportResultActivity;
import com.gzhealthy.health.activity.report.HealthyTianReportActivity;
import com.gzhealthy.health.adapter.HealthCardAdapter;
import com.gzhealthy.health.adapter.HomeTypesAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.contract.HomeContract;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.EmergencyContact;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.HomePageHealthClock;
import com.gzhealthy.health.model.HomeTypesModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.presenter.HomePresenter;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Utils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ShareUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CircleSmallView;
import com.gzhealthy.health.widget.PrivacyPolicyDialog;
import com.gzhealthy.health.widget.WalkerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.commonsdk.UMConfigure;
import com.youth.banner.Banner;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.util.BannerUtils;
import com.yxp.permission.util.lib.PermissionInfo;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionOriginResultCallBack;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class HomeFragment extends BaseFra implements HomeContract.View {

    @BindView(R.id.btn_walkcalorie)
    LinearLayout btn_walkcalorie;
    @BindView(R.id.user_icon)
    ImageView user_icon;
    @BindView(R.id.bage)
    TextView bage;
    @BindView(R.id.sos)
    RelativeLayout sos;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.calorie)
    TextView calroie;
    @BindView(R.id.icon_time_img)
    ImageView icon_time_img;
    @BindView(R.id.time_linear)
    LinearLayout time_linear;
    @BindView(R.id.time_merdian)
    TextView time_merdian;
    @BindView(R.id.time_merdian_desc)
    TextView time_merdian_desc;
    @BindView(R.id.time_regimen)
    TextView time_regimen;
    @BindView(R.id.card_manager)
    TextView card_manager;
    @BindView(R.id.card_recycler)
    RecyclerView card_recycler;
    @BindView(R.id.calorie_view)
    CircleSmallView calorie_view;
    @BindView(R.id.distance_view)
    CircleSmallView distance_view;
    @BindView(R.id.walk_view)
    WalkerView walk_view;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mBanner)
    Banner mBanner;

    private HealthCardAdapter adapter;
    private boolean isAcceptPrivacy = false;

    private UserInfo.DataBean userInfo;
    private HomePresenter presenter;
    private PrivacyPolicyDialog privacyPolicyDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home_layout;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this, loadingPageView);

        $(R.id.report).setOnClickListener(this::widgetClick);
        $(R.id.consult).setOnClickListener(this::widgetClick);
        btn_walkcalorie.setOnClickListener(this);
        $(R.id.health_data).setOnClickListener(this);
        $(R.id.health_big_data).setOnClickListener(this);
        $(R.id.health_que).setOnClickListener(this);
        sos.setOnClickListener(this);
        card_manager.setOnClickListener(this);
//        health_file.setOnClickListener(this);
//        pressure_linear.setOnClickListener(this);
//        rate_linear.setOnClickListener(this);
//        spo2_linear.setOnClickListener(this);
//        ecg_linear.setOnClickListener(this);
//        sleep_linear.setOnClickListener(this);
        location.setOnClickListener(this);
        location.setEnabled(false);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
//            if (isUserLogin()) {
//                getpersonInfo();
//                queryHealthyData();
//                getLoc();
//                personInsertPresenter.getEmergencyContact();
//            } else {
//                restUiState();
//            }
//            updateBdage();
            getHomeViewData();
            refreshLayout.finishRefresh();
        });

        // 初次安装应用显示用户协议和隐私政策
        isAcceptPrivacy = ShareUtils.getBoolean(getActivity(), "isAcceptPrivacy", false);
        if (!isAcceptPrivacy) {
            showPrivacyPolicy();
        }

        adapter = new HealthCardAdapter(getActivity(), onHealthClickListener);
        card_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        card_recycler.setHasFixedSize(true);
        card_recycler.setNestedScrollingEnabled(false);
        card_recycler.setAdapter(adapter);

        onSubscribe();
        presenter = new HomePresenter(this, this, this);
//        restUiState();
        getpersonInfo();
    }


    public void onSubscribe() {
        rxManager.onRxEvent(RxEvent.REFRESH_MESSAGE_BDAGE)
                .subscribe((Action1<Object>) o -> updateBdage());

        rxManager.onRxEvent(new String[]{RxEvent.ON_LOG_IN_SUCCESS, RxEvent.ON_LOG_OUT},
                o -> setUserName());

        rxManager.onRxEvent(RxEvent.ON_REFRESH_SOS_HEALTHINFO_STATUS)
                .subscribe(o -> {
                    getpersonInfo();
                    ifExistEmergencyContact();
                });

        rxManager.onRxEvent(RxEvent.ON_REFRESH_HOME_USER_HEADER_IMAGE)
                .subscribe(o ->{
                    getpersonInfo();
//                    String headerPath = (String) o;
//                    GlideUtils.CircleImage(getActivity(), headerPath, user_icon);
                });
    }


    /**
     * 获取首页所需要接口数据
     */
    public void getHomeViewData() {
        getClock();
        if (isUserLogin()) {
            updateBdage();
            queryHealthyData();
            getLoc();
            ifExistEmergencyContact();
        } else
            restUiState();
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()) {
//            case R.id.sleep_linear:
//                SleepMonitoringActivity.inStance(getActivity());
//                break;

            case R.id.health_que:
                UserInfo.DataBean user = DataSupport.findFirst(UserInfo.DataBean.class);
                if (user==null){
                    LoginActivity.instance(getActivity());
                }else{
                    getHaveReport();
                }
//                Utils.gotoActy(getActivity(), BodyInfoActivity.class);
                break;

            case R.id.health_big_data://1.7.0需求 改为蓝牙设备入口
                Utils.gotoActy(getActivity(), DesinfectionBlueToothActivity.class);
                break;

            case R.id.report:
                Utils.gotoActy(getActivity(), HealthyTianReportActivity.class);
                break;

            case R.id.health_data://1.7.0需求 改为健康数据汇总入口
                Utils.gotoActy(getActivity(), HealthyReportBigDataActivity.class);
//                Utils.gotoActy(getActivity(), HealthyReportBigDataActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 0);
//                bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, true);
//                Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
                break;

            case R.id.sos:
//                SosMessageActivity.instance(getActivity());
                Utils.gotoActy(getActivity(), SosMessageActivity.class);
                break;

            case R.id.btn_walkcalorie:
//                startActivity(
//                        new Intent(getActivity(), WalkAndCalorieActivity.class)
////                                    .putExtra("walk",steps.getText().toString())
//                                .putExtra("calorie",calroie.getText().toString()));
                Utils.gotoActy(getActivity(), WalkAndCalorieActivity.class);
                break;

            case R.id.location:
//                MapActivity.instance(getActivity());
                Utils.gotoActy(getActivity(), MapActivity.class);
                break;

            case R.id.card_manager:
//                HealthCardManagerActivity.instance(getActivity());
                Utils.gotoActy(getActivity(), HealthCardManagerActivity.class);
                break;
//
//            case R.id.sos_contract:
//                startActivity(new Intent(getActivity(), InsertSosContactsActivity.class));
//                break;

            case R.id.consult:
                VideoConsultationActivity.instance(getActivity());
                break;

//            case R.id.rate_linear:
//                HealthDataActivity.instance(getActivity(),0,false);
//                break;
//            case R.id.pressure_linear:
//                HealthDataActivity.instance(getActivity(),1,false);
//                break;
//            case R.id.spo2_linear:
//                HealthDataActivity.instance(getActivity(),2,false);
//                break;
//            case R.id.ecg_linear:
//                HealthDataActivity.instance(getActivity(),3,false);
//                Utils.gotoActy(getActivity(),VideoConsultationActivity.class);
//                break;


            default:
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getHomeViewData();
    }

    // FragmentPagerAdapter+ViewPager的使用场景
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getHomeViewData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(watchBindCofirmCallback);
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }


    /**
     * 重置首页数据状态
     */
    public void restUiState() {
        if (walk_view != null) {
            walk_view.setWalk(0);
        }
        if (distance != null) {
            distance.setText("0");
        }
        if (calroie != null) {
            calroie.setText("0");
        }
        if (calorie_view != null) {
            calorie_view.setValue(0);
        }
        if (distance_view != null) {
            distance_view.setValue(0);
        }
        if (location != null) {
            location.setText("暂无手表定位");
        }


        if (adapter != null) {
            List<HealthyInfo.DataBean> healthList = new ArrayList<>();
            List<Integer> typeList = HealthCardManagerActivity.getSPList();
            if (typeList != null && !typeList.isEmpty()) {
                for (int index = 0; index < typeList.size(); index++) {
                    healthList.add(new HealthyInfo.DataBean());
                }
            }
            adapter.refreshData(healthList, typeList);
        }

    }

    /**
     * 获取个人信息
     */
    public void getpersonInfo() {
        String token = SPCache.getString(SPCache.KEY_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            name.setText("");
            user_icon.setImageResource(R.mipmap.ic_details_map_bg);
            restUiState();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getAccountInfo(hashMap),
                new CallBack<UserInfo>() {
                    @Override
                    public void onResponse(UserInfo data) {
//                        if (data.getCode() == 10086) {
//                            LoginActivity.instance(getActivity());
//                            SPCache.putBoolean("islogin", false);
//                            return;
//                        }

                        if (data.data != null) {
                            GlideUtils.CircleImage(getActivity(), Constants.ImageResource.OSSHEAD + data.data.getHeadPic(), user_icon);
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                            userInfo = new UserInfo.DataBean();
                            userInfo = data.getData();
                            userInfo.save();

                            String textName = "";
                            if (TextUtils.isEmpty(userInfo.getNickName())) {
                                textName = userInfo.getWechatNickName();

                                if (TextUtils.isEmpty(textName)) {
                                    textName = userInfo.getName();
                                }
                            } else {
                                textName = userInfo.getNickName();
                            }
                            name.setText(textName);
                        } else {
                            name.setText("");
                            user_icon.setImageResource(R.mipmap.ic_details_map_bg);
                        }
                    }
                });

    }


    /**
     * 获取未读消息数
     */
    public void getNoReadMessage() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getUnEmergencyMsgCount(hashMap),
                new CallBack<BaseModel<Integer>>() {
                    @Override
                    public void onResponse(BaseModel<Integer> data) {
                        if (data.code == 1) {
                            if (data.data <= 0) {
                                bage.setVisibility(View.GONE);
                            } else {
                                bage.setVisibility(View.VISIBLE);
                                bage.setText(data.data > 99 ? data.data + "+" : String.valueOf(data.data));
                            }
                        } else {
                            bage.setVisibility(View.GONE);
                        }
                    }
                });

    }


    /**
     * 查看是否有体质分析
     */
    public void getHaveReport() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isHaveReport(hashMap),
                new CallBack<BaseModel<Boolean>>() {
                    @Override
                    public void onResponse(BaseModel<Boolean> data) {
                        if (data.code == 1) {
                            if (data.data) {
                                getReport();
                            }else {
                                String url = Constants.HealthDataValue.HEALTH_QUETION_URL+ getUserToken()+"&agent=Android";
                                WebViewActivity.startLoadLink(getActivity(),url,"中医体质问卷");
                            }
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }


    public void getClock() {
        HashMap<String, String> hashMap = new HashMap<>();
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getHomePageHealthClock(hashMap),
                new CallBack<HomePageHealthClock>() {
                    @Override
                    public void onResponse(HomePageHealthClock data) {
                        if (data.code == 1) {
                            //设置健康钟提示
                            String types = "";
                            switch (data.data.type) {
                                case 1:
                                    types = "子时";
                                    break;
                                case 2:
                                    types = "丑时";
                                    break;
                                case 3:
                                    types = "寅时";
                                    break;
                                case 4:
                                    types = "卯时";
                                    break;
                                case 5:
                                    types = "晨时";
                                    break;
                                case 6:
                                    types = "巳时";
                                    break;
                                case 7:
                                    types = "午时";
                                    break;
                                case 8:
                                    types = "未时";
                                    break;
                                case 9:
                                    types = "申时";
                                    break;
                                case 10:
                                    types = "酉时";
                                    break;
                                case 11:
                                    types = "戌时";
                                    break;
                                case 12:
                                    types = "亥时";
                                    break;
                            }

                            time_merdian.setText("此刻" + types);
                            time_merdian_desc.setText(data.data.meridian + "最旺");
                            time_regimen.setText(data.data.regimen);

                            time_linear.setSelected(DateUtil.isDayNight());
                            time_linear.setTag(data.data.type);
                            time_linear.setOnClickListener(v -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("title",time_merdian.getText().toString()+" | "+time_merdian_desc.getText().toString());
                                bundle.putString("description",data.data.regimen);
                                WebViewActivity.startLoadLink(getActivity()
                                        ,Constants.HealthDataValue.HEALTH_CLOCK_URL,"健康时钟",bundle);
                            });
                            icon_time_img.setSelected(DateUtil.isDayNight());
                        }
                    }
                });

    }

    /**
     * 获取GPS定位
     */
    public void getLoc() {
        if (presenter != null) {
            Map<String, String> params = new HashMap<>();
            params.put("token", SPCache.getString("token", ""));
            presenter.getLocation(params);
        }
    }


    /**
     * 查询首页数据
     */
    public void queryHealthyData() {
        if (isUserLogin()) {
            Map<String, String> params = new HashMap<>();
            params.put("token", SPCache.getString("token", ""));
            if (presenter != null)
                presenter.QueryHomeHealthyInfo(params);
        }
    }


    /**
     * 配置首页头像
     */
    public void setUserName() {
        UserInfo.DataBean bean = DataSupport.findFirst(UserInfo.DataBean.class);
        if (bean != null) {
            name.setText(
                    TextUtils.isEmpty(bean.getNickName()) ? bean.getWechatNickName() : bean.getNickName()
            );
            GlideUtils.CircleImage(getActivity(), Constants.ImageResource.OSSHEAD + bean.getHeadPic(), user_icon);
        } else {
            name.setText("");
            user_icon.setImageResource(R.mipmap.ic_details_map_bg);
        }
    }


    /**
     * 初次安装应用显示用户协议和隐私政策
     */
    private void showPrivacyPolicy() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_privacy, null);
        TextView tvPrivacy = (TextView) view.findViewById(R.id.tv_privacy);

        String content = "尊重用户个人隐私是体安App的一项基本政策。体安App是广州健康信息产业有限公司。用户通过点击“同意”的按钮后，表示用户明确知晓本隐私政策所列事实，并与广州健康信息产业有限公司达成协议并接受所有的服务条款。我们十分注重保护用户的个人信息及隐私安全，我们理解您通过网络向我们提供信息是建立在对我们信任的基础上，我们重视这种信任，也深知隐私对您的重要性，并尽最大努力全力保护您的隐私。详情请查看体安App《用户协议》和《隐私政策》声明的内容。";
        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        int startIndex = 0;
        int endIndex = 0;
        if (!TextUtils.isEmpty(content)) {
            //获取位置
            startIndex = content.indexOf("《");
            endIndex = content.lastIndexOf("》") - 6;
            spannable.setSpan(new TextClick(0), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            startIndex = content.indexOf("《") + 7;
            endIndex = content.lastIndexOf("》") + 1;
            spannable.setSpan(new TextClick(1), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        //这个一定要记得设置，不然点击不生效。
        tvPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        tvPrivacy.setText(spannable);

        privacyPolicyDialog = new PrivacyPolicyDialog(getActivity())
                .isCancelableOnTouchOutside(false)
                .MandatoryUpdate(false)
                .withCustomContentView(view)
                .withMessage("用户协议及隐私政策声明")
                .withCancelText("暂不使用", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        privacyPolicyDialog.dismiss();
                        getActivity().finish();
                        RxBus.getInstance().postEmpty(RxEvent.ON_APP_EXIT);
                    }
                }).withComfirmText("同意", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareUtils.putBoolean(getActivity(), "isAcceptPrivacy", true);
                        privacyPolicyDialog.dismiss();
                        ToastUtil.showToastLong("您已同意相关的用户协议及隐私政策声明");

                        PermissionUtil.getInstance().request(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                new PermissionOriginResultCallBack() {
                                    @Override
                                    public void onResult(List<PermissionInfo> acceptList, List<PermissionInfo> rationalList, List<PermissionInfo> deniedList) {
                                        if (!acceptList.isEmpty()) {
                                            for (PermissionInfo permissionInfo : acceptList) {
                                                if ("ACCESS_FINE_LOCATION".equals(permissionInfo.getShortName())) {

                                                }
                                            }
                                        }

                                        if (!rationalList.isEmpty()) {
//                            ToastUtils.showShort("权限获取失败");
                                        }
                                        if (!deniedList.isEmpty()) {
//                            ToastUtils.showShort("权限被拒绝");
                                        }
                                    }
                                });

                        UMConfigure.init(HealthApp.context
                                ,Constants.UmengValue.UMENG_SDK_APPKEY
                                ,Constants.UmengValue.CHANNEL_ANDROID
                                ,UMConfigure.DEVICE_TYPE_PHONE
                                ,null);
                    }
                });
        privacyPolicyDialog.show();

    }

    @Override
    public void QueryInfoSuccess(HealthyInfo info) {
        List<HealthyInfo.DataBean> healthList = new ArrayList<>();
        List<Integer> typeList = HealthCardManagerActivity.getSPList();
        if (typeList != null && !typeList.isEmpty()) {
            for (int index = 0; index < typeList.size(); index++) {
                healthList.add(info.data);
            }
        }

        adapter.refreshData(healthList, typeList);

        calorie_view.setValue(info.data.kcal);
        distance_view.setValue(info.data.distance);
        walk_view.setWalk(info.data.walk);
        distance.setText(String.valueOf((int) info.data.distance));
        calroie.setText(String.valueOf((int) info.data.kcal));
    }

    @Override
    public void QueryInfoFail(String msg) {
        restUiState();
    }

    @Override
    public void goBindApply(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void failBindApply(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void confirmSuccess(String msg) {

    }

    @Override
    public void confirmFail(String msg) {

    }

    @Override
    public void getLocSuccess(GPS msg) {
        location.setEnabled(true);
        location.setText(msg.data);
    }

    @Override
    public void getLocFail(String msg) {
        location.setEnabled(false);
        location.setText(msg);
    }

    private void ifExistEmergencyContact() {
        String token = SPCache.getString(SPCache.KEY_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().ifExistEmergencyContact(param),
                new CallBack<EmergencyContact>() {

                    @Override
                    public void onResponse(EmergencyContact data) {
                        List<HomeTypesModel> typesModelList = new ArrayList<>();
                        if (data.getCode() == 1) {
                            UserInfo.DataBean bean = DataSupport.findFirst(UserInfo.DataBean.class);
                            if (bean != null) {
                                if (bean.getRecordModifyStatus() == 0)
                                    typesModelList.add(new HomeTypesModel(R.mipmap.icon_health_btn, 1));
                            }
                            if(data.getData()==0){
                                typesModelList.add(new HomeTypesModel(R.mipmap.icon_sos_btn, 2));
                            }
                            if(data.getIfExistMember()==0){
                                typesModelList.add(new HomeTypesModel(R.mipmap.icon_family_member_btn, 3));
                            }
                        }
                        if (typesModelList.isEmpty()) {
                            mBanner.setVisibility(View.GONE);
                        } else {
                            mBanner.setVisibility(View.VISIBLE);
                            mBanner.setIndicator(new RectangleIndicator(getActivity()));
                            mBanner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(12));
                            mBanner.setIndicatorSpace((int) BannerUtils.dp2px(4));
                            mBanner.setIndicatorRadius(8);
                            mBanner.setAdapter(new HomeTypesAdapter(getActivity(), typesModelList))
                                    .addBannerLifecycleObserver(getActivity()) // 添加生命周期观察者
                                    .setOnBannerListener((data1, position) -> {
                                        HomeTypesModel model = (HomeTypesModel) data1;
                                        switch (model.type) {
                                            case 1://个人健康档案
                                                Utils.gotoActy(getActivity(), HealthBodyInfoActivity.class);
                                                break;
                                            case 2://sos紧急呼叫同步记录
                                                Utils.gotoActy(getActivity(), InsertSosContactsActivity.class);
                                                break;
                                            case 3://家庭成员共享监看数据
                                                Utils.gotoActy(getActivity(), MyFamilyInviteActivity.class);
                                                break;
                                        }
                                    });
                        }
                    }
                });

    }

    /**** 超链接文字点击事件*/
    private class TextClick extends ClickableSpan {
        int flag;

        public TextClick(int flag) {
            this.flag = flag;
        }

        @Override
        public void onClick(View view) {
            String title = flag == 0 ? "体安用户协议" : "隐私协议";
            getCompanyPrivacy(title);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.btn_border_txt_color));   //设置字体颜
            ds.setUnderlineText(false); //设置没有下划线
        }
    }

    public void reformSleep(String str, TextView view) {
        try {
            if (TextUtils.isEmpty(str)) {
                view.setText("--");
                return;
            }

            int size = DispUtil.sp2px(getActivity(), 18);
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            if (str.contains("小") && str.contains("分")) {
                //有小时和分钟
                builder.setSpan(new AbsoluteSizeSpan(size), 0, str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new AbsoluteSizeSpan(size), str.indexOf("时") + 1, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (str.contains("小")) {
                builder.setSpan(new AbsoluteSizeSpan(size), 0, str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                builder.setSpan(new AbsoluteSizeSpan(size), 0, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            view.setText(builder);
        } catch (Exception e) {
            e.printStackTrace();
            view.setText("--");
        }

    }

    public void updateBdage() {
        if (bage != null) {
            if (isUserLogin()) {
                getNoReadMessage();
            } else {
                bage.setVisibility(View.GONE);
            }
        }
    }

    private void getCompanyPrivacy(String title) {
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getPlatformAgreement(title)
                , new CallBack<CompanyPrivacy>() {
                    @Override
                    public void onResponse(CompanyPrivacy data) {
                        AboutHealthContentActivity.instance(getActivity(), title, data.data);
                    }
                });
    }


    View.OnClickListener onHealthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (!isUserLogin()) {
//                LoginActivity.instance(getActivity(),0);
//                return;
//            }
            Bundle bundle = new Bundle();
            int type = (int) v.getTag();
            switch (type) {
                case 1://心率
                    bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 0);
                    bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, false);
                    Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
//                    HealthDataActivity.instance(getActivity(),0,false);
                    break;
                case 2://血压
                    bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 1);
                    bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, false);
                    Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
//                    HealthDataActivity.instance(getActivity(),1,false);
                    break;
                case 3://睡眠
//                    SleepMonitoringActivity.inStance(getActivity());
                    Utils.gotoActy(getActivity(), SleepMonitoringActivity.class);
                    break;
                case 4://心电
                    bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 3);
                    bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, false);
                    Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
//                    HealthDataActivity.instance(getActivity(),3,false);
                    break;
                case 5://血糖
                    bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 4);
                    bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, false);
                    Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
//                    HealthDataActivity.instance(getActivity(),4,false);
                    break;
                case 6://血氧
                    bundle.putInt(IntentParam.HEALTH_TAB_POSITION, 2);
                    bundle.putBoolean(IntentParam.HEALTH_TAB_IS_ALL, false);
                    Utils.gotoActy(getActivity(), HealthDataActivity.class, bundle);
//                    HealthDataActivity.instance(getActivity(),2,false);
                    break;
                case 7:

                    break;
                case 8:

                    break;
            }
        }
    };



    //获取报告
    public void getReport(){
        Map<String,String> param = new HashMap<>();
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
                new CallBack<HealthyReport>() {
                    @Override
                    public void onResponse(HealthyReport data) {
                        if (data.code == 1){
                            HealthyReportResultActivity.inStance(getActivity(), data);
                        }else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });
    }
}







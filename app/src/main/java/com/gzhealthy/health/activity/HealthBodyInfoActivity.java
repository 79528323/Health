package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.PersonHealthInfo;
import com.gzhealthy.health.model.RegionBean;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.PickerViewUtils;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class HealthBodyInfoActivity extends BaseAct implements OptionsPickerView.OnOptionsSelectListener {

    private boolean mIsGender = true;
    private String mBirthday;
    private int mHeight;
    private int mWeight;
    private int mWaist;
    private String mMarriage;
    private String mProvince = "";
    private String mCity = "";
    private String mArea = "";

    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvHight)
    TextView tvHight;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvWaist)
    TextView tvWaist;
    @BindView(R.id.tvMarriage)
    TextView tvMarriage;
    @BindView(R.id.tvArea)
    TextView tvArea;


    public static void instance(Context context) {
        Intent intent = new Intent(context, HealthBodyInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_body_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("基本信息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        getInfo();
    }


    public void getInfo() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getAccountHealthRecord(param),
                new CallBack<PersonHealthInfo>() {

                    @Override
                    public void onResponse(PersonHealthInfo data) {
                        if (data.code == 1) {
                            if (data.data.province == null) {
                                data.data.province = "";
                            }
                            if (data.data.city == null) {
                                data.data.city = "";
                            }
                            if (data.data.county == null) {
                                data.data.county = "";
                            }
                            if (data.data.marriage == null) {
                                data.data.marriage = "";
                            }

                            mBirthday = data.data.birthday;
                            tvBirthday.setText(mBirthday);
                            tvAge.setText(data.data.age + "岁");

                            mHeight = Integer.parseInt(data.data.height);
                            if(mHeight>0){
                                tvHight.setText(mHeight + "cm");
                            }

                            mWaist = Integer.parseInt(data.data.waist);
                            if(mWaist>0){
                                tvWaist.setText(mWaist + "cm");
                            }

                            mWeight = Integer.parseInt(data.data.weight);
                            if(mWeight>0){
                                tvWeight.setText(mWeight + "kg");
                            }

                            mIsGender = data.data.sex == 1 ? true : false;
                            tvSex.setText(mIsGender ? "男" : "女");

                            mMarriage = data.data.marriage;
                            tvMarriage.setText(mMarriage);


                            mProvince = data.data.province;
                            mCity = data.data.city;
                            mArea = data.data.county;
                            if (mProvince != null && mCity != null && mProvince.equals(mCity)) {
                                tvArea.setText(mCity + mArea);
                            } else {
                                tvArea.setText(mProvince + mCity + mArea);
                            }

                        } else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }

    @OnClick({R.id.llSex, R.id.llBirthday, R.id.llHight, R.id.llWeight, R.id.llWaist, R.id.llMarriage, R.id.llArea})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llSex://性别
                selectSex();
                break;
            case R.id.llBirthday://生日
                selectBirthday();
                break;
            case R.id.llHight://身高
                selectHight();
                break;
            case R.id.llWeight://体重
                setlectWeight();
                break;
            case R.id.llWaist://腰围
                selectWaist();
                break;
            case R.id.llMarriage://婚姻状态
                selectMarriage();
                break;
            case R.id.llArea://所在地区
                selectArea();
                break;
            default:
                break;
        }
    }


    private OptionsPickerView opvSex;
    private List<String> sexs = Arrays.asList(
            "男",
            "女"
    );

    /**
     * 选择性别
     */
    private void selectSex() {
        if (opvSex == null) {
            opvSex = PickerViewUtils.OptionsPickerView(
                    this,
                    "性别",
                    (options1, options2, options3, v) -> {
                        String bad = sexs.get(options1);
                        if ("男".equals(bad)) {
                            mIsGender = true;
                        } else {
                            mIsGender = false;
                        }
                        bindInfoSubmit();
                    }
            );
            opvSex.setPicker(sexs);
        }
        KeyboardUtils.hideSoftInput(this);
        opvSex.show();
    }


    private TimePickerView tpvBirthday;

    /**
     * 选择生日
     */
    private void selectBirthday() {
        if (tpvBirthday == null) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.YEAR, 1900);
            startDate.set(Calendar.MONTH, 0);
            startDate.set(Calendar.DAY_OF_MONTH, 1);
            Calendar selectData = Calendar.getInstance();
            selectData.set(Calendar.YEAR, 1985);
            selectData.set(Calendar.MONTH, 0);
            selectData.set(Calendar.DAY_OF_MONTH, 1);
            tpvBirthday = PickerViewUtils.getTimePickerView(this, startDate, Calendar.getInstance(), selectData, "生日", (date, v) -> {
                mBirthday = TimeUtils.millis2String(date.getTime(), "yyyy-MM-dd");
                bindInfoSubmit();
            });
        }
        KeyboardUtils.hideSoftInput(this);
        tpvBirthday.show();
    }


    private OptionsPickerView opvHeight;
    private List<Integer> heights = new ArrayList<>();

    /**
     * 选择身高
     */
    private void selectHight() {
        if (opvHeight == null) {
            for (int i = 30; i < 251; i++) {
                heights.add(i);
            }
            opvHeight = PickerViewUtils.OptionsPickerView(
                    this,
                    "身高",
                    new String[]{"cm", "", ""},
                    (options1, options2, options3, v) -> {
                        mHeight = heights.get(options1);
                        bindInfoSubmit();
                    }
            );
            opvHeight.setPicker(heights);
            opvHeight.setSelectOptions(140);
        }
        KeyboardUtils.hideSoftInput(this);
        opvHeight.show();
    }

    private OptionsPickerView opvWeight;
    private List<Integer> weights = new ArrayList<>();

    /**
     * 选择体重
     */
    private void setlectWeight() {
        if (opvWeight == null) {
            for (int i = 1; i < 301; i++) {
                weights.add(i);
            }
            opvWeight = PickerViewUtils.OptionsPickerView(
                    this,
                    "体重",
                    new String[]{"kg", "", ""},
                    (options1, options2, options3, v) -> {
                        mWeight = weights.get(options1);
                        bindInfoSubmit();
                    }
            );
            opvWeight.setPicker(weights);
            opvWeight.setSelectOptions(54);
        }
        KeyboardUtils.hideSoftInput(this);
        opvWeight.show();
    }


    private OptionsPickerView opvWaist;
    private List<Integer> waists = new ArrayList<>();

    /**
     * 选择腰围
     */
    private void selectWaist() {
        if (opvWaist == null) {
            for (int i = 10; i < 201; i++) {
                waists.add(i);
            }
            opvWaist = PickerViewUtils.OptionsPickerView(
                    this,
                    "腰围",
                    new String[]{"cm", "", ""},
                    (options1, options2, options3, v) -> {
                        mWaist = waists.get(options1);
                        bindInfoSubmit();
                    }
            );
            opvWaist.setPicker(waists);
            opvWaist.setSelectOptions(70);
        }
        KeyboardUtils.hideSoftInput(this);
        opvWaist.show();
    }


    private OptionsPickerView opvMarriage;
    private List<String> marriages = Arrays.asList(
            "未婚",
            "已婚",
            "丧偶",
            "离婚"
    );

    /**
     * 选择婚姻状态
     */
    private void selectMarriage() {
        if (opvMarriage == null) {
            opvMarriage = PickerViewUtils.OptionsPickerView(
                    this,
                    "婚姻状态",
                    (options1, options2, options3, v) -> {
                        mMarriage = marriages.get(options1);
                        bindInfoSubmit();
                    }
            );
            opvMarriage.setPicker(marriages);
        }
        KeyboardUtils.hideSoftInput(this);
        opvMarriage.show();
    }

    private RegionBean regionBean;

    /**
     * 选择所在地区
     */
    private void selectArea() {
        Observable
                .create((Observable.OnSubscribe<RegionBean>) subscriber -> {
                    if (regionBean == null) {
                        try {
                            regionBean = PickerViewUtils.getRegionData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    subscriber.onNext(regionBean);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBack<RegionBean>() {
                    @Override
                    public void onResponse(RegionBean data) {
                        if (regionBean != null) {
                            KeyboardUtils.hideSoftInput(HealthBodyInfoActivity.this);
                            OptionsPickerView pickerView =
                                    PickerViewUtils.OptionsPickerView(
                                            HealthBodyInfoActivity.this,
                                            "省市区",
                                            HealthBodyInfoActivity.this
                                    );
                            pickerView.setPicker(
                                    regionBean.getProvince(),
                                    regionBean.getCity(),
                                    regionBean.getArea());
                            pickerView.show();
                        }
                    }
                });
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        if (regionBean != null) {
            mProvince = regionBean.getProvince().get(options1);
            mCity = regionBean.getCity().get(options1).get(options2);
            mArea = regionBean.getArea().get(options1).get(options2).get(options3);
        }
        bindInfoSubmit();
    }

    public void bindInfoSubmit() {
        Map<String, String> param = new HashMap<>();
        param.put("birthday", mBirthday);
        param.put("height", mHeight + "");
        param.put("sex", mIsGender ? "1" : "2");
        param.put("weight", mWeight + "");
        param.put("waist", mWaist + "");
        param.put("marriage", mMarriage);
        param.put("province", mProvince);
        param.put("city", mCity);
        param.put("county", mArea);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));
        String token = SPCache.getString(SPCache.KEY_TOKEN, "");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().bindOrEditPersonBaseInfo(body, token),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            getInfo();
                        }
                    }
                });
    }


}

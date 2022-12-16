package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.FenceScope;
import com.gzhealthy.health.model.FenceSetting;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 安全围栏范围
 */
public class SecurityFenceRangeActivity extends BaseAct {

//    @BindView(R.id.container_time)
//    LinearLayout container_time;
//
//    @BindView(R.id.lay_all_day)
//    LinearLayout lay_all_day;
//
//    @BindView(R.id.lay_custom)
//    LinearLayout lay_custom;
//
//    @BindView(R.id.lay_day)
//    LinearLayout lay_day;

    @BindView(R.id.tx_address)
    TextView tx_address;

    @BindView(R.id.btn_locate)
    TextView btn_locate;

    @BindView(R.id.tx_custom_range)
    TextView tx_custom_range;

    @BindView(R.id.radio_1)
    RadioButton radio_1;

    @BindView(R.id.radio_2)
    RadioButton radio_2;

    @BindView(R.id.radio_3)
    RadioButton radio_3;

    @BindView(R.id.radio_4)
    RadioButton radio_4;

    FenceSetting.DataBean bean;

    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;

    String radiusOPtions;

    int fenceType = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_security_fence_range;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("安全围栏范围");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getTvRight().setText("确定");
        getTvRight().setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getTvRight().setOnClickListener(v -> {
            getUpdateScope();
        });
        initView();
    }


    public void initView(){
        btn_locate.setOnClickListener(this::onViewClicked);
        tx_custom_range.setOnClickListener(this::onViewClicked);
        bean = (FenceSetting.DataBean) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.radio_1:
                    radiusOPtions = "500";
                    break;

                case R.id.radio_2:
                    radiusOPtions = "1000";
                    break;

                case R.id.radio_3:
                    radiusOPtions = "3000";
                    break;

                case R.id.radio_4:
                    radiusOPtions = "5000";
                    break;
            }
            fenceType= 1;
        });

        rxManager.onRxEvent(RxEvent.ON_POST_FENCE_RESULT)
                .subscribe(o -> {
                    String radius = (String) o;

                    tx_custom_range.setTag(R.id.tag_fencerange_radius_option,radius);
                    tx_custom_range.setText("已创建");
                    mRadioGroup.clearCheck();
                    mRadioGroup.check(-1);
                    radiusOPtions = radius;
                    fenceType= 2;
        });

        getScope();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        getScope();
//        MobclickAgent.onPageStart("我的-账号与安全");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("我的-账号与安全");
    }

    @OnClick({R.id.tx_custom_range, R.id.btn_locate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_custom_range:
                Bundle bundle = new Bundle();
                String location = (String) view.getTag(R.id.tag_fencerange_location);
                bundle.putString("location",location);
                Object obj = view.getTag(R.id.tag_fencerange_radius_option);
                if (null != obj){
                    String raduis = (String) obj;
                    bundle.putString("radius",raduis);
                }
                SafeFenceMapActivity.instance(this,bundle);
                break;

            case R.id.btn_locate:
//                getScope();
                mRadioGroup.clearCheck();
                mRadioGroup.check(-1);
                break;

            default:
                break;
        }
    }


    public void getScope(){
        Map<String, String> params = new HashMap<>();
        params.put("imei", bean.imei);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getSafetyFenceScope(params),
                new CallBack<FenceScope>() {
                    @Override
                    public void onResponse(FenceScope data) {
                        if (data.code == 1) {
                            tx_address.setText(data.data.address);

                            if (!TextUtils.isEmpty(data.data.location)) {
                                tx_custom_range.setTag(R.id.tag_fencerange_location, data.data.location);
                            }

                            //围栏有半径或者坐标时
                            fenceType = data.data.fenceType;
                            if (!TextUtils.isEmpty(data.data.radiusOrPoints)){
                                if (data.data.fenceType==1){
                                    int metro = Integer.parseInt(data.data.radiusOrPoints);
                                    switch (metro){
                                        case 500:
                                            mRadioGroup.check(R.id.radio_1);
                                            radiusOPtions = "500";
                                            break;

                                        case 1000:
                                            mRadioGroup.check(R.id.radio_2);
                                            radiusOPtions = "1000";
                                            break;

                                        case 3000:
                                            mRadioGroup.check(R.id.radio_3);
                                            radiusOPtions = "3000";
                                            break;

                                        case 5000:
                                            mRadioGroup.check(R.id.radio_4);
                                            break;
                                    }
                                }else {
                                    tx_custom_range.setText("已创建");
                                    tx_custom_range.setTag(R.id.tag_fencerange_radius_option,data.data.radiusOrPoints);
                                }
                            }

                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }



    public void getUpdateScope(){
        if (TextUtils.isEmpty(tx_address.getText().toString())){
            ToastUtil.showToast("请获取当前定位");
            return;
        }
        if (fenceType==0){
            ToastUtil.showToast("请选择半径范围");
            return;
        }


        Map<String, String> params = new HashMap<>();
        params.put("imei", bean.imei);
        params.put("id", String.valueOf(bean.id));
        params.put("fenceType", String.valueOf(fenceType));
        params.put("radiusOrPoints", radiusOPtions);
        String location = (String) tx_custom_range.getTag(R.id.tag_fencerange_location);
        params.put("location", location);
        params.put("address", tx_address.getText().toString());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().updateSafetyFenceScope(params),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1) {
                            goBack();
                        }

                        ToastUtil.showToast(data.msg);
                    }
                });
    }

    public static void instance(Context context, FenceSetting.DataBean setting) {
        Intent intent = new Intent(context, SecurityFenceRangeActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN,setting);
        context.startActivity(intent);
    }


}

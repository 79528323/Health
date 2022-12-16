package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginPwsSettingActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.FenceSetting;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WeChatAuthModel;
import com.gzhealthy.health.model.banner.DeviceDetail;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

/**
 * 安全围栏与设置
 */
public class SecurityFenceActivity extends BaseAct {

    @BindView(R.id.container)
    LinearLayout container;

    @BindView(R.id.fence_switch)
    TextView fence_switch;

    @BindView(R.id.tx_hour)
    TextView tx_hour;

    @BindView(R.id.tx_range)
    TextView tx_range;

    @BindView(R.id.tx_contact)
    TextView tx_contact;

    String imei="";

    int id = 0;

    FenceSetting.DataBean setting;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_security_fence;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("安全围栏与设置");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        initView();
    }


    public void initView(){
        imei=getIntent().getStringExtra(IntentParam.IMEI);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSetting(imei);
//        MobclickAgent.onPageStart("我的-账号与安全");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("我的-账号与安全");
    }

    @OnClick({R.id.fence_switch, R.id.tx_hour, R.id.tx_range,R.id.tx_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fence_switch:
                updateSafeFenceSwitch(view.isSelected());
                break;

            case R.id.tx_hour:
                SecurityFenceEntryIntoforceTimeActivity.instance(this,setting);
                break;

            case R.id.tx_range:
                SecurityFenceRangeActivity.instance(this,setting);
                break;

            case R.id.tx_contact:
                SecurityFenceContactsActivity.instance(this,setting);
                break;

            default:
                break;
        }
    }




    public void getSetting(String imei) {
        Map<String, String> params = new HashMap<>();
//        params.put("token", SPCache.getString("token", ""));
        params.put("imei", imei);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getSafetyFenceSetting(params),
                new CallBack<FenceSetting>() {
                    @Override
                    public void onResponse(FenceSetting data) {
                        if (data.code == 1) {
                            setting = data.data;
                            container.setVisibility(data.data.status==1?View.VISIBLE:View.GONE);
                            fence_switch.setSelected(data.data.status==1?true:false);
                            tx_hour.setText(data.data.fenceEffectTimeTypeStr);
                            tx_range.setText(data.data.fenceTypeStr);
                            List<FenceSetting.DataBean.RemindPeopleBean> remindPeopleBeanList = data.data.remindPeople;
                            StringBuffer buffer = new StringBuffer();
                            if (remindPeopleBeanList != null && !remindPeopleBeanList.isEmpty()){
                                for (int index=0; index < remindPeopleBeanList.size(); index++){
                                    FenceSetting.DataBean.RemindPeopleBean bean = data.data.remindPeople.get(index);
                                    if (bean.ifRemind < 1)
                                        continue;
                                    String name =bean.nickName;
                                    buffer.append(name).append(",");
                                }

                                if (buffer.length()>0)
                                    buffer.substring(0,buffer.length()-1);
                            }

                            tx_contact.setText(buffer.length() > 0 ?buffer.substring(0,buffer.length()-1) : "请选择");
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }



    public void updateSafeFenceSwitch(boolean isON){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("id", String.valueOf(setting.id));
        params.put("status",isON?"0":"1");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateSafetyFenceStatus(params),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1) {
                            fence_switch.setSelected(!fence_switch.isSelected());
                            container.setVisibility(fence_switch.isSelected()?View.VISIBLE:View.GONE);
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }


    public static void instance(Context context) {
        Intent intent = new Intent(context, SecurityFenceActivity.class);
        context.startActivity(intent);
    }

    public static void instance(Context context,String imei) {
        Intent intent = new Intent(context, SecurityFenceActivity.class);
        intent.putExtra(IntentParam.IMEI,imei);
        context.startActivity(intent);
    }
}

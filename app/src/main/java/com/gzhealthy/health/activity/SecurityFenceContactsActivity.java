package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.SecurityFenceContactsAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.FenceSetting;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 安全围栏与设置
 */
public class SecurityFenceContactsActivity extends BaseAct {

    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;

    SecurityFenceContactsAdapter adapter;

    FenceSetting.DataBean setting;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_security_fence_contacts;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("选择提醒的成员");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getTvRight().setText("确定");
        getTvRight().setTextColor(ContextCompat.getColor(this, R.color.global_333333));
        getTvRight().setOnClickListener(v -> {
            save();
        });
        initView();
    }


    public void initView(){
        setting = (FenceSetting.DataBean) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        adapter = new SecurityFenceContactsAdapter();
        rvContacts.setAdapter(adapter);
        getContact();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void getContact() {
        if (setting.remindPeople!=null && !setting.remindPeople.isEmpty()){
            List<FenceSetting.DataBean.RemindPeopleBean> list = new ArrayList<>();
            list.addAll(setting.remindPeople);
            adapter.setNewData(list);
        }else {
            adapter.setEmptyView(R.layout.activity_security_fence_contacts_not_data,rvContacts);
        }
    }


    private void save() {
        if (setting.remindPeople==null || setting.remindPeople.isEmpty()){
            goBack();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("id", setting.id);

        for (int index=0;  index < adapter.getIsChecked().size(); index++){
            Iterator<FenceSetting.DataBean.RemindPeopleBean> iterator = setting.remindPeople.iterator();
            while (iterator.hasNext()){
                FenceSetting.DataBean.RemindPeopleBean bean = iterator.next();
                bean.ifRemind = adapter.getIsChecked().get(index)?1:0;
            }
        }

        Object[] objects = setting.remindPeople.toArray(new Object[setting.remindPeople.size()]);
        param.put("remindPeople", objects);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));
        HttpUtils.invoke(this, this,InsuranceApiFactory.getmHomeApi().updateSafetyFenceRemindPeople(body),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            goBack();
                        }
                    }
                });
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, SecurityFenceContactsActivity.class);
        context.startActivity(intent);
    }

    public static void instance(Context context,FenceSetting.DataBean bean) {
        Intent intent = new Intent(context, SecurityFenceContactsActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN,bean);
        context.startActivity(intent);
    }
}

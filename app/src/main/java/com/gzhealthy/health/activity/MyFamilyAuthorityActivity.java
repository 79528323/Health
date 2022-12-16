package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FamilyMemberAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CircleImageView;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 家庭成员 邀请
 */
public class MyFamilyAuthorityActivity extends BaseAct {

    @BindView(R.id.rate_agree)
    CheckBox rate_agree;

    @BindView(R.id.walk_agree)
    CheckBox walk_agree;

    @BindView(R.id.pressure_agree)
    CheckBox pressure_agree;

    @BindView(R.id.spo2_agree)
    CheckBox spo2_agree;

    @BindView(R.id.sugar_agree)
    CheckBox sugar_agree;

    @BindView(R.id.temperature_agree)
    CheckBox temperature_agree;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.uid)
    TextView uid;

    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.user_icon)
    CircleImageView user_icon;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_family_authority;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("成员权限");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        initView();
    }


    public void initView(){
        save.setOnClickListener(this);
        FamilyMemberDetail bean = (FamilyMemberDetail)
                getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        if (bean!=null){
            uid.setText("ID："+bean.data.member);
            name.setText(bean.data.memberNickName);
            save.setTag(bean.data.id);
            GlideUtils.CircleImage(this,bean.data.memberAvatar,user_icon);
            walk_agree.setChecked(bean.data.walkIfSee == 0?true:false);
            rate_agree.setChecked(bean.data.rateIfSee == 0?true:false);
            pressure_agree.setChecked(bean.data.bloodPressureIfSee == 0?true:false);
            sugar_agree.setChecked(bean.data.bloodSugarIfSee == 0?true:false);
            spo2_agree.setChecked(bean.data.spo2IfSee == 0?true:false);
            temperature_agree.setChecked(bean.data.temperatureIfSee == 0?true:false);
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.save:
                if (view.getTag()!=null){
                    int id = (int) view.getTag();
                    updateAuthor(id);
                }
                break;
        }
    }


    public void updateAuthor(int id){
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("walkIfSee", walk_agree.isChecked()?0:1);
        param.put("rateIfSee", rate_agree.isChecked()?0:1);
        param.put("bloodPressureIfSee", pressure_agree.isChecked()?0:1);
        param.put("spo2IfSee", spo2_agree.isChecked()?0:1);
        param.put("bloodSugarIfSee", sugar_agree.isChecked()?0:1);
        param.put("temperatureIfSee", temperature_agree.isChecked()?0:1);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));

        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateAuthority(body),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1){
                            RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_MEMBER_DETAIL);
                            goBack();
                        }
                        ToastUtil.showToast(data.msg);
                    }
                });
    }

    public static void instance(Context context, FamilyMemberDetail detail){
        Intent intent = new Intent(context,MyFamilyAuthorityActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN,detail);
        context.startActivity(intent);
    }
}

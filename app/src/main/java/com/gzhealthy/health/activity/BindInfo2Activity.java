package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BindInfo2Adapter;
import com.gzhealthy.health.api.ApiInterface;
import com.gzhealthy.health.api.ApiService;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.BindInfoListModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 绑定信息2
 */
public class BindInfo2Activity extends BaseAct {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_other_view)
    LinearLayout ll_other_view;
    @BindView(R.id.ce_import)
    ClearableEditText ce_import;

    private String importContent;
    private boolean isImport;
    private BindInfo2Adapter mBindInfo2Adapter;
    private List<BindInfoListModel.DataBean> mList = new ArrayList<>();

    private List<String> addDataList = new ArrayList<>();

    public static String NAME = "name";
    public static String BIRTHDAY = "birthday";
    public static String BODYHEIGHTVALUE = "bodyHeightValue";
    public static String BODYWEIGHTVALUE = "bodyWeightValue";
    public static String IS_GENDER = "isGender";
    public static String JUMP_CHANNEL = "jumpChannel";

    private String name;
    private String birthday;
    private String bodyHeightValue;
    private String bodyWeightValue;
    private boolean isGender;
    private int jumpChannel = 0;

    /**
     * @param jumpChannel 跳转渠道
     */
    public static void instance(Context context, int jumpChannel) {
        Intent intent = new Intent(context, BindInfo2Activity.class);
        intent.putExtra(JUMP_CHANNEL, jumpChannel);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param name            姓名
     * @param birthday        生日
     * @param bodyHeightValue 身高
     * @param bodyWeightValue 体重
     * @param isGender        性别
     * @param jumpChannel     跳转渠道
     */
    public static void instance(Context context, String name, String birthday, String bodyHeightValue,
                                String bodyWeightValue, boolean isGender, int jumpChannel) {
        Intent intent = new Intent(context, BindInfo2Activity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(BIRTHDAY, birthday);
        intent.putExtra(BODYHEIGHTVALUE, bodyHeightValue);
        intent.putExtra(BODYWEIGHTVALUE, bodyWeightValue);
        intent.putExtra(IS_GENDER, isGender);
        intent.putExtra(JUMP_CHANNEL, jumpChannel);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind2_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("绑定信息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(BindInfo2Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        jumpChannel = getIntent().getIntExtra(JUMP_CHANNEL, 0);
        if (jumpChannel != 0) {
            name = getIntent().getStringExtra(NAME);
            birthday = getIntent().getStringExtra(BIRTHDAY);
            bodyHeightValue = getIntent().getStringExtra(BODYHEIGHTVALUE);
            bodyWeightValue = getIntent().getStringExtra(BODYWEIGHTVALUE);
            isGender = getIntent().getBooleanExtra(IS_GENDER, false);
        }

        setDataToUI();
    }

    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                importContent = ce_import.getText().toString().trim();
                if (!importContent.isEmpty() && isImport) {
                    addDataList.add(importContent);
                }
                L.i("添加的数据List：" + addDataList);
                if (null == addDataList || addDataList.isEmpty()) {
                    ToastUtil.showToast("您还未选中任何内容哦~~");
                    return;
                }

                bindInfoSubmit();
                break;
            default:
                break;
        }
    }

    private void setDataToUI() {

        List<String> nameList = new ArrayList<>();
        nameList.add("无病史");
        nameList.add("心脏病");
        nameList.add("糖尿病");
        nameList.add("高血压");
        nameList.add("冠心病");
        nameList.add("恶性肿瘤");
        nameList.add("慢性气管炎");
        nameList.add("肺气泡");
        nameList.add("其他");
        for (int i = 0; i < nameList.size(); i++) {
            BindInfoListModel.DataBean dataBean = new BindInfoListModel.DataBean();
            dataBean.setName(nameList.get(i));
            mList.add(dataBean);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mBindInfo2Adapter = new BindInfo2Adapter(this, mList);
        mRecyclerView.setAdapter(mBindInfo2Adapter);

        mBindInfo2Adapter.setItemClikListener(new BindInfo2Adapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                BindInfoListModel.DataBean dataBean = new BindInfoListModel.DataBean();
                dataBean.setName(mList.get(position).getName());
                if (mList.get(position).getName().equals("其他")) {
                    if (mList.get(position).isSelected()) {
                        dataBean.setSelected(false);
                        ll_other_view.setVisibility(View.GONE);
                        isImport = false;
                    } else {
                        dataBean.setSelected(true);
                        ll_other_view.setVisibility(View.VISIBLE);
                        isImport = true;
                    }
                } else {
                    if (mList.get(position).isSelected()) {
                        dataBean.setSelected(false);
                        addDataList.remove(mList.get(position).getName());
                    } else {
                        dataBean.setSelected(true);
                        addDataList.add(mList.get(position).getName());
                    }
                }
                mList.set(position, dataBean);
                mBindInfo2Adapter.notifyDataSetChanged();
                L.i("添加的数据List：" + addDataList);
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });

    }


    public void bindInfoSubmit(){
        String name = getIntent().getStringExtra(NAME);
        String birthday = getIntent().getStringExtra(BIRTHDAY);
        String bodyHeightValue = getIntent().getStringExtra(BODYHEIGHTVALUE);
        String bodyWeightValue = getIntent().getStringExtra(BODYWEIGHTVALUE);
        boolean isGender = getIntent().getBooleanExtra(IS_GENDER,false);

        StringBuffer buffer = new StringBuffer();
        for (int i=0; i < addDataList.size(); i++){
            buffer.append(addDataList.get(i)).append(",");
        }
        Map<String,String> param = new HashMap<>();
        param.put("birthday",birthday);
        param.put("chronicDiseaseHis",buffer.toString());
        param.put("height",bodyHeightValue);
        param.put("name",name);
        param.put("sex",isGender?"1":"2");
        param.put("weight",bodyWeightValue);
//        param.put("token", SPCache.getString("token",""));

//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        for (Map.Entry<String,String> entry:param.entrySet()){
//            builder.addFormDataPart(entry.getKey(),entry.getValue());
//        }


        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"),JSON.toJSONString(param));
        String token = SPCache.getString("token","");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().bindOrEditPersonBaseInfo(body,token),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1){
                            RxBus.getInstance().postEmpty(RxEvent.FINISH_BINDINFO_ACTIVITY);
                            finish();
                        }
                    }
                });
    }

}

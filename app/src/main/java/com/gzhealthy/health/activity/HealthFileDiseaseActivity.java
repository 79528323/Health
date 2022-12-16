package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BindInfo2Adapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 绑定信息2
 */
public class HealthFileDiseaseActivity extends BaseAct {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
//    @BindView(R.id.ll_other_view)
//    LinearLayout ll_other_view;
//    @BindView(R.id.ce_import)
//    ClearableEditText ce_import;

    private String importContent;
    private boolean isImport;
    private BindInfo2Adapter mBindInfo2Adapter;
    private List<BindInfoListModel.DataBean> mList = new ArrayList<>();

//    private List<String> addDataList = new ArrayList<>();


    public static void instance(Context context, String chronicdiseasehis) {
        Intent intent = new Intent(context, HealthFileDiseaseActivity.class);
        intent.putExtra(IntentParam.CHRONICDISEASEHIS, chronicdiseasehis);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_file_disease;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("修改疾病史");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(HealthFileDiseaseActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        setDataToUI();
    }

    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
//                importContent = ce_import.getText().toString().trim();
//                if (!importContent.isEmpty() && isImport) {
//                    addDataList.add(importContent);
//                }
//                L.i("添加的数据List：" + addDataList);
//                if (null == addDataList || addDataList.isEmpty()) {
//                    ToastUtil.showToast("您还未选中任何内容哦~~");
//                    return;
//                }

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
//        nameList.add("肺气泡");
        nameList.add("其他");
        for (int i = 0; i < nameList.size(); i++) {
            BindInfoListModel.DataBean dataBean = new BindInfoListModel.DataBean();
            String name = nameList.get(i);
            dataBean.setName(name);
            dataBean.setSelected(false);
            mList.add(dataBean);
        }


        String chronicdiseasehis = getIntent().getStringExtra(IntentParam.CHRONICDISEASEHIS);
        if (!TextUtils.isEmpty(chronicdiseasehis)){
            String[] chron = null;
            if (chronicdiseasehis.contains("、"))
                chron = chronicdiseasehis.split("、");
            else if (chronicdiseasehis.contains("，"))
                chron = chronicdiseasehis.split("，");
            else
                chron = chronicdiseasehis.split(",");
            for (int j =0; j < chron.length; j++){
                for (int k=0; k < mList.size(); k++){
                    String name = mList.get(k).getName();
                    if (TextUtils.equals(name,chron[j]))
                        mList.get(k).setSelected(true);
                }
            }
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBindInfo2Adapter = new BindInfo2Adapter(this, mList);
        mRecyclerView.setAdapter(mBindInfo2Adapter);

        mBindInfo2Adapter.setItemClikListener(new BindInfo2Adapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
//                BindInfoListModel.DataBean dataBean = new BindInfoListModel.DataBean();
//                BindInfoListModel.DataBean bean = mList.get(position);
//
                String name = mList.get(position).getName();
////                dataBean.setName(name);
//                if (TextUtils.equals(bean.getName(),"无病史")) {
//
//                } else {
//                    if (mList.size()==1 && mList.get(0).getName().equals("无病史"))
//                        mList.clear();
//
//
//                    if (mList.get(position).isSelected()) {
//                        dataBean.setSelected(false);
////                        addDataList.remove(mList.get(position).getName());
//                    } else {
//                        dataBean.setSelected(true);
////                        addDataList.add(mList.get(position).getName());
//                    }
//                }
//                mList.set(position, dataBean);

                if (name.equals("无病史")){
                    Iterator<BindInfoListModel.DataBean> iterator = mList.iterator();
                    while (iterator.hasNext()){
                        BindInfoListModel.DataBean next = iterator.next();
                        if (next.getName().equals("无病史")){
                            next.setSelected(!next.isSelected());
                        }else {
                            next.setSelected(false);
                        }
                    }
                }else {
                    mList.get(0).setSelected(false);
                    mList.get(position).setSelected(!mList.get(position).isSelected());
                }
                mBindInfo2Adapter.notifyDataSetChanged();
                L.i("添加的数据List：" + mList);
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });

    }


    public void bindInfoSubmit(){
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i < mList.size(); i++){
            if (mList.get(i).isSelected()) {
                buffer.append(mList.get(i).getName()).append("、");
            }
        }

        if (buffer.toString().isEmpty()){
            ToastUtil.showToast("请至少选择一项");
            return;
        }

        Map<String,String> param = new HashMap<>();
        param.put("chronicDiseaseHis",buffer.toString().substring(0,buffer.length()-1));
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
                            finish();
                        }
                    }
                });
    }

}

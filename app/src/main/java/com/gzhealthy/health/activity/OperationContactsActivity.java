package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.contract.PersonInfoContract;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.presenter.PersonInsertPresenter;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.edittext.ClearableEditText;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.limuyang2.customldialog.IOSMsgDialog;

public class OperationContactsActivity extends BaseAct implements PersonInfoContract.operationPersonView {
    private PersonInsertPresenter presenter;
    @BindView(R.id.name)
    ClearableEditText name;
    @BindView(R.id.phone)
    ClearableEditText phone;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.delete)
    TextView delete;
    private List<ContractModel.DataBean> list = new ArrayList<>();
    private ContractModel.DataBean bean;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_opertion_contacts;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);

//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("紧急联系人");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        bean = (ContractModel.DataBean) getIntent().getSerializableExtra("bean");
        if (bean!=null){
            name.setText(bean.emerContactName);
            phone.setText(bean.emerContactPhone);
            delete.setVisibility(View.VISIBLE);
            save.setText("保存提交");
            setTitle("编辑紧急联系人");
        }else {
            delete.setVisibility(View.GONE);
            save.setText("提交");
            setTitle("添加紧急联系人");
        }

        presenter = new PersonInsertPresenter(this,this,this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-紧急联系人");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-紧急联系人");
    }

    @OnClick({R.id.delete,R.id.save})
    public void onViewClicked(View view){
        Map<String,String> param = new HashMap<>();
        switch (view.getId()){
            case R.id.save:
                if (save.getText().equals("提交")){
                    param.put("emerContactName",name.getText().toString());
                    param.put("emerContactPhone",phone.getText().toString());
                    presenter.addEmergencyContact(param);
                }else {

                    param.put("emerContactName",name.getText().toString());
                    param.put("emerContactPhone",phone.getText().toString());
                    param.put("id",String.valueOf(bean.id));
                    presenter.editEmergencyContact(param);
                }

                break;

            case R.id.delete:
                IOSMsgDialog.Companion.init(getSupportFragmentManager())
                        .setTitle("温馨提示")
                        .setMessage("确定要删除该紧急联系人吗？")
                        .setNegativeButton("取消")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String id = null;
                                    if (bean!=null){
                                        id = String.valueOf(bean.id);
                                    }
                                    param.put("id",id);
                                    presenter.deleteEmergencyContact(param);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).show();
                break;
        }
    }

    @Override
    public void FailRespone(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void operSuccess() {
        setResult(InsertSosContactsActivity.REQ_CODE_EDITION);
        finish();
    }

    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }
}
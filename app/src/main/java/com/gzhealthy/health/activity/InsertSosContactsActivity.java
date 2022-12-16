package com.gzhealthy.health.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.ContractAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.contract.PersonInfoContract;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.presenter.PersonInsertPresenter;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.decoration.ContractItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class InsertSosContactsActivity extends BaseAct implements PersonInfoContract.insertPersonView {
    public static final int REQ_CODE_INSERT = 0x101;
    public static final int REQ_CODE_EDITION = 0x102;
//    public static final int REQ_CODE_INSERT = 0x101;
    private PersonInsertPresenter presenter;
    private ContractAdapter adapter;
    @BindView(R.id.contract_recylcer)
    RecyclerView contract_recylcer;
    @BindView(R.id.add_contract)
    LinearLayout add_contract;
    private List<ContractModel.DataBean> list = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_insert_sos_contacts;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back, v -> {
            goBack();
        });
        setTitle("紧急联系人");

//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
//        setstatueColor(R.color.white);
//        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("紧急联系人");
//        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
//        StatusBarUtil.StatusBarLightMode(this, true);
//        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
//        getCenterTextView().setTextSize(18);

        adapter = new ContractAdapter(this, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractModel.DataBean bean = (ContractModel.DataBean) v.getTag();
                startActivityForResult(new Intent(
                        InsertSosContactsActivity.this,OperationContactsActivity.class)
                        .putExtra("bean",bean),REQ_CODE_EDITION);
            }
        });
        contract_recylcer.setLayoutManager(new LinearLayoutManager(this));
        contract_recylcer.addItemDecoration(new ContractItemDecoration(this));
        contract_recylcer.setAdapter(adapter);

        presenter = new PersonInsertPresenter(this,this,this);
        presenter.getEmergencyContact();
    }


    @OnClick(R.id.add_contract)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_contract:
                startActivityForResult(
                        new Intent(this,OperationContactsActivity.class),REQ_CODE_INSERT);
                break;
        }
    }


    @Override
    public void getSuccess(List<ContractModel.DataBean> list) {
        adapter.refreshData(list);
        RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_SOS_HEALTHINFO_STATUS);
//        add_contract.setVisibility(list.size() > 2?View.GONE:View.VISIBLE);
    }

    @Override
    public void FailRespone(String msg) {
        ToastUtil.showToast(msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.getEmergencyContact();
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }
}
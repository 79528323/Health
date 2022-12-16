package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BlueToothListAdapter;
import com.gzhealthy.health.adapter.FamilyMemberInviteListAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.FamilyMemberInvite;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.TextViewUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class BluetoothListDialog extends Dialog implements View.OnClickListener, OnRefreshListener {
    Context context;

    RecyclerView mRecyclerView;

    ImageView close;

    TextView desc;

    BlueToothListAdapter adapter;

    SmartRefreshLayout smartRefreshLayout;

    OnSelectedBlueDeviceCallBack onSelectedBlueDeviceCallBack;

    BleScanRuleConfig config;

    public BluetoothListDialog(Context context,OnSelectedBlueDeviceCallBack onSelectedBlueDeviceCallBack) {
        super(context);
        this.context = context;
        this.onSelectedBlueDeviceCallBack = onSelectedBlueDeviceCallBack;
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bluetooth_list,null);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.addItemDecoration(new LineDividerItemDecoration(this.context));
        close = view.findViewById(R.id.close);
        close.setOnClickListener(this);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        desc = view.findViewById(R.id.desc);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.CENTER);
        getWindow().getAttributes().width = ScreenUtils.getScreenWidth()/10 * 8;
        getWindow().getAttributes().height = ScreenUtils.getScreenHeight() / 8 * 4;

        config = new BleScanRuleConfig.Builder()
                .setAutoConnect(true)//自动重连
                .setScanTimeOut(5 * 1000)//扫描超时
//                .setServiceUuids(new UUID[]{UUID.fromString(serviceUuids)}) // 只扫描指定的服务的设备，可选
                .setDeviceName(true, "体安") // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac) // 只扫描指定mac的设备，可选
                .build();


        adapter = new BlueToothListAdapter();
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BleDevice  bleDevice = (BleDevice) adapter.getData().get(position);
                if (onSelectedBlueDeviceCallBack!=null){
                    onSelectedBlueDeviceCallBack.onSelected(bleDevice);
                    dismiss();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void show() {
        desc.setText("已找到"+0+"个设备");
        super.show();
        adapter.getData().clear();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        BleManager.getInstance().cancelScan();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dismiss();
                break;
        }
    }



    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        getList();
    }

    public void getList(){
        BleManager.getInstance().initScanRule(config);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                desc.setText("已找到"+scanResultList.size()+"个设备");
                adapter.setNewData(scanResultList);
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onScanStarted(boolean success) {
                smartRefreshLayout.autoRefresh();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
//                hideWaitDialog();
            }
        });
    }


    public interface OnSelectedBlueDeviceCallBack{
        void onSelected(BleDevice bleDevice);
    }

    public void setOnSelectedBlueDeviceCallBack(OnSelectedBlueDeviceCallBack onSelectedBlueDeviceCallBack) {
        this.onSelectedBlueDeviceCallBack = onSelectedBlueDeviceCallBack;
    }
}

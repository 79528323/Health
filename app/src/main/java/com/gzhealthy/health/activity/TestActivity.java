package com.gzhealthy.health.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BlueToothListAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试用例
 */
public class TestActivity extends BaseAct {

//    private boolean islogin;

//    @BindView(R.id.ll_search)
//    LinearLayout ll_search;
//    @BindView(R.id.mBanner)
//    Banner mBanner;
//    @BindView(R.id.notice_view)
//    NoticeView notice_view;
//    @BindView(R.id.mTabLayout)
//    TabLayout mTabLayout;
//    @BindView(R.id.mViewPager)
//    ViewPager mViewPager;
    @BindView(R.id.img_gif)
    ImageView img_gif;

    List<String> mTitle;
    List<Fragment> mFragment;

    @BindView(R.id.blueTooth_recyler)
    RecyclerView blueTooth_recyler;
    BlueToothListAdapter adapter;

    @BindView(R.id.btAdd)
    Button btAdd;

    boolean isConnect = false;

    String serviceUuids = "7282991b-a122-4176-a0d3-f8b57af28bc7";
    BluetoothGattCharacteristic bluetoothGattCharacteristic = null;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
//        islogin = SPCache.getBoolean("islogin", false);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("测试用例");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(true);

//        getBannerData();
//        getNoticeViewData();
//        getTabData();

        GlideUtils.loadCustomeImgGif(img_gif,R.drawable.icon_gif_loading);
        btAdd.setOnClickListener(v -> {
            scanDevice();
        });
        adapter = new BlueToothListAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BleDevice bleDevice = ((BleDevice)adapter.getData().get(position));
                connect(bleDevice);
            }
        });
        blueTooth_recyler.setAdapter(adapter);
        initBlueTooth();
        openBlueTooth();

    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().destroy();
    }

    public void initBlueTooth(){
        BleManager.getInstance().init(HealthApp.getInstance1());
        BleManager.getInstance()
                .enableLog(BuildConfig.DEBUG?true:false)
                .setReConnectCount(1,5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
    }


    public void openBlueTooth(){
        BleManager.getInstance().enableBluetooth();
    }


    public void scanDevice(){
        BleScanRuleConfig config = new BleScanRuleConfig.Builder()
                .setAutoConnect(true)//自动重连
                .setScanTimeOut(10 * 1000)//扫描超时
//                .setServiceUuids(new UUID[]{UUID.fromString(serviceUuids)}) // 只扫描指定的服务的设备，可选
                .setDeviceName(true, "体安") // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac) // 只扫描指定mac的设备，可选
                .build();
        BleManager.getInstance().initScanRule(config);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                adapter.setNewData(scanResultList);
                hideWaitDialog();
            }

            @Override
            public void onScanStarted(boolean success) {
                showWaitDialog("扫描设备中...");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
//                hideWaitDialog();
            }
        });
    }


    public void connect(BleDevice bleDevice){
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                showWaitDialog("开始连接设备");
                Log.e("111","onStartConnect");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                hideWaitDialog();
                Log.e("111","onConnectFail  -----   "+exception.getDescription());
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                hideWaitDialog();
                ToastUtil.showToast("连接成功");
                Log.e("111","onConnectSuccess");
                String serviceStr , chartStr;
                List<BluetoothGattService> bluetoothGattServiceList = gatt.getServices();
                for (BluetoothGattService service : bluetoothGattServiceList){
                    UUID uuid_service = service.getUuid();
                    Log.e("111","uuid_service.toString() === "+uuid_service.toString());
                    serviceStr = uuid_service.toString();

                    List<BluetoothGattCharacteristic> bluetoothGattCharacteristicList = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : bluetoothGattCharacteristicList){
                        bluetoothGattCharacteristic = characteristic;
                        if (bluetoothGattCharacteristic!=null)
                            break;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString("service",bluetoothGattCharacteristic.getService().getUuid().toString());
                bundle.putString("Characteristic",bluetoothGattCharacteristic.getUuid().toString());
                bundle.putParcelable("device",bleDevice);
                DesinfectionBlueToothActivity.instance(TestActivity.this,bundle);
//                readCommon(bleDevice,
//                        bluetoothGattCharacteristic.getService().getUuid().toString(),
//                        bluetoothGattCharacteristic.getUuid().toString());
//                String strbyte = "AE0201FE";
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        writeCommon(bleDevice
//                                ,bluetoothGattCharacteristic.getService().getUuid().toString()
//                                ,bluetoothGattCharacteristic.getUuid().toString()
//                                ,strbyte.getBytes());
//                    }
//                },3000);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                hideWaitDialog();
                Log.e("111","onDisConnected");
            }
        });
    }


    public void readCommon(BleDevice bleDevice,String uuid_service, String uuid_write){
        BleManager.getInstance().read(bleDevice, uuid_service, uuid_write, new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                Log.e("111","onReadSuccess=="+HexUtil.formatHexString(data));
            }

            @Override
            public void onReadFailure(BleException exception) {
                Log.e("111","onReadFailure=="+exception.getDescription());
            }
        });
    }


    public void writeCommon(BleDevice bleDevice,String uuidService,String uuidWrite,byte[] data){
        BleManager.getInstance().write(bleDevice, uuidService, uuidWrite, data, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                Log.e("111","onWriteSuccess=="+HexUtil.formatHexString(data));
            }

            @Override
            public void onWriteFailure(BleException exception) {
                Log.e("111","onWriteFailure=="+exception.getDescription());
            }
        });


//        BleManager.getInstance().indicate(bleDevice, uuidService, uuidWrite, new BleIndicateCallback() {
//            @Override
//            public void onIndicateSuccess() {
//                Log.e("111","onIndicateSuccess");
//            }
//
//            @Override
//            public void onIndicateFailure(BleException exception) {
//                Log.e("111","onIndicateFailure="+exception.getDescription());
//            }
//
//            @Override
//            public void onCharacteristicChanged(byte[] data) {
//                Log.e("111","onCharacteristicChanged=="+HexUtil.formatHexString(data));
//            }
//        });
    }
}

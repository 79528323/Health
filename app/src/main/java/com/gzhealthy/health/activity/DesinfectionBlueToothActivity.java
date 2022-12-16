package com.gzhealthy.health.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
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
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.tool.BitmapUtil;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.BluetoothListDialog;
import com.gzhealthy.health.widget.BluetoothResultDialog;
import com.luck.picture.lib.tools.ScreenUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

/**
 * 蓝牙消毒机
 */
public class DesinfectionBlueToothActivity extends BaseAct {
    @BindView(R.id.btn_switch)
    TextView btn_switch;
    @BindView(R.id.btn_min_3)
    TextView btn_min_3;
    @BindView(R.id.btn_min_5)
    TextView btn_min_5;
    @BindView(R.id.btn_min_6)
    TextView btn_min_6;
    @BindView(R.id.btn_min_10)
    TextView btn_min_10;
    @BindView(R.id.btn_min_15)
    TextView btn_min_15;
    @BindView(R.id.tvdevice)
    TextView tvdevice;
    @BindView(R.id.btn_connect)
    Button btn_connect;
//    @BindView(R.id.btn_checked)
//    Button  btn_checked;
    @BindView(R.id.desinfection_starte_up)
    LinearLayout desinfection_starte_up;
    @BindView(R.id.desinfection_no_started)
    ConstraintLayout desinfection_no_started;
    @BindView(R.id.desinfection_standing)
    LinearLayout desinfection_standing;

    @BindView(R.id.tv_rate1)
    TextView tv_rate1;
    @BindView(R.id.tv_rate2)
    TextView tv_rate2;
    @BindView(R.id.tv_rate3)
    TextView tv_rate3;
    @BindView(R.id.tv_min)
    TextView tv_min;

    static final int TYPE_NO_START=0x101;

    static final int TYPE_START_UP=0x102;

    static final int TYPE_STANDING=0x103;

    BluetoothListDialog dialog;

    String service = "" , Characteristic = "";

    boolean isConnect = false;

//    boolean isStarted = false;

    boolean isReadDataLoop = false;//是否轮询读数据

    int PERIOD = 1500;//轮询间隔

    int DELAY = 0;//轮询启动延时

    Disposable disposable;

    BluetoothGattCharacteristic[] bluetoothGattCharacteristicsArry;

    BleDevice mBleDevice;

    BluetoothResultDialog bluetoothResultDialog;

    BluetoothMonitorReceiver monitorReceiver;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_desinfection_bluetooth;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setSwipeBackEnable(false);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("消毒机");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = ScreenUtils.dip2px(this,227f);//metrics.widthPixels;
        int screenHeight= ScreenUtils.dip2px(this,415f);//metrics.heightPixels;

        bluetoothResultDialog = new BluetoothResultDialog(this, selected -> {
            String filePath = BitmapUtil.viewSaveToImage(
                    BitmapUtil.layoutView(bluetoothResultDialog.getShareView(),screenWidth,screenHeight));
            if (selected==1){
                WechatPlatform.shareImageSession(
                        DesinfectionBlueToothActivity.this,"", BitmapFactory.decodeFile(filePath));
            }else {
                WechatPlatform.shareImageMoments(
                        DesinfectionBlueToothActivity.this,"", BitmapFactory.decodeFile(filePath));
            }
        });
        dialog = new BluetoothListDialog(this,bleDevice ->{
            connect(bleDevice.getMac());
        });
        initBlueTooth();
        btn_switch.setOnClickListener(this::onClick);
        btn_min_3.setOnClickListener(this::onClick);
        btn_min_5.setOnClickListener(this::onClick);
        btn_min_6.setOnClickListener(this::onClick);
        btn_min_10.setOnClickListener(this::onClick);
        btn_min_15.setOnClickListener(this::onClick);
        btn_connect.setOnClickListener(v -> {
            if (!isConnect)
                openBlueTooth();
        });

        getReConnect();

        rxManager.onRxEvent(RxEvent.ON_BLUETOOTH_BREAK_READ_DATA_LOOP)
                .subscribe(o -> {
            isReadDataLoop =false;
            readDataLoop();
        });
    }

    public static void instance(Context context,Bundle bundle) {
        Intent intent = new Intent(context, DesinfectionBlueToothActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (!BleManager.getInstance().isConnected(mBleDevice)){
//
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null){
            disposable.dispose();
            disposable = null;
            isReadDataLoop = false;
        }
        BleManager.getInstance().removeNotifyCallback(mBleDevice,Characteristic);
        BleManager.getInstance().removeConnectGattCallback(mBleDevice);
        BleManager.getInstance().removeWriteCallback(mBleDevice,Characteristic);
        unregisterReceiver(monitorReceiver);
//        BleManager.getInstance().destroy();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_custom_toolbar_left){
            if (isConnect){
                goBackDailog();
            }else
                super.onClick(view);

            return;
        }

        if (!isConnect) {
            ToastUtil.showToast("请连接设备");
            return;
        }

        String common = "";
        switch (view.getId()){
            case R.id.btn_switch:
                //开关操作
                if (!btn_switch.isSelected())
                    return;
                common = "AE0200FE";
                isReadDataLoop = false;
                changeDataViewType(TYPE_STANDING);

                break;

            case R.id.btn_min_3:
                common = "AE0201FE";
                isReadDataLoop = true;
                break;
            case R.id.btn_min_5:
                common = "AE0202FE";
                isReadDataLoop = true;
                break;
            case R.id.btn_min_6:
                common = "AE0203FE";
                isReadDataLoop = true;
                break;
            case R.id.btn_min_10:
                common = "AE0204FE";
                isReadDataLoop = true;
                break;
            case R.id.btn_min_15:
                common = "AE0205FE";
                isReadDataLoop = true;
                break;
        }

        DELAY = 2000;
        readDataLoop();
        writeCommon(mBleDevice,service,Characteristic,common.getBytes());
    }


    public void getReConnect(){
        //获取在连设备并重连
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList!=null && !bleDeviceList.isEmpty()){
            if (BleManager.getInstance().isConnected(bleDeviceList.get(0))){
                mBleDevice = bleDeviceList.get(0);

                if (!BleManager.getInstance().isBlueEnable()){
                    Log.e("111","isBlueEnable true");
                    BleManager.getInstance().enableBluetooth();
                }else {
                    connect(mBleDevice.getMac());
                }
            }
        }
    }

    public void initBlueTooth(){
        BleManager.getInstance().init(HealthApp.getInstance1());
        BleManager.getInstance()
                .enableLog(BuildConfig.DEBUG?true:false)
                .setReConnectCount(1,5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(6000);

        monitorReceiver = new BluetoothMonitorReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(monitorReceiver,filter);
    }

    public void openBlueTooth(){
        if (!BleManager.getInstance().isBlueEnable()){
            Log.e("111","isBlueEnable true");
            BleManager.getInstance().enableBluetooth();
        }else{
            dialog.show();
        }
    }


    public void writeCommon(BleDevice bleDevice,String uuidService,String uuidWrite,byte[] data) {
        BleManager.getInstance().write(bleDevice, uuidService, uuidWrite, data, bleWriteCallback);
    }


    public void readCommon(BleDevice bleDevice,String uuid_service, String uuid_write,byte[] data){
        BleManager.getInstance().write(bleDevice, uuid_service, uuid_write,data, bleWriteCallback);
    }


    public void connect(String mac){
        BleManager.getInstance().connect(mac, bleGattCallback);
    }


    public void setBlueToothNotification(BleDevice bleDevice,String uuidService,String uuidWrite){
        BleManager.getInstance().notify(bleDevice, uuidService, uuidWrite, bleNotifyCallback);
    }

    public void readDataLoop(){
        if (isReadDataLoop){
            if (disposable==null){
                disposable = Observable.interval(DELAY,PERIOD, TimeUnit.MILLISECONDS)
                        .map(aLong -> aLong+1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            if (isReadDataLoop) {
                                String common = "AE01FE";
                                readCommon(mBleDevice, bluetoothGattCharacteristicsArry[0].getService().getUuid().toString()
                                        , bluetoothGattCharacteristicsArry[0].getUuid().toString(),common.getBytes());
                            }
                        });
            }

        }else {
            if (disposable!=null){
                disposable.dispose();
                disposable = null;
            }
        }
    }

    BleGattCallback bleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            showWaitDialog("开始连接设备");
            Log.e("111","onStartConnect");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            hideWaitDialog();
            Log.e("111","onConnectFail  -----   "+exception.getDescription());
            String exp = exception.getDescription();
            if (TextUtils.equals("Timeout",exp)){
                ToastUtil.showToast("连接超时");
            }else {
                ToastUtil.showToast("连接失败");
            }

        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            hideWaitDialog();
            Log.e("111","onConnectSuccess");
            List<BluetoothGattService> bluetoothGattServiceList = gatt.getServices();
            for (BluetoothGattService service : bluetoothGattServiceList){
                List<BluetoothGattCharacteristic> bluetoothGattCharacteristicList = service.getCharacteristics();
                bluetoothGattCharacteristicsArry = new BluetoothGattCharacteristic[bluetoothGattCharacteristicList.size()];
                for (int index=0; index < bluetoothGattCharacteristicList.size(); index++){
                    BluetoothGattCharacteristic characteristic = bluetoothGattCharacteristicList.get(index);
                    bluetoothGattCharacteristicsArry[index] = characteristic;
                }
            }

            service = bluetoothGattCharacteristicsArry[0].getService().getUuid().toString();
            Characteristic = bluetoothGattCharacteristicsArry[0].getUuid().toString();
            mBleDevice = bleDevice;

            isConnect = true;
            changeDataViewType(TYPE_STANDING);
            setBlueToothNotification(mBleDevice,bluetoothGattCharacteristicsArry[1].getService().getUuid().toString()
                    ,bluetoothGattCharacteristicsArry[1].getUuid().toString());
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            hideWaitDialog();
            Log.e("111","onDisConnected");
            if (isActiveDisConnected) {
                ToastUtil.showToast("设备已断开");
                service = Characteristic = "";
                mBleDevice = null;
                isConnect =false;
                changeDataViewType(TYPE_NO_START);
            }
        }
    };

    StringBuilder builder = new StringBuilder();
    BleNotifyCallback bleNotifyCallback =  new BleNotifyCallback() {

        @Override
        public void onNotifySuccess() {
            Log.e("111","onNotifySuccess");
            //连接成功后查询一次当前设备工作状态
            writeCommon(mBleDevice,service,Characteristic,"AE01FE".getBytes());
        }

        @Override
        public void onNotifyFailure(BleException exception) {
            Log.e("111","onNotifyFailure==="+exception.getDescription());
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            String result = new String(data, Charset.forName("UTF-8"));
            if (!TextUtils.isEmpty(result)){
                if (result.contains("A") && result.contains("F")){
                    //完整数据
                    builder.setLength(0);
                    builder.append(result);

                    coverInstruct(builder.toString());
                }else if (result.contains("A")){
                    builder.setLength(0);
                    builder.append(result);
                }else {
                    builder.append(result);
                    coverInstruct(builder.toString());
                }
            }
        }
    };


    BleWriteCallback bleWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            String s = new String(justWrite);
            btn_switch.setSelected(TextUtils.equals(s,"AE0200FE")?false:true);
        }

        @Override
        public void onWriteFailure(BleException exception) {
            Log.e("111", "onWriteFailure==" + exception.getDescription());
            String description = exception.getDescription();
            if (description.contains("not connect")){
                //设备未连接 重围初始状态
                if (disposable!=null){
                    disposable.dispose();
                    disposable = null;
                }
                BleManager.getInstance().removeNotifyCallback(mBleDevice,Characteristic);
                BleManager.getInstance().removeConnectGattCallback(mBleDevice);
                BleManager.getInstance().removeWriteCallback(mBleDevice,Characteristic);
                isConnect = false;
                isReadDataLoop = false;
                btn_switch.setSelected(false);
                btn_min_3.setSelected(false);
                btn_min_5.setSelected(false);
                btn_min_6.setSelected(false);
                btn_min_10.setSelected(false);
                btn_min_15.setSelected(false);
                changeDataViewType(TYPE_NO_START);
            }
        }
    };


    public void coverInstruct(String instruct){
        instruct = instruct.substring(0,instruct.lastIndexOf("FE")+2);
        Log.e("111","coverInstruct="+instruct);
        if (instruct.length() > 8){//TODO 除茵数据
            //获取指示灯工作状态
//            isStarted = true;
//            btn_switch.setSelected(true);
            String isWorkState = instruct.substring(4,6);
            String switchLight = instruct.substring(6,8);
            if (TextUtils.equals(isWorkState,"01")){
                //工作中退出后返回来时发送查询状态，如正在工作中设置轮询判断为true
                isReadDataLoop = true;
                if (disposable==null){
                    DELAY = 0;
                    readDataLoop();
                }
            }else {
                //非工作时

            }
            String lightStatus = instruct.substring(6,18);
            changeLinghtStatus(lightStatus);
            setDataView(switchLight.equals("01"),instruct.substring(18,26));
        }else {
            //TODO 工作模式指令
            setWorkModel(instruct);
        }
    }

    public void setWorkModel(String s){
        btn_switch.setSelected(false);
        btn_min_3.setSelected(false);
        btn_min_5.setSelected(false);
        btn_min_6.setSelected(false);
        btn_min_10.setSelected(false);
        btn_min_15.setSelected(false);

        String model = s.substring(4,s.length()-2);
        if (TextUtils.equals(model,"00")){
//            btn_switch.setSelected(false);
            changeDataViewType(TYPE_STANDING);
        }else {
            switch (model){
                case "01":
                    btn_min_3.setSelected(true);
                    break;
                case "02":
                    btn_min_5.setSelected(true);
                    break;
                case "03":
                    btn_min_6.setSelected(true);
                    break;
                case "04":
                    btn_min_10.setSelected(true);
                    break;
                case "05":
                    btn_min_15.setSelected(true);
                    break;
            }

            changeDataViewType(TYPE_START_UP);
        }

    }


    public void changeLinghtStatus(String s){
        List<String> arrayList= new ArrayList<>();
        for (int i=0; i < s.length(); i+=2){
            arrayList.add(s.substring(i,i+2));
        }

        for (int j=0; j < arrayList.size(); j++){
            String status = arrayList.get(j);
            switch (j){
                case 0:
                    btn_switch.setSelected(TextUtils.equals("01",status)?true:false);
//                    btn_switch.setText(btn_switch.isSelected()?"停止":"开关");
                    break;
                case 1:
                    btn_min_3.setSelected(TextUtils.equals("01",status)?true:false);
                    break;
                case 2:
                    btn_min_5.setSelected(TextUtils.equals("01",status)?true:false);
                    break;
                case 3:
                    btn_min_6.setSelected(TextUtils.equals("01",status)?true:false);
                    break;
                case 4:
                    btn_min_10.setSelected(TextUtils.equals("01",status)?true:false);
                    break;
                case 5:
                    btn_min_15.setSelected(TextUtils.equals("01",status)?true:false);
                    break;

            }
        }
    }


    /**
     * 设置消毒机运行界面
     */
    public void setDataView(boolean isSwitchLightOn ,String s){
        changeDataViewType(TYPE_START_UP);
        String s1 = s.substring(0,2);
        tv_rate1.setText(s1+"%");
        String s2 = s.substring(2,4);
        tv_rate2.setText(s2+"%");
        String s3 = s.substring(4,6);
        tv_rate3.setText(s3+"%");
        int min = Integer.parseInt(s.substring(6,8));
        if (min <=0){
            //为0是已完成
//            isStarted=false;
            isReadDataLoop = false;
//            btn_switch.setSelected(isReadDataLoop);
            readDataLoop();
            bluetoothResultDialog.setS1(s1);
            bluetoothResultDialog.setS2(s2);
            bluetoothResultDialog.setS3(s3);
            tv_min.setText("0");
            String m = "";
            int type = 0;
            if (btn_min_3.isSelected()){
                m = "3";
                type= 1;
            }else if (btn_min_5.isSelected()){
                m = "5";
                type= 2;
            }else if (btn_min_6.isSelected()){
                m = "6";
                type= 3;
            }else if (btn_min_10.isSelected()){
                m = "10";
                type= 4;
            }else if (btn_min_15.isSelected()){
                m = "15";
                type= 5;
            }
            bluetoothResultDialog.setMin(m);
            bluetoothResultDialog.setType(type);
            if (isSwitchLightOn)
                bluetoothResultDialog.show();


            changeDataViewType(isSwitchLightOn?TYPE_START_UP:TYPE_STANDING);
//            btn_switch.setText("开关");
//            btn_switch.setSelected(false);
//            btn_min_3.setSelected(false);
//            btn_min_5.setSelected(false);
//            btn_min_6.setSelected(false);
//            btn_min_10.setSelected(false);
//            btn_min_15.setSelected(false);
        }else {
            tv_min.setText(min+"");
        }
    }


    public void changeDataViewType(int type){
        switch (type){
            case TYPE_NO_START:
                desinfection_starte_up.setVisibility(View.GONE);
                desinfection_no_started.setVisibility(View.VISIBLE);
                desinfection_standing.setVisibility(View.GONE);

                tv_min.setText("0");
                tv_rate1.setText("0%");
                tv_rate2.setText("0%");
                tv_rate3.setText("0%");
                break;
            case TYPE_START_UP:
                desinfection_starte_up.setVisibility(View.VISIBLE);
                desinfection_no_started.setVisibility(View.GONE);
                desinfection_standing.setVisibility(View.GONE);

                break;
            case TYPE_STANDING:
                desinfection_starte_up.setVisibility(View.GONE);
                desinfection_no_started.setVisibility(View.GONE);
                desinfection_standing.setVisibility(View.VISIBLE);

                tv_min.setText("0");
                tv_rate1.setText("0%");
                tv_rate2.setText("0%");
                tv_rate3.setText("0%");
                break;
        }

//        tv_min.setText("0");
//        tv_rate1.setText("0%");
//        tv_rate2.setText("0%");
//        tv_rate3.setText("0%");
    }


    public void goBackDailog(){
        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_unbind_watch)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        ((TextView)viewHolder.getView(R.id.title)).setText("返回可能导致消毒机蓝牙数据\n丢失，您确定要返回吗？");
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            SPCache.putString(SPCache.KEY_BLUETOOTH_MAC,mBleDevice.getMac());
                            goBack();
                            baseLDialog.dismiss();
                        });
                    }
                })
                .show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isConnect){
                goBackDailog();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public class BluetoothMonitorReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)){
                if (TextUtils.equals(action,BluetoothAdapter.ACTION_STATE_CHANGED)){
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_ON:
                            getReConnect();
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            if (disposable!=null){
                                disposable.dispose();
                                disposable = null;
                            }
                            BleManager.getInstance().removeNotifyCallback(mBleDevice,Characteristic);
                            BleManager.getInstance().removeConnectGattCallback(mBleDevice);
                            BleManager.getInstance().removeWriteCallback(mBleDevice,Characteristic);
                            isConnect = false;
                            isReadDataLoop = false;
                            btn_switch.setSelected(false);
                            btn_min_3.setSelected(false);
                            btn_min_5.setSelected(false);
                            btn_min_6.setSelected(false);
                            btn_min_10.setSelected(false);
                            btn_min_15.setSelected(false);
                            changeDataViewType(TYPE_NO_START);
                            ToastUtil.showToast("蓝牙已断开");
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                    }
                }
            }
        }
    }
}

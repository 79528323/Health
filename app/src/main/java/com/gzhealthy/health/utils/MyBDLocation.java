//package com.gzhealthy.health.utils;
//
//import android.content.Context;
//
////import com.baidu.location.BDLocation;
////import com.baidu.location.BDLocationListener;
////import com.baidu.location.LocationClient;
////import com.baidu.location.LocationClientOption;
//
//public class MyBDLocation implements BDLocationListener {
//
//    public double myBDlatitude;
//    public double myBDlongitude;
//
//    private static MyBDLocation INSTANCE = new MyBDLocation();
//
//    // 关于定位的参数
//    LocationClient mLocClient;
//
//    //保证只有一个BDLocation实例
//    private MyBDLocation() {
//    }
//
//    //单例模式 获取BDLocation对象
//    public static MyBDLocation getInstance() {
//        return INSTANCE;
//    }
//
//    public void init(Context context) {
//        // 定位初始化
//        mLocClient = new LocationClient(context);
//        mLocClient.registerLocationListener(MyBDLocation.getInstance());
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
//    }
//
//    @Override
//    public void onReceiveLocation(BDLocation bdLocation) {
//
//        myBDlatitude = bdLocation.getLatitude();
//        myBDlongitude = bdLocation.getLongitude();
//
//    }
//}

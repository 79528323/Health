package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 高德地图
 * Created by lbd
 * 2021-06-25
 */
public class SafeFenceMapActivity extends BaseAct implements AMap.OnMapClickListener{

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.fence_reset)
    TextView fence_reset;

    @BindView(R.id.fence_tips)
    TextView fence_tips;

    AMap aMap;
    MarkerOptions markerOption;
    SupportMapFragment aMapFragment;
    GeoFenceClient mGeoFenceClient = null;
    List<DPoint> points = new ArrayList<DPoint>();
    Polygon polygon;
    LatLng myLaLng = null;
    boolean isGenerate = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(1f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("自定义围栏范围");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        setBarRightText("保存", Color.parseColor("#00C9B4"));
        getTvRight().setOnClickListener(v -> {
            if (!isGenerate){
                ToastUtil.showToast("请完整创建安全围栏");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            for (int index=0; index < points.size(); index++){
                DPoint dPoint = points.get(index);
                String pointLocation = dPoint.getLongitude() +","+ dPoint.getLatitude();
                buffer.append(pointLocation);
                if (index+1 < points.size()){
                    buffer.append(";");
                }
            }
            RxBus.getInstance().post(RxEvent.ON_POST_FENCE_RESULT,buffer.toString());
            goBack();
        });
        fence_tips.setVisibility(View.VISIBLE);
        fence_reset.setVisibility(View.VISIBLE);
        fence_reset.setOnClickListener(v -> {
            isGenerate =false;
            points.clear();
            aMap.clear();
            addMyMarkersToMap(myLaLng);
        });
        mapView.onCreate(savedInstanceState);
        initMap();
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, SafeFenceMapActivity.class);
        context.startActivity(intent);
    }


    public static void instance(Context context,Bundle bundle) {
        Intent intent = new Intent(context, SafeFenceMapActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    private void initMap() {
        if (aMap == null){
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
//            getLocation();
        }

        mGeoFenceClient = new GeoFenceClient(getApplicationContext());
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_OUT);
        mGeoFenceClient.setGeoFenceListener(fenceListenter);//设置回调监听

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String[] location = bundle.getString("location","").split(",");
//            String lat = data.location.substring(0,data.location.indexOf(","));
//            String lng = data.location.substring(data.location.indexOf(",")+1);
            double dlat = Double.parseDouble(location[0]);
            double dlng = Double.parseDouble(location[1]);
            myLaLng = new LatLng(dlng,dlat);
            addMyMarkersToMap(myLaLng);

            //围栏坐标
            String radius = bundle.getString("radius","");
            if (!TextUtils.isEmpty(radius)){
                String[] array = radius.split(";");
                for (String item:array){
                    String[] radiusArray = item.split(",");
                    double lat = Double.parseDouble(radiusArray[1]);
                    double lng = Double.parseDouble(radiusArray[0]);
                    LatLng latLng = new LatLng(lat,lng);
                    addMarkersToMap(latLng);
                    points.add(new DPoint(latLng.latitude,latLng.longitude));
                }
                if (points.size()>1){
                    createPolygon();
                }

            }
        }
    }


    /**
     * 在地图上添加marker
     */
    private void addMyMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng)
                .draggable(false);
        aMap.addMarker(markerOption);
        //移动窗口并放大
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,0,0)));
    }


    private void addMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(latLng)
                .draggable(false);
        aMap.addMarker(markerOption);
        //移动窗口并放大
//        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,0,0)));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (points.size() >= 4){
            ToastUtil.showToast("最多4个点");
            return;
        }
        addMarkersToMap(latLng);
        points.add(new DPoint(latLng.latitude,latLng.longitude));
        if (points.size()>=4){
            //创建围栏
            createPolygon();
        }

    }


    //创建回调监听
    public GeoFenceListener fenceListenter = new GeoFenceListener() {

        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList,
                                             int errorCode,String s) {
            if(errorCode == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
                ToastUtil.showToast("成功生成安全围栏");
                //geoFenceList是已经添加的围栏列表，可据此查看创建的围栏
                isGenerate = true;
            } else {
                ToastUtil.showToast("生成安全围栏失败");
            }
        }
    };



    public void getLocation(){
        Map<String,String> param = new HashMap<>();
        param.put(SPCache.KEY_TOKEN,SPCache.getString(SPCache.KEY_TOKEN,""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getLatestGPS(param),
                new CallBack<GPS>() {
                    @Override
                    public void onResponse(GPS data) {
                        LatLng latLng = null;
                        if (data.code == 1){
                            String lat = data.location.substring(0,data.location.indexOf(","));
                            String lng = data.location.substring(data.location.indexOf(",")+1);
                            double dlat = Double.parseDouble(lat);
                            double dlng = Double.parseDouble(lng);
                            myLaLng = new LatLng(dlng,dlat);
                        }else{
                            myLaLng = new LatLng(39.761, 116.434);
                        }

                        addMyMarkersToMap(myLaLng);
                    }
                });
    }


    public void createPolygon(){
        mGeoFenceClient.addGeoFence(points,"创建围栏");

        PolygonOptions pOption = new PolygonOptions();
        for (int index=0; index < points.size(); index++){
            DPoint dPoint = points.get(index);
            pOption.add(new LatLng(dPoint.getLatitude(),dPoint.getLongitude()));
        }
        polygon = aMap.addPolygon(pOption.strokeWidth(2)
                .strokeColor(Color.argb(100, 255, 82, 82))
                .fillColor(Color.argb(50, 255, 180, 180)));

    }
}

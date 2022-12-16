package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 高德地图
 * Created by lbd
 * 2021-06-25
 */
public class MapActivity extends BaseAct {

    @BindView(R.id.map)
    MapView mapView;

    private AMap aMap;

    private MarkerOptions markerOption;
    private SupportMapFragment aMapFragment;
//    private LatLng latlng;// = new LatLng(39.761, 116.434);
    GeoFenceClient geoFenceClient = null;

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
        setTitle("定位");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        mapView.onCreate(savedInstanceState);
        initMap();
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
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
            getLocation();
        }
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(false);
        aMap.addMarker(markerOption);
        //移动窗口并放大
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latlng,18,0,0)));
    }


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
                            latLng = new LatLng(dlng,dlat);
                        }else{
                            latLng = new LatLng(39.761, 116.434);
                        }

                        addMarkersToMap(latLng);
                    }
                });
    }
}

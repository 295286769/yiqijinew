package com.yiqiji.money.modules.common.request;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yiqiji.money.modules.common.plication.MyApplicaction;

/**
 * Created by ${huangweishui} on 2017/3/19.
 * address huang.weishui@71dai.com
 */
public class LocationServer {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Object objLock = new Object();

    public void onCreate() {
        mLocationClient = new LocationClient(MyApplicaction.getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
    }

    public LocationServer(Context locationContext) {
        synchronized (objLock) {
            if (mLocationClient == null) {
                onCreate();
                initLocation();
            }
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public void start() {
        synchronized (objLock) {
            if (mLocationClient != null && !mLocationClient.isStarted()) {
                mLocationClient.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        }
    }

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            mLocationClient.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            mLocationClient.unRegisterLocationListener(listener);
        }
    }

    public static void getLocation(Context context, String[] keys, String[] values) {


    }


}

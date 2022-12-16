package com.gzhealthy.health.api;

import com.facebook.stetho.okhttp3.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.tool.CacheUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiService {

    private static OkHttpClient client = null;
    private static Retrofit retrofit = null;
    private static ApiService _instance = null;
    private static int cacheSize = 30 * 1024 * 1024; // 30 MiB

    public ApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File mCacheFile = new File(CacheUtils.getApplicationCache(HealthApp.getInstance()) + "/network_cache");
        if (!mCacheFile.exists())
            mCacheFile.mkdir();
        if (BuildConfig.DEBUG) {
            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(interceptor)
                    .addInterceptor(new ParamsInterceptor())
                    .cache(new Cache(mCacheFile, cacheSize))
                    .addInterceptor(new CacheInterceptor())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .cache(new Cache(mCacheFile, cacheSize))
                    .addInterceptor(new CacheInterceptor())
                    .addInterceptor(new ParamsInterceptor())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        retrofit = new Retrofit.Builder().client(client)
                .baseUrl(Constants.Api.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit init() {
        if (_instance == null)
            _instance = new ApiService();
        return retrofit;
    }

}

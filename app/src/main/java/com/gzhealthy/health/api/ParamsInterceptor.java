package com.gzhealthy.health.api;

import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.tool.SPCache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求参数拦截器
 * →_→
 * 769856557@qq.com
 * yangyong
 */
class ParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("version", BuildConfig.VERSION_NAME);
        builder.addHeader("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        return chain.proceed(builder.build());
    }
}

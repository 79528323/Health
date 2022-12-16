package com.gzhealthy.health.api;

import android.util.Log;

import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.tool.NetworkUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Tools;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class CacheInterceptor implements Interceptor {

    private final String TAG = CacheInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if ("post".equalsIgnoreCase(chain.request().method())) {
//            Logger.e(TAG, "reqeust : method : " + chain.request().method() + "body : " + chain.request().body().toString());
//            Logger.e(TAG, "请求地址" + request.url().url());
//            Logger.e(TAG, "请求地址2" + URLDecoder.decode(request.url().url().toString(), "UTF-8"));
//            Logger.json(TAG, "reqeust : body : " + chain.request().body().toString());
        } else {
            HttpUrl httpUrl = request.url();
            Set<String> queryNames = httpUrl.queryParameterNames();
            StringBuffer sb = new StringBuffer("{");
            for (String queryName : queryNames) {
                sb.append("\"").append(queryName).append("\"")
                        .append(":").append("\"").append(httpUrl.queryParameter(queryName)).append("\"").append(",");
            }
            sb.substring(sb.length() - 2, sb.length() - 1);
            sb.append("}");
//        String cyptoStr = CyptoUtils.encode(Constants.CYPTO_KEY, sb.toString());
            String cyptoStr = sb.toString();
            URL url = new URL(request.url().scheme(), request.url().host(), request.url().encodedPath() + "?" + "data=" + cyptoStr);
            Logger.e(TAG, "reqeust : url1 : " + url);
            builder.url(url);
            request = builder.build();
        }
        if (!NetworkUtils.isConnected(HealthApp.getInstance())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        Response.Builder newBuilder = response.newBuilder();
        newBuilder.addHeader(SPCache.KEY_TOKEN,SPCache.getString(SPCache.KEY_TOKEN,""));
        String ver = Tools.getVersion(HealthApp.getInstance());
        Log.e("111","ver  "+ver);
        newBuilder.addHeader("version",ver);
        newBuilder.addHeader("agent","Android");
        if (NetworkUtils.isConnected(HealthApp.getInstance())) {
            int maxAge = 0;
            // 有网络时 设置缓存超时时间0个小时
            newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
        } else {
            // 无网络时，设置超时为4周
            int maxStale = 60 * 60 * 24 * 28;
            newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }
        return newBuilder.build();
    }
}

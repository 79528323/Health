package com.gzhealthy.health.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.base.HealthApp;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;


public class LoginInvalidInterceptor implements Interceptor {

    private final String TAG = LoginInvalidInterceptor.class.getSimpleName();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if(!HttpHeaders.hasBody(response)){ //HttpHeader -> 改成了 HttpHeaders，看版本进行选择
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
//                Log.e("111","<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                JSONObject object = JSON.parseObject(result);
                int code = object.getInteger("code");
                if (code == 10086){
                    //TODO 拦截登录信息失效
                    LoginActivity.instance(HealthApp.getInstance(), "10086");
                }
                //获取到response的body的string字符串
                //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }

//            Log.e("111","<-- END HTTP (" + buffer.size() + "-byte body)");
        }
        return response;
    }



    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}

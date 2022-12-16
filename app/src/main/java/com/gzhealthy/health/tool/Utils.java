package com.gzhealthy.health.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.UserInfo;

import org.litepal.crud.DataSupport;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     desc  : Utils初始化相关
 * </pre>
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    public static final void gotoActy(Context context,Class<?> cls){
        UserInfo.DataBean user = DataSupport.findFirst(UserInfo.DataBean.class);
        if (user==null){
            LoginActivity.instance(context,cls,null);
        }else {
            Intent intent = new Intent(context,cls);
            context.startActivity(intent);
        }
    }


    public static final void gotoActy(Context context, Class<?> cls, Bundle bundle){
        UserInfo.DataBean user = DataSupport.findFirst(UserInfo.DataBean.class);
        if (user==null){
            LoginActivity.instance(context,cls,bundle);
        }else {
            Intent intent = new Intent(context,cls);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}

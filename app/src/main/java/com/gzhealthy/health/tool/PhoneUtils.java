package com.gzhealthy.health.tool;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionResultCallBack;


public class PhoneUtils {

    public static void callLocalPhone(String phoneNum, Context context) {
        String[] mPermissionList = new String[]{Manifest.permission.CALL_PHONE};
        PermissionUtil.getInstance().request(mPermissionList, new PermissionResultCallBack() {
            /**
             * 当所有权限的申请被用户同意之后,该方法会被调用
             */
            @Override
            public void onPermissionGranted() {
                callPhone(phoneNum, context);
            }

            /**
             * 返回此次申请中通过的权限列表
             */
            @Override
            public void onPermissionGranted(String... strings) {
            }

            /**
             * 当权限申请中的某一个或多个权限,在此次申请中被用户否定了,并勾选了不再提醒选项时（权限的申请窗口不能再弹出，
             * 没有办法再次申请）,该方法将会被调用。该方法调用时机在onRationalShow之前.onDenied和onRationalShow
             * 有可能都会被触发.
             */
            @Override
            public void onPermissionDenied(String... strings) {
            }

            /**
             * 当权限申请中的某一个或多个权限,在此次申请中被用户否定了,但没有勾选不再提醒选项时（权限申请窗口还能再次申请弹出）
             * 该方法将会被调用.这个方法会在onPermissionDenied之后调用,当申请权限为多个时,onDenied和onRationalShow
             * 有可能都会被触发.
             */
            @Override
            public void onRationalShow(String... strings) {
            }
        });
    }


    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(String phoneNum, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}

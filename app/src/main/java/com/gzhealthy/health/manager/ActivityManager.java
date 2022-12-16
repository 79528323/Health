package com.gzhealthy.health.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Activity管理
 */

public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {

    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);

        Log.e("activityStackSize", "activityStackSize:" + activityStack.size());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (activityStack != null && activityStack.size() >= 1) {
            Activity activity = activityStack.lastElement();
            return activity;
        } else {
            return null;
        }

    }

    /**
     * 是否存在此Activity
     */
    public static boolean hasActivity(Class<?> cls) {
        boolean has = false;
        if (activityStack != null && !activityStack.empty()){
            for (Activity activity : activityStack) {
                Log.e("111","activity  ="+activity.getClass().getName());
                if (activity != null && activity.getClass().equals(cls)) {
                    has = true;
                    Log.e("111","has  ="+has);
                    break;
                }
            }
        }

        return has;
    }


    /**
     * 判断是否在前台
     * @param cls
     * @return
     */
    public static boolean isActivityForeground(Class<?> cls){
        Activity activity = getActivityStack().peek();
        return activity != null && activity.getClass().equals(cls);
    }


//    public static Activity getActivityInstance(Class<Activity> cls){
//        if (!getActivityStack().empty()){
//            for (Activity activity : activityStack) {
//                if (activity != null && activity.getClass().equals(cls)) {
//                    return activity;
//                }
//            }
//        }
//
//        return null;
//    }



    /**
     * 回到某个页面
     *
     * @param cls
     */
    public static void goActivity(Class<?> cls) {
        try {
            List<Activity> activities = new ArrayList<Activity>();
            for (int i = activityStack.size() - 1; i > 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity != null && !activity.getClass().equals(cls)) {
                    activities.add(activity);
                    activity.finish();
                    activity = null;
                } else {
                    break;
                }
            }
            for (int i = 0; i < activities.size(); i++) {
                activityStack.remove(activities.get(i));
            }
            activities.clear();
            activities = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                try {
                    activityStack.remove(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 移除指定的Activity 并未进行finish操作
     */
    public static void removeActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static void setActivityStack(Stack<Activity> activityStack) {
        ActivityManager.activityStack = activityStack;
    }

    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


    //打开其他应用
    public static boolean doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (packageinfo == null) {
            return false;
        }
        try {

            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageinfo.packageName);

            // 通过getPackageManager()的queryIntentActivities方法遍历
            List<ResolveInfo> resolveinfoList = context.getPackageManager()
                    .queryIntentActivities(resolveIntent, 0);

            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null) {
                // packagename = 参数packname
                String packageName = resolveinfo.activityInfo.packageName;
                // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                String className = resolveinfo.activityInfo.name;
                // LAUNCHER Intent
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 设置ComponentName参数1:packagename参数2:MainActivity路径
                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            ToastUtils.showShort("应用打开出错！");
            return true;
        }

    }

    //打开自己应用
    public static boolean doStartApplicationWithPackageNameMy(Context context) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo("com.justep.yunpay", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (packageinfo == null) {
            return false;
        }
        try {

            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageinfo.packageName);

            // 通过getPackageManager()的queryIntentActivities方法遍历
            List<ResolveInfo> resolveinfoList = context.getPackageManager()
                    .queryIntentActivities(resolveIntent, 0);

            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null) {
                // packagename = 参数packname
                String packageName = resolveinfo.activityInfo.packageName;
                // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                String className = resolveinfo.activityInfo.name;
                // LAUNCHER Intent
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 设置ComponentName参数1:packagename参数2:MainActivity路径
                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            ToastUtils.showShort("应用打开出错！");
            return true;
        }

    }


    public static String packageName = "com.gzhealthy.health";
    public static String platform;


    public static void goMyApp(Context context) {
        ActivityManager.doStartApplicationWithPackageName(context, "com.justep.yunpay");
    }

    /**
     * 判断是否在主进程,这个方法判断进程名或者pid都可以,如果进程名一样那pid肯定也一样
     *
     * @return true:当前进程是主进程 false:当前进程不是主进程
     */
    public static boolean isProcessRun(Context context) {
        android.app.ActivityManager am = ((android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<android.app.ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (android.app.ActivityManager.RunningAppProcessInfo info : processInfos) {
            Log.e("111","ProcessInfo="+info.processName);
            if (packageName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


}
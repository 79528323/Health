package com.gzhealthy.health.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gzhealthy.health.base.HealthApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    public static String delHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        return htmlStr.trim(); // 返回文本字符串
    }


    /**
     * @param activity 保存屏幕宽高
     */
    public static void saveDisplaySize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        SPCache.putInt(Constants.SharePreference.SCREEN_WIDTH, displayMetrics.widthPixels);
//        SPCache.putInt(Constants.SharePreference.SCREEN_HEIGHT, displayMetrics.heightPixels);
    }

    /**
     * 将px类型的尺寸转换成dp类型的尺寸
     *
     * @param pxValue
     * @param context
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @方法名称: sp2px
     * @描述: 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 获取屏幕宽度(像素)
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度(像素)
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取设备的唯一标识码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            return device_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    @SuppressLint("NewApi")
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getApplicationCache(Context context) {
        String cache = "";
        File external = context.getExternalCacheDir();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            Log.e("tools", "Environment :" + Environment.getExternalStorageDirectory().getPath());
            cache = Environment.getExternalStorageDirectory().getPath() + "/com.aiten.yunfu";
        } else if (external != null) {
//            Log.e("tools", "getExternalCacheDir :" + external.getAbsolutePath());
            cache = external.getAbsolutePath();
        } else {
//            Log.e("tools", "getCacheDir :" + context.getCacheDir().getAbsolutePath());
            cache = context.getCacheDir().getAbsolutePath();
        }
        File cacheFile = new File(cache);
        if (!cacheFile.exists())
            cacheFile.mkdir();
        Log.e("cache", "cache : " + cacheFile.getAbsolutePath());
        return cacheFile.getAbsolutePath();
    }

    /***
     * 读取本地文件中JSON字符串
     *
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 启动第三方 app
     *
     * @param mcontext
     * @param packagename
     */
    public static void doStartApplicationWithPackageName(Context mcontext, String packagename) { //"com.justep.yunpay"

        // 通过包名获取此 APP 详细信息，包括 Activities、 services 、versioncode 、 name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mcontext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            Toast.makeText(mcontext, "请先去应用市场安装云支付", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建一个类别为 CATEGORY_LAUNCHER 的该包名的 Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过 getPackageManager()的 queryIntentActivities 方法遍历
        List<ResolveInfo> resolveinfoList = mcontext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该 APP 的LAUNCHER 的 Activity[组织形式： packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置 ComponentName参数 1:packagename 参数2: MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            mcontext.startActivity(intent);
        }
    }


    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.e("tools", "" + e);
        }

        return hasNavigationBar;

    }

    /***
     * 时间格式转换
     *
     */
    public static String DataForString(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    public static String getPriceType(float price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(price);
        return p;
    }

    public static String getPriceType2(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(price);
        return p;
    }

    /**
     * 从身份证获取性别
     *
     * @return
     */
    public static String getCardSex(String card) {
        if (card.length() == 15) {
            String sex = card.substring(14, 15);
            if (Integer.parseInt(sex) % 2 == 0) {
                sex = "女";
            } else {
                sex = "男";
            }
            return sex;
        } else {
            String sex = card.substring(16, 17);
            if (Integer.parseInt(sex) % 2 == 0) {
                sex = "女";
            } else {
                sex = "男";
            }
            return sex;
        }
    }

    /**
     * 从身份证中获取出生日期
     *
     * @param card
     * @return
     */
    public static String getCardBirthDate(String card) {
        StringBuffer birthDate = new StringBuffer();
        if (card.length() == 15) {
            birthDate.append(19).append(card.substring(6, 8)).append("-").append(card.substring(8, 10)).append("-").append(card.substring(10, 12));
        } else if (card.length() == 18) {
            birthDate.append(card.substring(6, 10)).append("-").append(card.substring(10, 12)).append("-").append(card.substring(12, 14));
        }
        return birthDate.toString();
    }

    // 将EditText的光标定位到字符的最后面
    public static void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    // 判断一个字符串是否含有数字

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
    }

    public static Context getContext() {
        return HealthApp.getInstance();
    }

    public static Resources getResource() {
        return HealthApp.getInstance().getResources();
    }

    public static int getColor(int txt_color) {
        return getResource().getColor(txt_color);
    }


    /**
     * 获取随机整数
     * @return
     */
    public static int getRandomInt(){
        return new Random().nextInt();
    }


    /**
     * 获取随机整数 （指定范围内）
     * @param minRange
     * @param maxRange
     * @return
     */
    public static int getRandomInt(int minRange,int maxRange){
        return new Random().nextInt(maxRange - minRange) + minRange;
    }


    /**
     * 获取随机整数 （最大值以内）
     * @param maxRange
     * @return
     */
    public static int getRandomWithinInt(int maxRange){
        return new Random().nextInt(maxRange);
    }


    /**
     * 获取随机整数 （最小值以上）
     * @param minRange
     * @return
     */
    public static int getRandomOutSideInt(int minRange){
        return new Random().nextInt()+minRange;
    }
}

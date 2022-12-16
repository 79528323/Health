package com.gzhealthy.health.tool;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author T_xin
 */
public class StringUtils {

    /**
     * 获取id内容String
     *
     * @param mContext
     * @param path
     * @return
     */
    public static String getStr(Context mContext, int path) {
        return mContext.getResources().getString(path);
    }


    /**
     * 是否两位小数的格式
     */
    public static boolean twoDecimal(String body) {

        Pattern p = Pattern.compile("/^(([1-9]+)|([0-9]+\\.[0-9]{1,2}))$/");
        Matcher m = p.matcher(body);
        return m.matches();
    }


    /**
     * 验证密码   字母+数字组合
     *
     * @param psw
     * @return
     */
    public static boolean isPsw(String psw) {
        // String strPattern =
        // "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        String strPattern = "^(?![^a-zA-Z]+$)(?!\\D+$).{6,16}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(psw);
        return m.matches();
    }


    /**
     * 验证邮箱
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        // String strPattern =
        // "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
//		String strPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String strPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }


    /**
     * 判断指定的字符串是否是合法的电话号码
     *
     * @param numberString
     * @return
     */
    public static boolean isPhoneNumber(String numberString) {
        boolean isNumber = false;
        if (!numberString.equals("")) {
            Log.e("", "验证手机号码是否正确的数字位数");
            if (numberString.length() == 11
                    && (isNumber(numberString))
                    && (numberString.startsWith("13")
                    || numberString.startsWith("18")
                    || numberString.startsWith("15") || numberString
                    .startsWith("14"))) {
                isNumber = true;
            }
        }
        Log.e("", isNumber + "==numberString");
        return isNumber;
    }

    /**
     * 判断给定的文本是否是数字
     *
     * @param numberString
     * @return
     */
    public static boolean isNumber(String numberString) {
        return numberString.matches("^[0-9]*$");
    }

    /**
     * 是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isTelNO(String phonenumber) {
        String phone = "0\\d{2,3}-\\d{7,8}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }
//	/(^13\d{9}$)|(^14)[5,7]\d{8}$|(^15[0,1,2,3,5,6,7,8,9]\d{8}$)|(^17)[6,7,8]\d{8}$|(^18\d{9}$)/g

    /**
     * 是否是手机号码
     */
    public static boolean isMobileOrTelNO(String mobiles) {
        return true;
//		if(isMobileNO(mobiles)){
//			return true;
//		}else if(isTelNO(mobiles)){
//			return true;
//		}
//		return false;
    }


    /**
     * 把文字格式转换成utf -8格式
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            if (result == null) {
                return null;
            } else {
                result = java.net.URLEncoder.encode(source, "utf-8");
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把文字格式转换成utf -8格式
     *
     * @param source
     * @return
     */
    public static String urlEncodeGbk(String source) {
        String result = source;
        try {
            if (result == null) {
                return null;
            } else {
                result = java.net.URLEncoder.encode(source, "gbk");
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static boolean Section(String str, int min, int max) {
        int ln = str.length();
        return ln >= min && ln <= max;
    }

    public static int spliteStr(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return 0;
            } else {
                String[] sourceStrArray = str.split("\\.");
                if (sourceStrArray.length >= 0) {
                    return Integer.parseInt(sourceStrArray[0]);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 保留两位小数点  四舍五入
     *
     * @param str
     * @return
     */
    public static String decimalFormatRounded(String str) {
        DecimalFormat df = new DecimalFormat("#0.00");
        try {
            Double valueOf = Double.valueOf(str);
            String format = df.format(valueOf);
            return format;
        } catch (Exception e) {
            return "0.00";
        }

    }

    /**
     * 保留两位小数点  非 四舍五入
     *
     * @param str
     * @return
     */
    public static String decimalFormat(String str) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            Double valueOf = Double.valueOf(str);
            String format = df.format(valueOf).replace(",", "");
            return format;
        } catch (Exception e) {
            return "0.00";
        }

    }

    public static String urlSplit(String url, String parms) {
        Map<String, Object> hm = urlSplit(url);
        // 第二种方式 entrySet()
        Set<Map.Entry<String, Object>> set2 = hm.entrySet();
        for (Iterator<Map.Entry<String, Object>> iterator = set2.iterator(); iterator
                .hasNext(); ) {
            Map.Entry<String, Object> entry = iterator
                    .next();
            String key = entry.getKey();
            // Object valueString=entry.getValue();
            // System.out.println(key+"...."+valueString);
            if (key.equals(parms)) {
                return entry.getValue().toString();
            }
        }
        return "";
    }

    public static Map<String, Object> urlSplit(String data) {
        StringBuffer strbuf = new StringBuffer();
        StringBuffer strbuf2 = new StringBuffer();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < data.length(); i++) {
            if (data.substring(i, i + 1).equals("=")) {
                for (int n = i + 1; n < data.length(); n++) {
                    if (data.substring(n, n + 1).equals("&")
                            || n == data.length() - 1) {
                        map.put(strbuf.toString(), strbuf2);
                        strbuf = new StringBuffer("");
                        strbuf2 = new StringBuffer("");
                        i = n;
                        break;
                    }
                    strbuf2.append(data.substring(n, n + 1));
                }
                continue;
            }
            strbuf.append(data.substring(i, i + 1));
        }

        return map;
    }

    public static boolean isMoney(String money) {
        try {
            Double valueOf = Double.valueOf(money);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static String DeleteBlank(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.replaceAll("\\s*", "");//去空格
        } else {
            return "";
        }
    }


    //四位分隔  银行卡
    public static String separateFour(String str1) {
        String str2 = "";
        for (int i = 0; i < str1.length(); i++) {
            if (i * 4 + 4 > str1.length()) {
                str2 += str1.substring(i * 4, str1.length());
                break;
            }
            str2 += str1.substring(i * 4, i * 4 + 4) + " ";
        }
        if (str2.endsWith(" ")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        return new StringBuilder(str2).toString();
    }


    //四位分隔  银行卡 加密
    public static String separateFourEncrypt(String str1) {
        if (TextUtils.isEmpty(str1)) {
            return "";
        }
        str1 = new StringBuilder(str1).reverse().toString(); // 先将字符串颠倒顺序
        String str2 = "";
        for (int i = 0; i < str1.length(); i++) {
            if (i * 4 + 4 > str1.length()) {
                str2 += str1.substring(i * 4, str1.length());
                break;
            }
            str2 += str1.substring(i * 4, i * 4 + 4) + " ";
        }
        if (str2.endsWith(" ")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        // 最后再将顺序反转过来
        System.err.println(new StringBuilder(str2).reverse().toString());
        String original = new StringBuilder(str2).reverse().toString();
        if (original.length() >= 4) {
            return "**** **** **** " + original.substring(original.length() - 4, original.length());
        } else {
            return original;
        }

    }

    //身份证  前三后三
    public static String getIdentityEncrypt(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() <= 6) {
            stringBuffer.replace(0, stringBuffer.length(), "******");
        } else {
            String substringS = str.substring(0, 3);
            String substringE = str.substring(str.length() - 3, str.length());
            stringBuffer.append(substringS);
            stringBuffer.append("************");
            stringBuffer.append(substringE);

        }
        return stringBuffer.toString();
    }

    //真实姓名 2字，最后   >2中间
    public static String getUserNameEncrypt(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        if (length < 2) {
            return "*";
        } else if (length == 2) {
            String substringE = str.substring(str.length() - 1, str.length());
            stringBuffer.append("*");
            stringBuffer.append(substringE);
            return stringBuffer.toString();
        } else if (length > 2) {
            String substringS = str.substring(0, 1);
            String substringE = str.substring(str.length() - 1, str.length());
            stringBuffer.append(substringS);
            stringBuffer.append("*");
            stringBuffer.append(substringE);
            return stringBuffer.toString();
        }
        return "*";
    }

    //手机号 前三后四
    public static String getTelEncrypt(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() <= 0) {
            return "";
        } else if (str.length() <= 7) {
            stringBuffer.replace(0, stringBuffer.length(), "*******");
        } else {
            String substringS = str.substring(0, 3);
            String substringE = str.substring(str.length() - 4, str.length());
            stringBuffer.append(substringS);
            stringBuffer.append("****");
            stringBuffer.append(substringE);

        }
        return stringBuffer.toString();
    }

    //loginName为11位时候  手机号 前三后四
    public static String getLoginNameEncrypt(String str) {
        if (!TextUtils.isEmpty(str) && str.length() == 11) {
            return getTelEncrypt(str);
        } else if (!TextUtils.isEmpty(str) && str.length() > 11 && str.indexOf("_") != -1) {
            return getTelEncrypt(str.substring(0, 11)) + str.substring(11, str.length());
        } else {
            return str;
        }

    }

    //银行卡号 前四后三
    public static String getBankAccountEncrypt(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() <= 7) {
            stringBuffer.replace(0, stringBuffer.length(), "*******");
        } else {
            String substringS = str.substring(0, 4);
            String substringE = str.substring(str.length() - 3, str.length());
            stringBuffer.append(substringS);
            stringBuffer.append(" **** **** *");
            stringBuffer.append(substringE);

        }
        return stringBuffer.toString();
    }

    //云商圈距离换算
    public static String distanceConversion(String str) {
        if (!TextUtils.isEmpty(str)) {
            Double aDouble = Double.valueOf(str);
            if (aDouble <= 0) {
                return "0.00米";
            } else if (aDouble < 1000) {
                return decimalFormat(str) + "米";
            } else {
                double v = aDouble / 1000;
                return decimalFormat(String.valueOf(v)) + "公里";
            }
        } else {
            return "0.00米";
        }
    }

    //云商圈距离换算
    public static String distanceConversion(Double lng1Dou, Double lat1Dou, String lng2, String lat2) {
        try {
            double lng2Dou = Double.valueOf(lng2);
            double lat2Dou = Double.valueOf(lat2);
            Double distance = distance(lng1Dou, lat1Dou, lng2Dou, lat2Dou);
            String str = String.valueOf(distance);
            if (!TextUtils.isEmpty(str)) {
                Double aDouble = Double.valueOf(str);
                if (aDouble <= 0) {
                    return "0.00米";
                } else if (aDouble < 1000) {
                    return decimalFormat(str) + "米";
                } else {
                    double v = aDouble / 1000;
                    return decimalFormat(String.valueOf(v)) + "公里";
                }
            } else {
                return "0.00米";
            }
        } catch (Exception e) {
            return "0.00米";
        }

    }

    //云商圈距离换算
    public static String distanceConversion2(Double lng1Dou, Double lat1Dou, String lng2, String lat2) {
        try {
            double lng2Dou = Double.valueOf(lng2);
            double lat2Dou = Double.valueOf(lat2);
            Double distance = distance(lng1Dou, lat1Dou, lng2Dou, lat2Dou);
            String str = String.valueOf(distance);
            if (!TextUtils.isEmpty(str)) {
                Double aDouble = Double.valueOf(str);
                if (aDouble <= 0) {
                    return "0.00米";
                } else if (aDouble < 1000) {
                    return decimalFormat(str) + "米";
                } else {
                    double v = aDouble / 1000;
                    return decimalFormat(String.valueOf(v)) + "公里";
                }
            } else {
                return "0.00米";
            }
        } catch (Exception e) {
            return "0.00米";
        }

    }

    //距离计算
    public static Double distance(double lng1, double lat1, double lng2, double lat2) {
        double theta = lng1 - lng2;
        double dist = Math.sin(lat1 / 180 * Math.PI) * Math.sin(lat2 / 180 * Math.PI)
                + Math.cos(lat1 / 180 * Math.PI) * Math.cos(lat2 / 180 * Math.PI)
                * Math.cos(theta / 180 * Math.PI);
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        double miles = dist * 60 * 1.1515;
        Double aDouble = miles * 1609.344;

        return aDouble;
    }

    //GPS两点坐标之间的距离计算   出来的结果单位为千米
    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }


    /**
     * 1.  getPhoneNumber方法返回当前手机的电话号码，
     * 同时必须在androidmanifest.xml中
     * 加入 android.permission.READ_PHONE_STATE 这个权限，
     * 2.  主流的获取用户手机号码一般采用用户主动发送短信到SP或接收手机来获取。
     *
     * @param context <a href="http://my.oschina.net/u/556800" class="referer" target="_blank">@return</a>
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tel = mTelephonyMgr.getLine1Number();
        if (TextUtils.isEmpty(tel)) {
            tel = "";
        }
        return tel;
    }


    public static String getPhoneEncrypt(String tel) {
        if (TextUtils.isEmpty(tel)) {
            return "";
        }
        if (tel.length() >= 11) {
            String start = tel.substring(0, 3);
            String end = tel.substring(tel.length() - 2, tel.length());
            String phoneNum = start + "******" + end;
            return phoneNum;
        } else {
            return "";
        }

    }

    public static String getReturn(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String res = "";
        try {
            byte[] decode = Base64.decode(result, Base64.DEFAULT);
            byte[] decrypt = DesUtil.decrypt(decode, getKey().getBytes("utf-8"));
            res = new String(decrypt, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPswDe(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String res = "";
        try {
            byte[] decode = Base64.decode(result, Base64.DEFAULT);
            byte[] decrypt = DesUtil.decrypt(decode, DesPswUtil.getDesStr().getBytes("utf-8"));
            res = new String(decrypt, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setPswEn(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String res = "";
        try {
            byte[] encrypt = DesUtil.encrypt(result.getBytes("utf-8"), DesPswUtil.getDesStr().getBytes("utf-8"));
            byte[] encode = Base64.encode(encrypt, Base64.DEFAULT);
            res = new String(encode, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getKey() {
        String res = "";
        try {
            byte[] decode = Base64.decode(DesPswUtil.getKeyStr(), Base64.DEFAULT);
            byte[] decrypt = DesUtil.decrypt(decode, DesPswUtil.getDesKeyStr().getBytes("utf-8"));
            res = new String(decrypt, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //DES加密
    public static String setEn(String result, String key) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String res = "";
        try {
            byte[] encrypt = DesUtil.encrypt(result.getBytes("utf-8"), key.getBytes("utf-8"));
            byte[] encode = Base64.encode(encrypt, Base64.DEFAULT);
            res = new String(encode, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDe(String result, String key) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String res = "";
        try {
            byte[] decode = Base64.decode(result, Base64.DEFAULT);
            byte[] decrypt = DesUtil.decrypt(decode, key.getBytes("utf-8"));
            res = new String(decrypt, "utf-8");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getIntFromFloat(float from) {
        Float F1 = new Float(from);
        double d1 = F1.doubleValue();//F1.doubleValue()为Float类的返回double值型的方法
        return getIntFromDouble(d1);
    }

    public static int getIntFromDouble(double from) {
        Double D1 = new Double(from);
        int ratingInt = D1.intValue();
        return ratingInt;
    }

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        str = str.trim();
        try {
            Double i = Double.valueOf(str);
            if (i > 0) {
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    //100的倍数
    public static boolean isInteger100(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        str = str.trim();
        try {
            Double i = Double.valueOf(str);
            if (i > 0 && i % 100 == 0) {
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException ex) {
            return false;
        }
    }


    public static String getPercentageStr(double a, double b) {
        if (b == 0) {
            return "0.0%";
        }
        DecimalFormat format = new DecimalFormat("#0.00%");
//		Format format=new DecimalFormat("0.00");
        String s = format.format((double) a / b);
        return s;
    }

    public static double getPercentageInt(double a, double b) {
        if (b == 0) {
            return 0;
        }
        if (a > b) {
            return 100;
        }
        Format format = new DecimalFormat("0.00");
        double s = ((a * 1.0 / b) * 100);
        return s;
    }

    //将String类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字
    public static String formatTosepara(String data) {
        if (TextUtils.isEmpty(data)) {
            return "0.00";
        }
        try {

            BigDecimal bigDecimal = new BigDecimal(data.replace(",", ""));
            DecimalFormat df = new DecimalFormat("#,##0.00");
//			df.setRoundingMode(RoundingMode.HALF_DOWN);
            return df.format(bigDecimal);
        } catch (Exception e) {
            return data;
        }

    }

    //将String类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字
    public static String formatTosepara(double data) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
//			df.setRoundingMode(RoundingMode.HALF_DOWN);
            return df.format(data);
        } catch (Exception e) {
            return "0.00";
        }

    }

    //将String类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字
    public static String formatTosepara(BigDecimal data) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
//			df.setRoundingMode(RoundingMode.HALF_DOWN);
            return df.format(data);
        } catch (Exception e) {
            return "0.00";
        }
    }

    //将String类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字
    public static String formatToseparaUp(BigDecimal data) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            return df.format(data);
        } catch (Exception e) {
            return "0.00";
        }
    }

    //将String类型的数据转换成以3位逗号隔开的字符串，并且保留两位有效数字
    public static String formatToseparaThree(BigDecimal data) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.000");
//			df.setRoundingMode(RoundingMode.HALF_DOWN);
            return df.format(data);
        } catch (Exception e) {
            return "0.00";
        }

    }

    //将String类型的数据转换成整数
    public static String formatToseparaInt(String data) {
        try {
            BigDecimal bigDecimal = new BigDecimal(data.replace(",", ""));
            int i = bigDecimal.intValue();
            return i + "";
        } catch (Exception e) {
            return "0";
        }

    }

    public static String formatToseparaTwo2(BigDecimal data) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
//			df.setRoundingMode(RoundingMode.HALF_DOWN);
            return df.format(data);
        } catch (Exception e) {
            return "0.00";
        }

    }

    public static final String homeRechargeTrafficSignKey = "tgNfaDueXBi1JPmUhvBjdK6t4Hv1ZwCX";

    public static String getHomeRechargeTrafficSign(String tel, String loginName, String nonce_str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tel).append(loginName).append(nonce_str).append(homeRechargeTrafficSignKey);
        String s = Md5Utils.encryptH(stringBuilder.toString());
        return s;
    }

}

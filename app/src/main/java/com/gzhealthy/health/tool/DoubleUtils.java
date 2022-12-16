package com.gzhealthy.health.tool;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleUtils {

    public static String dealAmount(String amount) {
        BigDecimal result = null;
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(100));
            result = a1.divide(b1);
        }
        return String.valueOf(result.doubleValue());
    }

    public static String dealAmountRInt(String amount) {
        BigDecimal result = null;
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(100));
            result = a1.divide(b1);
        }
        return String.valueOf(result.intValue());
    }

    public static String dealAmountSaveTwo(String amount) {
        BigDecimal result = null;
        DecimalFormat df2 = new DecimalFormat("#.00");
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(100));
            result = a1.divide(b1);
        }
        return String.valueOf(df2.format(result.doubleValue()));
    }

    public static String deal1000SaveTwo(String amount) {
        if (TextUtils.isEmpty(amount) || amount.equals("0") || "null".equals(amount)) {
            return "0.000";
        }
        BigDecimal result = null;
        DecimalFormat df2 = new DecimalFormat("#.000");
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(1000));
            result = a1.divide(b1);
        }
        return String.valueOf(df2.format(result.doubleValue()));
    }

    public static String deal100SaveTwo(String amount) {
        if (TextUtils.isEmpty(amount) || amount.equals("0") || "null".equals(amount)) {
            return "0.00";
        }
        BigDecimal result = null;
        DecimalFormat df2 = new DecimalFormat("0.00");
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(100));
            result = a1.divide(b1);
        }
        return String.valueOf(df2.format(result.doubleValue()));
    }

    public static String multiplyAmount(String amount) {
        BigDecimal result = null;
        if (!TextUtils.isEmpty(amount)) {
            BigDecimal a1 = new BigDecimal(Double.toString(Double.parseDouble(amount)));
            BigDecimal b1 = new BigDecimal(Double.toString(100));
            result = a1.multiply(b1);// 相乘结果
        }
        return String.valueOf(result.intValue());
    }

    public static String addAmount(String amount, String amount2) {
        if (TextUtils.isEmpty(amount) || amount.equals("0") || "null".equals(amount)) {
            amount = "0";
        }
        if (TextUtils.isEmpty(amount2) || amount2.equals("0") || "null".equals(amount2)) {
            amount2 = "0";
        }
        BigDecimal result = null;
        BigDecimal a1 = new BigDecimal(amount);
        BigDecimal b1 = new BigDecimal(amount2);
        result = a1.add(b1);
        return String.valueOf(result);
    }

}

package com.gzhealthy.health.utils;


import java.math.BigDecimal;

public class Arith {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //构造器私有，让这个类不能实例化
    private Arith() {
    }

    //提供精确的加法运算
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }

    //提供精确的减法运算
    public static double sub(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }

    //提供精确的乘法运算
    public static double mul(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).doubleValue();
    }

    //提供精确的除法运算
    public static double div(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2).doubleValue();
    }
}


package com.gzhealthy.health.tool;


public class NumRandomTools {
    public static String RandomData(int count) {
        int numcode = (int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1));
        return numcode + "";
    }
}

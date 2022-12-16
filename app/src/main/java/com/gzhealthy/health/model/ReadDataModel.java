package com.gzhealthy.health.model;

/**
 * Created by Justin_Liu
 * on 2021/11/17
 */
public class ReadDataModel {
    private String head = "AE"; //命令头
    private String no = "";//命令号
    private String content = "";//内容

    private String checkCode = "FE";//校正码

    public ReadDataModel(byte[] bytes) {
        String data = new String(bytes);

    }
}

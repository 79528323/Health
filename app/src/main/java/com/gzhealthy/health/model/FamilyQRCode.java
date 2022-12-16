package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/9/3
 */
public class FamilyQRCode implements Serializable {

    /**
     * msg : 获取数据成功
     * code : 1
     * data : {"inviteNum":3,"introduceUrl":"http://xxxx","data":[{"member":"6620021","memberNickName":"隔壁老王","createTime":1622372482000,"memberAvatar":"http://xxx","id":16254,"healthData":[{"type":1,"value":"75","status":"","name":"心率"},{"type":2,"value":"75","status":"","name":"血压"}]},{"member":"6620021","memberNickName":"隔壁老王","createTime":1622372482000,"memberAvatar":"http://xxx","id":16254,"healthData":[{"type":1,"value":"75","status":"","name":"心率"},{"type":2,"value":"75","status":"","name":"血压"}]}]}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {
        public String avatar;
        public String nickName;
        public String qrCodePath;
        public String uid;
    }
}

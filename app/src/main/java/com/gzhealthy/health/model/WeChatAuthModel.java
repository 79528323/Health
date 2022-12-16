package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/5/13
 */
public class WeChatAuthModel implements Serializable {

    /**
     * openid : oPc-zwgsqs-z09x5czY80PEAsVV4
     * nickname : -
     * sex : 1
     * language : zh_CN
     * city : Guangzhou
     * province : Guangdong
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/ibRLxrcn3GezQDHxMOqZfc5sEpQpU7iawERnE0G1tz7YktZKDThoPhljYU2nYdicnasjtEDMAWdmHzwFdkyicTT6T2e1PuMazwYQ/0
     * privilege : []
     * unionid : or_ZDwKWbrFaf3xo5dqudQ8BAoKA
     */

    public String openid;
    public String nickname;
    public int sex;
    public String language;
    public String city;
    public String province;
    public String country;
    public String headimgurl;
    public String unionid;
    public List<?> privilege;
}

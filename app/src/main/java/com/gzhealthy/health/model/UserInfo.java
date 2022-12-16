package com.gzhealthy.health.model;


import com.gzhealthy.health.model.base.BaseModel;

import org.litepal.crud.DataSupport;


public class UserInfo extends BaseModel<UserInfo.DataBean> {


    public static final int TYPE_GUIDE = 1;

    public static final int TYPE_TOURIST = 0;

    public static class DataBean extends DataSupport {

        private String birthday;
        private String city;
        private String cityName;
        private int countDays;
        private String county;
        private String countyName;
        private String detailedAddress;
        private String educationalLevel;
        private Object effectiveDate;
        private String email;
        private String gesturesPassword;
        private Object guideOverTime;
        private String headPic;
        private int id;
        private Object idNumber;
        private int isGuide;
        private String loginName;
        private String marriage;
        private String name;
        private String nickName;
        private String payPassword;
        private String phone;
        private String province;
        private String provinceName;
        private Object recomId;
        private int sex;
        private int type;
        private String cash;
        private int isOpenGestures;
        private String yunPay;
        private String qq;
        private String weChat;
        private String sina;
        private String realName;
        private String realNameStatus;
        private String idCard;
        private int isSuper;
        private String deviceToken;
        private String wechatNickName;
        private String password;
        private int recordModifyStatus;
        private int vipStatus;
        private String effectiveTime;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getRecordModifyStatus() {
            return recordModifyStatus;
        }

        public void setRecordModifyStatus(int recordModifyStatus) {
            this.recordModifyStatus = recordModifyStatus;
        }

        public String getWechatNickName() {
            return wechatNickName;
        }

        public void setWechatNickName(String wechatNickName) {
            this.wechatNickName = wechatNickName;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public int getIsSuper() {
            return isSuper;
        }

        public void setIsSuper(int isSuper) {
            this.isSuper = isSuper;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getRealName() {
            return realName == null ? "" : realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRealNameStatus() {
            return realNameStatus == null ? "" : realNameStatus;
        }

        public void setRealNameStatus(String realNameStatus) {
            this.realNameStatus = realNameStatus;
        }

        public String getQq() {
            return qq == null ? "" : qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWeChat() {
            return weChat == null ? "" : weChat;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }

        public String getSina() {
            return sina == null ? "" : sina;
        }

        public void setSina(String sina) {
            this.sina = sina;
        }

        public String getCash() {
            return cash == null ? "" : cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getYunPay() {
            return yunPay;
        }

        public void setYunPay(String yunPay) {
            this.yunPay = yunPay;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getCountDays() {
            return countDays;
        }

        public void setCountDays(int countDays) {
            this.countDays = countDays;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public String getDetailedAddress() {
            return detailedAddress;
        }

        public void setDetailedAddress(String detailedAddress) {
            this.detailedAddress = detailedAddress;
        }

        public String getEducationalLevel() {
            return educationalLevel;
        }

        public void setEducationalLevel(String educationalLevel) {
            this.educationalLevel = educationalLevel;
        }

        public Object getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(Object effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGesturesPassword() {
            return gesturesPassword;
        }

        public void setGesturesPassword(String gesturesPassword) {
            this.gesturesPassword = gesturesPassword;
        }

        public Object getGuideOverTime() {
            return guideOverTime;
        }

        public void setGuideOverTime(Object guideOverTime) {
            this.guideOverTime = guideOverTime;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(Object idNumber) {
            this.idNumber = idNumber;
        }

        public int getIsGuide() {
            return isGuide;
        }

        public void setIsGuide(int isGuide) {
            this.isGuide = isGuide;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMarriage() {
            return marriage;
        }

        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPayPassword() {
            return payPassword;
        }

        public void setPayPassword(String payPassword) {
            this.payPassword = payPassword;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public Object getRecomId() {
            return recomId;
        }

        public void setRecomId(Object recomId) {
            this.recomId = recomId;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsOpenGestures() {
            return isOpenGestures;
        }

        public void setIsOpenGestures(int isOpenGestures) {
            this.isOpenGestures = isOpenGestures;
        }

        public int getVipStatus() {
            return vipStatus;
        }

        public void setVipStatus(int vipStatus) {
            this.vipStatus = vipStatus;
        }

        public String getEffectiveTime() {
            return effectiveTime;
        }

        public void setEffectiveTime(String effectiveTime) {
            this.effectiveTime = effectiveTime;
        }
    }
}
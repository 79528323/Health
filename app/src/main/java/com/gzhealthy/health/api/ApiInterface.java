package com.gzhealthy.health.api;


import com.gzhealthy.health.model.Admodel;
import com.gzhealthy.health.model.BindType;
import com.gzhealthy.health.model.CardPackageModel;
import com.gzhealthy.health.model.ChineseMedicineBodyModel;
import com.gzhealthy.health.model.CollectListModel;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.model.CustomerServiceModel;
import com.gzhealthy.health.model.DiseaseRecord;
import com.gzhealthy.health.model.DiseaseRecordDetail;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.model.EmergencyContact;
import com.gzhealthy.health.model.EnterInvitePhoneModel;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.model.FamilyMemberInvite;
import com.gzhealthy.health.model.FamilyQRCode;
import com.gzhealthy.health.model.FenceScope;
import com.gzhealthy.health.model.FenceSetting;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.HaveFamilyMember;
import com.gzhealthy.health.model.HealthRecordModel;
import com.gzhealthy.health.model.HealthyEcgReport;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.model.HealthyNewReport;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.HealthyReportHistory;
import com.gzhealthy.health.model.HealthyStatistics;
import com.gzhealthy.health.model.HomePageHealthClock;
import com.gzhealthy.health.model.MedicationRecord;
import com.gzhealthy.health.model.MedicationRecordDetailModel;
import com.gzhealthy.health.model.MedicationRecordModel;
import com.gzhealthy.health.model.NewsCategoryModel;
import com.gzhealthy.health.model.NewsDetailModel;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.model.OssModel;
import com.gzhealthy.health.model.PelletFrameModel;
import com.gzhealthy.health.model.PersonHealthInfo;
import com.gzhealthy.health.model.PushMessage;
import com.gzhealthy.health.model.QuePaper;
import com.gzhealthy.health.model.RateWarnHelpModel;
import com.gzhealthy.health.model.ReLocate;
import com.gzhealthy.health.model.RollNewsModel;
import com.gzhealthy.health.model.ScoreDetailModel;
import com.gzhealthy.health.model.ScoreInfoModels;
import com.gzhealthy.health.model.SleepInfo;
import com.gzhealthy.health.model.SosListModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WalkInfoModel;
import com.gzhealthy.health.model.WatchDeviceModel;
import com.gzhealthy.health.model.WechatApplet;
import com.gzhealthy.health.model.banner.DeviceDetail;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.model.home.ConfigModel;
import com.gzhealthy.health.model.home.VersionModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiInterface<T> {

    // 获取App配置信息
    @POST("api/index/config")
    Observable<ConfigModel> mainConfig(@QueryMap Map<String, String> params);

    // 获取App版本信息
    @POST("api/appInfo/getAppVersion")
    Observable<VersionModel> getAppVersion(@QueryMap Map<String, String> params);

    // OSS验签
    @POST("api/service/getKey")
    Observable<OssModel> getKey(@QueryMap Map<String, String> params);

    // 重置登录密码
    @POST("api/center/updatePsd")
    Observable<ComModel> centerupdatePsd(@QueryMap Map<String, String> params);

    // 会员名或手机号-登录
    @POST("api/account/login")
    Observable<ComModel> login(@QueryMap Map<String, String> params);

    // 短信验证码登录
    @POST("api/account/dynamicLogin")
    Observable<ComModel> dynamicLogin(@QueryMap Map<String, String> params);

    // 获取绑定手机号短信验证码
    @POST("api/message/getBindingCode")
    Observable<ComModel> getBindingCode(@QueryMap Map<String, String> params);

    /**
     * 第三方授权登录之后绑定手机号
     *
     * @param params
     * @return
     */
    @POST("api/account/bindingPhone")
    Observable<BaseModel<String>> bindingPhone(@QueryMap Map<String, String> params);

    /**
     * 第三方授权登录之后请求进行判断，若数据库中存在直接登录
     *
     * @param params
     * @return
     */
    @POST("api/account/isExist")
    Observable<BaseModel<String>> isExist(@QueryMap Map<String, String> params);

    // 获取短信验证码 登录、注册、发送验证码、找回密码、绑定手机号
    @POST("api/message/getCode")
    Observable<ComModel> getRegisterCode(@QueryMap Map<String, String> params);

    // 注册
    @POST("api/account/register ")
    Observable<ComModel> register(@QueryMap Map<String, String> params);

    // 获取找回密码短信验证码
    @POST("api/message/getCode")
    Observable<ComModel> getFindPasswordCode(@QueryMap Map<String, String> params);

    // 重置密码
    @POST("api/account/updatePsd")
    Observable<ComModel> updatePsd(@QueryMap Map<String, String> params);

    // 获取个人信息
    @POST("api/center/getAccountInfo")
    Observable<UserInfo> getAccountInfo(@QueryMap Map<String, String> params);

    // 云付通绑定账号
    @POST("api/bindingAccount/binding")
    Observable<ComModel> yunPay(@QueryMap Map<String, String> params);

    // 第三方账号解绑
    @POST("api/bindingAccount/untie")
    Observable<ComModel> untie(@QueryMap Map<String, String> params);

    // 意见反馈和报故障
    @POST("api/appInfo/addFeedBack")
    Observable<ComModel> addFeedBack(@QueryMap Map<String, String> params);

    // 修改手机号：将前面页面的手机号带过来
    @POST("api/center/editAccountInfo")
    Observable<ComModel> editAccountInfo(@Body RequestBody body, @Header("token") String token);

    // 实名认证
    @POST("api/bank/realName")
    Observable<ComModel> realName(@QueryMap Map<String, String> params);

    /**
     * 添加收藏
     *
     * @param params
     * @return
     */
    @POST("api/myCollection/add")
    Observable<ComModel> addCollection(@QueryMap Map<String, String> params);

    /**
     * 是否已收藏
     *
     * @param params
     * @return
     */
    @POST("api/myCollection/isCollection")
    Observable<ComModel> isCollection(@QueryMap Map<String, String> params);

    /**
     * 取消收藏
     *
     * @param params
     * @return
     */
    @POST("api/myCollection/del")
    Observable<ComModel> cancelCollection(@QueryMap Map<String, String> params);


    // 删除收藏
    @POST("api/myCollection/delCollection")
    Observable<ComModel> delCollection(@QueryMap Map<String, String> params);

    // 收藏列表
    @POST("api/myCollection/getList")
    Observable<CollectListModel> collectList(@QueryMap Map<String, String> params);

    // 轮播数据列表
    @POST("api/advert/getInfo")
    Observable<Admodel> getBannerList(@QueryMap Map<String, String> params);

    // 滚动资讯
    @POST("api/article/rollNews")
    Observable<RollNewsModel> getRollNews(@QueryMap Map<String, String> params);

    // 资讯分类Tab标签
    @POST("api/article/category")
    Observable<NewsCategoryModel> getCategory(@QueryMap Map<String, String> params);

    // 资讯列表
    @POST("api/article/list")
    Observable<NewsListModel> getNewsList(@QueryMap Map<String, String> params);


    // 资讯列表
    @POST("api/article/detail")
    Observable<NewsDetailModel> getNewsdetail(@QueryMap Map<String, String> params);


    // 积分收支明细
    @POST("api/point/getRecord")
    Observable<ScoreDetailModel> getRecord(@QueryMap Map<String, String> params);

    // 我的积分
    @POST("api/point/info")
    Observable<ScoreInfoModels> getScoresInfo(@QueryMap Map<String, String> params);

    /**
     * (主页）获取最新一条步数、血压、血氧等健康记录
     *
     * @param params
     * @return
     */
    @POST("api/health/queryLatestHealthInfo")
    Observable<HealthyInfo> queryLatestHealthInfo(@QueryMap Map<String, String> params);

    /**
     * 绑定手表申请
     *
     * @param params
     * @return
     */
    @POST("api/watch/bindOrUnbind/watchBindApply")
    Observable<BaseModel> watchBindApply(@QueryMap Map<String, String> params);


    /**
     * 根据时间段、类型获取对应数据
     *
     * @param params
     * @return
     */
    @POST("api/health/getHealthInfo")
    Observable<HealthyListDataModel> getHealthInfo(@QueryMap Map<String, String> params);


    /**
     * 获取心电图数据
     *
     * @param params
     * @return
     */
    @POST("api/health/getHealthInfo")
    Observable<EKGModel> getEKGInfo(@QueryMap Map<String, String> params);


    /**
     * 根据时间段、类型获取对应数据
     *
     * @param params
     * @return
     */
    @POST("api/phoneCall/callRemind")
    Observable<HealthyListDataModel> callRemind(@QueryMap Map<String, String> params);


    /**
     * 获取手表绑定状态
     * @param params
     * @return
     */
//    @POST("api/watch/bindOrUnbind/getWatchBindSituation")
//    Observable<BaseModel> getWatchBindSituation(@QueryMap Map<String, String> params);


    /**
     * 确认手表绑定
     *
     * @param params
     * @return
     */
    @POST("api/watch/bindOrUnbind/watchBindConfirm")
    Observable<BaseModel> watchBindConfirm(@QueryMap Map<String, String> params);


    /**
     * 添加紧急联系人
     *
     * @param params
     * @return
     */
    @POST("api/center/addEmergencyContact")
    Observable<BaseModel> addEmergencyContact(@QueryMap Map<String, String> params, @Header("token") String token);


    /**
     * 获取紧急联系人
     *
     * @return
     */
    @POST("api/center/getEmergencyContact")
    Observable<ContractModel> getEmergencyContact(@Header("token") String token);


    /**
     * 获取紧急联系人
     *
     * @return
     */
    @POST("api/center/editEmergencyContact")
    Observable<BaseModel> editEmergencyContact(@QueryMap Map<String, String> params, @Header("token") String token);


    /**
     * 获取紧急联系人
     *
     * @return
     */
    @POST("api/center/deleteEmergencyContact")
    Observable<BaseModel> deleteEmergencyContact(@QueryMap Map<String, String> params, @Header("token") String token);

    /**
     * 获取紧急联系人
     *
     * @return
     */
    @POST("api/center/bindOrEditPersonBaseInfo")
    Observable<BaseModel> bindOrEditPersonBaseInfo(@Body RequestBody params, @Header("token") String token);

    /**
     * 获取GPS位置
     *
     * @return
     */
    @POST("api/watchGps/getLatestGPS")
    Observable<GPS> getLatestGPS(@QueryMap Map<String, String> params);

    /**
     * 获取步数
     *
     * @return
     */
    @POST("api/health/getWalkInfo")
    Observable<WalkInfoModel> getWalkInfo(@QueryMap Map<String, String> params);

    /**
     * 获取手表设备
     *
     * @return
     */
    @POST("api/watch/bindOrUnbind/getBindList")
    Observable<WatchDeviceModel> getBindList(@QueryMap Map<String, String> params);

    /**
     * 手表解绑
     *
     * @return
     */
    @POST("api/watch/bindOrUnbind/watchUnBind")
    Observable<BaseModel> watchUnBind(@QueryMap Map<String, String> params);

    /**
     * 获取SOS消息列表
     *
     * @return
     */
    @POST("api/systemMsg/getList")
    Observable<SosListModel> getSosList(@QueryMap Map<String, String> params);

    /**
     * 心率预警求助
     *
     * @return
     */
    @POST("/api/watchSos/rateWarnHelp")
    Observable<RateWarnHelpModel> rateWarnHelp(@QueryMap Map<String, String> params);

    /**
     * 关闭心率预警求助弹窗
     *
     * @return
     */
    @POST("/api/watchSos/appRateWarnCancel")
    Observable<ComModel> appRateWarnCancel(@QueryMap Map<String, String> params);


    /**
     * 获取我的消息未读数
     *
     * @param params
     * @return
     */
    @POST("api/systemMsg/getMyUnReadMessageCount")
    Observable<BaseModel<Integer>> getMyUnReadMessageCount(@QueryMap Map<String, String> params);


    /**
     * 获取紧急消息未读数
     *
     * @param params
     * @return
     */
    @POST("api/systemMsg/getUnEmergencyMsgCount")
    Observable<BaseModel<Integer>> getUnEmergencyMsgCount(@QueryMap Map<String, String> params);


    /**
     * 添加极光推送设备唯一标识
     *
     * @return
     */
    @POST("api/center/addDeviceToken")
    Observable<BaseModel> addDeviceToken(@QueryMap Map<String, String> params);

    /**
     * 添加极光推送设备唯一标识
     *
     * @return
     */
    @POST("api/center/getAccountHealthRecord")
    Observable<PersonHealthInfo> getAccountHealthRecord(@QueryMap Map<String, String> params);

    /**
     * 添加极光推送设备唯一标识
     *
     * @return
     */
    @POST("api/appInfo/getPlatformAgreement")
    Observable<CompanyPrivacy> getPlatformAgreement(@Query("name") String name);


    /**
     * 健康数据统计
     *
     * @return
     */
    @POST("api/health/Statistic/healthDataStatistic")
    Observable<HealthyStatistics> healthDataStatistic(@QueryMap Map<String, String> params);


    /**
     * 获取问卷
     *
     * @param params
     * @return
     */
    @POST("api/health/paper/getPaper")
    Observable<QuePaper> getQuestionPaper(@QueryMap Map<String, String> params);


    /**
     * 保存答案
     *
     * @param params
     * @return
     */
    @POST("api/health/paper/saveAnswer")
    Observable<BaseModel<Boolean>> saveAnswer(@QueryMap Map<String, String> params, @Header("token") String token);


    /**
     * 获取健康报告详情页
     *
     * @param params
     * @param token
     * @return
     */
    @POST("api/health/report/getInfo")
    Observable<HealthyReport> getReportInfo(@QueryMap Map<String, String> params, @Header("token") String token);


    /**
     * 获取健康报告历史
     *
     * @param params
     * @param token
     * @return
     */
    @POST("api/health/report/getList")
    Observable<HealthyReportHistory> getReportList(@QueryMap Map<String, String> params, @Header("token") String token);


    /**
     * 提交问卷
     *
     * @param paperId
     * @param token
     * @return
     */
    @POST("api/health/paper/submitPaper")
    Observable<BaseModel> submitPaper(@Query("id") long paperId, @Header("token") String token);

    /**
     * 能否获取健康报告，若用户未完成问卷，则无法获取健康报告
     *
     * @param token
     * @return
     */
    @POST("api/health/report/isGetReport")
    Observable<BaseModel<Boolean>> isGetReport(@Header("token") String token);


    /**
     * 忘记密码时校验验证码
     *
     * @param params
     * @return
     */
    @POST("api/center/verifyMessageCode")
    Observable<BaseModel> verifyMessageCode(@QueryMap Map<String, String> params);


    /**
     * 忘记密码时设置密码
     *
     * @param params
     * @return
     */
    @POST("api/center/setPsd")
    Observable<BaseModel> setPsd(@QueryMap Map<String, String> params);


    /**
     * 更新手机（只针对更换绑定手机）
     *
     * @param params
     * @return
     */
    @POST("api/center/changePhone")
    Observable<BaseModel> changePhone(@QueryMap Map<String, String> params);


    /**
     * 获取手表设备详情
     *
     * @param params
     * @return
     */
    @POST("api/watch/getDeviceDetail")
    Observable<DeviceDetail> getDeviceDetail(@QueryMap Map<String, String> params);


    /**
     * 设置抬手亮屏
     *
     * @param params
     * @return
     */
    @POST("api/watch/updateScreenStatus")
    Observable<BaseModel> updateScreenStatus(@QueryMap Map<String, String> params);


    /**
     * 设置久坐提醒
     *
     * @param params
     * @return
     */
    @POST("api/watch/updateSedentaryStatus")
    Observable<BaseModel> updateSedentaryStatus(@QueryMap Map<String, String> params);

    /**
     * 心率预警自动求助开关
     *
     * @return
     */
    @POST("/api/watch/updateRateWarnStatus")
    Observable<BaseModel> updateRateWarnStatus(@QueryMap Map<String, String> params);


    /**
     * 视频问医
     *
     * @param params
     * @return
     */
    @POST("api/videoAskDoctor/intoApplet")
    Observable<WechatApplet> intoApplet(@QueryMap Map<String, String> params);


    /**
     * 后台消息列表
     *
     * @param params
     * @return
     */
    @POST("api/systemMsg/getMyMessageList")
    Observable<PushMessage> getMyMessageList(@QueryMap Map<String, String> params);


    /**
     * 一键已读
     *
     * @param params
     * @return
     */
    @POST("api/systemMsg/setRead")
    Observable<BaseModel> setRead(@Body RequestBody params);


    /**
     * 注销账户
     *
     * @param params
     * @return
     */
    @POST("api/account/logout")
    Observable<BaseModel> logout(@QueryMap Map<String, String> params);


    /**
     * @param params
     * @return
     */
    @POST("api/health/getSleepInfo")
    Observable<SleepInfo> getSleepInfo(@QueryMap Map<String, String> params);


    /**
     * 设置睡眠时间
     *
     * @param params
     * @return
     */
    @POST("api/watch/setSleep")
    Observable<BaseModel> setSleep(@QueryMap Map<String, String> params);

    /**
     * 获取健康时钟详情
     *
     * @param params
     * @return
     */
    @POST("api/health/getHealthClock")
    Observable<BaseModel> getHealthClock(@QueryMap Map<String, String> params);


    /**
     * 首页健康时钟
     *
     * @param params
     * @return
     */
    @POST("api/health/getHomePageHealthClock")
    Observable<HomePageHealthClock> getHomePageHealthClock(@QueryMap Map<String, String> params);


    /**
     * 获取手表绑定操作类型
     *
     * @param params
     * @return
     */
    @POST("api/watch/bindOrUnbind/getWatchBindType")
    Observable<BindType> getWatchBindType(@QueryMap Map<String, String> params);


    /**
     * 获取健康档案列表
     *
     * @return
     */
    @POST("/api/record/getHealthRecord")
    Observable<HealthRecordModel> getHealthRecord(@QueryMap Map<String, String> params);

    /**
     * 新获取健康报告
     *
     * @return
     */
    @POST("/api/health/report/getHealthReport")
    Observable<HealthyNewReport> getHealthReport(@QueryMap Map<String, String> params);

    /**
     * 获取中医体质列表
     *
     * @return
     */
    @POST("/api/record/getPhysiqueList")
    Observable<ChineseMedicineBodyModel> getChineseMedicineBody(@QueryMap Map<String, String> params);

    /**
     * 获取用药记录列表
     *
     * @return
     */
    @POST("/api/record/getMedicationRecordList")
    Observable<MedicationRecordModel> getMedicationRecord(@QueryMap Map<String, String> params);

    /**
     * 新增用药记录
     *
     * @return
     */
    @POST("/api/record/saveMedicationRecord")
    Observable<BaseModel> addMedicationRecord(@Body RequestBody params);

    /**
     * 获取用药记录详情
     *
     * @return
     */
    @POST("/api/record/getMedicationRecordDetail")
    Observable<MedicationRecordDetailModel> getMedicationRecordDetail(@QueryMap Map<String, String> params);

    /**
     * 获取我的卡包
     *
     * @return
     */
    @POST("/api/cardPackage/getCardPackage")
    Observable<CardPackageModel> getCardPackage(@QueryMap Map<String, String> params);

    /**
     * 检查首页弹窗
     *
     * @return
     */
    @POST("/api/systemMsg/pelletFrame")
    Observable<PelletFrameModel> pelletFrame(@QueryMap Map<String, String> params);

    /**
     * 共享成员，通过手机号查询用户信息
     *
     * @return
     */
    @POST("/api/familyMember/enterInvitePhone")
    Observable<EnterInvitePhoneModel> enterInvitePhone(@QueryMap Map<String, String> params);

    /**
     * 共享成员，通过扫描二维码查询用户信息
     *
     * @return
     */
    @POST("/api/familyMember/scanCode")
    Observable<EnterInvitePhoneModel> scanCode(@QueryMap Map<String, String> params);

    /**
     * 共享成员，修改昵称
     *
     * @return
     */
    @POST("/api/familyMember/updateMemberNickName")
    Observable<BaseModel> updateMemberNickName(@QueryMap Map<String, String> params);

    /**
     * 共享成员，发送邀请验证码
     *
     * @return
     */
    @POST("/api/familyMember/sendSmsCode")
    Observable<BaseModel> sendSmsCode(@QueryMap Map<String, String> params);

    /**
     * 共享成员，通过邀请验证码进行邀请
     *
     * @return
     */
    @POST("/api/familyMember/smsCodeInvite")
    Observable<BaseModel> smsCodeInvite(@QueryMap Map<String, String> params);

    /**
     * 共享成员，通过扫描二维码进行邀请
     *
     * @return
     */
    @POST("/api/familyMember/scanCodeInvite")
    Observable<BaseModel> scanCodeInvite(@QueryMap Map<String, String> params);

    /**
     * 联系客服
     *
     * @return
     */
    @POST("/api/center/contactCustomerService")
    Observable<CustomerServiceModel> customerService();

    /**
     * 判断是否存在紧急联系人及是否有家庭成员
     */
    @POST("/api/center/ifExistEmergencyContact")
    Observable<EmergencyContact> ifExistEmergencyContact(@QueryMap Map<String, String> params);

    /**
     * 获取心电报告
     *
     * @param params
     * @return
     */
    @POST("/api/health/getEcgReport")
    Observable<HealthyEcgReport> getHealthEcgReport(@QueryMap Map<String, String> params);


    /**
     * 获取家庭成员列表
     *
     * @param params
     * @return
     */
    @POST("/api/familyMember/getFamilyMember")
    Observable<FamilyMember> getFamilyMember(@QueryMap Map<String, String> params);


    /**
     * 删除家庭成员
     *
     * @param params
     * @return
     */
    @POST("/api/familyMember/deleteMember")
    Observable<BaseModel> deleteMember(@QueryMap Map<String, String> params);


    /**
     * 成员详情
     *
     * @param params
     * @return
     */
    @POST("/api/familyMember/getMemberDetail")
    Observable<FamilyMemberDetail> getMemberDetail(@QueryMap Map<String, String> params);


    /**
     * 修改成员权限
     *
     * @param params
     * @return
     */
    @POST("api/familyMember/updateAuthority")
    Observable<BaseModel> updateAuthority(@Body RequestBody params);


    /**
     * 获取二维码
     *
     * @param params
     * @return
     */
    @POST("api/familyMember/getQcode")
    Observable<FamilyQRCode> getQcode(@QueryMap Map<String, String> params);


    /**
     * 获取邀请列表
     *
     * @param params
     * @return
     */
    @POST("api/familyMember/getMemberInviteList")
    Observable<FamilyMemberInvite> getMemberInviteList(@QueryMap Map<String, String> params);


    /**
     * 是否同意邀请
     *
     * @param params
     * @return
     */
    @POST("api/familyMember/inviteIfAgree")
    Observable<BaseModel> inviteIfAgree(@QueryMap Map<String, String> params);


    /**
     * 判断是否有家庭成员
     *
     * @param params
     * @return
     */
    @POST("api/familyMember/ifHaveFamilyMember")
    Observable<HaveFamilyMember> ifHaveFamilyMember(@QueryMap Map<String, String> params);


    /**
     * 用户手动上传血糖
     * @param params
     * @return
     */
    @POST("api/health/uploadBloodSugar")
    Observable<BaseModel> uploadBloodSugar(@QueryMap Map<String, String> params);

    /**
     * 用户手动上传血压
     * @param params
     * @return
     */
    @POST("api/health/uploadBloodPressure")
    Observable<BaseModel> uploadBloodPressure(@QueryMap Map<String, String> params);


    /**
     * 运动提醒、睡眠提醒、早餐提醒开关
     * @param params
     * @return
     */
    @POST("api/watch/controlSwitch")
    Observable<BaseModel> controlSwitch(@QueryMap Map<String, String> params);


    /**
     * 设置运动 睡眠 早餐提醒时间
     * @param params
     * @return
     */
    @POST("api/watch/setTime")
    Observable<BaseModel> setTime(@QueryMap Map<String, String> params);

    /**
     * 获取安全围栏设置详情
     * @param params
     * @return
     */
    @POST("api/safetyFence/getSafetyFenceSetting")
    Observable<FenceSetting> getSafetyFenceSetting(@QueryMap Map<String, String> params);


    /**
     * 更新围栏状态
     * @param params
     * @return
     */
    @POST("api/safetyFence/updateSafetyFenceStatus")
    Observable<BaseModel> updateSafetyFenceStatus(@QueryMap Map<String, String> params);

    /**
     * 更新围栏生效时间
     * @param params
     * @return
     */
    @POST("api/safetyFence/updateSafetyFenceTime")
    Observable<BaseModel> updateSafetyFenceTime(@QueryMap Map<String, String> params);

    /**
     * 进入编辑用药记录
     * @param params
     * @return
     */
    @POST("api/record/intoEditMedicationRecord")
    Observable<MedicationRecord> intoEditMedicationRecord(@QueryMap Map<String, String> params);


    /**
     * 删除用药记录
     * @param params
     * @return
     */
    @POST("api/record/deleteMedicationRecord")
    Observable<BaseModel> deleteMedicationRecord(@QueryMap Map<String, String> params);


    /**
     * 更新用药记录
     * @param params
     * @return
     */
    @POST("api/record/updateMedicationRecord")
    Observable<BaseModel> updateMedicationRecord(@Body RequestBody params);


    /**
     * 病历记录列表
     * @param params
     * @return
     */
    @POST("api/record/getDiseaseRecordList")
    Observable<DiseaseRecord> getDiseaseRecordList(@QueryMap Map<String, String> params);


    /**
     * 进入编辑或查看病历记录
     * @param params
     * @return
     */
    @POST("api/record/getDiseaseRecordDetail")
    Observable<DiseaseRecordDetail> getDiseaseRecordDetail(@QueryMap Map<String, String> params);

    /**
     * 新增病历
     * @param params
     * @return
     */
    @POST("api/record/saveDiseaseRecord")
    Observable<BaseModel> saveDiseaseRecord(@Body RequestBody params);


    /**
     * 更新病历记录
     * @param params
     * @return
     */
    @POST("api/record/udpateDiseaseRecord")
    Observable<BaseModel> udpateDiseaseRecord(@Body RequestBody params);

    /**
     * 删除病历
     * @param params
     * @return
     */
    @POST("api/record/deleteDiseaseRecord")
    Observable<BaseModel> deleteDiseaseRecord(@QueryMap Map<String, String> params);


    /**
     * 是否已经有分析报告
     * @param params
     * @return
     */
    @POST("api/health/report/isHaveReport")
    Observable<BaseModel<Boolean>> isHaveReport(@QueryMap Map<String, String> params);


    /**
     * 获取设置安全围栏范围详情
     * @param params
     * @return
     */
    @POST("api/safetyFence/getSafetyFenceScope")
    Observable<FenceScope> getSafetyFenceScope(@QueryMap Map<String, String> params);


    /**
     * 更新安全围栏范围
     * @param params
     * @return
     */
    @POST("api/safetyFence/updateSafetyFenceScope")
    Observable<BaseModel> updateSafetyFenceScope(@QueryMap Map<String, String> params);


    /**
     * 重新定位
     * @param params
     * @return
     */
    @POST("api/safetyFence/relocate")
    Observable<ReLocate> relocate(@QueryMap Map<String, String> params);


    /**
     * 更新安全围栏提醒人
     * @param params
     * @return
     */
    @POST("api/safetyFence/updateSafetyFenceRemindPeople")
    Observable<BaseModel> updateSafetyFenceRemindPeople(@Body RequestBody params);

}

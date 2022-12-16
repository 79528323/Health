package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.AddressModels;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.OssModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.FileUtils;
import com.gzhealthy.health.tool.GetJsonDataUtil;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.StringUtils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.GlideEngine;
import com.gzhealthy.health.widget.BottomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Func1;

/**
 * 个人资料
 */
public class PersonInofActivity extends BaseAct {

    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.rl_nick_name)
    RelativeLayout rlNickName;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.rl_user_phone)
    RelativeLayout rlUserPhone;
    @BindView(R.id.rl_user_birday)
    RelativeLayout rlUserBirday;
    //    @BindView(R.id.rl_user_sex)
//    RelativeLayout rlUserSex;
//    @BindView(R.id.rl_email)
//    RelativeLayout rlEmail;
//    @BindView(R.id.rl_city)
//    RelativeLayout rlCity;
//    @BindView(R.id.rl_detail_place)
//    RelativeLayout rlDetailPlace;
//    @BindView(R.id.rl_marryed)
//    RelativeLayout rlMarryed;
//    @BindView(R.id.lr_education)
//    RelativeLayout lrEducation;
    @BindView(R.id.img_userhead)
    ImageView imgUserhead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    //    @BindView(R.id.tv_sex)
//    TextView tvSex;
//    @BindView(R.id.tv_email)
//    TextView tvEmail;
//    @BindView(R.id.tv_city)
//    TextView tvCity;
//    @BindView(R.id.tv_detail_palce)
//    TextView tvDetailPalce;
//    @BindView(R.id.tv_marrayed)
//    TextView tvMarrayed;
//    @BindView(R.id.tv_education)
//    TextView tvEducation;
    @BindView(R.id.tv_real_name)
    TextView tvRealName;
    @BindView(R.id.rl_realname)
    RelativeLayout rlRealname;

    @BindView(R.id.iv_verified)
    ImageView iv_verified;

    //地址信息
    private List<AddressModels.DataBean> datainfo;
    private OptionsPickerView pvOptions;
    private TimePickerView pvTime;
    private BottomDialog sexbottondialog;
    private BottomDialog marrayedbottondialog;
    private BottomDialog educationbottondialog;
    private HashMap<String, String> hashMap;
    private String headPicStr;
    private UserInfo userInfo2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("个人资料");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(PersonInofActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getJson();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-个人资料");
        getpersonInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-个人资料");
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, PersonInofActivity.class);
        context.startActivity(intent);
    }

    private ArrayList<String> firstitem;
    private ArrayList<ArrayList<String>> seconditem;
    private ArrayList<ArrayList<ArrayList<String>>> thirditem;

    /**
     * 解析地区JSON数据
     */
    public void getJson() {
        //获取assets目录下的json文件数据
        String JsonData = new GetJsonDataUtil().getJson(PersonInofActivity.this, "citys.json");
        Gson gson = new Gson();
        AddressModels addressData = gson.fromJson(JsonData, new TypeToken<AddressModels>() {
        }.getType());
        datainfo = addressData.getData();
        //省级别
        firstitem = new ArrayList<>();
        //市级别
        seconditem = new ArrayList<>();
        //区级别
        thirditem = new ArrayList<>();
        for (int i = 0; i < datainfo.size(); i++) {
            ArrayList<String> firstitem_1 = new ArrayList<>();
            ArrayList<ArrayList<String>> seconditem_1 = new ArrayList<>();
            for (int j = 0; j < datainfo.get(i).getChildren().size(); j++) {
                firstitem_1.add(datainfo.get(i).getChildren().get(j).getFullName());
                ArrayList<String> firstitem_2 = new ArrayList<>();
                for (int k = 0; k < datainfo.get(i).getChildren().get(j).getChildren().size(); k++) {
                    //加入区级别
                    firstitem_2.add(datainfo.get(i).getChildren().get(j).getChildren().get(k).getFullName());
                }
                //加入市级别
                seconditem_1.add(firstitem_2);
            }
            //加入省级别
            firstitem.add(datainfo.get(i).getFullName());
            seconditem.add(firstitem_1);
            thirditem.add(seconditem_1);
        }
    }

    /**
     * 时间选择框
     */
    private void initTimePicker() {
        if (pvTime == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.set(1920, 1, 1);
            Calendar endDate = Calendar.getInstance();
            Time t = new Time("GMT+8");
            t.setToNow();
            endDate.set(t.year, t.month, t.monthDay);
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    hashMap = new HashMap<>();
                    hashMap.put("birthday", "" + dateFormat.format(date));
                    upadatePersonInfo(hashMap);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCenterLabel(false)
                    .setDividerColor(Color.parseColor("#FFeeeeee"))
                    .setContentSize(21)
                    .setTitleText("选择出生日期")
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setDecorView(null)
                    .setSubmitColor(Color.parseColor("#FFf39700"))
                    .setCancelColor(Color.parseColor("#FFf39700"))
                    .build();
        }
        pvTime.show();
    }

    /**
     * 地区选择框
     */
    private void showPickerView() {
        if (pvOptions == null) {
            pvOptions = new OptionsPickerView.Builder(PersonInofActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = firstitem.get(options1) + seconditem.get(options1).get(options2) + thirditem.get(options1).get(options2).get(options3);
                    hashMap = new HashMap<>();
                    hashMap.put("province", "" + datainfo.get(options1).getCode());
                    hashMap.put("city", "" + datainfo.get(options1).getChildren().get(options2).getCode());
                    hashMap.put("county", "" + datainfo.get(options1).getChildren().get(options2).getChildren().get(options3).getCode());
                    upadatePersonInfo(hashMap);
                }
            }).setTitleText("选择所在区域")
                    .setLineSpacingMultiplier(2.0f)
                    .setCancelText("取消")
                    .setCancelColor(Color.parseColor("#FF22D393"))
                    .setSubmitText("完成")
                    .setSubmitColor(Color.parseColor("#FF22D393"))
                    .setDividerColor(Color.parseColor("#ffeeeeee"))
                    .setContentTextSize(18)
                    .setTextColorCenter(Color.parseColor("#ff202020"))
                    .setTextXOffset(12, 0, -12)
                    .build();
            //三级选择器
            pvOptions.setPicker(firstitem, seconditem, thirditem);
        }
        pvOptions.show();
    }

    /**
     * 性别选择
     */
    private void setSelectSexView() {
        if (sexbottondialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_select_sex_layout, null);
            TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
            TextView tv_man = (TextView) view.findViewById(R.id.tv_man);
            TextView tv_woman = (TextView) view.findViewById(R.id.tv_woman);
            tv_cancle.setOnClickListener(this);
            tv_man.setOnClickListener(this);
            tv_woman.setOnClickListener(this);
            sexbottondialog = new BottomDialog.Builder(PersonInofActivity.this).setCustomView(view).show();
        } else {
            sexbottondialog.show();
        }
    }

    /**
     * 婚姻状况选择
     */
    private void setSelectMerrayedView() {
        if (marrayedbottondialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_select_marrayed_layout, null);
            TextView tv_no_marrayed = (TextView) view.findViewById(R.id.tv_no_marrayed);
            TextView tv_marrayed = (TextView) view.findViewById(R.id.tv_marrayed);
            TextView tv_other = (TextView) view.findViewById(R.id.tv_mother);
            TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
            tv_cancle.setOnClickListener(this);
            tv_marrayed.setOnClickListener(this);
            tv_other.setOnClickListener(this);
            tv_no_marrayed.setOnClickListener(this);
            marrayedbottondialog = new BottomDialog.Builder(PersonInofActivity.this).setCustomView(view).show();
        } else {
            marrayedbottondialog.show();
        }
    }

    /**
     * 学历选择
     */
    private void setSelectEducationView() {
        if (educationbottondialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_select_education_layout, null);
            view.findViewById(R.id.tv_highschool).setOnClickListener(this);
            view.findViewById(R.id.tv_special_education).setOnClickListener(this);
            view.findViewById(R.id.tv_college).setOnClickListener(this);
            view.findViewById(R.id.tv_master).setOnClickListener(this);
            view.findViewById(R.id.tv_doctor).setOnClickListener(this);
            view.findViewById(R.id.tv_others).setOnClickListener(this);
            view.findViewById(R.id.tv_cancle).setOnClickListener(this);
            educationbottondialog = new BottomDialog.Builder(PersonInofActivity.this).setCustomView(view).show();
        } else {
            educationbottondialog.show();
        }

    }

    @OnClick({R.id.rl_head, R.id.rl_nick_name, R.id.rl_user_name, R.id.rl_name, R.id.rl_user_phone, R.id.rl_user_birday,
            /*R.id.rl_user_sex, R.id.rl_email, R.id.rl_city, R.id.rl_detail_place, R.id.rl_marryed, R.id.lr_education,*/ R.id.rl_realname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_realname:
                RealNameCheckActivity.instance(PersonInofActivity.this);
                break;
            case R.id.rl_head:
                checkPermission();
                break;
            case R.id.rl_nick_name:
                UserNickNameChangeActivity.instance(PersonInofActivity.this, UserNickNameChangeActivity.NICKNAME, tvNickname.getText().toString());
                break;
            case R.id.rl_user_name:
                break;
            case R.id.rl_name:
//                MessageItemChangeActivity.instance(PersonInofActivity.this, MessageItemChangeActivity.NAMENAME, tvName.getText().toString().trim());
                break;
            case R.id.rl_user_phone:
//                ChangePhoneActivity.newinstance(PersonInofActivity.this, false, userInfo2.getData().getPhone());
                break;
            case R.id.rl_user_birday:
                initTimePicker();
                break;
//            case R.id.rl_user_sex:
//                setSelectSexView();
//                break;
//            case R.id.rl_email:
//                MessageItemChangeActivity.instance(PersonInofActivity.this, MessageItemChangeActivity.EMAIL, tvEmail.getText().toString().trim());
//                break;
//            case R.id.rl_city:
//                showPickerView();
//                break;
//            case R.id.rl_detail_place:
//                MessageItemChangeActivity.instance(PersonInofActivity.this, MessageItemChangeActivity.ADDRESSDETAIL, tvDetailPalce.getText().toString());
//                break;
//            case R.id.rl_marryed:
//                setSelectMerrayedView();
//                break;
//            case R.id.lr_education:
//                setSelectEducationView();
//                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_cancle:
                if (sexbottondialog != null) {
                    sexbottondialog.dismiss();
                }
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                if (marrayedbottondialog != null) {
                    marrayedbottondialog.dismiss();
                }
                break;
            case R.id.tv_man:
                hashMap = new HashMap<>();
                hashMap.put("sex", "1");
                upadatePersonInfo(hashMap);
                if (sexbottondialog != null) {
                    sexbottondialog.dismiss();
                }
                break;
            case R.id.tv_woman:
                hashMap = new HashMap<>();
                hashMap.put("sex", "2");
                upadatePersonInfo(hashMap);
                if (sexbottondialog != null) {
                    sexbottondialog.dismiss();
                }
                break;

            case R.id.tv_highschool:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "1");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;
            case R.id.tv_special_education:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "2");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;
            case R.id.tv_college:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "3");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;
            case R.id.tv_master:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "4");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;
            case R.id.tv_doctor:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "5");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;
            case R.id.tv_others:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "6");
                upadatePersonInfo(hashMap);
                if (educationbottondialog != null) {
                    educationbottondialog.dismiss();
                }
                break;

            case R.id.tv_no_marrayed:
                hashMap = new HashMap<>();
                hashMap.put("marriage", "2");
                upadatePersonInfo(hashMap);
                if (marrayedbottondialog != null) {
                    marrayedbottondialog.dismiss();
                }
                break;
            case R.id.tv_marrayed:
                hashMap = new HashMap<>();
                hashMap.put("marriage", "1");
                upadatePersonInfo(hashMap);
                if (marrayedbottondialog != null) {
                    marrayedbottondialog.dismiss();
                }
                break;
            case R.id.tv_mother:
                hashMap = new HashMap<>();
                hashMap.put("educationalLevel", "6");
                upadatePersonInfo(hashMap);
                if (marrayedbottondialog != null) {
                    marrayedbottondialog.dismiss();
                }
                break;
        }
    }

    /**
     * 发送获取个人信息
     */
    public void getpersonInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getAccountInfo(hashMap), new CallBack<UserInfo>() {
            @Override
            public void onResponse(UserInfo data) {
                updateUserInfo(data);
            }
        });
    }

    /**
     * 更新个人信息
     *
     * @param userInfo
     */
    public void updateUserInfo(UserInfo userInfo) {
        this.userInfo2 = userInfo;
        switchSex(userInfo.getData().getSex());
        switchEducation(userInfo.getData().getEducationalLevel());
        switchMarrayed(userInfo.getData().getMarriage());
        setdata(tvUsername, userInfo.getData().getLoginName());
        setdata(tvName, userInfo.getData().getRealName());
        setdata(tvPhone, StringUtils.getTelEncrypt(userInfo.getData().getPhone()));
//        setdata(tvEmail, userInfo.getData().getEmail());
        setdata(tvNickname, userInfo.getData().getNickName());
        setdata(tvBirthday, userInfo.getData().getBirthday());
        setdata(imgUserhead, Constants.ImageResource.OSSHEAD + userInfo.getData().getHeadPic());
//        setdata(tvDetailPalce, userInfo.getData().getDetailedAddress());

        String provinceName = TextUtils.isEmpty(userInfo.getData().getProvinceName()) ? "" : userInfo.getData().getProvinceName() + "-";
        String cityName = TextUtils.isEmpty(userInfo.getData().getCityName()) ? "" : userInfo.getData().getCityName() + "-";
        String countyName = TextUtils.isEmpty(userInfo.getData().getCountyName()) ? "" : userInfo.getData().getCountyName();
//        setdata(tvCity, provinceName + cityName + countyName);
        if (userInfo.getData().getRealNameStatus().equals("1")) {
            setdata(tvRealName, "未认证");
            iv_verified.setVisibility(View.VISIBLE);
            rlRealname.setEnabled(true);
        } else if (userInfo.getData().getRealNameStatus().equals("2")) {
            setdata(tvRealName, "认证成功");
            iv_verified.setVisibility(View.GONE);
            rlRealname.setEnabled(false);
        } else {
            setdata(tvRealName, "认证失败");
            iv_verified.setVisibility(View.VISIBLE);
            rlRealname.setEnabled(true);
        }

    }

    /**
     * 设置性别
     *
     * @param sexid
     */
    public void switchSex(int sexid) {
//        switch (sexid) {
//            case 1:
//                tvSex.setText("男");
//                break;
//            case 2:
//                tvSex.setText("女");
//                break;
//            case 3:
//                tvSex.setText("其他");
//                break;
//            default:
//                tvSex.setText("");
//        }
    }

    /**
     * 筛选个人信息
     *
     * @param education
     */
    public void switchEducation(String education) {
//        if (!TextUtils.isEmpty(education)) {
//            switch (education) {
//                case "1":
//                    tvEducation.setText("高中");
//                    break;
//                case "2":
//                    tvEducation.setText("专科");
//                    break;
//                case "3":
//                    tvEducation.setText("本科");
//                    break;
//                case "4":
//                    tvEducation.setText("硕士");
//                    break;
//                case "5":
//                    tvEducation.setText("博士");
//                    break;
//                case "6":
//                    tvEducation.setText("其他");
//                    break;
//                default:
//                    tvEducation.setText("");
//            }
//        }

    }

    /**
     * 筛选是否已婚
     *
     * @param marrayed
     */
    public void switchMarrayed(String marrayed) {
//        if (!TextUtils.isEmpty(marrayed)) {
//            switch (marrayed) {
//                case "1":
//                    tvMarrayed.setText("已婚");
//                    break;
//                case "2":
//                    tvMarrayed.setText("未婚");
//                    break;
//                default:
//                    tvMarrayed.setText("");
//            }
//        }
    }

    /**
     * 更新个人信息
     */
    public void upadatePersonInfo(HashMap<String, String> hashMap) {
//        hashMap.put("token", SPCache.getString("token", ""));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(hashMap));

        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().editAccountInfo(body, SPCache.getString("token", "")),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
                            getpersonInfo();
//                            String headerPath = Constants.ImageResource.OSSHEAD+headPicStr;
                            RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_HOME_USER_HEADER_IMAGE);
                        }
                    }
                });
    }

    public <T1> void setdata(T1 view, String object) {
        if (view != null && object != null) {
            if (view instanceof TextView) {
                if (!TextUtils.isEmpty(object.toString())) {
                    ((TextView) view).setText(object.toString());
                }
            }
            if (view instanceof ImageView) {
//                GlideUtils.loadCustomeImg(((ImageView) view), object,R.mipmap.com_head);
                Glide.with(PersonInofActivity.this).load(object.toString().trim()).centerCrop().crossFade().into((ImageView) view);
//                GlideUtils.loadCustomeImg(((ImageView) view),object);
            }
        }
    }


    //------------OSS start------------>
    public void getOssMsg(final File file) {
        Map<String, String> params = new HashMap<>();
        params.put("sign", Md5Utils.encryptH("gzhealthy"));
//        params.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getKey(params).map(new Func1<OssModel, OssModel>() {
                    @Override
                    public OssModel call(OssModel ossModel) {
                        return ossModel;
                    }
                })
                , new CallBack<OssModel>() {
                    @Override
                    public void onResponse(OssModel data) {
                        //获取权限成功，开始上传
                        sendUpLoadFile(file, data.getData().getAccessKeyId(), data.getData().getAccessKeySecret(),
                                data.getData().getSecurityToken(), data.getData().getDomain(), data.getData().getBucket());
                    }
                });
    }

    private void sendUpLoadFile(final File file, String accessKeyId, String accessKeySecret, String stsToken, String endpoint, final String bucket) {
        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, stsToken);
        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求书，默认5个
        conf.setMaxConcurrentRequest(5);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        final OSS oss = new OSSClient(aty, endpoint, credentialProvider, conf);
        String fileName = (int) ((Math.random() * 6 + 1) * 100000) + ".png";
        sendUpFileRequest(oss, bucket, FileUtils.createContent(fileName), file);
    }

    private Handler headHandle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                hashMap = new HashMap<>();
                hashMap.put("headPic", "" + headPicStr);
                upadatePersonInfo(hashMap);
            }
            return false;
        }

    });

    private boolean sendUpFileRequest(OSS oss, String bucket, final String fileName, File file) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, fileName, file.getPath());
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logger.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                try {
                    headPicStr = request.getObjectKey();
                    headHandle.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        if (task == null || !task.isCompleted()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查相关权限
     */
    private void checkPermission() {
        PermissionUtils.permissionGroup(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        openPictureSelector();
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
    }

    /**
     * 打开图片选择器
     */
    private void openPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isCompress(true)
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .withAspectRatio(1, 1)//裁剪比例
                .isEnableCrop(true)//是否开启裁剪
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener() {


                    @Override
                    public void onResult(List result) {
                        String path = ((LocalMedia) result.get(0)).getCompressPath();
                        getOssMsg(new File(path));
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


}

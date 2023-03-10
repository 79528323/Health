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
 * ????????????
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

    //????????????
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
        setTitle("????????????");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(PersonInofActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getJson();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("??????-????????????");
        getpersonInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("??????-????????????");
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, PersonInofActivity.class);
        context.startActivity(intent);
    }

    private ArrayList<String> firstitem;
    private ArrayList<ArrayList<String>> seconditem;
    private ArrayList<ArrayList<ArrayList<String>>> thirditem;

    /**
     * ????????????JSON??????
     */
    public void getJson() {
        //??????assets????????????json????????????
        String JsonData = new GetJsonDataUtil().getJson(PersonInofActivity.this, "citys.json");
        Gson gson = new Gson();
        AddressModels addressData = gson.fromJson(JsonData, new TypeToken<AddressModels>() {
        }.getType());
        datainfo = addressData.getData();
        //?????????
        firstitem = new ArrayList<>();
        //?????????
        seconditem = new ArrayList<>();
        //?????????
        thirditem = new ArrayList<>();
        for (int i = 0; i < datainfo.size(); i++) {
            ArrayList<String> firstitem_1 = new ArrayList<>();
            ArrayList<ArrayList<String>> seconditem_1 = new ArrayList<>();
            for (int j = 0; j < datainfo.get(i).getChildren().size(); j++) {
                firstitem_1.add(datainfo.get(i).getChildren().get(j).getFullName());
                ArrayList<String> firstitem_2 = new ArrayList<>();
                for (int k = 0; k < datainfo.get(i).getChildren().get(j).getChildren().size(); k++) {
                    //???????????????
                    firstitem_2.add(datainfo.get(i).getChildren().get(j).getChildren().get(k).getFullName());
                }
                //???????????????
                seconditem_1.add(firstitem_2);
            }
            //???????????????
            firstitem.add(datainfo.get(i).getFullName());
            seconditem.add(firstitem_1);
            thirditem.add(seconditem_1);
        }
    }

    /**
     * ???????????????
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
                    .setLabel("???", "???", "???", "???", "???", "???")
                    .isCenterLabel(false)
                    .setDividerColor(Color.parseColor("#FFeeeeee"))
                    .setContentSize(21)
                    .setTitleText("??????????????????")
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
     * ???????????????
     */
    private void showPickerView() {
        if (pvOptions == null) {
            pvOptions = new OptionsPickerView.Builder(PersonInofActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //?????????????????????????????????????????????
                    String tx = firstitem.get(options1) + seconditem.get(options1).get(options2) + thirditem.get(options1).get(options2).get(options3);
                    hashMap = new HashMap<>();
                    hashMap.put("province", "" + datainfo.get(options1).getCode());
                    hashMap.put("city", "" + datainfo.get(options1).getChildren().get(options2).getCode());
                    hashMap.put("county", "" + datainfo.get(options1).getChildren().get(options2).getChildren().get(options3).getCode());
                    upadatePersonInfo(hashMap);
                }
            }).setTitleText("??????????????????")
                    .setLineSpacingMultiplier(2.0f)
                    .setCancelText("??????")
                    .setCancelColor(Color.parseColor("#FF22D393"))
                    .setSubmitText("??????")
                    .setSubmitColor(Color.parseColor("#FF22D393"))
                    .setDividerColor(Color.parseColor("#ffeeeeee"))
                    .setContentTextSize(18)
                    .setTextColorCenter(Color.parseColor("#ff202020"))
                    .setTextXOffset(12, 0, -12)
                    .build();
            //???????????????
            pvOptions.setPicker(firstitem, seconditem, thirditem);
        }
        pvOptions.show();
    }

    /**
     * ????????????
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
     * ??????????????????
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
     * ????????????
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
     * ????????????????????????
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
     * ??????????????????
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
            setdata(tvRealName, "?????????");
            iv_verified.setVisibility(View.VISIBLE);
            rlRealname.setEnabled(true);
        } else if (userInfo.getData().getRealNameStatus().equals("2")) {
            setdata(tvRealName, "????????????");
            iv_verified.setVisibility(View.GONE);
            rlRealname.setEnabled(false);
        } else {
            setdata(tvRealName, "????????????");
            iv_verified.setVisibility(View.VISIBLE);
            rlRealname.setEnabled(true);
        }

    }

    /**
     * ????????????
     *
     * @param sexid
     */
    public void switchSex(int sexid) {
//        switch (sexid) {
//            case 1:
//                tvSex.setText("???");
//                break;
//            case 2:
//                tvSex.setText("???");
//                break;
//            case 3:
//                tvSex.setText("??????");
//                break;
//            default:
//                tvSex.setText("");
//        }
    }

    /**
     * ??????????????????
     *
     * @param education
     */
    public void switchEducation(String education) {
//        if (!TextUtils.isEmpty(education)) {
//            switch (education) {
//                case "1":
//                    tvEducation.setText("??????");
//                    break;
//                case "2":
//                    tvEducation.setText("??????");
//                    break;
//                case "3":
//                    tvEducation.setText("??????");
//                    break;
//                case "4":
//                    tvEducation.setText("??????");
//                    break;
//                case "5":
//                    tvEducation.setText("??????");
//                    break;
//                case "6":
//                    tvEducation.setText("??????");
//                    break;
//                default:
//                    tvEducation.setText("");
//            }
//        }

    }

    /**
     * ??????????????????
     *
     * @param marrayed
     */
    public void switchMarrayed(String marrayed) {
//        if (!TextUtils.isEmpty(marrayed)) {
//            switch (marrayed) {
//                case "1":
//                    tvMarrayed.setText("??????");
//                    break;
//                case "2":
//                    tvMarrayed.setText("??????");
//                    break;
//                default:
//                    tvMarrayed.setText("");
//            }
//        }
    }

    /**
     * ??????????????????
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
                        //?????????????????????????????????
                        sendUpLoadFile(file, data.getData().getAccessKeyId(), data.getData().getAccessKeySecret(),
                                data.getData().getSecurityToken(), data.getData().getDomain(), data.getData().getBucket());
                    }
                });
    }

    private void sendUpLoadFile(final File file, String accessKeyId, String accessKeySecret, String stsToken, String endpoint, final String bucket) {
        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, stsToken);
        ClientConfiguration conf = new ClientConfiguration();
        // ?????????????????????15???
        conf.setConnectionTimeout(15 * 1000);
        // socket???????????????15???
        conf.setSocketTimeout(15 * 1000);
        // ??????????????????????????????5???
        conf.setMaxConcurrentRequest(5);
        // ????????????????????????????????????2???
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
        // ??????????????????
        PutObjectRequest put = new PutObjectRequest(bucket, fileName, file.getPath());
        // ???????????????????????????????????????
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
                // ????????????
                if (clientExcepion != null) {
                    // ??????????????????????????????
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // ????????????
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
     * ??????????????????
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
     * ?????????????????????
     */
    private void openPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isCompress(true)
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)
                .withAspectRatio(1, 1)//????????????
                .isEnableCrop(true)//??????????????????
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

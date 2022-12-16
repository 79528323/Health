package com.gzhealthy.health.fragment;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.AccountSafeActivity;
import com.gzhealthy.health.activity.AppSettingActivity;
import com.gzhealthy.health.activity.CustomerServiceActivity;
import com.gzhealthy.health.activity.FeedBackDetailActivity;
import com.gzhealthy.health.activity.HealthArchivesActivity;
import com.gzhealthy.health.activity.InsertSosContactsActivity;
import com.gzhealthy.health.activity.MyCardPackageActivity;
import com.gzhealthy.health.activity.MyCollectActivity;
import com.gzhealthy.health.activity.MyFamilyInviteActivity;
import com.gzhealthy.health.activity.MyFamilyPersonActivity;
import com.gzhealthy.health.activity.MyMessageActivity;
import com.gzhealthy.health.activity.TestActivity;
import com.gzhealthy.health.activity.WatchBindOperationActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.activity.account.PersonInofActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.HaveFamilyMember;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Tools;
import com.gzhealthy.health.tool.Utils;
import com.gzhealthy.health.utils.ToastUtil;
import com.yxp.permission.util.lib.PermissionInfo;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionOriginResultCallBack;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;

import static org.litepal.crud.DataSupport.findFirst;

public class MineFragment extends BaseFra {

    //    private ImageView img_settings;
//    private RelativeLayout rl_set_bluetooth;
    private RelativeLayout rl_set_view;
    private RelativeLayout rl_set_contract;
    private LinearLayout my_device;
    private LinearLayout tl_user_info;
    private TextView tv_user_name;
    private ImageView user_pic;
    private LinearLayout llVipHint;
    private TextView tvVipHint;
    private RelativeLayout ll_collection, ll_my_message;
    private LinearLayout add_family_view;
    private LinearLayout ll_my_scores;
    private String header;
    private TextView tv_version;
    TextView tv_user_id;
    UserInfo.DataBean userInfo;

    TextView tv_no_read;

    @Override
    protected void init(Bundle savedInstanceState) {
//        img_settings = (ImageView) $(R.id.img_settings);
//        img_settings.setOnClickListener(this);
        rl_set_view = (RelativeLayout) $(R.id.rl_set_view);
        rl_set_view.setOnClickListener(this);
        rl_set_contract = (RelativeLayout) $(R.id.rl_set_contract);
        rl_set_contract.setOnClickListener(this);
        tv_user_name = (TextView) $(R.id.tv_user_name);
//        rl_set_bluetooth = (RelativeLayout)$(R.id.rl_set_bluetooth);
//        rl_set_bluetooth.setOnClickListener(this);
        user_pic = (ImageView) $(R.id.user_icon);
        tvVipHint = (TextView) $(R.id.tvVipHint);
        llVipHint = (LinearLayout) $(R.id.llVipHint);
        llVipHint.setOnClickListener(this);
        ll_collection = (RelativeLayout) $(R.id.ll_collection);
        ll_collection.setOnClickListener(this);
        ll_my_scores = (LinearLayout) $(R.id.ll_my_scores);
        ll_my_scores.setOnClickListener(this);
        my_device = (LinearLayout) $(R.id.my_device);
        my_device.setOnClickListener(this);
        ll_my_message = (RelativeLayout) $(R.id.ll_my_message);
        ll_my_message.setOnClickListener(this);
        tv_no_read = (TextView) $(R.id.no_read);
        $(R.id.rl_set_archives).setOnClickListener(this);
        $(R.id.rl_safe).setOnClickListener(this);
        $(R.id.rl_feedback).setOnClickListener(this);
        $(R.id.rl_coustom).setOnClickListener(this);
        $(R.id.rl_my_card).setOnClickListener(this);
        $(R.id.rl_set_family).setOnClickListener(this);

        tv_user_id = (TextView) $(R.id.tv_user_id);
        tl_user_info = (LinearLayout) $(R.id.tl_user_info);
        tl_user_info.setOnClickListener(v -> {
            if (isUserLogin()) {
                gotoUserInfo();
            } else {
                gotoLogin();
            }
        });

        String version = Tools.getVersion(getActivity());
        tv_version = (TextView) $(R.id.tv_version);
        String bg_ver = SPCache.getString(SPCache.KEY_APP_VER, "");
        tv_version.setText((bg_ver.equals(version) ? "V" + version + "已是最新" : "更新版本"));

        rxManager.onRxEvent(RxEvent.REFRESH_BACKGROUND_MESSAGE_BDAGE)
                .subscribe(o -> {
                    setUnReadNumber();
                });

        setUnReadNumber();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void widgetClick(View view) {
//        if (!SPCache.getBoolean("islogin", false)) {
////            ToastUtil.showToast("请先登录哦~~");
//            LoginActivity.instance(getActivity(),2);
//            return;
//        }
        switch (view.getId()) {
//            case R.id.rl_set_bluetooth:
//                PermissionUtil.getInstance().request(new String[]{
//                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
//                        , new PermissionOriginResultCallBack() {
//                            @Override
//                            public void onResult(List<PermissionInfo> list, List<PermissionInfo> list1, List<PermissionInfo> list2) {
//                                TestActivity.instance(getActivity());
//                            }
//                        });
//
//
//                break;
            //vip
            case R.id.llVipHint:
                WebViewActivity.startLoadLink(getContext(), WebViewActivity.splicingToken(Constants.LINK_MEMBER_CENT), "");
                break;
            //健康档案
            case R.id.rl_set_archives:
                Utils.gotoActy(getActivity(), HealthArchivesActivity.class);
                break;
            //客服
            case R.id.rl_coustom:
                CustomerServiceActivity.instance(getActivity());
                break;
            // 系统
            case R.id.rl_set_view:
                AppSettingActivity.instance(getActivity());
                break;
            //我的卡包
            case R.id.rl_my_card:
                Utils.gotoActy(getActivity(), MyCardPackageActivity.class);
                break;

            // 账户安全
            case R.id.rl_safe:
                Utils.gotoActy(getActivity(), AccountSafeActivity.class);
                break;

            // 意见反馈
            case R.id.rl_feedback:
                Utils.gotoActy(getActivity(), FeedBackDetailActivity.class);
                break;

            case R.id.rl_set_contract:
                //sos紧急联络人
                Utils.gotoActy(getActivity(), InsertSosContactsActivity.class);
                break;

            // 我的收藏
            case R.id.ll_collection:
                Utils.gotoActy(getActivity(), MyCollectActivity.class);
                break;

            case R.id.my_device:
                Utils.gotoActy(getActivity(), WatchBindOperationActivity.class);
                break;

            case R.id.ll_my_message:
                Utils.gotoActy(getActivity(), MyMessageActivity.class);
                break;

            case R.id.rl_set_family:
                String token = SPCache.getString(SPCache.KEY_TOKEN, "");
                if (TextUtils.isEmpty(token)) {
                    LoginActivity.instance(getActivity());
                } else
                    ishaveFamilyMember();
                break;

            default:
                break;
        }
    }


    public void setUnReadNumber() {
        if (tv_no_read != null) {
            if (isUserLogin()) {
                getUnReadMessage();
            } else {
                tv_no_read.setText("");
            }
        }

    }

    /**
     * 进入个人信息页面
     */
    public void gotoUserInfo() {
        if (isUserLogin()) {
            PersonInofActivity.instance(getActivity());
        } else {
            ToastUtil.showToast("请先登录哦~~");
        }
    }

    /**
     * 登录操作
     */
    public void gotoLogin() {
        LoginActivity.instance(getActivity());
    }

    @Override
    public void onResume() {
        if (!TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN, ""))) {
            getPersonInfo();
        } else {
            tv_user_id.setVisibility(View.GONE);
            tv_user_name.setText("登录账号");
            user_pic.setImageResource(R.mipmap.ic_details_map_bg);
            tvVipHint.setText("未开通");
            tvVipHint.setSelected(true);
        }
//        setUnReadNumber();
        super.onResume();
    }


    /**
     * 获取个人信息
     */
    public void getPersonInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getAccountInfo(hashMap),
                new CallBack<UserInfo>() {
                    @Override
                    public void onResponse(UserInfo data) {
                        if (data.getCode() == 10086) {
                            LoginActivity.instance(getActivity());
                            SPCache.putString(SPCache.KEY_TOKEN, "");
                            return;
                        }
                        header = (Constants.ImageResource.OSSHEAD + data.getData().getHeadPic()).toString().trim();
                        GlideUtils.CircleImage(getActivity(), header, user_pic);

                        tv_user_id.setText("ID:" + data.getData().getLoginName());
                        if (null != findFirst(UserInfo.DataBean.class)) {
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                        }
                        userInfo = new UserInfo.DataBean();
                        userInfo = data.getData();
                        userInfo.save();
                        tv_user_name.setText(TextUtils.isEmpty(userInfo.getNickName()) ? userInfo.getWechatNickName()
                                : userInfo.getNickName());
                        tv_user_id.setVisibility(View.VISIBLE);
                        tv_user_id.setText("ID:" + userInfo.getLoginName());

                        switch (userInfo.getVipStatus()) {
                            case 0:
                                tvVipHint.setText("未开通");
                                tvVipHint.setSelected(true);
                                break;
                            case 1:
                                tvVipHint.setText("会员有效期至" + userInfo.getEffectiveTime());
                                tvVipHint.setSelected(false);
                                break;
                            case 2:
                                tvVipHint.setText("已失效");
                                tvVipHint.setSelected(false);
                                break;
                            default:
                                tvVipHint.setText("");
                                tvVipHint.setSelected(false);
                        }


                        //判断个人账户是否已上传极光regid
                        if (TextUtils.isEmpty(userInfo.getDeviceToken())) {
                            RxBus.getInstance().postEmpty(RxEvent.JPUSH_REGID_ADD);
                        }
                    }
                });

    }


    /**
     * 获取消息未读数
     */
    public void getUnReadMessage() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getMyUnReadMessageCount(hashMap),
                new CallBack<BaseModel<Integer>>() {
                    @Override
                    public void onResponse(BaseModel<Integer> data) {
                        if (data.code == 1) {
                            tv_no_read.setText(data.data > 0 ? "未读" + data.data + "条" : "");
                        } else {
                            tv_no_read.setText("");
                        }
                    }
                });

    }


    /**
     * 判断是否有家庭成员
     */
    public void ishaveFamilyMember() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().ifHaveFamilyMember(hashMap),
                new CallBack<HaveFamilyMember>() {
                    @Override
                    public void onResponse(HaveFamilyMember data) {
                        if (data.code == 1) {
                            if (data.data.ifExist == 0) {
//                                intent.setClass(getActivity(),MyFamilyInviteActivity.class);
//                                intent.putExtra(IntentParam.TYPE_FAMILY_INVITE_URL,data.data.introduceUrl);
//                                intent.putExtra(IntentParam.TYPE_FAMILY_INVITE_NUM,data.data.inviteNum);
                                Bundle bundle = new Bundle();
                                bundle.putString(IntentParam.TYPE_FAMILY_INVITE_URL, data.data.introduceUrl);
                                bundle.putInt(IntentParam.TYPE_FAMILY_INVITE_NUM, data.data.inviteNum);
                                Utils.gotoActy(getActivity(), MyFamilyInviteActivity.class, bundle);
                            } else {
                                Utils.gotoActy(getActivity(), MyFamilyPersonActivity.class);
                            }
                        } else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
//            setUnReadNumber();
        }
    }

    // FragmentPagerAdapter+ViewPager的使用场景
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 可见时
//            setUnReadNumber();
        }
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }
}







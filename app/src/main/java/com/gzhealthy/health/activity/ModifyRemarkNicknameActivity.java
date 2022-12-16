package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.ModifyRemarkNicknameAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CommonKnowDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改备注昵称
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class ModifyRemarkNicknameActivity extends BaseAct implements BaseQuickAdapter.OnItemClickListener, TextWatcher {
    /**
     * 验证码邀请
     */
    public static final String ACTION_VERIFY_CODE_INVITE = "action_verify_code_invite";
    /**
     * 扫码码邀请
     */
    public static final String ACTION_SCAN_CODE_INVITE = "action_scan_code_invite";
    /**
     * 单纯修改昵称
     */
    public static final String ACTION_MODIFY_NICKNAME = "action_modify_nickname";

    /**
     * 功能动作
     *
     * @see ModifyRemarkNicknameActivity#ACTION_VERIFY_CODE_INVITE
     * @see ModifyRemarkNicknameActivity#ACTION_SCAN_CODE_INVITE
     * @see ModifyRemarkNicknameActivity#ACTION_MODIFY_NICKNAME
     */
    private String mAction;
    /**
     * 成员手机号
     */
    private String mMemberMobile = "";
    /**
     * 成员昵称
     */
    private String mMemberNickname = "";
    /**
     * 成员ID
     */
    private String mMemberId = "";
    /**
     * 成员主键id
     */
    private String mId = "";


    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.rvRemark)
    RecyclerView rvRemark;
    @BindView(R.id.btOk)
    Button btOk;

    private List<String> mRemarkNicknames =
            Arrays.asList("妈妈", "爷爷", "奶奶", "爸爸",
                    "老婆", "老公", "外公", "外婆",
                    "老伴儿");

    private ModifyRemarkNicknameAdapter mAdapter = new ModifyRemarkNicknameAdapter();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_remark_nickname;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mAction = getIntent().getAction();
        if (mAction == null) {
            ToastUtil.showToast("参数异常：mAction" + mAction);
            finish();
            return;
        }

        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("修改备注昵称");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        mMemberMobile = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_MOBILE);
        mMemberNickname = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_NICKNAME);
        mMemberId = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_ID);
        mId = getIntent().getStringExtra(IntentParam.ID);
        if (ACTION_MODIFY_NICKNAME.equals(mAction)) {
            btOk.setText("确定");
        }

        etRemark.setText(mMemberNickname);
        etRemark.setSelection(etRemark.length());
        etRemark.addTextChangedListener(this);

        rvRemark.setAdapter(mAdapter);
        mAdapter.setNewData(mRemarkNicknames);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etRemark.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            View itemView = mAdapter.getViewByPosition(rvRemark, i, R.id.tvRemark);
            if (itemView.isSelected()) {
                itemView.setSelected(false);
                break;
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        etRemark.setText(mAdapter.getItem(position));
        etRemark.setSelection(etRemark.length());
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            View itemView = mAdapter.getViewByPosition(rvRemark, i, R.id.tvRemark);
            if (itemView.isSelected()) {
                itemView.setSelected(false);
                break;
            }
        }
        view.setSelected(true);
    }

    @OnClick({R.id.ivClear, R.id.btOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivClear://清除
                etRemark.setText("");
                etRemark.setSelection(etRemark.length());
                break;
            case R.id.btOk://确定
                ok();
                break;
        }
    }


    private void ok() {
        String remark = etRemark.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            ToastUtil.showToast("请输入备注昵称");
            return;
        }
        if (remark.length() > 30) {
            ToastUtil.showToast("昵称最多30字符");
            return;
        }
        mMemberNickname = remark;
        if (ACTION_VERIFY_CODE_INVITE.equals(mAction)) {
            //验证码方式邀请
            sendSmsCode();
        } else if (ACTION_SCAN_CODE_INVITE.equals(mAction)) {
            //扫描二维码方式邀请
            scanCodeInvite();
        } else if (ACTION_MODIFY_NICKNAME.equals(mAction)) {
            //单纯修改昵称
            updateMemberNickName();
        }
    }

    /**
     * 共享成员，发送邀请验证码
     */
    private void sendSmsCode() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("phone", mMemberMobile);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().sendSmsCode(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            Intent intent = new Intent(ModifyRemarkNicknameActivity.this, SharedMemberVerifyCodeActivity.class);
                            intent.putExtra(IntentParam.EXTRA_MEMBER_MOBILE, mMemberMobile);
                            intent.putExtra(IntentParam.EXTRA_MEMBER_NICKNAME, mMemberNickname);
                            intent.putExtra(IntentParam.EXTRA_MEMBER_ID, mMemberId);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }

    /**
     * 共享成员，扫描二维码邀请
     */
    private void scanCodeInvite() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("memberNickName", mMemberNickname);
        param.put("member", mMemberId);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().scanCodeInvite(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            new CommonKnowDialog(ModifyRemarkNicknameActivity.this)
                                    .setHint("已成功发送邀请，请等待对方同意")
                                    .setIsCancelable(false)
                                    .setCommonKnowDialogCallBack(() -> {
                                        finish();
                                    }).show();
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }

    /**
     * 共享成员，修改昵称
     */
    private void updateMemberNickName() {
        Map<String, String> param = new HashMap<>();
        param.put("id", mId);
        param.put("memberNickName", mMemberNickname);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateMemberNickName(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            RxBus.getInstance().post(RxEvent.MODIFY_FAMILY_DETAIL_NICKNAME, mMemberNickname);
                            RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_MEMBER_DETAIL);
                            RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
                            finish();
                        }
                    }
                });
    }


    /**
     * 验证码邀请修改昵称
     *
     * @param context
     * @param memberMobile   成员手机号
     * @param memberNickname 成员昵称
     * @param memberId       成员id
     */
    public static void verifyCodeInvite(Context context, String memberMobile, String memberNickname, String memberId) {
        if (TextUtils.isEmpty(memberMobile)) {
            ToastUtil.showToast("参数异常memberMobile=" + memberMobile);
            return;
        }
        if (TextUtils.isEmpty(memberId)) {
            ToastUtil.showToast("参数异常memberId=" + memberId);
            return;
        }
        Intent intent = new Intent(context, ModifyRemarkNicknameActivity.class);
        intent.setAction(ModifyRemarkNicknameActivity.ACTION_VERIFY_CODE_INVITE);
        intent.putExtra(IntentParam.EXTRA_MEMBER_MOBILE, memberMobile);
        intent.putExtra(IntentParam.EXTRA_MEMBER_NICKNAME, memberNickname);
        intent.putExtra(IntentParam.EXTRA_MEMBER_ID, memberId);
        context.startActivity(intent);
    }

    /**
     * 扫码邀请修改昵称
     *
     * @param context
     * @param memberNickname 成员昵称
     * @param memberId       成员id
     */
    public static void scanCodeInvite(Context context, String memberNickname, String memberId) {
        if (TextUtils.isEmpty(memberId)) {
            ToastUtil.showToast("参数异常memberId=" + memberId);
            return;
        }
        Intent intent = new Intent(context, ModifyRemarkNicknameActivity.class);
        intent.setAction(ModifyRemarkNicknameActivity.ACTION_SCAN_CODE_INVITE);
        intent.putExtra(IntentParam.EXTRA_MEMBER_NICKNAME, memberNickname);
        intent.putExtra(IntentParam.EXTRA_MEMBER_ID, memberId);
        context.startActivity(intent);
    }

    /**
     * 单纯修改昵称
     *
     * @param context
     * @param memberNickname 昵称
     * @param id             成员主键id
     */
    public static void modifyNickname(Context context, String memberNickname, String id) {
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast("参数异常id=" + id);
            return;
        }
        Intent intent = new Intent(context, ModifyRemarkNicknameActivity.class);
        intent.setAction(ModifyRemarkNicknameActivity.ACTION_MODIFY_NICKNAME);
        intent.putExtra(IntentParam.EXTRA_MEMBER_NICKNAME, memberNickname);
        intent.putExtra(IntentParam.ID, id);
        context.startActivity(intent);
    }
}

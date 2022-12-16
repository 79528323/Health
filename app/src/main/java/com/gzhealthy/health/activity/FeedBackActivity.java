package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseAct {

    @BindView(R.id.tv_fault)
    TextView tvFault;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.tv_fault2)
    TextView tvFault2;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("意见反馈");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(FeedBackActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

    }


    @OnClick({R.id.rl_1, R.id.rl_2})
    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.rl_1:
//                FeedBackDetailActivity.instance(FeedBackActivity.this, FeedBackDetailActivity.TYPE_FAULT);
//                break;
//            case R.id.rl_2:
//                FeedBackDetailActivity.instance(FeedBackActivity.this, FeedBackDetailActivity.TYPE_SUME);
//                break;
//            default:
//                break;
//        }
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }
}

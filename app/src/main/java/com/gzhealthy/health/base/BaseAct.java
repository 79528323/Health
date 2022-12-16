package com.gzhealthy.health.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.gzhealthy.health.R;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.manager.ActivityManager;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.push.MyJPushMessageReceiver;
import com.gzhealthy.health.receiver.ExitReceiver;
import com.gzhealthy.health.receiver.NetWorkChangeReceiver;
import com.gzhealthy.health.tool.KeyboardUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.TDevice;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.CommonKnowDialog;
import com.gzhealthy.health.widget.HealthDateChoiceView;
import com.gzhealthy.health.widget.LoadingPageView;
import com.gzhealthy.health.widget.RateWarnDialog;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseAct extends SwipeBackActivity implements View.OnClickListener, SwipeBackActivityBase, LifeSubscription, ResponseState {
    protected RxManager rxManager = new RxManager();
    private final String TAG = BaseAct.class.getSimpleName();
    private AppBarLayout appBar;

    public Toolbar getToolBar() {
        return toolBar;
    }

    private Toolbar toolBar;
    private ActionBar actionBar;
    protected ImageView ivLeft;
    protected ImageView ivRight;

    public ImageView getIvLeft() {
        return ivLeft;
    }

    protected ImageView ivWait;
    protected ImageView iv_left_right;
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;
    protected FrameLayout flContent;
    protected boolean isDesignBar = true;
    private BroadcastReceiver existReceiver, netReceiver;
    protected boolean needSildeExit = true;
    private SwipeBackLayout mSwipeBackLayout;
    private CompositeSubscription compositeSubscription;
    protected LoadingPageView loadingPageView;
    protected Activity aty;
    private LinearLayout llWait;
    private AnimationDrawable animation;
    public ImmersionBar mImmersionBar;
    /**
     * 心率异常提醒弹窗
     */
    private RateWarnDialog mRateWarnDialog;

    protected TextView inviteRight;
    protected ImageView invitebage;

    protected RelativeLayout rlay_clock,rlay_medication;
    private TextView tvWait;

    // 处理主线程UI更新
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.RequestValue.CANCEL_WAIT_DIALOG:
//                    if (animation != null)
//                        animation.stop();
                    llWait.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    protected void showWaitDialog() {
        showWaitDialog(null);
    }

    protected void showWaitDialog(String tip) {
        llWait.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(tip)) {
            tvWait.setText(tip);
        }
//        animation = (AnimationDrawable) ivWait.getBackground();
//        ivWait.post(new Runnable() {
//            @Override
//            public void run() {
//                animation.start();
//            }
//        });
    }

    public void hideWaitDialog() {
        if (handler != null)
            handler.sendEmptyMessage(Constants.RequestValue.CANCEL_WAIT_DIALOG);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 禁用系统字体大小改变应用文字大小
        DispUtil.disabledDisplayDpiChange(this.getResources());
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        // 先把状态栏默认设置成透明色
        mImmersionBar = ImmersionBar.with(this);

        mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).navigationBarAlpha(0.1f).navigationBarColor(R.color.bg_gray).init();
        aty = this;
        ActivityManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getView());
        if (isDesignBar) initActionBar();
        if (needSildeExit) {
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        } else {
            setSwipeBackEnable(false);
        }
        flContent = (FrameLayout) findViewById(getContentViewId());
        llWait = (LinearLayout) findViewById(R.id.ll_wait_dialog);
        ivWait = (ImageView) findViewById(R.id.loadingIv);
        tvWait = (TextView) findViewById(R.id.loadingTv);
        Glide.with(ivWait.getContext()).load(R.drawable.icon_gif_loading).into(ivWait);
        if (loadingPageView == null) {
            loadingPageView = new LoadingPageView(this) {
                @Override
                protected void initView() {
                    BaseAct.this.init(savedInstanceState);
                }

                @Override
                protected void loadData() {
                    BaseAct.this.initData();
                    BaseAct.this.refreshData();
                }

                @Override
                protected int getLayoutId() {
                    return BaseAct.this.getContentLayout();
                }
            };
        }
        flContent.addView(loadingPageView);

        rxManager.onRxEvent(MyJPushMessageReceiver.CONTENT_TYPE_RATE_WARN)
                .subscribe((Action1<Object>) o -> {
                    if (ActivityUtils.getTopActivity() == this) {
                        //栈顶activity弹窗心率异常弹窗
                        if (mRateWarnDialog == null) {
                            mRateWarnDialog = new RateWarnDialog(this, rxManager, this, this);
                        }
                        if (!mRateWarnDialog.isShowing()) {
                            mRateWarnDialog.setRemark(o.toString()).show();
                        }
                    }
                });
        rxManager.onRxEvent(MyJPushMessageReceiver.CONTENT_TYPE_TIP)
                .subscribe((Action1<Object>) o -> {
                    if (ActivityUtils.getTopActivity() == this) {
                        //栈顶activity提示信息
                        new CommonKnowDialog(this).setHint(o.toString()).show();
                    }
                });


        initData();
        initReceiver();
//        PushAgent.getInstance(this).onAppStart();//初始化 友盟推送
    }

    public void setstatueColor(@NonNull int id) {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.statusBarColor(id).keyboardEnable(true).statusBarColorTransform(id).init();
    }

    /**
     * 获取界面主视图
     */
    protected int getView() {
        return R.layout.activity_base_normal;
    }

    /**
     * 初始化presenter和delegate模块
     */
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }

    /**
     * 点击刷新刷新数据
     */
    protected void refreshData() {
    }

    /**
     * 获取内容视图
     */
    protected int getContentViewId() {
        return R.id.fl_base_container;
    }

    /**
     * 获取内容视图布局
     */
    protected abstract int getContentLayout();

    /**
     * 初始化内容视图各个控件
     */
    protected abstract void init(Bundle savedInstanceState);


    protected <V extends View> V $(int mResId) {
        V view = (V) findViewById(mResId);
        return view;
    }

    // 用于添加rx的监听的在onDestroy中记得关闭不然会内存泄漏
    @Override
    public void bindSubscription(Subscription subscription) {
        if (this.compositeSubscription == null) {
            this.compositeSubscription = new CompositeSubscription();
        }
        this.compositeSubscription.add(subscription);
    }

    protected void setOnClickListener(int... mResId) {
        if (mResId == null) {
            return;
        }
        for (int id : mResId) {
            $(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_custom_toolbar_left || view.getId() == R.id.tv_custom_toolbar_left) {
            Log.d(TAG, "onClick: ");
            goBack();
        }
    }

    public TextView getTvLeft() {
        return tvLeft;
    }


    public void goBack() {
        // 返回
        ActivityManager.getAppManager().finishActivity(this);
        KeyboardUtils.hideSoftInput(this);
    }

    private void initActionBar() {
        appBar = (AppBarLayout) findViewById(R.id.custom_appbar);
        toolBar = (Toolbar) findViewById(R.id.main_toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_custom_toolbar_title);
        ivLeft = (ImageView) findViewById(R.id.iv_custom_toolbar_left);
        tvLeft = (TextView) findViewById(R.id.tv_custom_toolbar_left);
        ivRight = (ImageView) findViewById(R.id.iv_custom_toolbar_right);
        tvRight = (TextView) findViewById(R.id.tv_custom_toolbar_right);
        iv_left_right = (ImageView) findViewById(R.id.iv_custom_toolbar_left_right);
        rlay_clock = (RelativeLayout) findViewById(R.id.rlay_clock);
        rlay_medication = (RelativeLayout)findViewById(R.id.rlay_medication);
        setToolBarPadding();
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        invitebage = (ImageView) findViewById(R.id.bage);
        inviteRight = (TextView) findViewById(R.id.tv_custom_tips_toolbar_right);
    }

    public void setToolBarPadding() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            appBar.setPadding(0, TDevice.getStatusBarHeight(this), 0, 0);
        }
    }

    protected void setIvLeftGone(boolean visable) {
        ivLeft.setVisibility(visable ? View.GONE : View.VISIBLE);
    }

    protected void setTitle(String mTitle) {
        tvTitle.setText(mTitle);
    }

    protected void setTitle(String mTitle, int textColor) {
        tvTitle.setText(mTitle);
        tvTitle.setTextColor(textColor);
    }

    protected TextView getCenterTextView() {
        return tvTitle;
    }

    protected void setBarLeftText(String mLeft) {
        try {
            actionBar.setDisplayHomeAsUpEnabled(false);
        } catch (NullPointerException e) {
            Logger.e("actionbar", "actionbar = " + e);
        }
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(mLeft);
        tvLeft.setOnClickListener(this);
    }

    protected void setBarLeftIcon(int mResId) {
        try {
            actionBar.setDisplayHomeAsUpEnabled(false);
        } catch (NullPointerException e) {
            Logger.e("actionbar", "actionbar = " + e);
        }
        if (mResId != -1) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(mResId);
            ivLeft.setOnClickListener(this);
        }
    }


    protected void setBarLeftIcon(int mResId, @Nullable View.OnClickListener onClickListener) {
        try {
            actionBar.setDisplayHomeAsUpEnabled(false);
        } catch (NullPointerException e) {
            Logger.e("actionbar", "actionbar = " + e);
        }
        if (mResId != -1) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(mResId);
            ivLeft.setOnClickListener(onClickListener);
        }
    }

    protected void setHasBack() {
        setBarLeftIcon(R.mipmap.ic_general_return_bg);
    }


    protected void setBarRightText(String mRight) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(mRight);
        tvRight.setOnClickListener(this);
    }

    protected void setBarRightText(String mRight, int color) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(mRight);
        tvRight.setTextColor(color);
        tvRight.setOnClickListener(this);
    }

    protected void setBarRightText(String mRight,int color,int bgRes){
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(mRight);
        tvRight.setTextColor(color);
        tvRight.setBackgroundResource(bgRes);
        tvRight.setOnClickListener(this);
    }

    protected void setBarRighticon(int mResId) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(mResId);
        ivRight.setOnClickListener(this);
    }

    protected void setBarLeftRighticon(int mResId) {   /*设置toobar右边的第二个按钮*/
        iv_left_right.setVisibility(View.VISIBLE);
        iv_left_right.setImageResource(mResId);
        iv_left_right.setOnClickListener(this);
    }

    protected void setInviteRightText(String mRight) {
        inviteRight.setVisibility(View.VISIBLE);
        inviteRight.setText(mRight);
    }

    protected void setInviteRightOnClick(View.OnClickListener onClick) {
        inviteRight.setOnClickListener(onClick);
    }

    protected void setClockOnClick(View.OnClickListener onClick) {
        rlay_clock.setVisibility(View.VISIBLE);
        rlay_clock.setOnClickListener(onClick);
    }

    protected void setMedicationRight(View.OnClickListener onClick){
        rlay_medication.setOnClickListener(onClick);
        rlay_medication.setVisibility(View.VISIBLE);
    }

    protected void setInviteRightBage(int count) {
        invitebage.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    protected ImageView getIvRight() {
        return ivRight;
    }

    protected TextView getTvRight() {
        return tvRight;
    }

    protected void hideOrShowToolbar(boolean isHidden) {
        toolBar.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    protected void hideOrShowAppbar(boolean isHidden) {
        appBar.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    protected void setBarBackgroundColor(int color) {
        toolBar.setBackgroundColor(getColor(color));
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        existReceiver = new ExitReceiver(this);
        IntentFilter filter = new IntentFilter(Constants.Receiver.INTENT_ACTION_EXIT_APP);
        registerReceiver(existReceiver, filter);

        netReceiver = new NetWorkChangeReceiver();
        IntentFilter netFilter = new IntentFilter(Constants.Receiver.INTENT_ACTION_NET_ERROR);
        registerReceiver(netReceiver, netFilter);
    }


    @Override
    protected void onDestroy() {
        rxManager.clear();
        super.onDestroy();
        ActivityManager.getAppManager().removeActivity(this);
        if (existReceiver != null)
            unregisterReceiver(existReceiver);
        if (netReceiver != null)
            unregisterReceiver(netReceiver);
        if (this.compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            this.compositeSubscription.unsubscribe();
        }
        // 必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        if (mImmersionBar != null)
            mImmersionBar.destroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(this);
    }

    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    public String getInputStr(TextView editText) {
        return editText.getText().toString().trim();
    }

    public void statusBarGone() {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    public void setEn(View vi, boolean enable) {
        vi.setEnabled(enable);
    }

    @Override
    public void setState(int state) {
        loadingPageView.state = state;
        loadingPageView.showPage();
    }


    public void setDateView(HealthDateChoiceView healthDateChoiceView, HealthDateChoiceView.OnDateChoiceListener onDateChoiceListener) {
        if (healthDateChoiceView != null) {
            healthDateChoiceView.setOnDateChoiceListener(onDateChoiceListener);
        }
    }


    protected boolean isUserLogin() {
        return !TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN, ""));
    }

    protected String getUserToken() {
        return SPCache.getString(SPCache.KEY_TOKEN, "");
    }

    protected void clearUserToken() {
        SPCache.putString(SPCache.KEY_TOKEN, "");
    }
}

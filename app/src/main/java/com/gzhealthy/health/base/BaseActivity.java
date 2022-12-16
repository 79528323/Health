package com.gzhealthy.health.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.protocol.BaseView;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.receiver.ExitReceiver;
import com.gzhealthy.health.receiver.NetWorkChangeReceiver;
import com.gzhealthy.health.tool.TDevice;
import com.gzhealthy.health.widget.LoadingPageView;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;



public abstract class BaseActivity<T extends BasePresenter, H extends BaseView> extends SwipeBackActivity implements View.OnClickListener, SwipeBackActivityBase, LifeSubscription {

    private final String TAG = BaseActivity.class.getSimpleName();
    private AppBarLayout appBar;
    private Toolbar toolBar;
    private ActionBar actionBar;
    private ImageView ivLeft, ivRight;
    private TextView tvTitle, tvLeft, tvRight;
    protected FrameLayout flContent;
    private boolean isHidden = false;
    protected boolean isDesignBar = true;
    private BroadcastReceiver existReceiver, netReceiver;
    protected boolean needSildeExit = true;
    private SwipeBackLayout mSwipeBackLayout;
    private CompositeSubscription compositeSubscription;
    protected LoadingPageView loadingPageView;
    protected T presenter;
    protected H delegate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(getView());
        if (isDesignBar) initActionBar();
        if (needSildeExit) {
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        } else {
            setSwipeBackEnable(false);
        }
        flContent = (FrameLayout) findViewById(getContentViewId());
        if (loadingPageView == null) {
            loadingPageView = new LoadingPageView(this) {
                @Override
                protected void initView() {
                    BaseActivity.this.init(savedInstanceState);
                }

                @Override
                protected void loadData() {
                    BaseActivity.this.initData();
                }

                @Override
                protected int getLayoutId() {
                    return BaseActivity.this.getContentLayout();
                }
            };
        }
        flContent.addView(loadingPageView);
        initData();
        initReceiver();
    }

    /**
     * 获取界面主视图
     */
    protected abstract int getView();

    /**
     * 初始化presenter和delegate模块
     */
    protected abstract void initData();

    /**
     * 获取内容视图
     */
    protected abstract int getContentViewId();

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

    //用于添加rx的监听的在onDestroy中记得关闭不然会内存泄漏。
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

    }


    private void initActionBar() {
        appBar = (AppBarLayout) findViewById(R.id.custom_appbar);
        toolBar = (Toolbar) findViewById(R.id.main_toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_custom_toolbar_title);
        ivLeft = (ImageView) findViewById(R.id.iv_custom_toolbar_left);
        tvLeft = (TextView) findViewById(R.id.tv_custom_toolbar_left);
        ivRight = (ImageView) findViewById(R.id.iv_custom_toolbar_right);
        tvRight = (TextView) findViewById(R.id.tv_custom_toolbar_left);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            appBar.setPadding(0, TDevice.getStatusBarHeight(this), 0, 0);
        }
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void setTitle(String mTitle) {
        tvTitle.setText(mTitle);
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
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(mResId);
        ivLeft.setOnClickListener(this);
    }


    protected void setBarRightText(String mRight) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(mRight);
        tvRight.setOnClickListener(this);
    }

    protected void setBarRightcon(int mResId) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(mResId);
        ivRight.setOnClickListener(this);
    }

    protected void hideOrShowToolbar(boolean isHidden) {
//        appBar.animate()
//                .translationY(isHidden ? 0 : -appBar.getHeight())
//                .setInterpolator(new DecelerateInterpolator(2))
//                .start();
//        isHidden = !isHidden;
        appBar.setVisibility(isHidden ? View.GONE : View.VISIBLE);
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
        super.onDestroy();
        unregisterReceiver(existReceiver);
        unregisterReceiver(netReceiver);
        if (this.compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            this.compositeSubscription.unsubscribe();
        }
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

    public String getInputStr(EditText editText) {
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
    }


}

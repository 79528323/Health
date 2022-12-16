package com.gzhealthy.health.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gzhealthy.health.R;
import com.gzhealthy.health.protocol.BaseView;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.LoadingPageView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseFragment<T extends BasePresenter, H extends BaseView> extends Fragment implements View.OnClickListener, LifeSubscription {
    private CompositeSubscription mCompositeSubscription;
    protected LoadingPageView loadingPageView;
    protected T presenter;
    protected H delegate;
    protected Fragment mFragment;
    protected RxManager rxManager = new RxManager();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 禁用系统字体大小改变应用文字大小
        DispUtil.disabledDisplayDpiChange(this.getResources());
        if (loadingPageView == null) {
            loadingPageView = new LoadingPageView(getContext()) {
                @Override
                protected void initView() {
                    BaseFragment.this.init(savedInstanceState);
                }

                @Override
                protected void loadData() {
                    BaseFragment.this.initData();
                }

                @Override
                protected int getLayoutId() {
                    return BaseFragment.this.getContentLayout();
                }
            };
        }
        initData();
        mFragment = this;
        return loadingPageView;
    }

    protected <V extends View> V $(int mResId) {
        V view = (V) loadingPageView.findViewById(mResId);
        return view;
    }

    /**
     * 初始化内容视图各个控件
     */
    protected abstract void init(Bundle savedInstanceState);


    /**
     * 获取内容视图布局
     */
    protected abstract int getContentLayout();

    /**
     * 初始化presenter和delegate模块
     */
    protected void initData() {
        loadingPageView.state = (Constants.ResponseStatus.STATE_SUCCESS);
        loadingPageView.showPage();
    }

    protected abstract void widgetClick(View view);

    @Override
    public void onClick(View view) {
        widgetClick(view);
    }

    public String getInputStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }


    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    protected void showWaitDialog() {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).showWaitDialog();
        }
    }

    protected void showWaitDialog(String tip) {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).showWaitDialog(tip);
        }
    }

    protected void hideWaitDialog() {
        if (getActivity() instanceof BaseAct) {
            ((BaseAct) getActivity()).hideWaitDialog();
        }
    }

    @Override
    public void bindSubscription(Subscription subscription) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
    }

    @Override
    public void onDestroy() {
        rxManager.clear();
        super.onDestroy();
    }
}

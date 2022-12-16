package com.gzhealthy.health.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseAct {

    private static final String TAG = "GuideActivity";
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private TextView iv_go_away;

    // 引导页图片资源
    private static final int[] pics = {R.layout.guid_view1, R.layout.guid_view2, R.layout.guid_view3};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    private LinearLayout ll;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideOrShowAppbar(true);
        views = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            if (i == pics.length - 1) {
                iv_go_away = (TextView) view.findViewById(R.id.iv_go_away);
                iv_go_away.setTag("enter");
                iv_go_away.setOnClickListener(this);
            }

            views.add(view);

        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());

        initDots();
    }

//    protected void setStatusBar() {
//        StatusBarUtil.setTransparent(this);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SPCache.putBoolean(Constants.FIRST_OPEN, false);
        finish();
    }

    private void initDots() {
        ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setVisibility(View.VISIBLE);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
//        if (position<2){
//
//        }

        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;

//        if (position==3){
//            ll.setVisibility(View.GONE);
//        }else {
//            ll.setVisibility(View.VISIBLE);
//        }
        ll.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(GuideActivity.this,
                HomeActivity.class);
        startActivity(intent);
        SPCache.putBoolean(Constants.FIRST_OPEN, false);
        goBack();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置


        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected: " + position);
            // 设置底部小点选中状态
            setCurDot(position);

        }
    }
}

package com.gzhealthy.health.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.callback.OnDateCalendarViewCallBack;
import com.gzhealthy.health.model.tab.HealthTab;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Justin_Liu
 * on 2021/3/5
 */
public class HealthTabPager extends LinearLayout {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<HealthTab> healthTabList;
    private List<Fragment> fragmentList;
    private DisplayMetrics metrics;
    private OnHealTabClickListener onHealTabClickListener;
    private FrameLayout container;
    private int oldPos = -1;
    private final static int _ID_CONTAINER = 0x10101;
    private Context context;
    private HealthDateChoiceView choiceView;

    public int getOldPos() {
        return oldPos;
    }

    public void setOldPos(int oldPos) {
        this.oldPos = oldPos;
    }

    public HealthTabPager(Context context) {
        super(context);
        this.init(context);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public HealthTabPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public HealthTabPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i=0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof TabLayout){
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = metrics.widthPixels;
                view.setLayoutParams(params);
            }else if (view instanceof ViewPager){
                Log.e("111","ViewPager w = "+view.getMeasuredWidth()+"h ="+view.getMeasuredHeight());
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = metrics.widthPixels;
                params.height = metrics.heightPixels;
                params.weight = 1;
                view.setLayoutParams(params);
            }
//            Log.e("111","set tablayout w = "+view.getMeasuredWidth());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @SuppressLint("ResourceType")
    private void init(Context context){
        this.context = context;
        metrics = context.getResources().getDisplayMetrics();
        tabLayout = new TabLayout(context);
        tabLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        tabLayout.setTabRippleColor(context.getResources().getColorStateList(R.color.transparent));
//        tabLayout.setTabTextColors(context.getColor(R.color.global_333333),context.getColor(R.color.colorPrimary));
        viewPager = new ViewPager(context);
        viewPager.setId(0x1001);
        container = new FrameLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        container.setLayoutParams(params);
        container.setId(_ID_CONTAINER);
        fragmentList = new ArrayList<>();

        choiceView = new HealthDateChoiceView(context);

        addView(tabLayout);
        addView(choiceView);
        addView(container);
    }

    public void setTabTextColor(int normalColor, int selectedColor){
        tabLayout.setTabTextColors(normalColor,selectedColor);
    }

    public void setTabIndicatorColor(int color){
        tabLayout.setSelectedTabIndicatorColor(color);
    }

    public void setTabIndicatorWidth(boolean tabIndicatorFullWidth){
        tabLayout.setTabIndicatorFullWidth(tabIndicatorFullWidth);
    }

    public void setTabPosition(int position){

    }

    public void setTabGone(){
        tabLayout.setVisibility(GONE);
    }

    public HealthDateChoiceView getChoiceView() {
        return choiceView;
    }

    //    public void setTabFragment(FragmentManager fragmentManager,List<HealthTab> healthTabList){
//        this.healthTabList = healthTabList;
//        if (healthTabList != null && healthTabList.size()>0){
//            HealthTab healthTab;
//            tabLayout.removeAllTabs();
//            for (int index=0; index < this.healthTabList.size(); index++){
//                healthTab = healthTabList.get(index);
//                tabLayout.addTab(tabLayout.newTab().setText(healthTab.getTabName()));
//                fragmentList.add(healthTab.getFragment());
//            }
//
//            viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
//                @NonNull
//                @Override
//                public Fragment getItem(int position) {
//                    return fragmentList.get(position);
//                }
//
//                @Override
//                public int getCount() {
//                    return fragmentList.size();
//                }
//
//                @Nullable
//                @Override
//                public CharSequence getPageTitle(int position) {
//                    return healthTabList.get(position).getTabName();
//                }
//
//                @Override
//                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
////                    super.destroyItem(container, position, object);
//                }
//            });
//
//            tabLayout.setupWithViewPager(viewPager,false);
//        }
//    }


    public void setTabFragment(FragmentManager fragmentManager,List<HealthTab> healthTabList,int firstPos){
        this.healthTabList = healthTabList;
        if (healthTabList != null && healthTabList.size()>0){
            HealthTab healthTab;
            tabLayout.removeAllTabs();
            for (int index=0; index < this.healthTabList.size(); index++){
                healthTab = healthTabList.get(index);
//                int selectPos = 0;
//                boolean isSelect = false;
                if (firstPos < 0){
                    firstPos = 0;
                }
                tabLayout.addTab(tabLayout.newTab().setText(healthTab.getTabName()),index,
                        index==firstPos?true:false);
//                fragmentList.add(healthTab.getFragment());
            }

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    showFragment(fragmentManager,tab.getPosition());
                    Log.e("111","onTabSelected="+tab.getText().toString()+"  Pos="+tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    Log.e("111","onTabUnselected="+tab.getText().toString());

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Log.e("111","onTabReselected="+tab.getText().toString());
                }
            });
            showFragment(fragmentManager,0);
        }
    }


    public void showFragment(FragmentManager fragmentManager){
        HealthTab tab = healthTabList.get(0);
        Fragment fragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            fragment = Fragment.instantiate(this.context,tab.fragment.getName());
            transaction.add(_ID_CONTAINER,fragment,"fragment");
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }


    public void showFragment(FragmentManager fragmentManager,int position){
        if (oldPos != position){
            HealthTab tab = healthTabList.get(position);
            Fragment fragment = fragmentManager.findFragmentByTag(makeFragmentTag(position));
            Fragment oldfragment = fragmentManager.findFragmentByTag(makeFragmentTag(oldPos));
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (fragment==null){
                try {
                    fragment = Fragment.instantiate(this.context,tab.fragment.getName());
                    transaction.add(_ID_CONTAINER,fragment,makeFragmentTag(position));
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }else {
                transaction.show(fragment);
            }

            if (oldfragment!=null){
                oldfragment.setMenuVisibility(false);
                oldfragment.setUserVisibleHint(false);
                transaction.hide(oldfragment);
            }

            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
        }
        oldPos = position;
//        Fragment fragment =
    }


    public void changeTabTextView(TabLayout.Tab tab, boolean isBold) {
        View view = tab.getCustomView();
        if (null == view) {
            tab.setCustomView(R.layout.layout_tab1);
        }
        TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
        if (isBold) {
            textView.setTextAppearance(context, R.style.TabLayoutBoldTextSize);
        } else {
            textView.setTextAppearance(context, R.style.TabLayoutNormalTextSize);
        }
    }

    class OnTabClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    interface OnHealTabClickListener {
        void tabClick();
    }

//    private OnDateChioceViewListener onDateChioceViewListener;
//    public interface OnDateChioceViewListener{
//        void onChioce();
//    }
    private OnDateCalendarViewCallBack OnDateCalendarViewCallBack;

    public void setOnDateCalendarViewCallBack(OnDateCalendarViewCallBack OnDateCalendarViewCallBack) {
        this.OnDateCalendarViewCallBack = OnDateCalendarViewCallBack;
    }

    public String makeFragmentTag(int position){
        return "com.gzhealthy.health."+position;
    }

    @Override
    protected void finalize() throws Throwable {
        tabLayout.setOnTabSelectedListener(null);
        super.finalize();
    }
}

package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FamilyMemberHorizontalAdapter;
import com.gzhealthy.health.adapter.MyFragmentPagerAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.fragment.FamilyPersonTabFragment;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.FamilyInvitePopupWindow;
import com.gzhealthy.health.widget.FamilyInvitedListDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 家庭成员
 */
public class MyFamilyPersonActivity extends BaseAct implements BaseQuickAdapter.OnItemChildClickListener{

    int zoomOffset = 0;
    int avatarWidth = 0;
    int avatarHeight = 0;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

//    View mFooterView;

    FamilyInvitePopupWindow popupWindow;

    FamilyInvitedListDialog dialog;

    List<FamilyMember.DataBeanX.DataBean> mFamilyMemberList = new ArrayList<>();

    MyFragmentPagerAdapter adapter = null;

    List<FamilyPersonTabFragment> mTabFragmentList=new ArrayList<>();

    FamilyMemberHorizontalAdapter horizontalAdapter;

    String url = null;

    int inviteNum=0;

    int currenPosition = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_family_person;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("家庭成员");
        setInviteRightText("邀请");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        setSwipeBackEnable(false);
        initView();
    }


    public void initView(){
        zoomOffset = DispUtil.dp2px(this,10);
        avatarWidth = avatarHeight = DispUtil.dp2px(this,50);

        initPopWindow();
        dialog = new FamilyInvitedListDialog(this,this,this);

        horizontalAdapter = new FamilyMemberHorizontalAdapter(this);
        horizontalAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        mRecyclerView.setAdapter(horizontalAdapter);

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mTabFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mTabFragmentList.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currenPosition = position;
                horizontalAdapter.setIsSelectedId(mFamilyMemberList.get(position).id);
                mRecyclerView.smoothScrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rxManager.onRxEvent(RxEvent.ON_REFRESH_FAMILY_PERSON)
                .subscribe((Action1<Object>) o -> {
                    getData(0);
                });

        rxManager.onRxEvent(RxEvent.ON_REFRESH_FAMILY_PERSON_POSITION)
                .subscribe((Action1<Object>) o -> {
                    getData(currenPosition);
                });

        getData(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-家庭成员");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-家庭成员");
    }

    /**
     * 获取家庭列表所有成员数据
     */
    public void getData(int position){
        Map<String,String> param = new HashMap<>();
        param.put(SPCache.KEY_TOKEN,getUserToken());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getFamilyMember(param),
                new CallBack<FamilyMember>() {

                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(FamilyMember data) {
                        if (data.data.data.isEmpty()){
                            MyFamilyInviteActivity.instance(MyFamilyPersonActivity.this);
                            finish();
                        }else {

                            FamilyMember.DataBeanX beanX = data.data;
                            setInviteRightBage(beanX.inviteNum);
                            popupWindow.setBageCount(beanX.inviteNum);
                            url = data.data.introduceUrl;
                            inviteNum = data.data.inviteNum;
                            horizontalAdapter.setNewData(data.data.data);
                            mFamilyMemberList = horizontalAdapter.getData();
                            horizontalAdapter.setIsSelectedId(mFamilyMemberList.get(position).id);

                            mTabFragmentList.clear();
                            for (int index = 0; index < mFamilyMemberList.size(); index++){
                                mTabFragmentList.add(FamilyPersonTabFragment.newInstance(mFamilyMemberList.get(index)));
                            }
                            //设置适配器
                            adapter.notifyPagerAdapter(mViewPager,mTabFragmentList,position);
                        }
                    }
                });
    }


    public void initPopWindow(){
        popupWindow = new FamilyInvitePopupWindow(this,
                new FamilyInvitePopupWindow.OnSelectInviteListener() {
                    @Override
                    public void onInvitefriend() {
                        Intent intent = new Intent(MyFamilyPersonActivity.this,MyFamilyInviteActivity.class);
                        intent.putExtra(IntentParam.TYPE_FAMILY_INVITE_URL, TextUtils.isEmpty(url)?null:url);
                        intent.putExtra(IntentParam.TYPE_FAMILY_INVITE_DESTROY, 1);
                        intent.putExtra(IntentParam.TYPE_FAMILY_INVITE_NUM,inviteNum);
                        startActivity(intent);
                    }

                    @Override
                    public void onNewInvite() {
                        if (!dialog.isShowing()){
                            dialog.show();
                        }
                    }
                });
        setInviteRightBage(0);
        setInviteRightOnClick(v -> {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            int windowWidth = DispUtil.dp2px(this,179);
            int windowHeight = v.getMeasuredHeight();
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY
                    , (x - windowWidth/2), y + windowHeight /2 + DispUtil.dp2px(this,15));
        });

        popupWindow.setBageCount(0);
    }


    public static void instance(Context context){
        context.startActivity(new Intent(context, MyFamilyPersonActivity.class));
    }


    public static void instance(Context context,boolean isScrollLastPosition){
        context.startActivity(new Intent(context, MyFamilyPersonActivity.class)
                .putExtra(IntentParam.FAMILY_PERSON_SCROLL_POSITION,isScrollLastPosition));
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.container){
            mViewPager.setCurrentItem(position,true);
            ((FamilyMemberHorizontalAdapter)adapter).setIsSelectedId(mFamilyMemberList.get(position).id);
        }
    }


}

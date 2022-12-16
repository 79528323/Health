package com.gzhealthy.health.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;

import com.gzhealthy.health.fragment.FamilyPersonTabFragment;
import com.gzhealthy.health.fragment.HomeFragment;
import com.gzhealthy.health.fragment.MineFragment;
import com.gzhealthy.health.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    FragmentManager fm;

    List<FamilyPersonTabFragment> mTabFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public MyFragmentPagerAdapter(@NonNull FragmentManager fm,List<FamilyPersonTabFragment> mTabFragmentList) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
//        this.mTabFragmentList = mTabFragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mTabFragmentList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (!((FamilyPersonTabFragment) object).isAdded() || !mTabFragmentList.contains(object)) {
            return POSITION_NONE;
        }
        //否则就返回列表中的位置
        return mTabFragmentList.indexOf(object);
    }

    @Override
    public int getCount() {
        return mTabFragmentList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        FamilyPersonTabFragment instantiateItemFragment = (FamilyPersonTabFragment) super.instantiateItem(container,position);
        FamilyPersonTabFragment itemtFragment = mTabFragmentList.get(position);
        if (instantiateItemFragment == itemtFragment){
            return instantiateItemFragment;
        }else {
            this.fm.beginTransaction().add(container.getId(),itemtFragment).commitNowAllowingStateLoss();
            return itemtFragment;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (mTabFragmentList.contains(fragment)) {
            super.destroyItem(container, position, object);
            return;
        }
        if (!this.fm.isStateSaved()) {
            this.fm.beginTransaction().remove(fragment).commit();
        }
    }

    public void notifyPagerAdapter(ViewPager mViewPager,List<FamilyPersonTabFragment> mTabFragmentList,int scrollPagePosition){
        if (!this.mTabFragmentList.isEmpty())
            this.mTabFragmentList.clear();
        this.mTabFragmentList.addAll(mTabFragmentList);
        notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(this.mTabFragmentList.size());
        mViewPager.setCurrentItem(scrollPagePosition,true);
    }
}

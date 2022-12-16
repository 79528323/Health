package com.gzhealthy.health.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gzhealthy.health.fragment.HomeFragment;
import com.gzhealthy.health.fragment.MineFragment;
import com.gzhealthy.health.fragment.NewsFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
//    HomeFragment.OnEndCallCallback onEndCallCallback;
    public FragmentAdapter(FragmentManager fm/*, HomeFragment.OnEndCallCallback onEndCallCallback*/) {
        super(fm);
//        this.onEndCallCallback = onEndCallCallback;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position", position + "");
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
//                homeFragment.setOnEndCallCallback(this.onEndCallCallback);
                return homeFragment;
            case 1:
                return new NewsFragment();
            case 2:
                return new MineFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

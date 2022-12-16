package com.gzhealthy.health.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


public class ViewPagerBaseAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> data;
    private ArrayList<String> titles;

    public ViewPagerBaseAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<Fragment> data, ArrayList<String> titles) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
        if (titles != null) {
            this.titles = titles;
        } else {
            this.titles = new ArrayList<>();
        }
        if (data.size() != titles.size()) {
            try {
                throw new Exception("fragment and title length must be equels");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}

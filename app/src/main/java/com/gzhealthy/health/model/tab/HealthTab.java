package com.gzhealthy.health.model.tab;

import androidx.fragment.app.Fragment;

/**
 * Created by Justin_Liu
 * on 2021/3/5
 */
public class HealthTab {
    public String tabName;
    public Class<? extends Fragment> fragment;

    public HealthTab(String tabName, Class<? extends Fragment> fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

//    public Fragment getFragment() {
//        return fragment;
//    }

}

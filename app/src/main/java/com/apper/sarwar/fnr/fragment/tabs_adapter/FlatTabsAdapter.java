package com.apper.sarwar.fnr.fragment.tabs_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apper.sarwar.fnr.fragment.flat.FlatComponentFragment;
import com.apper.sarwar.fnr.fragment.flat.FlatPlanFragment;

public class FlatTabsAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public FlatTabsAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        try {
            switch (position) {

                case 0:
                    FlatComponentFragment buildingComponentFragment = new FlatComponentFragment();
                    return buildingComponentFragment;
                case 1:
                    FlatPlanFragment buildingPlanFragment = new FlatPlanFragment();
                    return buildingPlanFragment;
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}

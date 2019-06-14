package com.apper.sarwar.fnr.fragment.fragement_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apper.sarwar.fnr.fragment.BuildingComponentFragment;
import com.apper.sarwar.fnr.fragment.BuildingFlatFragment;
import com.apper.sarwar.fnr.fragment.BuildingPlanFragment;

public class BuildingTabsAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public BuildingTabsAdapter(FragmentManager fragmentManager, int NoofTabs) {
        super(fragmentManager);
        this.numberOfTabs = NoofTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BuildingFlatFragment buildingFlatFragment = new BuildingFlatFragment();
                return buildingFlatFragment;
            case 1:
                BuildingComponentFragment buildingComponentFragment = new BuildingComponentFragment();
                return buildingComponentFragment;
            case 2:
                BuildingPlanFragment buildingPlanFragment = new BuildingPlanFragment();
                return buildingPlanFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}

package com.apper.sarwar.fnr.fragment.building;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingPlanAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;

import java.util.ArrayList;
import java.util.List;


public class BuildingPlanFragment extends Fragment {


    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingPlanAdapter adapter;
    private View view;

    private List<BuildingPlanModel> buildingPlanModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {
/*
            view = inflater.inflate(R.layout.fragment_building_flat, viewGroup, false);
*/
            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_plan, viewGroup, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.building_plan_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            buildingPlanModels = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                System.out.println("Testing" + i);

                BuildingPlanModel myList = new BuildingPlanModel(
                        i,
                        "Building Plan - " + i
                );
                buildingPlanModels.add(myList);
            }

            adapter = new BuildingPlanAdapter(buildingPlanModels, inflater.getContext());
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }
}

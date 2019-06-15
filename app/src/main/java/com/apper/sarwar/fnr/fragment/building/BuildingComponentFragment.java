package com.apper.sarwar.fnr.fragment.building;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingComponentAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;

import java.util.ArrayList;
import java.util.List;


public class BuildingComponentFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingComponentAdapter adapter;
    private View view;

    private List<BuildingComponentListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.fragment_building_component, viewGroup, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.building_component_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            lists = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                System.out.println("Testing" + i);

                BuildingComponentListModel myList = new BuildingComponentListModel(
                        i,
                        "Building-" + i,
                        "80/100" + i,
                        85

                );
                lists.add(myList);
            }

            adapter = new BuildingComponentAdapter(lists, getActivity());
            recyclerView.setAdapter(adapter);

        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}

package com.apper.sarwar.fnr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.ProjectListAdapter;
import com.apper.sarwar.fnr.model.ProjectListModel;

import java.util.ArrayList;
import java.util.List;


public class BuildingFlatFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProjectListAdapter adapter;
    private View view;

    private List<ProjectListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {
/*
            view = inflater.inflate(R.layout.fragment_building_flat, viewGroup, false);
*/
            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_flat, viewGroup, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.building_flat_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            lists = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                System.out.println("Testing" + i);

                ProjectListModel myList = new ProjectListModel(
                        i,
                        "Scott Bradley",
                        "29-B, North Carolin, USA",
                        "80/100" + i,
                        85

                );
                lists.add(myList);
            }

            adapter = new ProjectListAdapter(lists, inflater.getContext());
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }

}

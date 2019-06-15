package com.apper.sarwar.fnr.fragment.building;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingFlatAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingFlatListModel;

import java.util.ArrayList;
import java.util.List;


public class BuildingFlatFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingFlatAdapter adapter;
    private View view;

    private List<BuildingFlatListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {
/*
            view = inflater.inflate(R.layout.fragment_building_flat, viewGroup, false);
*/
            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_flat, viewGroup, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.building_flat_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


            lists = new ArrayList<>();

            for (int i = 1; i <= 31; i++) {

                System.out.println("Testing" + i);

                BuildingFlatListModel myList = new BuildingFlatListModel(
                        i,
                        "Flat-" + i,
                        "85"

                );
                lists.add(myList);
            }

            adapter = new BuildingFlatAdapter(lists, inflater.getContext());
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }

}

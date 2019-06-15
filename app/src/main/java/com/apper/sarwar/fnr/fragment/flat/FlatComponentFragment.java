package com.apper.sarwar.fnr.fragment.flat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingComponentAdapter;
import com.apper.sarwar.fnr.adapter.flat_adapter.FlatComponentAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;
import com.apper.sarwar.fnr.model.flat_model.FlatComponentListModel;

import java.util.ArrayList;
import java.util.List;


public class FlatComponentFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FlatComponentAdapter adapter;
    private View view;

    private List<FlatComponentListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.fragment_flat_component, viewGroup, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.flat_component_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            lists = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                System.out.println("Testing" + i);

                FlatComponentListModel myList = new FlatComponentListModel(
                        i,
                        "Flat Component",
                        "80/100" + i,
                        85

                );
                lists.add(myList);
            }

            adapter = new FlatComponentAdapter(lists, getActivity());
            recyclerView.setAdapter(adapter);

        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}

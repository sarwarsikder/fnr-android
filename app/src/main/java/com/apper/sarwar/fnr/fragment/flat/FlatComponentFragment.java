package com.apper.sarwar.fnr.fragment.flat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.flat_adapter.FlatComponentAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;
import com.apper.sarwar.fnr.model.flat_model.FlatComponentListModel;
import com.apper.sarwar.fnr.service.api_service.FlatComponentApiService;
import com.apper.sarwar.fnr.service.iservice.FlatComponentIService;
import com.apper.sarwar.fnr.utils.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FlatComponentFragment extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FlatComponentAdapter adapter;
    private View view;
    private FlatComponentApiService flatComponentApiService;

    private List<FlatComponentListModel> lists;
    Loader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.fragment_flat_component, viewGroup, false);

            flatComponentApiService = new FlatComponentApiService(getContext(), new FlatComponentIService() {
                @Override
                public void onFlatComponentSuccess(JSONObject buildingFlatListModel) {


                    try {

                        lists = new ArrayList<>();

                        JSONArray buildingComponentList = (JSONArray) buildingFlatListModel.get("results");


                        for (int i = 0; i < buildingComponentList.length(); i++) {
                            JSONObject row = buildingComponentList.getJSONObject(i);


                            int component_id = (int) row.get("component_id");
                            String name = (String) row.get("name");
                            int total_tasks = (int) row.get("total_tasks");
                            int tasks_done = (int) row.get("tasks_done");


                            System.out.println("Testing" + i);

                            FlatComponentListModel myList = new FlatComponentListModel(
                                    component_id,
                                    name,
                                    total_tasks,
                                    tasks_done

                            );
                            lists.add(myList);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            recyclerView = (RecyclerView) view.findViewById(R.id.flat_component_recycler_view);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                            adapter = new FlatComponentAdapter(lists, getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    });


                }

                @Override
                public void onFlatComponentFailed(JSONObject jsonObject) {

                }
            });

            flatComponentApiService.get_flat_component(7);
            loader.stopLoading();


        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}

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
import com.apper.sarwar.fnr.service.api_service.BuildingComponentApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingComponentIServiceListener;
import com.apper.sarwar.fnr.service.iservice.BuildingFlatIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BuildingComponentFragment extends Fragment {

    RecyclerView recyclerView;
    BuildingFlatIServiceListener buildingFlatIServiceListener;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingComponentAdapter adapter;
    private View view;
    private BuildingComponentApiService buildingComponentApiService;
    private Loader loader;

    private List<BuildingComponentListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.fragment_building_component, viewGroup, false);


            buildingComponentApiService = new BuildingComponentApiService(getActivity(), new BuildingComponentIServiceListener() {

                @Override
                public void onBuildingComponentSuccess(JSONObject buildingComponentListModel) {
                    try {

                        lists = new ArrayList<>();
                        JSONArray buildingComponentList = (JSONArray) buildingComponentListModel.get("results");


                        for (int i = 0; i < buildingComponentList.length(); i++) {
                            JSONObject row = buildingComponentList.getJSONObject(i);


                            int component_id = (int) row.get("component_id");
                            String name = (String) row.get("name");
                            int total_tasks = (int) row.get("total_tasks");
                            int tasks_done = (int) row.get("tasks_done");


                            System.out.println("Testing" + i);

                            BuildingComponentListModel myList = new BuildingComponentListModel(
                                    component_id,
                                    name,
                                    total_tasks,
                                    tasks_done

                            );
                            lists.add(myList);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                recyclerView = (RecyclerView) view.findViewById(R.id.building_component_recycler_view);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                adapter = new BuildingComponentAdapter(lists, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBuildingComponentFailed(JSONObject jsonObject) {

                }
            });

            int BuildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, getContext());
            buildingComponentApiService.get_building_component(BuildingId);
            loader.stopLoading();

        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}

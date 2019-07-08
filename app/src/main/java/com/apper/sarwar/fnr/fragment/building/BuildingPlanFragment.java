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
import com.apper.sarwar.fnr.service.api_service.BuildingPlanApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingPlansIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BuildingPlanFragment extends Fragment {


    RecyclerView recyclerView;
    Loader loader;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingPlanAdapter adapter;
    private View view;
    private BuildingPlanApiService buildingPlanApiService;

    private List<BuildingPlanModel> buildingPlanModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {

            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_plan, viewGroup, false);

            buildingPlanApiService = new BuildingPlanApiService(getActivity(), new BuildingPlansIService() {
                @Override
                public void onBuildingPlanSuccess(JSONObject buildingPlansListModel) {

                    try {

                        buildingPlanModels = new ArrayList<>();
                        JSONArray buildingPlanList = (JSONArray) buildingPlansListModel.get("results");

                        for (int i = 0; i < buildingPlanList.length(); i++) {
                            JSONObject row = buildingPlanList.getJSONObject(i);
                            int plan_id = (int) row.get("id");
                            String plan_name = (String) row.get("title");
                            String plan_file = (String) row.get("plan_file");
                            String file_type = (String) row.get("file_type");


                            BuildingPlanModel myList = new BuildingPlanModel(
                                    plan_id,
                                    plan_name,
                                    plan_file,
                                    file_type
                            );
                            buildingPlanModels.add(myList);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {


                                recyclerView = (RecyclerView) view.findViewById(R.id.building_plan_recycler_view);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                adapter = new BuildingPlanAdapter(buildingPlanModels, getContext());
                                recyclerView.setAdapter(adapter);

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onBuildingPlanFailed(JSONObject jsonObject) {

                }
            });

            int BuildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, getContext());
            buildingPlanApiService.get_building_plan(BuildingId);
            loader.stopLoading();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }
}

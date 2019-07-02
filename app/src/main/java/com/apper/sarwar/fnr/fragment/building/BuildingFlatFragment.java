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
import com.apper.sarwar.fnr.service.api_service.BuildingFlatAPIService;
import com.apper.sarwar.fnr.service.iservice.BuildingFlatIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BuildingFlatFragment extends Fragment {

    RecyclerView recyclerView;
    Loader loader;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingFlatAdapter adapter;
    private View view;
    private BuildingFlatAPIService buildingFlatAPIService;

    private List<BuildingFlatListModel> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        try {
            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_flat, viewGroup, false);



            buildingFlatAPIService = new BuildingFlatAPIService(getActivity(), new BuildingFlatIServiceListener() {

                @Override
                public void onBuildingFlatSuccess(JSONObject buildingFlatListModel) {
                    try {

                        lists = new ArrayList<>();
                        JSONArray buildingFlatList = (JSONArray) buildingFlatListModel.get("results");


                        for (int i = 0; i < buildingFlatList.length(); i++) {
                            JSONObject row = buildingFlatList.getJSONObject(i);


                            int id = (int) row.get("id");
                            String number = (String) row.get("number");
                            String description = (String) row.get("description");
                            String client_name = (String) row.get("client_name");
                            String client_address = (String) row.get("client_address");
                            String client_email = (String) row.get("client_email");
                            String client_tel = (String) row.get("client_tel");
                            int total_tasks = (int) row.get("total_tasks");
                            int tasks_done = (int) row.get("tasks_done");

                            System.out.println("Testing" + i);

                            BuildingFlatListModel myList = new BuildingFlatListModel(
                                    id,
                                    number,
                                    description,
                                    client_name,
                                    client_address,
                                    client_email,
                                    client_tel,
                                    total_tasks,
                                    tasks_done

                            );
                            lists.add(myList);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                recyclerView = (RecyclerView) view.findViewById(R.id.building_flat_recycler_view);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                                adapter = new BuildingFlatAdapter(lists, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        });





                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBuildingFlatFailed(JSONObject jsonObject) {
                    String x = "";

                }
            });
            buildingFlatAPIService.get_building_flat(6);
            loader.stopLoading();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }

}

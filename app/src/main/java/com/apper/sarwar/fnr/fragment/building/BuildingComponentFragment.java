package com.apper.sarwar.fnr.fragment.building;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingComponentPostAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;
import com.apper.sarwar.fnr.service.api_service.BuildingComponentApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingComponentIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingComponentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayoutManager layoutManager;
    private BuildingComponentPostAdapter adapter;
    private View view;
    private BuildingComponentApiService buildingComponentApiService;
    private Loader loader;

    private List<BuildingComponentListModel> lists;
    private Context context;


    @BindView(R.id.building_component_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.buildingComponentListSwipeRefresh)
    SwipeRefreshLayout flatListSwipeRefresh;


    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    private int buildingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.fragment_building_component, viewGroup, false);
            context = inflater.getContext();

            ButterKnife.bind(this, view);

            buildingComponentApiService = new BuildingComponentApiService(getActivity(), new BuildingComponentIServiceListener() {

                @Override
                public void onBuildingComponentSuccess(final JSONObject buildingComponentListModel) {
                    try {


                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    lists = new ArrayList<>();
                                    JSONArray buildingComponentList = (JSONArray) buildingComponentListModel.get("results");


                                    for (int i = 0; i < buildingComponentList.length(); i++) {
                                        JSONObject row = buildingComponentList.getJSONObject(i);


                                        int component_id = (int) row.get("component_id");
                                        String name = (String) row.get("name");

                                        int total_tasks = 0;
                                        if (!row.get("total_tasks").equals(null)) {
                                            total_tasks = (int) row.get("total_tasks");
                                        }

                                        int tasks_done = 0;
                                        if (!row.get("tasks_done").equals(null)) {
                                            tasks_done = (int) row.get("tasks_done");
                                        }


                                        System.out.println("Testing" + i);

                                        BuildingComponentListModel myList = new BuildingComponentListModel(
                                                component_id,
                                                name,
                                                total_tasks,
                                                tasks_done

                                        );
                                        lists.add(myList);
                                    }


                                    if (currentPage != PAGE_START) adapter.removeLoading();
                                    adapter.addAll(lists);
                                    flatListSwipeRefresh.setRefreshing(false);
                                    if (currentPage < totalPage) adapter.addLoading();
                                    else isLastPage = true;
                                    isLoading = false;


                                    adapter.removeLoading();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, 500);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBuildingComponentFailed(JSONObject jsonObject) {
                    try {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.removeLoading();
                            }
                        }, 500);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, getContext());

            flatListSwipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new BuildingComponentPostAdapter(new ArrayList<BuildingComponentListModel>(), context);
            recyclerView.setAdapter(adapter);

            buildingComponentApiService.get_building_component(buildingId, currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    buildingComponentApiService.get_building_component(buildingId, currentPage);
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;

                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });


        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        buildingComponentApiService.get_building_component(buildingId, currentPage);
    }
}

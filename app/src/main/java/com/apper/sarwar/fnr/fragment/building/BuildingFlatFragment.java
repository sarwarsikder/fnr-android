package com.apper.sarwar.fnr.fragment.building;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingFlatPostAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingFlatListModel;
import com.apper.sarwar.fnr.service.api_service.BuildingFlatAPIService;
import com.apper.sarwar.fnr.service.iservice.BuildingFlatIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingFlatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Loader loader;
    private LinearLayoutManager layoutManager;
    private BuildingFlatPostAdapter adapter;
    private View view;
    private BuildingFlatAPIService buildingFlatAPIService;

    private List<BuildingFlatListModel> lists;
    private Context context;


    @BindView(R.id.building_flat_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.flatListSwipeRefresh)
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


            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_flat, viewGroup, false);
            context = inflater.getContext();

            ButterKnife.bind(this, view);

            buildingFlatAPIService = new BuildingFlatAPIService(context, new BuildingFlatIServiceListener() {

                @Override
                public void onBuildingFlatSuccess(final JSONObject buildingFlatListModel) {
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

                            int total_tasks = 0;
                            if (!row.get("total_tasks").equals(null)) {
                                total_tasks = (int) row.get("total_tasks");
                            }

                            int tasks_done = 0;
                            if (!row.get("tasks_done").equals(null)) {
                                tasks_done = (int) row.get("tasks_done");
                            }

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


                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                try {


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
                        }, 1500);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBuildingFlatFailed(JSONObject jsonObject) {
                    try {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.removeLoading();
                            }
                        }, 1500);


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
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            adapter = new BuildingFlatPostAdapter(new ArrayList<BuildingFlatListModel>(), context);
            recyclerView.setAdapter(adapter);

            buildingFlatAPIService.get_building_flat(buildingId, currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    buildingFlatAPIService.get_building_flat(buildingId, currentPage);
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


        } catch (Exception e) {
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
        buildingFlatAPIService.get_building_flat(buildingId, currentPage);
    }
}

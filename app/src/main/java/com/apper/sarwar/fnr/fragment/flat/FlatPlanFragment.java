package com.apper.sarwar.fnr.fragment.flat;

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
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingPlanPostAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;
import com.apper.sarwar.fnr.service.api_service.BuildingPlanApiService;
import com.apper.sarwar.fnr.service.api_service.FlatPlanApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingPlansIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FlatPlanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private LinearLayoutManager layoutManager;
    private BuildingPlanPostAdapter adapter;
    private View view;
    private FlatPlanApiService flatPlanApiService;

    private List<BuildingPlanModel> buildingPlanModels;
    private Context context;


    @BindView(R.id.building_plan_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.planBuildingListSwipeRefresh)
    SwipeRefreshLayout flatListSwipeRefresh;


    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    private int flatId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {

            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_plan, viewGroup, false);
            context = inflater.getContext();

            ButterKnife.bind(this, view);
            flatPlanApiService = new FlatPlanApiService(getActivity(), new BuildingPlansIService() {
                @Override
                public void onBuildingPlanSuccess(final JSONObject buildingPlansListModel) {

                    try {


                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {

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

                                    if (currentPage != PAGE_START) adapter.removeLoading();
                                    adapter.addAll(buildingPlanModels);
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
                public void onBuildingPlanFailed(JSONObject jsonObject) {
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

            flatId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentFlatId, getContext());

            flatListSwipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new BuildingPlanPostAdapter(new ArrayList<BuildingPlanModel>(), context);
            recyclerView.setAdapter(adapter);

            flatPlanApiService.get_flat_plan(flatId, currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    flatPlanApiService.get_flat_plan(flatId, currentPage);
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
        flatPlanApiService.get_flat_plan(flatId, currentPage);
    }
}

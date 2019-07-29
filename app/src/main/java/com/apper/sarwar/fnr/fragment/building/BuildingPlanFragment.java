package com.apper.sarwar.fnr.fragment.building;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.apper.sarwar.fnr.MainActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingPlanPostAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;
import com.apper.sarwar.fnr.service.api_service.BuildingPlanApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingPlansIService;
import com.apper.sarwar.fnr.utils.CheckForSDCard;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingPlanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "";
    private LinearLayoutManager layoutManager;
    private BuildingPlanPostAdapter adapter;
    private View view;
    private BuildingPlanApiService buildingPlanApiService;

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

    private int buildingId;

    ImageView building_file_download;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {


        try {

            view = inflater.from(viewGroup.getContext()).inflate(R.layout.fragment_building_plan, viewGroup, false);
            context = inflater.getContext();

            ButterKnife.bind(this, view);
            buildingPlanApiService = new BuildingPlanApiService(getActivity(), new BuildingPlansIService() {
                @Override
                public void onBuildingPlanSuccess(final JSONObject buildingPlansListModel) {

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


                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                try {
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

            buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, getContext());

            flatListSwipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new BuildingPlanPostAdapter(new ArrayList<BuildingPlanModel>(), context);
            recyclerView.setAdapter(adapter);

            buildingPlanApiService.get_building_plan(buildingId, currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    buildingPlanApiService.get_building_plan(buildingId, currentPage);
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
        buildingPlanApiService.get_building_plan(buildingId, currentPage);
    }


}

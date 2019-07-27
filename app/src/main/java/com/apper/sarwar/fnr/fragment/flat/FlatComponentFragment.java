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
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingComponentPostAdapter;
import com.apper.sarwar.fnr.adapter.flat_adapter.FlatComponentAdapter;
import com.apper.sarwar.fnr.adapter.flat_adapter.FlatComponentPostAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;
import com.apper.sarwar.fnr.model.flat_model.FlatComponentListModel;
import com.apper.sarwar.fnr.service.api_service.FlatComponentApiService;
import com.apper.sarwar.fnr.service.iservice.FlatComponentIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FlatComponentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayoutManager layoutManager;
    private FlatComponentPostAdapter adapter;
    private View view;
    private FlatComponentApiService flatComponentApiService;

    private List<FlatComponentListModel> lists;
    Loader loader;
    private Context context;


    @BindView(R.id.flat_component_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.flat_component_SwipeRefreshLayout)
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

            view = inflater.inflate(R.layout.fragment_flat_component, viewGroup, false);

            context = inflater.getContext();

            ButterKnife.bind(this, view);

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

                            int total_tasks = 0;
                            if (!row.get("total_tasks").equals(null)) {
                                total_tasks = (int) row.get("total_tasks");
                            }

                            int tasks_done = 0;
                            if (!row.get("tasks_done").equals(null)) {
                                tasks_done = (int) row.get("tasks_done");
                            }


                            System.out.println("Testing" + i);

                            FlatComponentListModel myList = new FlatComponentListModel(
                                    component_id,
                                    name,
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
                public void onFlatComponentFailed(JSONObject jsonObject) {
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
            adapter = new FlatComponentPostAdapter(new ArrayList<FlatComponentListModel>(), context);
            recyclerView.setAdapter(adapter);

            flatComponentApiService.get_flat_component(flatId, currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    flatComponentApiService.get_flat_component(flatId, currentPage);
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
        flatComponentApiService.get_flat_component(flatId, currentPage);
    }
}

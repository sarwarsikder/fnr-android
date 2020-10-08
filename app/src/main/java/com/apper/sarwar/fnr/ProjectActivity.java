package com.apper.sarwar.fnr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.project_adapter.ProjectPostListAdapter;
import com.apper.sarwar.fnr.model.project_model.ProjectListModel;
import com.apper.sarwar.fnr.project_swipe.SwipeController;
import com.apper.sarwar.fnr.service.api_service.ProjectApiService;
import com.apper.sarwar.fnr.service.iservice.ProjectIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ProjectIServiceListener {
    private static final String TAG = "ProjectActivity";

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private LinearLayoutManager layoutManager;
    private ProjectPostListAdapter adapter;
    Intent intent;
    Loader loader;
    Context context;
    ProjectApiService projectApiService;

    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    /*Projects Models objects from repository*/

    private List<ProjectListModel> lists;

    SwipeController swipeController = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), ProjectActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_current_activity:
                    /*intent = new Intent(getApplicationContext(), CurrentStateActivity.class);
                    startActivity(intent);*/
                    onCurrentState();
                    return true;
                case R.id.navigation_scan:
                    intent = new Intent(getApplicationContext(), ScanCaptureActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_project);
            ButterKnife.bind(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            // Title and subtitle
            toolbar.setTitle(R.string.title_activity_project);
            toolbar.setBackgroundColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.BLACK);


            final CharSequence title = toolbar.getTitle();
            final ArrayList<View> outViews = new ArrayList<>(1);

            toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
            if (!outViews.isEmpty()) {
                final TextView titleView = (TextView) outViews.get(0);
                titleView.setGravity(Gravity.CENTER_HORIZONTAL);
                final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.setMargins(0, 0, 60, 0);
                toolbar.requestLayout();
            }

            setSupportActionBar(toolbar);

            BottomNavigationView navView = findViewById(R.id.bottom_navigation_drawer);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            context = this;
            swipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ProjectPostListAdapter(new ArrayList<ProjectListModel>(), this);
            recyclerView.setAdapter(adapter);

            /**
             * Calling Project Api to get Project list
             */
            projectApiService = new ProjectApiService(this);
            projectApiService.get_projects(currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {

                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    projectApiService = new ProjectApiService(context);
                    projectApiService.get_projects(currentPage);
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

    }


    public void onCurrentState() {

        String currentState = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.currentState, this);

        if (currentState == null || currentState == "") {
            Toast.makeText(this, "You don't have any activity yet.", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(getApplicationContext(), CurrentStateActivity.class);
            startActivity(intent);
        }

    }


    /**
     * After Api call on success
     * @param  projectListJson
     * @return Return a Adapter Recycle view to load project list in the view.
     */
    @Override
    public void onProjectSuccess(final JSONObject projectListJson) {

        try {

            lists = new ArrayList<>();
            final JSONArray projectList = (JSONArray) projectListJson.get("results");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        for (int i = 0; i < projectList.length(); i++) {
                            JSONObject row = projectList.getJSONObject(i);

                            int id = (int) row.get("id");
                            String project_name = (String) row.get("name");
                            String address = (String) row.get("address");
                            String description = (String) row.get("description");
                            String city = (String) row.get("city");
                            String type = (String) row.get("type");
                            String energetic_standard = (String) row.get("energetic_standard");

                            int total_tasks = 0;
                            if (!row.get("total_tasks").equals(null)) {
                                total_tasks = (int) row.get("total_tasks");

                            }

                            int tasks_done = 0;

                            if (!row.get("tasks_done").equals(null)) {
                                tasks_done = (int) row.get("tasks_done");
                            }


                            ProjectListModel myList = new ProjectListModel(
                                    id,
                                    project_name,
                                    address,
                                    description,
                                    city,
                                    type,
                                    energetic_standard,
                                    total_tasks,
                                    tasks_done


                            );
                            lists.add(myList);
                        }

                        if (currentPage != PAGE_START) adapter.removeLoading();
                        adapter.addAll(lists);
                        swipeRefresh.setRefreshing(false);
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

    /**
     * After Api call on Failed
     * @param  jsonObject
     * @return Handle Project Get API failed error and Adapter Remove Adapter Loading
     */

    @Override
    public void onProjectFailed(JSONObject jsonObject) {
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

    /**
     * Project Screen on refresh
     */
    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        projectApiService = new ProjectApiService(this);
        projectApiService.get_projects(currentPage);
    }
}

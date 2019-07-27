package com.apper.sarwar.fnr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.building_adapter.BuildingPostListAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingListModel;
import com.apper.sarwar.fnr.model.user_model.LoginModel;
import com.apper.sarwar.fnr.service.api_service.BuildingApiService;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingIServiceListener;
import com.apper.sarwar.fnr.service.iservice.LoginIServiceListener;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, BuildingIServiceListener, ProfileIService {

    private static final String TAG = "BuildingListActivity";
    private String productId;

    @BindView(R.id.building_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.buildingSwipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private LinearLayoutManager layoutManager;
    private BuildingPostListAdapter adapter;
    private BuildingApiService buildingApiService;

    private List<BuildingListModel> lists;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;

    private ProfileApiService profileApiService;


    private String flat_number = "";
    private JSONObject currentActivityObject;

    private int building_id;
    private String building_number;


    Intent intent;
    Loader loader;
    int project_id;
    Context context;


    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


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
                    intent = new Intent(getApplicationContext(), CurrentStateActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_scan:
                    intent = new Intent(getApplicationContext(), ScanCaptureActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getApplicationContext(), "Hello Notification!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    Toast.makeText(getApplicationContext(), "Hello Profile!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Title and subtitle
        toolbar.setTitle(R.string.title_activity_building);
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


        toolbar.setNavigationIcon(R.drawable.ic_backwith_circle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ProjectActivity.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });


        BottomNavigationView navView = findViewById(R.id.bottom_navigation_drawer);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        try {

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("PROJECT_DATA");
            int pageId = 1;


            if (bundle != null) {
                project_id = bundle.getInt("EXTRA_PRODUCT_ID");
            } else {
                project_id = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentProjectId, this);
            }

            System.out.println("project_id" + project_id);

/*
            loader.startLoading(this);
*/

            context = this;
            swipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new BuildingPostListAdapter(new ArrayList<BuildingListModel>(), this);
            recyclerView.setAdapter(adapter);

            buildingApiService = new BuildingApiService(context);
            buildingApiService.get_building(project_id, currentPage);


            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage++;
                    buildingApiService = new BuildingApiService(context);
                    buildingApiService.get_building(project_id, currentPage);
                    adapter.addLoading();
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


          /*  buildingApiService = new BuildingApiService(this);
            buildingApiService.get_building(project_id, pageId);*/

           /* adapter = new BuildingListAdapter(lists, this);
            recyclerView.setAdapter(adapter);*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {

            switch (item.getItemId()) {
                case R.id.menu_screen_info:
                    /*initiatePopupWindow();*/
                    profileApiService = new ProfileApiService(this);
                    profileApiService.get_profile();
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // create the popup window

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            /* width = size.x - 50;*/
            System.out.println(width);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            View layout = inflater.inflate(R.layout.popup_menu_screen_info, null);
            layout.setPadding(10, 10, 10, 10);
            mPopupWindow = new PopupWindow(layout, width,
                    height, true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


            layout.findViewById(R.id.popup_menu_screen_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
            btnClosePopup = (ImageView) layout.findViewById(R.id.dismiss);
            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBuildingSuccess(JSONObject buildingListJson) {
        try {

            lists = new ArrayList<>();
            final JSONArray buildingList = (JSONArray) buildingListJson.get("results");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        for (int i = 0; i < buildingList.length(); i++) {
                            JSONObject row = buildingList.getJSONObject(i);

                            int id = (int) row.get("id");
                            String hause_number = (String) row.get("hause_number");
                            String description = (String) row.get("description");
                            String display_number = (String) row.get("display_number");


                            int total_tasks = 0;
                            if (!row.get("total_tasks").equals(null)) {
                                total_tasks = (int) row.get("total_tasks");

                            }

                            int tasks_done = 0;

                            if (!row.get("tasks_done").equals(null)) {
                                tasks_done = (int) row.get("tasks_done");
                            }


                            int total_flats = 0;

                            if (!row.get("total_flats").equals(null)) {
                                total_flats = (int) row.get("total_flats");
                            }


                            BuildingListModel myList = new BuildingListModel(
                                    id,
                                    hause_number,
                                    display_number,
                                    total_tasks,
                                    tasks_done,
                                    total_flats
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
            }, 1500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBuildingFailed(JSONObject jsonObject) {
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

    @Override
    public void onProfileSuccess(JSONObject profileListModel) {

        try {

            String current_activity = (String) profileListModel.get("current_activity");
            currentActivityObject = new JSONObject(current_activity);

            int project_id = (int) currentActivityObject.get("project_id");
            final String projectName = (String) currentActivityObject.get("project_name");
            /*int building_id = (int) currentActivityObject.get("building_id");
            final String building_number = (String) currentActivityObject.get("building_number");
            int flat_id = (int) currentActivityObject.get("flat_id");
            String flat_number = (String) currentActivityObject.get("flat_number");*/

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {


                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        // create the popup window

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        /* width = size.x - 50;*/
                        System.out.println(width);
                        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        View layout = inflater.from(BuildingListActivity.this).inflate(R.layout.popup_menu_screen_info, null);
                        TextView project_name = layout.findViewById(R.id.project_name);

                        TextView building_name = layout.findViewById(R.id.building_name);
                        TextView building_name_txt = layout.findViewById(R.id.building_name_txt);

                        TextView flat_name = layout.findViewById(R.id.flat_name);
                        TextView flat_name_txt = layout.findViewById(R.id.flat_name_txt);
                        project_name.setText(projectName);


                        if (currentActivityObject.has("building_id")) {
                            building_number = (String) currentActivityObject.get("building_number");
                            building_name.setText(building_number);

                        } else {
                            building_name.setVisibility(View.GONE);
                            building_name_txt.setVisibility(View.GONE);

                        }

                        if (currentActivityObject.has("flat_id")) {
                            flat_number = (String) currentActivityObject.get("flat_number");
                            flat_name.setText(flat_number);
                        } else {
                            flat_number = "";
                            flat_name.setVisibility(View.GONE);
                            flat_name_txt.setVisibility(View.GONE);
                        }
/*
                        View layout = inflater.inflate(R.layout.popup_menu_screen_info, null);
*/
                        layout.setPadding(10, 10, 10, 10);
                        mPopupWindow = new PopupWindow(layout, width,
                                height, true);
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


                        layout.findViewById(R.id.popup_menu_screen_info).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                        btnClosePopup = (ImageView) layout.findViewById(R.id.dismiss);
                        btnClosePopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPopupWindow.dismiss();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfileFailed(JSONObject jsonObject) {

    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        buildingApiService = new BuildingApiService(this);
        buildingApiService.get_building(project_id, currentPage);
    }

}
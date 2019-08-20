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
import com.apper.sarwar.fnr.adapter.sub_component.SubComponentPostAdapter;
import com.apper.sarwar.fnr.model.sub_component.SubComponentModel;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.api_service.SubComponentApiService;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.service.iservice.SubComponentIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubComponentActivity extends AppCompatActivity implements SubComponentIService, ProfileIService, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sub_component_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sub_component_SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;


    private LinearLayoutManager layoutManager;
    private SubComponentPostAdapter subComponentAdapter;
    private List<SubComponentModel> list;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;

    Intent intent;
    Loader loader;
    Context context;

    private SubComponentApiService subComponentApiService;
    private ProfileApiService profileApiService;

    private String flat_number = "";
    private JSONObject currentActivityObject;

    private int building_id;
    private String building_number;
    String currentState;
    private int componentId;


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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_component);

        ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        // Title and subtitle
        toolbar.setTitle(R.string.title_activity_sub_component);
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
                Intent intent = new Intent(view.getContext(), BuildingComponentActivity.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });

        BottomNavigationView navView = findViewById(R.id.bottom_navigation_drawer);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        componentId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentComponentId, this);

        currentState = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.currentState, this);


        context = this;
        swipeRefresh.setOnRefreshListener(this);
        /*recyclerView.setHasFixedSize(true);*/
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subComponentAdapter = new SubComponentPostAdapter(new ArrayList<SubComponentModel>(), this);
        recyclerView.setAdapter(subComponentAdapter);

        subComponentApiService = new SubComponentApiService(this);

        if (currentState.equals("building")) {
            int buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, this);
            subComponentApiService.get_sub_component(buildingId, componentId, currentPage);
        } else {
            int flatId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentFlatId, this);
            subComponentApiService.get_sub_component_flat(flatId, componentId, currentPage);
        }

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                subComponentAdapter.addLoading();
                isLoading = true;
                currentPage++;

                if (currentState.equals("building")) {
                    int buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, context);
                    subComponentApiService.get_sub_component(buildingId, componentId, currentPage);
                } else {
                    int flatId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentFlatId, context);
                    subComponentApiService.get_sub_component_flat(flatId, componentId, currentPage);
                }


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

    }


    static String getTimeAgo(long time_ago) {
        time_ago = time_ago / 1000;
        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        // Seconds
        if (seconds <= 60) {
            return "Gerade jetzt";
        }
        //Minutes
        else {
            int minutes = Math.round(time_elapsed / 60);

            if (minutes <= 60) {
                if (minutes == 1) {
                    return "Vor einer Minute";
                } else {
                    return minutes + " Vor ein paar Minuten";
                }
            }
            //Hours
            else {
                int hours = Math.round(time_elapsed / 3600);
                if (hours <= 24) {
                    if (hours == 1) {
                        return "Vor einer Stunde";
                    } else {
                        return hours + " vor std";
                    }
                }
                //Days
                else {
                    int days = Math.round(time_elapsed / 86400);
                    if (days <= 7) {
                        if (days == 1) {
                            return "Gestern";
                        } else {
                            return days + " Vor Tagen";
                        }
                    }
                    //Weeks
                    else {
                        int weeks = Math.round(time_elapsed / 604800);
                        if (weeks <= 4.3) {
                            if (weeks == 1) {
                                return "Vor einer Woche";
                            } else {
                                return weeks + " vor Wochen";
                            }
                        }
                        //Months
                        else {
                            int months = Math.round(time_elapsed / 2600640);
                            if (months <= 12) {
                                if (months == 1) {
                                    return "Vor einem Monat";
                                } else {
                                    return months + " vor wenigen Monaten";
                                }
                            }
                            //Years
                            else {
                                int years = Math.round(time_elapsed / 31207680);
                                if (years == 1) {
                                    return "Vor einem Jahr";
                                } else {
                                    return years + " vor Jahren";
                                }
                            }
                        }
                    }
                }
            }
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

        switch (item.getItemId()) {
            case R.id.menu_screen_info:
                /*initiatePopupWindow();*/
                profileApiService = new ProfileApiService(this);
                profileApiService.get_profile();

                break;

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
    public void onSubComponentSuccess(JSONObject subComponentListModel) {
        try {

            list = new ArrayList<>();
            JSONArray subComponentList = (JSONArray) subComponentListModel.get("results");
            for (int i = 0; i < subComponentList.length(); i++) {
                JSONObject row = subComponentList.getJSONObject(i);

                int id = (int) row.get("id");
                String name = (String) row.get("name");
                String description = (String) row.get("description");

                String update_date_value = (String) row.get("updated_at");
                SimpleDateFormat update_date_format = new SimpleDateFormat("yyyy-MM-dd");
                Date update_date = update_date_format.parse(update_date_value);
                String strDate = getTimeAgo(update_date.getTime());

                Date due_date = new Date();

                if (!row.get("due_date").equals(null)) {
                    String due_date_value = (String) row.get("due_date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    due_date = format.parse(due_date_value);
                } else {
                    due_date = null;
                }

                String task_status = (String) row.get("status");


                SubComponentModel myList = new SubComponentModel(
                        id,
                        name,
                        description,
                        strDate,
                        due_date,
                        task_status

                );
                list.add(myList);
            }

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {

                        if (currentPage != PAGE_START) subComponentAdapter.removeLoading();
                        subComponentAdapter.addAll(list);
                        swipeRefresh.setRefreshing(false);
                        if (currentPage < totalPage) subComponentAdapter.addLoading();
                        else isLastPage = true;
                        isLoading = false;


                        subComponentAdapter.removeLoading();

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
    public void onSubComponentFailed(JSONObject jsonObject) {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    subComponentAdapter.removeLoading();
                }
            }, 500);


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
                        View layout = inflater.from(SubComponentActivity.this).inflate(R.layout.popup_menu_screen_info, null);
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
        subComponentAdapter.clear();

        if (currentState.equals("building")) {
            int buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, this);
            subComponentApiService.get_sub_component(buildingId, componentId, currentPage);
        } else {
            int flatId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentFlatId, this);
            subComponentApiService.get_sub_component_flat(flatId, componentId, currentPage);
        }
    }
}

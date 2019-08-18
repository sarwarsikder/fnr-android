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

import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.notification.NotificationListAdapter;
import com.apper.sarwar.fnr.adapter.notification.NotificationListPostAdapter;
import com.apper.sarwar.fnr.adapter.project_adapter.ProjectPostListAdapter;
import com.apper.sarwar.fnr.model.notification_model.NotificationListModel;
import com.apper.sarwar.fnr.model.project_model.ProjectListModel;
import com.apper.sarwar.fnr.service.api_service.NotificationApiService;
import com.apper.sarwar.fnr.service.api_service.ProjectApiService;
import com.apper.sarwar.fnr.service.iservice.NotificationIService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements NotificationIService, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayoutManager layoutManager;
    private NotificationListPostAdapter adapter;
    Intent intent;
    private List<NotificationListModel> lists;
    private NotificationApiService notificationApiService;
    Context context;


    @BindView(R.id.notification_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.notificationSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;


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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_notification);

            ButterKnife.bind(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            // Title and subtitle
            toolbar.setTitle(R.string.title_notifications);
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
            adapter = new NotificationListPostAdapter(new ArrayList<NotificationListModel>(), this);
            recyclerView.setAdapter(adapter);


            notificationApiService = new NotificationApiService(this);
            notificationApiService.get_notification(currentPage);

            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {

                    adapter.addLoading();
                    isLoading = true;
                    currentPage++;
                    notificationApiService.get_notification(currentPage);
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
    public void onNotificationSuccess(JSONObject notificationListModel) {


        try {


            try {

                lists = new ArrayList<>();
                JSONArray notificationList = (JSONArray) notificationListModel.get("results");


                lists = new ArrayList<>();

                for (int i = 0; i < notificationList.length(); i++) {
                    JSONObject row = notificationList.getJSONObject(i);

                    String text = (String) row.get("text");
                    int task_id = (int) row.get("task_id");
                    String avatar = (String) row.get("avatar");

                    String due_date_value = (String) row.get("sending_at");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(due_date_value);
                    String strDate = getTimeAgo(date.getTime());


                    NotificationListModel myList = new NotificationListModel(
                            task_id,
                            "",
                            "Test User",
                            avatar,
                            text,
                            strDate
                    );
                    lists.add(myList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {

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

    @Override
    public void onNotificationFailed(JSONObject jsonObject) {
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

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        notificationApiService.get_notification(currentPage);
    }
}

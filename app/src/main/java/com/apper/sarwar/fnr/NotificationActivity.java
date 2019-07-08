package com.apper.sarwar.fnr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import com.apper.sarwar.fnr.adapter.notification.NotificationListAdapter;
import com.apper.sarwar.fnr.model.notification_model.NotificationListModel;
import com.apper.sarwar.fnr.service.api_service.NotificationApiService;
import com.apper.sarwar.fnr.service.iservice.NotificationIService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationIService {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NotificationListAdapter adapter;
    Intent intent;
    private List<NotificationListModel> lists;
    private NotificationApiService notificationApiService;


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
                    return true;
                case R.id.navigation_scan:
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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_notification);

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


            notificationApiService = new NotificationApiService(this);
            notificationApiService.get_notification(1);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNotificationSuccess(JSONObject notificationListModel) {


        try {

            try {

                lists = new ArrayList<>();

                for (int i = 1; i <= 10; i++) {
                    NotificationListModel myList = new NotificationListModel(
                            i,
                            "",
                            "Test User",
                            "",
                            "Michel Sergio comments on your post",
                            "39 minutes ago"
                    );
                    lists.add(myList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    try {

                        recyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        adapter = new NotificationListAdapter(lists, getApplicationContext());
                        recyclerView.setAdapter(adapter);

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
    public void onNotificationFailed(JSONObject jsonObject) {

    }
}

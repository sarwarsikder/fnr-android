package com.apper.sarwar.fnr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.apper.sarwar.fnr.adapter.building_adapter.BuildingListAdapter;
import com.apper.sarwar.fnr.model.building_model.BuildingListModel;
import com.apper.sarwar.fnr.service.api_service.BuildingApiService;
import com.apper.sarwar.fnr.service.iservice.BuildingIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuildingListActivity extends AppCompatActivity implements BuildingIServiceListener {

    private static final String TAG = "BuildingListActivity";
    private String productId;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingListAdapter adapter;
    private BuildingApiService buildingApiService;

    private List<BuildingListModel> lists;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;

    Intent intent;
    Loader loader;


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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

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

            /*recyclerView = (RecyclerView) findViewById(R.id.building_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            lists = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                System.out.println("Testing" + i);

                BuildingListModel myList = new BuildingListModel(
                        i,
                        "Haus-" + i,
                        "House 56",
                        "19",
                        "27",
                        "10"

                );
                lists.add(myList);
            }

*//*
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int productId = extras.getInt("EXTRA_PRODUCT_ID");*/

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("PROJECT_DATA");
            int project_id = bundle.getInt("EXTRA_PRODUCT_ID");
            int pageId = 1;

            System.out.println("project_id" + project_id);

            loader.startLoading(this);

            buildingApiService = new BuildingApiService(this);
            buildingApiService.get_building(project_id, pageId);

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

        switch (item.getItemId()) {
            case R.id.menu_screen_info:
                initiatePopupWindow();
                Toast.makeText(this, "You clicked menu info", Toast.LENGTH_SHORT).show();
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
    public void onBuildingSuccess(JSONObject buildingListJson) {
        try {

            lists = new ArrayList<>();
            JSONArray buildingList = (JSONArray) buildingListJson.get("results");
            for (int i = 0; i < buildingList.length(); i++) {
                JSONObject row = buildingList.getJSONObject(i);

                int id = (int) row.get("id");
                String hause_number = (String) row.get("hause_number");
                String description = (String) row.get("description");
                String display_number = (String) row.get("display_number");
                int total_tasks = (int) row.get("total_tasks");
                int tasks_done = (int) row.get("tasks_done");
                int total_flats = (int) row.get("total_flats");

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

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    recyclerView = (RecyclerView) findViewById(R.id.building_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                    adapter = new BuildingListAdapter(lists, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
            });


            loader.stopLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBuildingFailed(JSONObject jsonObject) {

    }
}

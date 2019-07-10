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

import com.apper.sarwar.fnr.adapter.sub_component.SubComponentAdapter;
import com.apper.sarwar.fnr.model.sub_component.SubComponentModel;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.api_service.SubComponentApiService;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.service.iservice.SubComponentIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubComponentActivity extends AppCompatActivity implements SubComponentIService, ProfileIService {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SubComponentAdapter subComponentAdapter;
    private List<SubComponentModel> list;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;

    Intent intent;
    Loader loader;

    private SubComponentApiService subComponentApiService;
    private ProfileApiService profileApiService;

    private String flat_number = "";
    private JSONObject currentActivityObject;

    private int building_id;
    private String building_number;


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
        setContentView(R.layout.activity_sub_component);

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

        int componentId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentComponentId, this);

        String currentState = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.currentState, this);


        subComponentApiService = new SubComponentApiService(this);

        if (currentState.equals("building")) {
            int buildingId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentBuildingId, this);
            subComponentApiService.get_sub_component(buildingId, componentId);
        } else {
            int flatId = SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentFlatId, this);
            subComponentApiService.get_sub_component_flat(flatId, componentId);
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
                SubComponentModel myList = new SubComponentModel(
                        id,
                        name,
                        description,
                        "2d ago"

                );
                list.add(myList);
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    recyclerView = (RecyclerView) findViewById(R.id.sub_component_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    subComponentAdapter = new SubComponentAdapter(list, getApplicationContext());
                    recyclerView.setAdapter(subComponentAdapter);

/*
                    loader.stopLoading();
*/

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubComponentFailed(JSONObject jsonObject) {

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
}

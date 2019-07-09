package com.apper.sarwar.fnr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import com.apper.sarwar.fnr.fragment.tabs_adapter.FlatTabsAdapter;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;

import org.json.JSONObject;

import java.util.ArrayList;

public class FlatComponentActivity extends AppCompatActivity implements ProfileIService {

    TabLayout flat_tab_layout;
    ViewPager pager;
    Intent intent;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;

    private ProfileApiService profileApiService;


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

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_flat_component);


            Toolbar toolbar = findViewById(R.id.toolbar);
            // Title and subtitle
            toolbar.setTitle(R.string.title_activity_flat_component);
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


            flat_tab_layout = (TabLayout) findViewById(R.id.flat_tabs);

            flat_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
            final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_flat);
            FlatTabsAdapter tabsAdapter = new FlatTabsAdapter(getSupportFragmentManager(), flat_tab_layout.getTabCount());

            viewPager.setAdapter(tabsAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(flat_tab_layout));

            flat_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

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
/*
                initiatePopupWindow();
*/
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
    public void onProfileSuccess(JSONObject profileListModel) {
        try {

            String current_activity = (String) profileListModel.get("current_activity");
            JSONObject currentActivityObject = new JSONObject(current_activity);

            int project_id = (int) currentActivityObject.get("project_id");
            final String projectName = (String) currentActivityObject.get("project_name");
            int building_id = (int) currentActivityObject.get("building_id");
            final String building_number = (String) currentActivityObject.get("building_number");
            int flat_id = (int) currentActivityObject.get("flat_id");
            final String flat_number = (String) currentActivityObject.get("flat_number");

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
                        View layout = inflater.from(FlatComponentActivity.this).inflate(R.layout.popup_menu_screen_info, null);
                        TextView project_name = layout.findViewById(R.id.project_name);
                        TextView building_name = layout.findViewById(R.id.building_name);
                        TextView flat_name = layout.findViewById(R.id.flat_name);
                        project_name.setText(projectName);
                        building_name.setText(building_number);
                        flat_name.setText(flat_number);
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



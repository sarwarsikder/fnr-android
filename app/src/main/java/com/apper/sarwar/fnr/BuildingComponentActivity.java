package com.apper.sarwar.fnr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BuildingComponentActivity extends AppCompatActivity {

    TabLayout building_tabs;
    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_component);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Title and subtitle
        toolbar.setTitle(R.string.title_activity_building_component);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.BLACK);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BuildingListActivity.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });


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

        building_tabs = (TabLayout) findViewById(R.id.building_tabs);

        building_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(BuildingComponentActivity.this, "Wohnungen", Toast.LENGTH_SHORT).show();
                        return;
                    case 1:
                        Toast.makeText(BuildingComponentActivity.this, "Katagorie", Toast.LENGTH_SHORT).show();
                        return;
                    case 2:
                        Toast.makeText(BuildingComponentActivity.this, "Plannen", Toast.LENGTH_SHORT).show();
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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
                Toast.makeText(this, "You clicked menu screen info", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}

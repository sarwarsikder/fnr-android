package com.apper.sarwar.fnr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apper.sarwar.fnr.adapter.BuildingListAdapter;
import com.apper.sarwar.fnr.model.BuildingListModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BuildingListAdapter adapter;

    private List<BuildingListModel> lists;


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


        recyclerView = (RecyclerView) findViewById(R.id.building_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lists = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {

            System.out.println("Testing" + i);

            BuildingListModel myList = new BuildingListModel(
                    i,
                    "Haus-" + i,
                    "19 task",
                    "27"

            );
            lists.add(myList);
        }

        adapter = new BuildingListAdapter(lists, this);
        recyclerView.setAdapter(adapter);
    }
}

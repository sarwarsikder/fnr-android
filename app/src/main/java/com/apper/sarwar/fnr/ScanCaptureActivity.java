package com.apper.sarwar.fnr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.service.api_service.ScanApiService;
import com.apper.sarwar.fnr.service.iservice.ScanIService;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCaptureActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ScanIService {
    private ZXingScannerView mScannerView;
    Loader loader;
    private ScanApiService scanApiService;
    JSONObject project;
    JSONObject building;
    JSONObject flat;
    Context context;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan_capture);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Title and subtitle
        toolbar.setTitle(R.string.title_activity_scan_capture);
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
                Intent intent = new Intent(view.getContext(), ScanActivity.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        context = this;

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        String scanData = rawResult.getBarcodeFormat().toString();
        String scanData1 = rawResult.getText();

        if (scanData.isEmpty()) {
            Toast.makeText(this, "Empty scan content!", Toast.LENGTH_SHORT).show();
        } else {
            scanApiService = new ScanApiService(this);
            scanApiService.get_scan_data(scanData1);
        }


        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
      /*  // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                *//*mScannerView.resumeCameraPreview(ScanCaptureActivity.this);*//*
                Intent intent = new Intent(getApplicationContext(), BuildingComponentActivity.class);
                getApplicationContext().startActivity(intent);
                finish();
            }
        }, 2000);*/
    }


    @Override
    public void onScanSuccess(JSONObject subScanListModel) {

        try {

            project = (JSONObject) subScanListModel.get("project");

            building = new JSONObject();
            if (!subScanListModel.get("building").equals(null)) {
                building = (JSONObject) subScanListModel.get("building");
            }

            flat = new JSONObject();
            if (!subScanListModel.get("flat").equals(null)) {
                flat = (JSONObject) subScanListModel.get("flat");

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        if (project.length() > 0) {
                            int projectId = (int) project.get("id");

                            System.out.println("Hello Project ID " + projectId);
                            SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentProjectId, projectId, context);
                            if (flat.length() > 0) {
                                int flatId = (int) flat.get("id");
                                SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentFlatId, flatId, context);
                                loader.startLoading(context);
                                Intent intent = new Intent(context
                                        , FlatComponentActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                int buildingId = (int) building.get("id");
                                SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentBuildingId, buildingId, context);
                                loader.startLoading(context);
                                Intent intent = new Intent(context
                                        , BuildingComponentActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(ScanCaptureActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                        }

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
    public void onScanFailed(JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanCaptureActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


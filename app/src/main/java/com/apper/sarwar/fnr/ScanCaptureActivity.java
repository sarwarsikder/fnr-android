package com.apper.sarwar.fnr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.utils.Loader;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCaptureActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Loader loader;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan_capture);
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
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        loader.startLoading(this);
        Intent intent = new Intent(this
                , BuildingComponentActivity.class);
        startActivity(intent);
        finish();

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

}


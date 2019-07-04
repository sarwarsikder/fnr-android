package com.apper.sarwar.fnr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.utils.Loader;

public class ScanActivity extends AppCompatActivity {
    Button scan_button;
    TextView scan_skip_button;
    Loader loader;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        scan_button = (Button) findViewById(R.id.scan_button);
        scan_skip_button = (TextView) findViewById(R.id.scan_skip_button);

       /* scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ScanCaptureActivity.class);
                view.getContext().startActivity(intent);

            }
        });*/

        scan_skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProjectActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }


    public void launchSimpleActivity(View v) {
        launchActivity(ScanCaptureActivity.class);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


}

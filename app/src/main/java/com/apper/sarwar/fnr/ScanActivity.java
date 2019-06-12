package com.apper.sarwar.fnr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScanActivity extends AppCompatActivity {
    Button scan_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan_button = (Button) findViewById(R.id.scan_button);

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch barcode activity.

            }
        });
    }
}

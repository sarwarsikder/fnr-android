package com.apper.sarwar.fnr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apper.sarwar.fnr.utils.Loader;

public class ScanActivity extends AppCompatActivity {
    Button scan_button;
    TextView scan_skip_button;
    Loader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        scan_button = (Button) findViewById(R.id.scan_button);
        scan_skip_button = (TextView) findViewById(R.id.scan_skip_button);

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ScanCaptureActivity.class);
                view.getContext().startActivity(intent);

            }
        });

        scan_skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProjectActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }


}

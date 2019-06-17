package com.apper.sarwar.fnr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apper.sarwar.fnr.utils.Loader;

public class LogInActivity extends AppCompatActivity {

    TextView go_sign_up_button, sign_in;
    Loader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sign_in = (Button) findViewById(R.id.sign_in);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.startLoading(view.getContext());
                Intent intent = new Intent(view.getContext(), ScanActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}

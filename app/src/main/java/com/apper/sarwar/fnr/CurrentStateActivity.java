package com.apper.sarwar.fnr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

public class CurrentStateActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {

            String currentState = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.currentState, this);

            currentState = currentState.toString();
            if (currentState.equals("building")) {
                intent = new Intent(this, BuildingComponentActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(this, FlatComponentActivity.class);
                startActivity(intent);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

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
        String currentState = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.currentState, this).toString();

        if (currentState.equals(null) || currentState == "") {
            Toast.makeText(this, "You don't have any activity yet.", Toast.LENGTH_SHORT).show();
        }


        if (currentState.equals("building")) {
            intent = new Intent(this, BuildingComponentActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, FlatComponentActivity.class);
            startActivity(intent);
            finish();
        }


    }
}

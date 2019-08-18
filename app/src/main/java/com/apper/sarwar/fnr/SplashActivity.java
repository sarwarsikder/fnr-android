package com.apper.sarwar.fnr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.apper.sarwar.fnr.model.user_model.LoginModel;
import com.apper.sarwar.fnr.service.api_service.RefreshTokenApiService;
import com.apper.sarwar.fnr.service.iservice.RefreshTokenListener;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity implements RefreshTokenListener {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private RefreshTokenApiService refreshTokenApiService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        context = this;


        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

               /* Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                finish();*/

                if (SharedPreferenceUtil.isLoggedIn(getApplicationContext())) {
                    refreshTokenApiService = new RefreshTokenApiService(context);
                    refreshTokenApiService.get_refresh_token();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }).start();


    }

    @Override
    public void onRefreshTokenSuccess(final LoginModel jsonObject) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.access_token, jsonObject.getAccess_token(), getApplicationContext());
                SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.refresh_token, jsonObject.getRefresh_token(), getApplicationContext());

                if (SharedPreferenceUtil.isLoggedIn(getApplicationContext())) {
                    Intent intentData = getIntent();
                    if (intentData.hasExtra("pushnotification")) {
                        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRefreshTokenFailed(JSONObject jsonObject) {

    }
}

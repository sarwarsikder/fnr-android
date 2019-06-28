package com.apper.sarwar.fnr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apper.sarwar.fnr.model.LoginModel;
import com.apper.sarwar.fnr.service.api_service.LoginApiService;
import com.apper.sarwar.fnr.service.iservice.LoginIServiceListener;
import com.apper.sarwar.fnr.utils.Loader;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity implements LoginIServiceListener {

    private static final String TAG = "LogInActivity";
    TextView go_sign_up_button, sign_in;
    Loader loader;
    LoginApiService loginApiService;
    EditText user_name, password;
    String text_user_name, text_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginApiService = new LoginApiService(this);

        sign_in = (Button) findViewById(R.id.sign_in);
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);


        try {
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    text_user_name = user_name.getText().toString().trim();
                    text_password = password.getText().toString().trim();

                    loader.startLoading(view.getContext());
                    loginApiService.login(text_user_name, text_password);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoginSuccess(LoginModel loginModel) {
        loader.stopLoading();
        Log.d(TAG, "onLoginSuccess: " + loginModel.getAccess_token());
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.access_token, loginModel.getAccess_token(), this);

        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onLoginFailed(JSONObject jsonObject) {
        loader.stopLoading();
    }
}

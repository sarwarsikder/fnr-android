package com.apper.sarwar.fnr;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.adapter.sub_component.CommentAdapter;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.CommentModel;
import com.apper.sarwar.fnr.model.sub_component.CommentUser;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.api_service.SubComponentDetailApiService;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.service.iservice.SubComponentDetailIService;
import com.apper.sarwar.fnr.utils.Image;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComponentDetailActivity extends AppCompatActivity implements SubComponentDetailIService, ProfileIService {

    Intent intent;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;
    private SubComponentDetailApiService subComponentDetailApiService;

    TextView assignee_name, task_name, task_detail, due_date, comment_counter;
    ImageView create_button, create_image_button;
    EditText comment_text;

    private ProfileApiService profileApiService;

    private String flat_number = "";
    private JSONObject currentActivityObject;

    int subComponentId;
    private String building_number;

    private String name, description, due_date_value, assign_to, strDate, user_image;
    private int comment_count;


    private JSONObject assign_to_data = new JSONObject();
    private JSONArray comments_data = new JSONArray();
    ImageView assignee_image;
    private AppConfigRemote appConfigRemote;

    private List<CommentModel> commentModels;

    private RecyclerView recyclerViewComment;
    private CommentAdapter adapterComment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), ProjectActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_current_activity:
                    intent = new Intent(getApplicationContext(), CurrentStateActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_scan:
                    intent = new Intent(getApplicationContext(), ScanCaptureActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getApplicationContext(), "Hello Notification!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    Toast.makeText(getApplicationContext(), "Hello Profile!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_component_detail);


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


            Toolbar toolbar = findViewById(R.id.toolbar);
            // Title and subtitle
            toolbar.setTitle(R.string.title_activity_component_detail);
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

            toolbar.setNavigationIcon(R.drawable.ic_back_cross_circle);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SubComponentActivity.class);
                    view.getContext().startActivity(intent);
                    finish();
                }
            });

            appConfigRemote = new AppConfigRemote();

            subComponentId = (int) SharedPreferenceUtil.getDefaultsId(SharedPreferenceUtil.currentSubComponentId, this);

            subComponentDetailApiService = new SubComponentDetailApiService(this);

            subComponentDetailApiService.get_sub_component_details(subComponentId);


            create_button = (ImageView) findViewById(R.id.create_button);
            comment_text = (EditText) findViewById(R.id.comment_text);

            create_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String commentTxt = (String) comment_text.getText().toString();
                    subComponentDetailApiService.create_comment(subComponentId, commentTxt, "");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            });

            create_image_button = (ImageView) findViewById(R.id.create_image_button);
            create_image_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "select multiple images"), 1);
                }
            });


/*
            subComponentDetailApiService.get_sub_component_details(268);
*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int c = 0; c < clipData.getItemCount(); c++) {
                    String imagePath = Image.getAndroidImagePath(ComponentDetailActivity.this, clipData.getItemAt(c).getUri());
                    Log.i("Imager", imagePath);

                }
            } else if (data.getData() != null) {
                String imagePath = Image.getAndroidImagePath(ComponentDetailActivity.this, data.getData());
                Log.i("Imager", imagePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_screen_info:
                /*initiatePopupWindow();*/
                profileApiService = new ProfileApiService(this);
                profileApiService.get_profile();
                break;

        }
        return true;
    }

    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // create the popup window

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            /* width = size.x - 50;*/
            System.out.println(width);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            View layout = inflater.inflate(R.layout.popup_menu_screen_info, null);
            layout.setPadding(10, 10, 10, 10);
            mPopupWindow = new PopupWindow(layout, width,
                    height, true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


            layout.findViewById(R.id.popup_menu_screen_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
            btnClosePopup = (ImageView) layout.findViewById(R.id.dismiss);
            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void createComment(View view) {
        try {
            create_button = (ImageView) findViewById(R.id.create_button);
            create_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment_text = (EditText) findViewById(R.id.comment_text);
                    String commentTxt = comment_text.getText().toString();
                    subComponentDetailApiService = new SubComponentDetailApiService(getApplicationContext());
                    subComponentDetailApiService.create_comment(subComponentId, commentTxt, "");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onSubComponentDetailSuccess(final JSONObject subComponentDetailsListModel) {

        try {

            name = (String) subComponentDetailsListModel.get("name");
            description = (String) subComponentDetailsListModel.get("description");

            if (!subComponentDetailsListModel.get("due_date").equals(null)) {
                due_date_value = (String) subComponentDetailsListModel.get("due_date");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(due_date_value);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                strDate = formatter.format(date);


                System.out.println("Date Format with dd MMMM yyyy : " + date);
            }

            comment_count = (int) subComponentDetailsListModel.get("total_comments");

            assign_to = "";


            if (!subComponentDetailsListModel.get("assign_to").equals(null)) {
                final JSONObject assign_to_data = (JSONObject) subComponentDetailsListModel.get("assign_to");

                if (assign_to_data.length() > 0) {
                    assign_to = (String) assign_to_data.get("company_name");
                }

                if (assign_to_data.has("avatar") && assign_to_data.get("avatar") != "") {
                    user_image = (String) assign_to_data.get("avatar");
                }


            }


            comments_data = (JSONArray) subComponentDetailsListModel.get("comments");

            commentModels = new ArrayList<>();


            if (comments_data.length() > 0) {


                for (int i = 0; i < comments_data.length(); i++) {
                    JSONObject row = comments_data.getJSONObject(i);

                    String text = (String) row.get("text");
                    String type = (String) row.get("type");
/*
                    String file_type = (String) row.get("file_type");
*/
                    String file_type = "";
                    JSONObject commentUserJsonArray = (JSONObject) row.get("user");

                    String cUserName = "";
                    String avatar = "";

                    if (commentUserJsonArray.length() > 0) {
                        cUserName = (String) commentUserJsonArray.get("name");
                        avatar = (String) commentUserJsonArray.get("avatar");
                    }

                    CommentUser commentUser = new CommentUser(
                            cUserName,
                            avatar
                    );

                    CommentModel commentModel = new CommentModel(
                            text,
                            type,
                            file_type,
                            commentUser

                    );

                    commentModels.add(commentModel);

                }

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    assignee_name = (TextView) findViewById(R.id.assignee_name);
                    task_name = (TextView) findViewById(R.id.task_name);
                    task_detail = (TextView) findViewById(R.id.task_detail);
                    task_detail = (TextView) findViewById(R.id.task_detail);
                    due_date = (TextView) findViewById(R.id.due_date);
                    assignee_image = (ImageView) findViewById(R.id.assignee_image);


                    comment_counter = (TextView) findViewById(R.id.comment_counter);


                    assignee_name.setText(assign_to);
                    task_name.setText(name);
                    task_detail.setText(description);
                    due_date.setText(strDate);
                    comment_counter.setText("Nachrichten(" + comment_count + ")");


                    Picasso.with(getApplicationContext())
                            .load(appConfigRemote.getBASE_URL() + "" + user_image)
                            .placeholder(R.drawable.fnr_logo)
                            .resize(106, 106)
                            .into(assignee_image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) assignee_image.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    assignee_image.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError() {
                                    assignee_image.setImageResource(R.drawable.ic_man_user);
                                }
                            });

                    recyclerViewComment = (RecyclerView) findViewById(R.id.comment_recycler_view);
                    recyclerViewComment.setHasFixedSize(true);
                    recyclerViewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    if (!commentModels.equals(null)) {
                        adapterComment = new CommentAdapter(commentModels, getApplicationContext());
                        recyclerViewComment.setAdapter(adapterComment);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubComponentDetailFailed(JSONObject jsonObject) {

    }

    @Override
    public void OnCommentCreateSuccess(JSONObject subComponentModel) {

        try {


            commentModels = new ArrayList<>();

            comments_data = (JSONArray) subComponentModel.get("results");
            comment_count = (int) subComponentModel.get("count");


            if (comments_data.length() > 0) {

                for (int i = 0; i < comments_data.length(); i++) {
                    JSONObject row = comments_data.getJSONObject(i);


                    String text = (String) row.get("text");
                    String type = (String) row.get("type");
/*
                    String file_type = (String) row.get("file_type");
*/
                    String file_type = "";
                    JSONObject commentUserJsonArray = (JSONObject) row.get("user");

                    String cUserName = "";
                    String avatar = "";

                    if (commentUserJsonArray.length() > 0) {
                        cUserName = (String) commentUserJsonArray.get("name");
                        avatar = (String) commentUserJsonArray.get("avatar");
                    }

                    CommentUser commentUser = new CommentUser(
                            cUserName,
                            avatar
                    );

                    CommentModel commentModel = new CommentModel(
                            text,
                            type,
                            file_type,
                            commentUser

                    );

                    commentModels.add(commentModel);


                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    comment_counter = (TextView) findViewById(R.id.comment_counter);
                    comment_counter.setText("Nachrichten(" + comment_count + ")");


                    comment_text.setText("");
                    recyclerViewComment = (RecyclerView) findViewById(R.id.comment_recycler_view);
                    recyclerViewComment.setHasFixedSize(true);
                    recyclerViewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    if (!commentModels.equals(null)) {
                        adapterComment = new CommentAdapter(commentModels, getApplicationContext());
                        recyclerViewComment.setAdapter(adapterComment);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCommentCreateFailed(JSONObject jsonObject) {

    }

    @Override
    public void onProfileSuccess(JSONObject profileListModel) {

        try {

            String current_activity = (String) profileListModel.get("current_activity");
            currentActivityObject = new JSONObject(current_activity);

            int project_id = (int) currentActivityObject.get("project_id");
            final String projectName = (String) currentActivityObject.get("project_name");
            /*int building_id = (int) currentActivityObject.get("building_id");
            final String building_number = (String) currentActivityObject.get("building_number");
            int flat_id = (int) currentActivityObject.get("flat_id");
            String flat_number = (String) currentActivityObject.get("flat_number");*/

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {


                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        // create the popup window

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        /* width = size.x - 50;*/
                        System.out.println(width);
                        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        View layout = inflater.from(ComponentDetailActivity.this).inflate(R.layout.popup_menu_screen_info, null);
                        TextView project_name = layout.findViewById(R.id.project_name);

                        TextView building_name = layout.findViewById(R.id.building_name);
                        TextView building_name_txt = layout.findViewById(R.id.building_name_txt);

                        TextView flat_name = layout.findViewById(R.id.flat_name);
                        TextView flat_name_txt = layout.findViewById(R.id.flat_name_txt);
                        project_name.setText(projectName);


                        if (currentActivityObject.has("building_id")) {
                            building_number = (String) currentActivityObject.get("building_number");
                            building_name.setText(building_number);

                        } else {
                            building_name.setVisibility(View.GONE);
                            building_name_txt.setVisibility(View.GONE);

                        }

                        if (currentActivityObject.has("flat_id")) {
                            flat_number = (String) currentActivityObject.get("flat_number");
                            flat_name.setText(flat_number);
                        } else {
                            flat_number = "";
                            flat_name.setVisibility(View.GONE);
                            flat_name_txt.setVisibility(View.GONE);
                        }
/*
                        View layout = inflater.inflate(R.layout.popup_menu_screen_info, null);
*/
                        layout.setPadding(10, 10, 10, 10);
                        mPopupWindow = new PopupWindow(layout, width,
                                height, true);
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


                        layout.findViewById(R.id.popup_menu_screen_info).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                        btnClosePopup = (ImageView) layout.findViewById(R.id.dismiss);
                        btnClosePopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPopupWindow.dismiss();

                            }
                        });

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
    public void onProfileFailed(JSONObject jsonObject) {

    }
}

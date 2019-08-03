package com.apper.sarwar.fnr;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.adapter.PaginationScrollListener;
import com.apper.sarwar.fnr.adapter.sub_component.CommentAdapter;
import com.apper.sarwar.fnr.adapter.sub_component.CommentPostAdapter;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.datetimepicker.DatePickerIService;
import com.apper.sarwar.fnr.datetimepicker.DateTimePickerFragment;
import com.apper.sarwar.fnr.model.sub_component.CommentModel;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentsModel;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsModel;
import com.apper.sarwar.fnr.model.sub_component.TaskStatusModel;
import com.apper.sarwar.fnr.service.api_service.ProfileApiService;
import com.apper.sarwar.fnr.service.api_service.SubComponentDetailApiService;
import com.apper.sarwar.fnr.service.iservice.ProfileIService;
import com.apper.sarwar.fnr.service.iservice.SubComponentDetailIService;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComponentDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SubComponentDetailIService, ProfileIService, DatePickerIService {

    Intent intent;

    private PopupWindow mPopupWindow;
    private ImageView btnClosePopup;
    private SubComponentDetailApiService subComponentDetailApiService;

    TextView assignee_name, task_name, task_detail, due_date, comment_counter;
    Spinner task_status;
    ImageView create_button, create_image_button;
    EditText comment_text;
    private String commentText = "";

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
    TaskDetailsModel taskDetailsModel;
    ArrayList<TaskDetailsCommentsModel> taskDetailsCommentsModel = new ArrayList<TaskDetailsCommentsModel>();

    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText hiddenDate;
    ImageView due_date_image;
    DatePickerDialog datePickerDialog;
    List<String> varSpinnerData;
    private boolean isSpinnerInitial = true;

    @BindView(R.id.comment_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.commentSwipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    Context context;
    private LinearLayoutManager layoutManager;
    private CommentPostAdapter adapter;

    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


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
            ButterKnife.bind(this);


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);


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
            /*subComponentDetailApiService.get_sub_component_details(350);*/
            /*subComponentDetailApiService.get_sub_component_details(267);*/


            context = this;
            swipeRefresh.setOnRefreshListener(this);
            /*recyclerView.setHasFixedSize(true);*/
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new CommentPostAdapter(new ArrayList<TaskDetailsCommentsModel>(), this);
            recyclerView.setAdapter(adapter);

            try {

                subComponentDetailApiService = new SubComponentDetailApiService(this);
                subComponentDetailApiService.get_sub_component_comment(subComponentId, currentPage);

                recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                    @Override
                    protected void loadMoreItems() {
                        adapter.addLoading();
                        isLoading = true;
                        currentPage++;
                        subComponentDetailApiService = new SubComponentDetailApiService(context);
                        subComponentDetailApiService.get_sub_component_comment(subComponentId, currentPage);
                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;

                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


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
                    commentText = (String) comment_text.getText().toString();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "select multiple images"), 1);
                }
            });


            due_date_image = (ImageView) findViewById(R.id.due_date_image);

            if (!SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                due_date_image.setClickable(false);
            }

/*
            subComponentDetailApiService.get_sub_component_details(268);
*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DateTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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

            String subComponentDetailStr = subComponentDetailsListModel.toString();
            taskDetailsModel = new ObjectMapper().readValue(subComponentDetailStr, TaskDetailsModel.class);


            if (!taskDetailsModel.getName().equals(null)) {
                name = taskDetailsModel.getName().toString();
            }

            if (!taskDetailsModel.getDescription().equals(null)) {
                description = taskDetailsModel.getDescription().toString();
            }


            comment_count = taskDetailsModel.getTotal_comments();

            int y = 0;


            if (taskDetailsModel.getDue_date() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(taskDetailsModel.getDue_date());
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                strDate = formatter.format(date);
            }

            if (taskDetailsModel.getAssign_to() != null) {
                assign_to = taskDetailsModel.getAssign_to().getCompany_name().toString();
                user_image = taskDetailsModel.getAssign_to().getAvatar().toString();

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    assignee_name = (TextView) findViewById(R.id.assignee_name);
                    task_name = (TextView) findViewById(R.id.task_name);
                    task_detail = (TextView) findViewById(R.id.task_detail);
                    due_date = (TextView) findViewById(R.id.due_date);
                    assignee_image = (ImageView) findViewById(R.id.assignee_image);
                    task_status = (Spinner) findViewById(R.id.task_status);


                    comment_counter = (TextView) findViewById(R.id.comment_counter);


                    task_name.setText(name);
                    task_detail.setText(description);
                    assignee_name.setText(assign_to);
                    due_date.setText(strDate);
                    comment_counter.setText("Nachrichten(" + comment_count + ")");


                    final List<String> myArraySpinnerValue = new ArrayList<String>();
                    final List<String> myArraySpinnerOption = new ArrayList<String>();

                    if (taskDetailsModel.getStatus_list().size() > 0) {
                        ArrayList<TaskStatusModel> taskStatusModel = taskDetailsModel.getStatus_list();

                        for (int i = 0; i < taskStatusModel.size(); i++) {
                            myArraySpinnerOption.add(taskStatusModel.get(i).getOption().toString());
                            myArraySpinnerValue.add(taskStatusModel.get(i).getValue().toString());

                        }

                    }


                    varSpinnerData = myArraySpinnerValue;


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, myArraySpinnerValue);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    task_status.setAdapter(spinnerArrayAdapter);

                    task_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if (isSpinnerInitial) {
                                isSpinnerInitial = false;
                                int index = (int) myArraySpinnerOption.indexOf(taskDetailsModel.getStatus());
                                task_status.setSelection(index);
                            } else {
                                String option = myArraySpinnerOption.get(i).toString();
                                subComponentDetailApiService.sub_component_status_change(subComponentId, option);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    Picasso.with(getApplicationContext())
                            .load(appConfigRemote.getBASE_URL() + "" + user_image)
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

                   /* recyclerViewComment = (RecyclerView) findViewById(R.id.comment_recycler_view);
                    recyclerViewComment.setHasFixedSize(true);
                    recyclerViewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    if (taskDetailsModel.getComments().size() > 0) {
                        adapterComment = new CommentAdapter(taskDetailsModel.getComments(), getApplicationContext());
                        recyclerViewComment.setAdapter(adapterComment);
                    }*/

                }
            });

            int xx = 0;
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


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    comment_text.setText("");
                    onRefresh();

                }
            }, 500);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCommentCreateFailed(JSONObject jsonObject) {

    }

    @Override
    public void OnDateChangedSuccess(final String dateStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    if (!dateStr.equals(null)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(dateStr);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                        strDate = formatter.format(date);
                        due_date.setText(strDate);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void OnDateChangedFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ComponentDetailActivity.this, "Something went wrong please choose again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnStatusChangedSuccess(JSONObject jsonObject) {

    }

    @Override
    public void OnStatusChangedFailed() {

    }

    @Override
    public void OnGetCommentSuccess(final JSONObject subCommentModel) {

        try {

            commentModels = new ArrayList<>();

            String comments_data = (String) subCommentModel.get("results").toString();
            comment_count = (int) subCommentModel.get("count");


            if (comments_data.length() > 0) {
                taskDetailsCommentsModel = new ObjectMapper().readValue(comments_data, new TypeReference<ArrayList<TaskDetailsCommentsModel>>() {
                });
            }


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {

                        comment_counter.setText("Nachrichten(" + comment_count + ")");


                        if (currentPage != PAGE_START) adapter.removeLoading();
                        adapter.addAll(taskDetailsCommentsModel);
                        swipeRefresh.setRefreshing(false);
                        if (currentPage < totalPage) adapter.addLoading();
                        else isLastPage = true;
                        isLoading = false;


                        adapter.removeLoading();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, 1500);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnGetCommentFailed(JSONObject jsonObject) {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.removeLoading();
                }
            }, 1500);


        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void getDate(String dateStr) {
        System.out.println(dateStr);
        subComponentDetailApiService.sub_component_date_change(subComponentId, dateStr);
        int x = 0;
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        subComponentDetailApiService.get_sub_component_comment(subComponentId, currentPage);
    }
}

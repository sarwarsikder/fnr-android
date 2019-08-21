package com.apper.sarwar.fnr.adapter.sub_component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.adapter.ImageAdapter;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentsModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    public List<TaskDetailsCommentsModel> commentModelList;
    public Context context;
    AppConfigRemote appConfigRemote;


    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    private ImageAdapter adapter;


    private View view;

    public CommentPostAdapter(List<TaskDetailsCommentsModel> commentModelList, Context context) {
        this.commentModelList = commentModelList;
        this.context = context;
        appConfigRemote = new AppConfigRemote();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.comment_list_odd, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(
                        LayoutInflater.from(context).inflate(R.layout.item_loading, viewGroup, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.onBind(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == commentModelList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return commentModelList == null ? 0 : commentModelList.size();
    }

    public void add(TaskDetailsCommentsModel response) {
        try {
            commentModelList.add(response);
            notifyItemInserted(commentModelList.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<TaskDetailsCommentsModel> projectItems) {

        try {
            for (TaskDetailsCommentsModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(TaskDetailsCommentsModel postItems) {
        int position = commentModelList.indexOf(postItems);
        if (position > -1) {
            commentModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new TaskDetailsCommentsModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = commentModelList.size() - 1;
        TaskDetailsCommentsModel item = getItem(position);
        if (item != null) {
            commentModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    TaskDetailsCommentsModel getItem(int position) {
        return commentModelList.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.comment_text)
        public TextView comment_text;

        @BindView(R.id.comment_user)
        public ImageView comment_user;

        @BindView(R.id.comment_image_layout)
        public LinearLayout comment_image_layout;

       /* @BindView(R.id.gridview)
        public GridView gridview;*/

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                final TaskDetailsCommentsModel commentModel = commentModelList.get(position);

                comment_text.setText(commentModel.getText());

                if (commentModel.getUser() != null) {
                    Picasso.with(context)
                            .load(appConfigRemote.getBASE_URL() + "" + commentModel.getUser().getAvatar())
                            .resize(106, 106)
                            .into(comment_user, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) comment_user.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    comment_user.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError() {
                                    if (SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                                        comment_user.setImageResource(R.drawable.ic_staff_user);
                                    } else {
                                        comment_user.setImageResource(R.drawable.ic_workder);
                                    }
                                }
                            });
                }

                if (commentModel.getFile_type() != null) {


                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<TaskDetailsCommentFileTypeModel> taskDetailsCommentFileTypeModel = commentModel.getFile_type();


                            recyclerView = (RecyclerView) itemView.findViewById(R.id.image_rec);

                            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                            adapter = new ImageAdapter(taskDetailsCommentFileTypeModel, context);
/*
                            final int targetCacheSize = 2;
                            recyclerView.setItemViewCacheSize(Integer.MIN_VALUE);
                            recyclerView.getRecycledViewPool().clear();
                            recyclerView.setItemViewCacheSize(targetCacheSize);*/

                            recyclerView.setAdapter(adapter);

                        }
                    });


                    int x = 0;

                   /* setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_one), 1);
                    setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_two), 2);
                    setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_three), 3);
                    setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_four), 4);*/

                    /*if (position % 2 == 0) {
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_one), 1);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_two), 2);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_three), 3);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_four), 4);

                    } else {
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_four), 1);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_three), 2);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_two), 3);
                        setImageInList(commentModel.getFile_type(), (ImageView) comment_image_layout.findViewById(R.id.iv_one), 4);
                    }*/

                } else {
                    comment_image_layout.setVisibility(LinearLayout.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void clear() {

        }

        private void setImageInList(List<TaskDetailsCommentFileTypeModel> fileTypeModels, ImageView iv, int index) {
            try {
                if (fileTypeModels.size() > 4 && index == 4) {
                    iv.setImageResource(R.drawable.ic_more);
                } else if (fileTypeModels.size() >= index) {
                    Picasso.with(context).load(appConfigRemote.getBASE_URL() + fileTypeModels.get(index - 1).getPath()).placeholder(R.raw.loading).error(R.drawable.error).into(iv);
                    //Glide.with(iv).load(fileTypeModels.get(index - 1).getPath()).placeholder(R.raw.loading).error(R.drawable.error).into(iv);
                } else {
                    iv.setVisibility(View.INVISIBLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }


    public class FooterHolder extends BaseViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;


        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }

    }
}

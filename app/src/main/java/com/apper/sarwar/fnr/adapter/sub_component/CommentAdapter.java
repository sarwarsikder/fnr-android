package com.apper.sarwar.fnr.adapter.sub_component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentsModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    public List<TaskDetailsCommentsModel> commentModelList;
    public Context context;
    AppConfigRemote appConfigRemote;


    private View view;

    public CommentAdapter(List<TaskDetailsCommentsModel> commentModelList, Context context) {
        this.commentModelList = commentModelList;
        this.context = context;
        appConfigRemote = new AppConfigRemote();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        try {
            if (i == 0) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_even, viewGroup, false);
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_odd, viewGroup, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CommentAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        TaskDetailsCommentsModel commentModel = commentModelList.get(position);
        viewHolder.comment_text.setText(commentModel.getText());

        if (commentModel.getUser() != null) {
            Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + "" + commentModel.getUser().getAvatar())
                    .placeholder(R.drawable.fnr_logo)
                    .resize(106, 106)
                    .into(viewHolder.comment_user, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) viewHolder.comment_user.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            viewHolder.comment_user.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {

                            if (SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                                viewHolder.comment_user.setImageResource(R.drawable.ic_staff_user);
                            } else {
                                viewHolder.comment_user.setImageResource(R.drawable.ic_workder);
                            }
                        }
                    });
        }

        if (commentModel.getFile_type() != null) {
            if (position % 2 == 0) {
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_one), 1);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_two), 2);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_three), 3);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_four), 4);

            } else {
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_four), 1);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_three), 2);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_two), 3);
                setImageInList(commentModel.getFile_type(), (ImageView) viewHolder.comment_image_layout.findViewById(R.id.iv_one), 4);
            }

        } else {
            viewHolder.comment_image_layout.setVisibility(LinearLayout.GONE);
        }

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

    @Override
    public int getItemCount() {
        return commentModelList.size();

    }

    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comment_text;
        public ImageView comment_user;
        public LinearLayout comment_image_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_text = (TextView) itemView.findViewById(R.id.comment_text);
            comment_user = (ImageView) itemView.findViewById(R.id.comment_user);
            comment_image_layout = (LinearLayout) itemView.findViewById(R.id.comment_image_layout);
        }


    }
}

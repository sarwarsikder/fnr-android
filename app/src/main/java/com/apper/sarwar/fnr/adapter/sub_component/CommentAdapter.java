package com.apper.sarwar.fnr.adapter.sub_component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.CommentModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    public List<CommentModel> commentModelList;
    public Context context;
    AppConfigRemote appConfigRemote;


    private View view;

    public CommentAdapter(List<CommentModel> commentModelList, Context context) {
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

        CommentModel commentModel = commentModelList.get(position);
        viewHolder.comment_text.setText(commentModel.getText());

        if (commentModel.getFile_type().equals("") || commentModel.getFile_type().equals(null)) {
            viewHolder.comment_image_layout.setVisibility(LinearLayout.GONE);
        }

        if (!commentModel.getCommentUser().getAvatar().equals(null)) {
            Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + "" + commentModel.getCommentUser().getAvatar())
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
                            viewHolder.comment_user.setImageResource(R.drawable.ic_man_user);
                        }
                    });
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

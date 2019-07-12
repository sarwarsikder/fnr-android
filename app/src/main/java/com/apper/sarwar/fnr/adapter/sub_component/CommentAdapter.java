package com.apper.sarwar.fnr.adapter.sub_component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.sub_component.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    public List<CommentModel> commentModelList;
    public Context context;

    private View view;

    public CommentAdapter(List<CommentModel> commentModelList, Context context) {
        this.commentModelList = commentModelList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        CommentModel commentModel = commentModelList.get(position);
        viewHolder.comment_text.setText(commentModel.getText());

        if (commentModel.getFile_type().equals("") || commentModel.getFile_type().equals(null)) {
            viewHolder.comment_image_layout.setVisibility(LinearLayout.GONE);
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

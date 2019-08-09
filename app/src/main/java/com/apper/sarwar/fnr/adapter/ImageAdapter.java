package com.apper.sarwar.fnr.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    public List<TaskDetailsCommentFileTypeModel> taskDetailsCommentFileTypeModels;
    private Context context;
    AppConfigRemote appConfigRemote;


    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;


    //initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();

    public ImageAdapter(List<TaskDetailsCommentFileTypeModel> taskDetailsCommentFileTypeModels, Context context) {
        this.taskDetailsCommentFileTypeModels = taskDetailsCommentFileTypeModels;
        this.context = context;
        appConfigRemote = new AppConfigRemote();
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.imageitem, viewGroup, false);
        return new ImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.ViewHolder viewHolder, int position) {
        TaskDetailsCommentFileTypeModel myList = taskDetailsCommentFileTypeModels.get(position);


        try {

             /* Picasso.with(context)
                .load(appConfigRemote.getBASE_URL() + myList.getPath())
                .placeholder(R.raw.loading)
                .error(R.drawable.error)
                .into(viewHolder.ImageName);*/

            Glide
                    .with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getPath())
                    .centerCrop()
                    .placeholder(R.raw.loading)
                    .into(viewHolder.ImageName);


            viewHolder.itemView.setTag(appConfigRemote.getBASE_URL() + myList.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return taskDetailsCommentFileTypeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ImageName;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ImageName = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}




package com.apper.sarwar.fnr.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
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

          /*  Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getPath())
                    .placeholder(R.raw.loading)
                    .error(R.drawable.error)
                    .into(viewHolder.ImageName);*/

            Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getPath())
                    .into(viewHolder.ImageName, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) viewHolder.ImageName.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            viewHolder.ImageName.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {
                            viewHolder.ImageName.setImageResource(R.drawable.error);
                        }
                    });

            /*Glide
                    .with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getPath())
                    .centerCrop()
                    .placeholder(R.raw.loading)
                    .priority(Priority.IMMEDIATE)
                    .into(viewHolder.ImageName);*/




            viewHolder.itemView.setTag(appConfigRemote.getBASE_URL() + myList.getPath());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewer.Builder(context, viewHolder.itemView.getTag())
                            .show();
                }

            });

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




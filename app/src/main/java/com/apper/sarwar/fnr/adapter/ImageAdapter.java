package com.apper.sarwar.fnr.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.apper.sarwar.fnr.utils.DownloadTask;
import com.squareup.picasso.Picasso;

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

        Picasso.with(context)
                .load(appConfigRemote.getBASE_URL() + myList.getPath())
                .placeholder(R.raw.loading)
                .error(R.drawable.error)
                .into(viewHolder.ImageName);

        viewHolder.itemView.setTag(appConfigRemote.getBASE_URL() + myList.getPath());


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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Toast.makeText(context, "TEST", Toast.LENGTH_SHORT).show();
                        ProgressDialog mProgressDialog;
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setMessage("Wait , Downloading....");
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setCancelable(true);


                        final DownloadTask downloadTask = new DownloadTask(context);
                        downloadTask.execute(itemView.getTag().toString());

                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }
}




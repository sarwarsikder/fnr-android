package com.apper.sarwar.fnr.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        final TaskDetailsCommentFileTypeModel myList = taskDetailsCommentFileTypeModels.get(position);


        try {

          /*  Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getPath())
                    .placeholder(R.raw.loading)
                    .error(R.drawable.error)
                    .into(viewHolder.ImageName);*/

            Picasso.with(context)
                    .load(appConfigRemote.getBASE_URL() + myList.getThumb_path())
                    .into(viewHolder.ImageName, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) viewHolder.ImageName.getDrawable()).getBitmap();
                            viewHolder.itemView.setTag(imageBitmap);
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


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        // create the popup window

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        /* width = size.x - 50;*/
                        System.out.println(width);
                        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        View layout = inflater.inflate(R.layout.popup_comment_image, null);
                        layout.setPadding(10, 10, 10, 10);
                        final PopupWindow mPopupWindow = new PopupWindow(layout, width,
                                height, true);
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        System.out.println(viewHolder.itemView.getTag());
                        PhotoView imageView = (PhotoView) layout.findViewById(R.id.comment_popup_image);
                        imageView.setImageBitmap((Bitmap) viewHolder.itemView.getTag());
                        //Glide.with(context).asGif().load(R.raw.loading).into(imageView);
                        //Glide.with(context).load(appConfigRemote.getBASE_URL() + myList.getPath()).into(imageView);
                        //Bitmap bt=getBitmapFromURL(appConfigRemote.getBASE_URL() + myList.getThumb_path());
                        //if(bt!=null){
                        //    imageView.setImageBitmap(bt);
                        //}

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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}




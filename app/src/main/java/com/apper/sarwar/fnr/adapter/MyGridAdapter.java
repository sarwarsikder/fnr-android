package com.apper.sarwar.fnr.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.sub_component.TaskDetailsCommentFileTypeModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TaskDetailsCommentFileTypeModel> imageObjects;

    private LayoutInflater mLayoutInflate;
    AppConfigRemote appConfigRemote;


    public MyGridAdapter(Context context, ArrayList<TaskDetailsCommentFileTypeModel> imageObjects) {
        this.context = context;
        this.imageObjects = imageObjects;

        this.mLayoutInflate = LayoutInflater.from(context);
        appConfigRemote = new AppConfigRemote();
    }

    public int getCount() {
        if (imageObjects != null) return imageObjects.size();
        return 0;
    }

    @Override
    public TaskDetailsCommentFileTypeModel getItem(int position) {
        if (imageObjects != null && imageObjects.size() > position)
            return imageObjects.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {
        if (imageObjects != null && imageObjects.size() > position)
            return 0;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = mLayoutInflate.inflate(R.layout.imageitem, parent,
                    false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskDetailsCommentFileTypeModel imageObject = (TaskDetailsCommentFileTypeModel) getItem(position);
        if (imageObject != null) {
            Glide
                    .with(context)
                    .load(appConfigRemote + "/" + imageObject.getPath())
                    .centerCrop()
                    .into(viewHolder.imageView);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView imageView;
    }
}

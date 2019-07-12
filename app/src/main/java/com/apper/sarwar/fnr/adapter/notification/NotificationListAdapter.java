package com.apper.sarwar.fnr.adapter.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apper.sarwar.fnr.ComponentDetailActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.notification_model.NotificationListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    public List<NotificationListModel> notificationListModels;
    private Context context;

    public static final int NOTIFICATION_ID = 2;
    public static final int TASK_ID = 1;

    public NotificationListAdapter(List<NotificationListModel> notificationListModels, Context context) {
        this.notificationListModels = notificationListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        try {
            NotificationListModel listModel = notificationListModels.get(position);
/*
        viewHolder.textUserPic.setImageResource(R.drawable.ic_man_user);
*/
            viewHolder.textNotificationText.setText(listModel.getNotificationText());
            viewHolder.textNotificationCreatedTime.setText(listModel.getNotificationCreatedTime());
            viewHolder.itemView.setTag(listModel.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return notificationListModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView textUserPic;
        public TextView textNotificationText;
        public TextView textNotificationCreatedTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserPic = (ImageView) itemView.findViewById(R.id.notification_user_image);
            textNotificationText = (TextView) itemView.findViewById(R.id.notification_text);
            textNotificationCreatedTime = (TextView) itemView.findViewById(R.id.notification_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int componentId = (int) view.getTag();
                    SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentSubComponentId, componentId, view.getContext());
                    Intent intent = new Intent(view.getContext(), ComponentDetailActivity.class);
                    view.getContext().startActivity(intent);

                }
            });

        }
    }
}

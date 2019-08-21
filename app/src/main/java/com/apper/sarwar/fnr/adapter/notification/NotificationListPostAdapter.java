package com.apper.sarwar.fnr.adapter.notification;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.ComponentDetailActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.notification_model.NotificationListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationListPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<NotificationListModel> notificationListModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private AppConfigRemote appConfigRemote;


    public NotificationListPostAdapter(List<NotificationListModel> notificationListModels, Context context) {
        this.notificationListModels = notificationListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new NotificationListPostAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.notification_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new NotificationListPostAdapter.FooterHolder(
                        LayoutInflater.from(context).inflate(R.layout.item_loading, viewGroup, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        baseViewHolder.onBind(position);

    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == notificationListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return notificationListModels == null ? 0 : notificationListModels.size();
    }

    public void add(NotificationListModel response) {
        try {
            notificationListModels.add(response);
            notifyItemInserted(notificationListModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<NotificationListModel> projectItems) {

        try {
            for (NotificationListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(NotificationListModel postItems) {
        int position = notificationListModels.indexOf(postItems);
        if (position > -1) {
            notificationListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new NotificationListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = notificationListModels.size() - 1;
        NotificationListModel item = getItem(position);
        if (item != null) {
            notificationListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    NotificationListModel getItem(int position) {
        return notificationListModels.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.notification_user_image)
        public ImageView textUserPic;
        @BindView(R.id.notification_text)
        public TextView textNotificationText;
        @BindView(R.id.notification_time)
        public TextView textNotificationCreatedTime;

        AppConfigRemote appConfigRemote = new AppConfigRemote();


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);

            try {
                final NotificationListModel listModel = notificationListModels.get(position);

                textNotificationText.setText(listModel.getNotificationText());
                textNotificationCreatedTime.setText(listModel.getNotificationCreatedTime());
                itemView.setTag(listModel.getId());


                Picasso.with(context)
                        .load(appConfigRemote.getBASE_URL() + "" + listModel.getUserPic())
                        .resize(106, 106)
                        .into(textUserPic, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) textUserPic.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                textUserPic.setImageDrawable(imageDrawable);
                            }

                            @Override
                            public void onError() {
                                if (SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                                    textUserPic.setImageResource(R.drawable.ic_staff_user);
                                } else {
                                    textUserPic.setImageResource(R.drawable.ic_workder);
                                }
                            }
                        });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int componentId = (int) view.getTag();
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentSubComponentId, componentId, view.getContext());
                        Intent intent = new Intent(view.getContext(), ComponentDetailActivity.class);
                        view.getContext().startActivity(intent);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void clear() {

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

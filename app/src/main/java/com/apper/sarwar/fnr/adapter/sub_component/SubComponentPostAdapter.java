package com.apper.sarwar.fnr.adapter.sub_component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.apper.sarwar.fnr.model.sub_component.SubComponentModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubComponentPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<SubComponentModel> subComponentModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private AppConfigRemote appConfigRemote;


    public SubComponentPostAdapter(List<SubComponentModel> subComponentModels, Context context) {
        this.subComponentModels = subComponentModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new SubComponentPostAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.sub_component_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new SubComponentPostAdapter.FooterHolder(
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
            return position == subComponentModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return subComponentModels == null ? 0 : subComponentModels.size();
    }

    public void add(SubComponentModel response) {
        try {
            subComponentModels.add(response);
            notifyItemInserted(subComponentModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<SubComponentModel> projectItems) {

        try {
            for (SubComponentModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(SubComponentModel postItems) {
        int position = subComponentModels.indexOf(postItems);
        if (position > -1) {
            subComponentModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new SubComponentModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = subComponentModels.size() - 1;
        SubComponentModel item = getItem(position);
        if (item != null) {
            subComponentModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    SubComponentModel getItem(int position) {
        return subComponentModels.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.sub_component_name)
        public TextView subComponentName;

        @BindView(R.id.sub_component_description)
        public TextView subComponentDescription;

        @BindView(R.id.created_time)
        public TextView createdTime;

        @BindView(R.id.sub_component_status)
        public ImageView subComponentStatus;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {

            super.onBind(position);

            try {

                SubComponentModel componentModel = subComponentModels.get(position);

                subComponentName.setText(componentModel.getComponentName());
                subComponentDescription.setText(componentModel.getComponentDescription());
                createdTime.setText(componentModel.getCreatedTime());


                if (componentModel.getDue_date() != null) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date today = dateFormat.parse(dateFormat.format(new Date()));

                    if (today.after(componentModel.getDue_date())) {
                        System.out.println("today() is after getDue_date()");
                        subComponentStatus.setImageResource(R.drawable.ic_component_red_status);
                    } else {
                        subComponentStatus.setImageResource(R.drawable.ic_component_blue_status);
                    }
                } else {
                    subComponentStatus.setImageResource(R.drawable.ic_component_green_status);
                }


                itemView.setTag(componentModel.getId());


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

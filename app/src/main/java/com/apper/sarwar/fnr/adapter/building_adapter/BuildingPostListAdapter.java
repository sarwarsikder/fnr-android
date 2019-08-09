package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.BuildingComponentActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.model.building_model.BuildingListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuildingPostListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<BuildingListModel> buildingListModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    public BuildingPostListAdapter(List<BuildingListModel> buildingListModels, Context context) {
        this.buildingListModels = buildingListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BuildingPostListAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.building_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new BuildingPostListAdapter.FooterHolder(
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
            return position == buildingListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return buildingListModels == null ? 0 : buildingListModels.size();
    }

    public void add(BuildingListModel response) {
        try {
            buildingListModels.add(response);
            notifyItemInserted(buildingListModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<BuildingListModel> projectItems) {

        try {
            for (BuildingListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(BuildingListModel postItems) {
        int position = buildingListModels.indexOf(postItems);
        if (position > -1) {
            buildingListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BuildingListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = buildingListModels.size() - 1;
        BuildingListModel item = getItem(position);
        if (item != null) {
            buildingListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BuildingListModel getItem(int position) {
        return buildingListModels.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.building_name)
        public TextView textBuildingName;
        @BindView(R.id.building_task)
        public TextView buildingTask;
        @BindView(R.id.building_flat)
        public TextView buildingFlat;
        @BindView(R.id.flat_lavel)
        public TextView flat_lavel;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                BuildingListModel myList = buildingListModels.get(position);
                textBuildingName.setText(myList.getDisplayNumber());

                if (!SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                    buildingTask.setVisibility(View.GONE);
                    buildingFlat.setVisibility(View.GONE);
                    flat_lavel.setVisibility(View.GONE);

                }

                buildingTask.setText(String.valueOf(myList.getTasksDone() + "/" + myList.getTotalTasks()));
                buildingFlat.setText(String.valueOf(myList.getTotalFlats()));

                itemView.setTag(myList.getId());


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int itemBuildingId = (int) view.getTag();
                        Intent intent = new Intent(view.getContext(), BuildingComponentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("EXTRA_BUILDING_ID", itemBuildingId);
                        intent.putExtra("BUILDING_DATA", bundle);
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentBuildingId, itemBuildingId, view.getContext());
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

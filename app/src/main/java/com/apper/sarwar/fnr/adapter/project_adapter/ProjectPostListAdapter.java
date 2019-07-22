package com.apper.sarwar.fnr.adapter.project_adapter;

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

import com.apper.sarwar.fnr.BuildingListActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.model.project_model.ProjectListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectPostListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    public List<ProjectListModel> projectAdapterList;
    private Context context;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public ProjectPostListAdapter(List<ProjectListModel> adapterList, Context context) {
        this.projectAdapterList = adapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.project_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(
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
            return position == projectAdapterList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return projectAdapterList == null ? 0 : projectAdapterList.size();
    }

    public void add(ProjectListModel response) {
        try {
            projectAdapterList.add(response);
            notifyItemInserted(projectAdapterList.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<ProjectListModel> projectItems) {

        try {
            for (ProjectListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(ProjectListModel postItems) {
        int position = projectAdapterList.indexOf(postItems);
        if (position > -1) {
            projectAdapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new ProjectListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = projectAdapterList.size() - 1;
        ProjectListModel item = getItem(position);
        if (item != null) {
            projectAdapterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    ProjectListModel getItem(int position) {
        return projectAdapterList.get(position);
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.project_name)
        public TextView textProjectTitle;
        @BindView(R.id.project_location)
        public TextView textProjectLocation;
        @BindView(R.id.project_progress_count)
        public TextView textProjectProgressCount;
        @BindView(R.id.project_progress)
        public ProgressBar textProjectProgress;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                ProjectListModel myList = projectAdapterList.get(position);
                textProjectTitle.setText(myList.getProject_name());
                textProjectLocation.setText(myList.getAddress());
                textProjectProgressCount.setText(myList.getTasks_done() + "/" + myList.getTotal_tasks());
                int progress = 0;
                if (myList.getTotal_tasks() > 0) {
                    progress = (myList.getTasks_done() * 100) / myList.getTotal_tasks();
                }

                else{
                    progress = (20 * 100) / 150;

                }
                textProjectProgress.setProgress(progress);
                itemView.setTag(myList.getId());


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int itemProductId = (int) view.getTag();
                        Intent intent = new Intent(view.getContext(), BuildingListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("EXTRA_PRODUCT_ID", itemProductId);
                        intent.putExtra("PROJECT_DATA", bundle);
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentProjectId, itemProductId, view.getContext());
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

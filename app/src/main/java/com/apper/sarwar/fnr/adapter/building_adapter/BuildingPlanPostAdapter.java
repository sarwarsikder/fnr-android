package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingPlanPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<BuildingPlanModel> buildingPlanModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private AppConfigRemote appConfigRemote;


    public BuildingPlanPostAdapter(List<BuildingPlanModel> buildingPlanModels, Context context) {
        this.buildingPlanModels = buildingPlanModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BuildingPlanPostAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.building_plan_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new BuildingPlanPostAdapter.FooterHolder(
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
            return position == buildingPlanModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return buildingPlanModels == null ? 0 : buildingPlanModels.size();
    }

    public void add(BuildingPlanModel response) {
        try {
            buildingPlanModels.add(response);
            notifyItemInserted(buildingPlanModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<BuildingPlanModel> projectItems) {

        try {
            for (BuildingPlanModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(BuildingPlanModel postItems) {
        int position = buildingPlanModels.indexOf(postItems);
        if (position > -1) {
            buildingPlanModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BuildingPlanModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = buildingPlanModels.size() - 1;
        BuildingPlanModel item = getItem(position);
        if (item != null) {
            buildingPlanModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BuildingPlanModel getItem(int position) {
        return buildingPlanModels.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.building_plan_name)
        public TextView textBuildingName;
        @BindView(R.id.building_file_download)
        public ImageView buildingFileDownload;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                final BuildingPlanModel myList = buildingPlanModels.get(position);
                textBuildingName.setText(myList.getPlanName());
                itemView.setTag(myList.getId());
                appConfigRemote = new AppConfigRemote();


                buildingFileDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String url = appConfigRemote.BASE_URL + "/" + myList.getPlanFile().toString();

                            /*new DownloadTask(context, url);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
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

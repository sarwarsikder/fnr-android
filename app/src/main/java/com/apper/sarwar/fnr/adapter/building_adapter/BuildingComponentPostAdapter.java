package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.SubComponentActivity;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.model.building_model.BuildingComponentListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuildingComponentPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    public List<BuildingComponentListModel> buildingComponentListModels;
    private Context context;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public BuildingComponentPostAdapter(List<BuildingComponentListModel> adapterList, Context context) {
        this.buildingComponentListModels = adapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.building_component_list, viewGroup, false));
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
            return position == buildingComponentListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return buildingComponentListModels == null ? 0 : buildingComponentListModels.size();
    }

    public void add(BuildingComponentListModel response) {
        try {
            buildingComponentListModels.add(response);
            notifyItemInserted(buildingComponentListModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<BuildingComponentListModel> projectItems) {

        try {
            for (BuildingComponentListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(BuildingComponentListModel postItems) {
        int position = buildingComponentListModels.indexOf(postItems);
        if (position > -1) {
            buildingComponentListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BuildingComponentListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = buildingComponentListModels.size() - 1;
        BuildingComponentListModel item = getItem(position);
        if (item != null) {
            buildingComponentListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BuildingComponentListModel getItem(int position) {
        return buildingComponentListModels.get(position);
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.component_name)
        public TextView componentName;
        @BindView(R.id.component_count)
        public TextView componentCount;
        @BindView(R.id.component_progress)
        public ProgressBar componentProgress;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {

                BuildingComponentListModel myList = buildingComponentListModels.get(position);
                componentName.setText(myList.getComponentName());
                componentCount.setText(myList.getTotalTask() + "");
                componentProgress.setProgress(myList.getTaskDone());
                itemView.setTag(myList.getId());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int componentId = (int) view.getTag();
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentComponentId, componentId, view.getContext());
                        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.currentState, "building", view.getContext());
                        Intent intent = new Intent(view.getContext(), SubComponentActivity.class);
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

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

import com.apper.sarwar.fnr.FlatComponentActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.model.building_model.BuildingFlatListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuildingFlatPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;


    public List<BuildingFlatListModel> buildingFlatListModels;
    private Context context;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public BuildingFlatPostAdapter(List<BuildingFlatListModel> adapterList, Context context) {
        this.buildingFlatListModels = adapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.building_flat_list, viewGroup, false));
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
            return position == buildingFlatListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return buildingFlatListModels == null ? 0 : buildingFlatListModels.size();
    }

    public void add(BuildingFlatListModel response) {
        try {
            buildingFlatListModels.add(response);
            notifyItemInserted(buildingFlatListModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<BuildingFlatListModel> projectItems) {

        try {
            for (BuildingFlatListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(BuildingFlatListModel postItems) {
        int position = buildingFlatListModels.indexOf(postItems);
        if (position > -1) {
            buildingFlatListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BuildingFlatListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = buildingFlatListModels.size() - 1;
        BuildingFlatListModel item = getItem(position);
        if (item != null) {
            buildingFlatListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BuildingFlatListModel getItem(int position) {
        return buildingFlatListModels.get(position);
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.building_flat_name)
        public TextView flatName;
        @BindView(R.id.building_flat_component)
        public TextView componentCount;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                BuildingFlatListModel myList = buildingFlatListModels.get(position);
                flatName = (TextView) itemView.findViewById(R.id.building_flat_name);
                componentCount = (TextView) itemView.findViewById(R.id.building_flat_component);

                System.out.println(myList.getFlatNumber());

                flatName.setText(myList.getFlatNumber());
                componentCount.setText(myList.getFlatTotalTaskDone() + "/" + myList.getFlatTotalTask() + "");

                if (!SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                    componentCount.setVisibility(View.GONE);
                }

                itemView.setTag(myList.getId());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int itemFlatId = (int) view.getTag();
                        Intent intent = new Intent(view.getContext(), FlatComponentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("EXTRA_FLAT_ID", itemFlatId);
                        intent.putExtra("FLAT_DATA", bundle);
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentFlatId, itemFlatId, view.getContext());
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

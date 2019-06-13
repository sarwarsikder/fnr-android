package com.apper.sarwar.fnr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.BuildingListActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.BuildingListModel;

import java.util.List;

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.ViewHolder> {

    public List<BuildingListModel> buildingListModels;
    private Context context;


    public BuildingListAdapter(List<BuildingListModel> buildingListModels, Context context) {
        this.buildingListModels = buildingListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.building_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BuildingListModel myList = buildingListModels.get(position);
        viewHolder.textBuildingName.setText(myList.getBuildingName());
        viewHolder.buildingTask.setText(myList.getBuildingTask());
        viewHolder.buildingFlat.setText(myList.getBuildingFlat());
    }

    @Override
    public int getItemCount() {
        return buildingListModels.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textBuildingName;
        public TextView buildingTask;
        public TextView buildingFlat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBuildingName = (TextView) itemView.findViewById(R.id.building_name);
            buildingTask = (TextView) itemView.findViewById(R.id.building_task);
            buildingFlat = (TextView) itemView.findViewById(R.id.building_flat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will Be Added Loader", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), BuildingListActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}

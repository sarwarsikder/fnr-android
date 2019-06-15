package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;

import java.util.List;

public class BuildingPlanAdapter extends RecyclerView.Adapter<BuildingPlanAdapter.ViewHolder> {

    public List<BuildingPlanModel> buildingPlanModels;
    private Context context;

    public BuildingPlanAdapter(List<BuildingPlanModel> adapterList, Context context) {
        this.buildingPlanModels = adapterList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.building_plan_list, viewGroup, false);
        return new BuildingPlanAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BuildingPlanAdapter.ViewHolder viewHolder, int position) {
        BuildingPlanModel myList = buildingPlanModels.get(position);
        viewHolder.textBuildingPlanName.setText(myList.getPlanName());


    }

    @Override
    public int getItemCount() {
        return buildingPlanModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textBuildingPlanName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBuildingPlanName = (TextView) itemView.findViewById(R.id.building_plan_name);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will add loader!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), BuildingListActivity.class);
                    view.getContext().startActivity(intent);
                }
            });*/

        }
    }
}
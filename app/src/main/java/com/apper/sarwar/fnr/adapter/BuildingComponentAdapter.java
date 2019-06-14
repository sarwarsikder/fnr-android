package com.apper.sarwar.fnr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.BuildingComponentActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.BuildingComponentListModel;
import com.apper.sarwar.fnr.model.ProjectListModel;

import java.util.List;

public class BuildingComponentAdapter extends RecyclerView.Adapter<BuildingComponentAdapter.ViewHolder> {
    public List<BuildingComponentListModel> buildingComponentListModels;
    private Context context;

    public BuildingComponentAdapter(List<BuildingComponentListModel> buildingComponentListModels, Context context) {
        this.buildingComponentListModels = buildingComponentListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingComponentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.building_component_list, viewGroup, false);
        return new BuildingComponentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BuildingComponentAdapter.ViewHolder viewHolder, int position) {
        BuildingComponentListModel myList = buildingComponentListModels.get(position);
        viewHolder.componentName.setText(myList.getComponentName());
        viewHolder.componentCount.setText(myList.getComponentCount());
        viewHolder.componentProgress.setProgress(myList.getComponentProgress());


    }

    @Override
    public int getItemCount() {
        return buildingComponentListModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView componentName;
        public TextView componentCount;
        public ProgressBar componentProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            componentName = (TextView) itemView.findViewById(R.id.component_name);
            componentCount = (TextView) itemView.findViewById(R.id.component_count);
            componentProgress = (ProgressBar) itemView.findViewById(R.id.component_progress);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will Be Added Loader", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), BuildingComponentActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
*/
        }
    }
}

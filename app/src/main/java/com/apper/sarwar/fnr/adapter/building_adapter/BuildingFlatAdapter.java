package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.FlatComponentActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.building_model.BuildingFlatListModel;

import java.util.List;

public class BuildingFlatAdapter extends RecyclerView.Adapter<BuildingFlatAdapter.ViewHolder> {

    public List<BuildingFlatListModel> buildingFlatListModels;
    private Context context;

    public BuildingFlatAdapter(List<BuildingFlatListModel> buildingFlatListModels, Context context) {
        this.buildingFlatListModels = buildingFlatListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingFlatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.building_flat_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BuildingFlatListModel myList = buildingFlatListModels.get(position);
        viewHolder.flatName.setText(myList.getFlatName());
        viewHolder.componentCount.setText(myList.getFlatComponent());

    }

    @Override
    public int getItemCount() {
        return buildingFlatListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView flatName;
        public TextView componentCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flatName = (TextView) itemView.findViewById(R.id.building_flat_name);
            componentCount = (TextView) itemView.findViewById(R.id.building_flat_component);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will Be Added Loader", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), FlatComponentActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


}

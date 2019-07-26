package com.apper.sarwar.fnr.adapter.building_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apper.sarwar.fnr.FlatComponentActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.building_model.BuildingFlatListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

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
        viewHolder.flatName.setText(myList.getFlatNumber());
        viewHolder.componentCount.setText(myList.getFlatTotalTask() + "");
        viewHolder.itemView.setTag(myList.getId());

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
                    int itemFlatId = (int) view.getTag();
                    Intent intent = new Intent(view.getContext(), FlatComponentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("EXTRA_FLAT_ID", itemFlatId);
                    intent.putExtra("FLAT_DATA", bundle);
                    SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentFlatId, itemFlatId, view.getContext());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


}

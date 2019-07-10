package com.apper.sarwar.fnr.adapter.sub_component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apper.sarwar.fnr.ComponentDetailActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.sub_component.SubComponentModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

public class SubComponentAdapter extends RecyclerView.Adapter<SubComponentAdapter.ViewHolder> {

    public List<SubComponentModel> subComponentModels;
    public Context context;

    public SubComponentAdapter(List<SubComponentModel> subComponentModels, Context context) {
        this.subComponentModels = subComponentModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_component_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SubComponentModel componentModel = subComponentModels.get(position);
        viewHolder.subComponentName.setText(componentModel.getComponentName());
        viewHolder.subComponentDescription.setText(componentModel.getComponentDescription());
        viewHolder.createdTime.setText(componentModel.getCreatedTime());
        viewHolder.itemView.setTag(componentModel.getId());

    }

    @Override
    public int getItemCount() {
        return subComponentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subComponentName;
        public TextView subComponentDescription;
        public TextView createdTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subComponentName = (TextView) itemView.findViewById(R.id.sub_component_name);
            subComponentDescription = (TextView) itemView.findViewById(R.id.sub_component_description);
            createdTime = (TextView) itemView.findViewById(R.id.created_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int componentId = (int) view.getTag();
                    SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentSubComponentId, componentId, view.getContext());
                    Intent intent = new Intent(view.getContext(), ComponentDetailActivity.class);
                    view.getContext().startActivity(intent);
                }
            });


        }


    }
}

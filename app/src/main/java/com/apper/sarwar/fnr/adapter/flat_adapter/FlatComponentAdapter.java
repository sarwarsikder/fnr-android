package com.apper.sarwar.fnr.adapter.flat_adapter;

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

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.SubComponentActivity;
import com.apper.sarwar.fnr.model.flat_model.FlatComponentListModel;
import com.google.zxing.client.result.VINParsedResult;

import java.util.List;

public class FlatComponentAdapter extends RecyclerView.Adapter<FlatComponentAdapter.ViewHolder> {
    public List<FlatComponentListModel> flatComponentListModels;
    private Context context;

    public FlatComponentAdapter(List<FlatComponentListModel> flatComponentListModels, Context context) {
        this.flatComponentListModels = flatComponentListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public FlatComponentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flat_component_list, viewGroup, false);
        return new FlatComponentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FlatComponentAdapter.ViewHolder viewHolder, int position) {
        FlatComponentListModel myList = flatComponentListModels.get(position);
        viewHolder.componentName.setText(myList.getComponentName());
        viewHolder.componentCount.setText(myList.getTaskDone() + "/" + myList.getTotalTask());
        viewHolder.componentProgress.setProgress(0);
        viewHolder.itemView.setTag(myList.getId());


    }

    @Override
    public int getItemCount() {
        return flatComponentListModels.size();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will Be Added Loader", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), SubComponentActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}

package com.apper.sarwar.fnr.adapter.project_adapter;

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

import com.apper.sarwar.fnr.BuildingListActivity;
import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.model.project_model.ProjectListModel;

import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    public List<ProjectListModel> projectAdapterList;
    private Context context;

    public ProjectListAdapter(List<ProjectListModel> adpterList, Context context) {
        this.projectAdapterList = adpterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectListAdapter.ViewHolder viewHolder, int position) {
        ProjectListModel myList = projectAdapterList.get(position);
        viewHolder.textProjectTitle.setText(myList.getProject_name());
        viewHolder.textProjectLocation.setText(myList.getAddress());
        viewHolder.textProjectProgressCount.setText(myList.getTasks_done() + "/" + myList.getTotal_tasks());
        viewHolder.textProjectProgress.setProgress(80);
    }

    @Override
    public int getItemCount() {
        return projectAdapterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textProjectTitle;
        public TextView textProjectLocation;
        public TextView textProjectProgressCount;
        public ProgressBar textProjectProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProjectTitle = (TextView) itemView.findViewById(R.id.project_name);
            textProjectLocation = (TextView) itemView.findViewById(R.id.project_location);
            textProjectProgressCount = (TextView) itemView.findViewById(R.id.project_progress_count);
            textProjectProgress = (ProgressBar) itemView.findViewById(R.id.project_progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Will add loader!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), BuildingListActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}

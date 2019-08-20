package com.apper.sarwar.fnr.adapter.flat_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.SubComponentActivity;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.flat_model.FlatComponentListModel;
import com.apper.sarwar.fnr.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FlatComponentPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<FlatComponentListModel> flatComponentListModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private AppConfigRemote appConfigRemote;


    public FlatComponentPostAdapter(List<FlatComponentListModel> flatComponentListModels, Context context) {
        this.flatComponentListModels = flatComponentListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new FlatComponentPostAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.flat_component_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new FlatComponentPostAdapter.FooterHolder(
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
            return position == flatComponentListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return flatComponentListModels == null ? 0 : flatComponentListModels.size();
    }

    public void add(FlatComponentListModel response) {
        try {
            flatComponentListModels.add(response);
            notifyItemInserted(flatComponentListModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<FlatComponentListModel> projectItems) {

        try {
            for (FlatComponentListModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(FlatComponentListModel postItems) {
        int position = flatComponentListModels.indexOf(postItems);
        if (position > -1) {
            flatComponentListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new FlatComponentListModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = flatComponentListModels.size() - 1;
        FlatComponentListModel item = getItem(position);
        if (item != null) {
            flatComponentListModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    FlatComponentListModel getItem(int position) {
        return flatComponentListModels.get(position);
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
                final FlatComponentListModel myList = flatComponentListModels.get(position);
                System.out.println(myList.getComponentName());
                componentName.setText(myList.getComponentName());

                componentCount.setText(myList.getTaskDone() + "/" + myList.getTotalTask());
                componentProgress.setProgress(0);

                if (!SharedPreferenceUtil.getDefaultsBool(SharedPreferenceUtil.isStaff, context)) {
                    componentCount.setVisibility(View.GONE);
                    componentProgress.setVisibility(View.GONE);
                }

                itemView.setTag(myList.getId());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int componentId = (int) view.getTag();
                        SharedPreferenceUtil.setDefaultsId(SharedPreferenceUtil.currentComponentId, componentId, view.getContext());
                        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.currentState, "flat", view.getContext());
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

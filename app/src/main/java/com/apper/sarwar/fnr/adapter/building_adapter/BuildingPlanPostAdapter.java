package com.apper.sarwar.fnr.adapter.building_adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apper.sarwar.fnr.R;
import com.apper.sarwar.fnr.adapter.BaseViewHolder;
import com.apper.sarwar.fnr.config.AppConfigRemote;
import com.apper.sarwar.fnr.model.building_model.BuildingPlanModel;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BuildingPlanPostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public List<BuildingPlanModel> buildingPlanModels;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private AppConfigRemote appConfigRemote;


    public BuildingPlanPostAdapter(List<BuildingPlanModel> buildingPlanModels, Context context) {
        this.buildingPlanModels = buildingPlanModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BuildingPlanPostAdapter.ViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.building_plan_list, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new BuildingPlanPostAdapter.FooterHolder(
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
            return position == buildingPlanModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return buildingPlanModels == null ? 0 : buildingPlanModels.size();
    }

    public void add(BuildingPlanModel response) {
        try {
            buildingPlanModels.add(response);
            notifyItemInserted(buildingPlanModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addAll(List<BuildingPlanModel> projectItems) {

        try {
            for (BuildingPlanModel response : projectItems) {
                add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void remove(BuildingPlanModel postItems) {
        int position = buildingPlanModels.indexOf(postItems);
        if (position > -1) {
            buildingPlanModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BuildingPlanModel());
    }


    public void removeLoading() {
        isLoaderVisible = false;
        int position = buildingPlanModels.size() - 1;
        BuildingPlanModel item = getItem(position);
        if (item != null) {
            buildingPlanModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BuildingPlanModel getItem(int position) {
        return buildingPlanModels.get(position);
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.building_plan_name)
        public TextView textBuildingName;
        @BindView(R.id.building_file_download)
        public ImageView buildingFileDownload;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void onBind(int position) {
            super.onBind(position);
            try {
                final BuildingPlanModel myList = buildingPlanModels.get(position);
                textBuildingName.setText(myList.getPlanName());
                itemView.setTag(myList.getId());
                appConfigRemote = new AppConfigRemote();


                buildingFileDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            String url = appConfigRemote.BASE_URL + myList.getPlanFile().toString();
                            new DownloadFileFromURL().execute(url, myList.getPlanName() + "." + myList.getFileType().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
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


    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {


                // File myFile= new File(Environment.getExternalStorageDirectory() + "/fnr");


                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                File file = new File(path, f_url[1]);

                /*pathFolder = Environment.getExternalStorageDirectory().getAbsolutePath();*/


                /*String filepath = Environment.getExternalStorageDirectory().getPath();
                File file = new File(filepath + "/fnr" );
                if (!file.exists()) {
                    file.mkdirs();
                }
                 (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
                */
               /* File futureStudioIconFile = new File(pathFolder);
                if (!futureStudioIconFile.exists()) {
                    futureStudioIconFile.mkdirs();
                }*/

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lengthOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024]; //anybody know what 1024 means ?
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            }

            return pathFile;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pd != null) {
                pd.dismiss();
            }
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.setDataAndType(Uri.fromFile(new File(file_url)), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }

}

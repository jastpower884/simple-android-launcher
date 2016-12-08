package org.jast.simpleandroidlauncher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ptc_02008 on 2016/11/3.
 */

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface RecyclerViewClick {
        void onClick(View view, int position);
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<AppDetail> apps;
    private int layoutID = 0;


    public AppListAdapter(Context context, List<AppDetail> apps) {
        this.apps = apps;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if(layoutID!=0){
//        return new MainViewHolder(mLayoutInflater.inflate(layoutID, parent, false), mRecyclerViewClick);
//        }
        return new MainViewHolder(mLayoutInflater.inflate(R.layout.item_app_launcher, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MainViewHolder) holder).mImageViewAppIcon.setImageDrawable(apps.get(position).getIcon());
        ((MainViewHolder) holder).mTextViewAppName.setText(apps.get(position).getLabel());
        ((MainViewHolder) holder).setApp(apps.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return apps == null ? 0 : apps.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewAppIcon;
        TextView mTextViewAppName;

        ImageView mImageView;

        AppDetail app;

        RecyclerViewClick mRecyclerViewClick;

        MainViewHolder(View view) {
            super(view);
//            view.setFocusable(true);
//            view.setFocusableInTouchMode(true);
            view.findViewById(R.id.linear_layout).setFocusable(true);
            view.findViewById(R.id.linear_layout).setFocusableInTouchMode(true);
            mImageViewAppIcon = (ImageView) view.findViewById(R.id.image_view_app_icon);
            mTextViewAppName = (TextView) view.findViewById(R.id.text_view_app_name);
            view.findViewById(R.id.linear_layout).setOnClickListener(mOnClickListener);
        }

        // RecyclerView 的監聽者
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.v("AppLuncher", "app.name:" + app.name);

                switch (app.getAppType()) {

                    case AppDetail.TYPE_OTHER_APP:
                        Intent i = v.getContext().getPackageManager().getLaunchIntentForPackage(app.name.toString());
                        v.getContext().startActivity(i);
                        break;
                    case AppDetail.TYPE_SETTING_APP:
                        i = new Intent();
                        i.setClassName(v.getContext(), app.getName().toString());
                        v.getContext().startActivity(i);

                        break;

                }


            }
        };

        public void setApp(AppDetail app) {
            this.app = app;
        }
    }
}
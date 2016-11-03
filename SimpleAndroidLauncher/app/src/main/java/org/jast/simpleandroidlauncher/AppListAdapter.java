package org.jast.simpleandroidlauncher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ptc_02008 on 2016/11/3.
 */

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 這是由於 RecyclerView 沒辦法直接使用 onItemClick 去監聽，而直接用 onClick
     * 監聽又難以取得點擊位置資訊，所以乾脆自訂一個抽象實體，讓外面去監聽比較好處理
     */
    public interface RecyclerViewClick {
        void onClick(View view, int position);
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;
    private RecyclerViewClick mRecyclerViewClick;
    private int layoutID = 0;


    public AppListAdapter(Context context, String[] titles, RecyclerViewClick mRecylclerViewClick) {
        mTitles = titles;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mRecyclerViewClick = mRecylclerViewClick;
    }

    public AppListAdapter(Context context, String[] titles, RecyclerViewClick mRecylclerViewClick, int layoutID) {
        mTitles = titles;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mRecyclerViewClick = mRecylclerViewClick;
        this.layoutID = layoutID;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if(layoutID!=0){
        return new MainViewHolder(mLayoutInflater.inflate(layoutID, parent, false), mRecyclerViewClick);
//        }
//        return new MainViewHolder(mLayoutInflater.inflate(R.layout.item_main_button, parent, false), mRecyclerViewClick);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MainViewHolder) holder).mTextViewTitle.setText(mTitles[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearLayoutMainButton;
        TextView mTextViewTitle;
        TextView mTextViewContent;

        ImageView mImageView;


        RecyclerViewClick mRecyclerViewClick;

        MainViewHolder(View view, RecyclerViewClick mRecyclerViewClick) {
            super(view);
            this.mRecyclerViewClick = mRecyclerViewClick;
//            mLinearLayoutMainButton = (LinearLayout) view.findViewById(R.id.ll_main_button);
//            mTextViewTitle = (TextView) view.findViewById(R.id.tv_title);
//            mTextViewContent = (TextView) view.findViewById(R.id.tv_content);
//            mLinearLayoutMainButton.setOnClickListener(mOnClickListener);
//            mLinearLayoutMainButton.getLayoutParams().height = MainApplication.getInstance().getScreenHeight() / 8;
        }

        // RecyclerView 的監聽者
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 這邊會將物件和位置傳入Activity實作的實體中
                mRecyclerViewClick.onClick(v, getAdapterPosition());
            }
        };
    }
}


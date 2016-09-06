package com.xuhong.baseclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by BHKJ on 2016/5/11.
 */
public abstract  class BaseAdapter extends RecyclerView.Adapter {


    private OnItemClickListener listener;

    protected final LayoutInflater inflater;
    protected final List<?> mData;

    protected final Context  mContext;

    public BaseAdapter(Context context, List<?> data) {
        this.inflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(getLayoutId(), parent, false);
        onMyCreateViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder  viewHolder = (ViewHolder) holder;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.click(view,position);
                }
            }
        });
        onMyBindViewHolder(viewHolder,position);

    }

    protected abstract  void onMyBindViewHolder(ViewHolder holder, final int position);
    protected abstract  int getLayoutId();
    protected void onMyCreateViewHolder(View view){

    };

    public interface OnItemClickListener {
        void click(View v, int position);
    }
}

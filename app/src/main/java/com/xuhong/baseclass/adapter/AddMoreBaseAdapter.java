package com.xuhong.baseclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuhong.baseclass.R;

import java.util.List;


/**
 * Created by BHKJ on 2016/8/30.
 */

public abstract class AddMoreBaseAdapter extends RecyclerView.Adapter {

    public static final int LAYOUT_ORDINARY = 1;
    public static final int LAYOUT_ADD_MORE = 2;


    protected final LayoutInflater inflater;
    protected final List<?> mData;

    protected final Context mContext;

    private boolean isHasMore = false;

    private OnLoadMoreListener mListener;

    private Animation animation;
    private ImageView ivLoading;
    private TextView tvMore;

    private OnItemClickListener listener;

    public AddMoreBaseAdapter(Context context, List<?> data) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case LAYOUT_ORDINARY:
                view = inflater.inflate(getLayoutId(), parent, false);
                break;
            case LAYOUT_ADD_MORE:
                view = inflater.inflate(R.layout.item_add_more, parent, false);
                break;
        }
        if (view == null) {
            viewHolder = onMyCreateViewHolder(parent, viewType);
        } else {
            viewHolder = new ViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        if (position == getItemCount() - 1) {
            if (isHasMore && mData.size() != 0) {
                viewHolder.getConvertView().setVisibility(View.VISIBLE);
                if (animation == null) {
                    animation = new RotateAnimation(0, 359, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(-1);
                }
                if (ivLoading == null) {
                    ivLoading = viewHolder.getView(R.id.iv_loading);
                }
                ivLoading.setVisibility(View.VISIBLE);
                ivLoading.setAnimation(animation);
                if (mListener != null) {
                    mListener.onLoad();
                }


            } else if (!isHasMore && mData.size() > 0) {
                if (ivLoading == null) {
                    ivLoading = viewHolder.getView(R.id.iv_loading);
                }
                if (tvMore==null){
                    tvMore = viewHolder.getView(R.id.tv_more);
                }
                viewHolder.getConvertView().setVisibility(View.VISIBLE);
                ivLoading.setVisibility(View.GONE);
                tvMore.setText("没有更多了");

            } else {
                viewHolder.getConvertView().setVisibility(View.GONE);
            }

        } else {
            onMyBindViewHolder(viewHolder, position);
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(v, position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LAYOUT_ADD_MORE;
        }
        return LAYOUT_ORDINARY;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public void setLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public void stopAnimation() {

        if (ivLoading != null) {
            ivLoading.clearAnimation();
        }
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }


    protected abstract int getLayoutId();

    protected abstract void onMyBindViewHolder(ViewHolder holder, final int position);

    protected ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    ;

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

}

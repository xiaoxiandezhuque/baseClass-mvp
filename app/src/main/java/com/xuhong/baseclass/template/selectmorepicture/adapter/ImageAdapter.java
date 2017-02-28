package com.xuhong.baseclass.template.selectmorepicture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.ui.adapter.holder.ViewHolder;
import com.xuhong.baseclass.template.selectmorepicture.bean.Image;
import com.xuhong.baseclass.template.selectmorepicture.ImageLoaderListener;

import java.util.List;


/**
 * 图片列表界面适配器
 */
public class ImageAdapter extends RecyclerView.Adapter {
    private ImageLoaderListener loader;
    protected final LayoutInflater inflater;
    private List<Image> mData;


    private OnItemClickListener  mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public ImageAdapter(Context context, ImageLoaderListener loader, List<Image> data) {
        inflater = LayoutInflater.from(context);
        this.loader = loader;
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        Image image = mData.get(position);
        if (image.getId() == 0)
            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_list_cam, parent, false);
            return new ViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_list_image, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Image image = mData.get(position);

        if (image.getId() != 0) {
            ImageView mImageView = ((ViewHolder) holder).getView(R.id.iv_image);
            ImageView mCheckView = ((ViewHolder) holder).getView(R.id.cb_selected);
            View mMaskView = ((ViewHolder) holder).getView(R.id.lay_mask);
            mCheckView.setSelected(image.isSelect());
            mMaskView.setVisibility(image.isSelect() ? View.VISIBLE : View.GONE);
            loader.displayImage(mImageView, image.getPath());
        }
        ((ViewHolder) holder).getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null){
                    mListener.onClick(v,position);
                }
            }
        });
    }

}

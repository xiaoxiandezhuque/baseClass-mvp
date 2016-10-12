package com.xuhong.baseclass.template.selectmorepicture.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.adapter.BaseAdapter;
import com.xuhong.baseclass.adapter.ViewHolder;
import com.xuhong.baseclass.template.selectmorepicture.bean.ImageFolder;
import com.xuhong.baseclass.template.selectmorepicture.ImageLoaderListener;


/**
 * Created by huanghaibin_dev
 * on 2016/7/13.
 */

public class ImageFolderAdapter extends BaseAdapter<ImageFolder> {
    private ImageLoaderListener loader;

    public ImageFolderAdapter(Context context) {
        super(context,false);
    }


//    public ImageFolderAdapter(Context context) {
//        super(context, NEITHER);
//    }

//    @Override
//    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
//        return new FolderViewHolder(mInflater.inflate(R.layout.item_list_folder, parent, false));
//    }

//    @Override
//    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, ImageFolder item, int position) {
//        FolderViewHolder h = (FolderViewHolder) holder;
//        h.tv_name.setText(item.getName());
//        h.tv_size.setText(String.format("(%s)", item.getImages().size()));
//        if (loader != null) {
//            loader.displayImage(h.iv_image, item.getAlbumPath());
//        }
//    }

    public void setLoader(ImageLoaderListener loader) {
        this.loader = loader;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.item_list_folder;
    }

    @Override
    protected void onMyBindViewHolder(ViewHolder holder, int position, ImageFolder data) {


        int i = getItemCount();
        ImageView iv_image = holder.getView(R.id.iv_folder);
        TextView tv_name= holder.getView(R.id.tv_folder_name);
        TextView   tv_size= holder.getView(R.id.tv_size);

        tv_name.setText(data.getName());
        tv_size.setText(String.format("(%s)", data.getImages().size()));
        if (loader != null) {
            loader.displayImage(iv_image, data.getAlbumPath());
        }
    }



//    private static class FolderViewHolder extends RecyclerView.ViewHolder {
//        ImageView iv_image;
//        TextView tv_name, tv_size;
//
//        public FolderViewHolder(View itemView) {
//            super(itemView);
//            iv_image = (ImageView) itemView.findViewById(R.id.iv_folder);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_folder_name);
//            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
//        }
//    }
}

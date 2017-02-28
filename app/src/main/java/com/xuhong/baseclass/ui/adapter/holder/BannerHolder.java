package com.xuhong.baseclass.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.xuhong.baseclass.utils.ImageLoaderUtils;


/**
 * Created by BHKJ on 2016/8/19.
 */

public class BannerHolder implements Holder<String> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        ImageLoaderUtils.displayImage(context,data,imageView);
    }
}

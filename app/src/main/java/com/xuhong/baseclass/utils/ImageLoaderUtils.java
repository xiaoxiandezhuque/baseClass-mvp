package com.xuhong.baseclass.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xuhong.baseclass.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by BHKJ on 2016/5/11.
 */
public class ImageLoaderUtils {

//    private static ImageLoaderUtils imageLoaderUtils;

//    public static ImageLoaderUtils getInstance() {
//
//        if (imageLoaderUtils == null) {
//            synchronized (ImageLoaderUtils.class){
//                if (imageLoaderUtils == null){
//                    imageLoaderUtils = new ImageLoaderUtils();
//                }
//            }
//        }
//        return imageLoaderUtils;
//    }

   public static void displayImage(Context context, String url, ImageView imageView){
       Glide.with(context)
               .load(url)
               .diskCacheStrategy(DiskCacheStrategy.RESULT)
               .placeholder(R.mipmap.ic_launcher)
               .error(R.mipmap.ic_launcher)
               .centerCrop()
               .into(imageView);
   }
    public static void displayImageAvatar(Context context, String url, ImageView imageView){
        //毛玻璃效果  需要时可以添加 BlurTransformation
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }
    public static void displayImageAvatar(Context context, Uri  uri, ImageView imageView){
        //毛玻璃效果  需要时可以添加 BlurTransformation
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void getBitmapFromUrl(Context context, String url,final LoadBitmapListener  listener){
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        listener.loadDown(resource);
                    }
                });
    }


    public  interface  LoadBitmapListener{
        void  loadDown(Bitmap bitmap);
    }
}

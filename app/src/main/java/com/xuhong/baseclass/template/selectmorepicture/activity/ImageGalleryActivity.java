package com.xuhong.baseclass.template.selectmorepicture.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xuhong.baseclass.R;
import com.xuhong.baseclass.activity.BaseActivity;
import com.xuhong.baseclass.template.selectmorepicture.AppOperator;
import com.xuhong.baseclass.utils.PicturesCompress;
import com.xuhong.baseclass.utils.StreamUtils;
import com.xuhong.baseclass.template.selectmorepicture.view.ImagePreviewView;
import com.xuhong.baseclass.template.selectmorepicture.view.PreviewerViewPager;

import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * 图片预览Activity
 */
public class ImageGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String KEY_IMAGE = "images";
    public static final String KEY_POSITION = "position";
    public static final String KEY_NEED_SAVE = "save";
    private PreviewerViewPager mImagePager;
    private TextView mIndexText;
    private String[] mImageSources;
    private int mCurPosition;
    private boolean mNeedSaveLocal;

    public static void show(Context context, String images) {
        show(context, images, true);
    }

    public static void show(Context context, String images, boolean needSaveLocal) {
        if (images == null)
            return;
        show(context, new String[]{images}, 0);
    }

    public static void show(Context context, String[] images, int position) {
        show(context, images, position, true);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal) {
        if (images == null || images.length == 0)
            return;
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra(KEY_IMAGE, images);
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra(KEY_NEED_SAVE, needSaveLocal);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_gallery;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        mImageSources = bundle.getStringArray(KEY_IMAGE);
        mCurPosition = bundle.getInt(KEY_POSITION, 0);
        mNeedSaveLocal = bundle.getBoolean(KEY_NEED_SAVE, true);


        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        setTitle("");
        mImagePager = (PreviewerViewPager) findViewById(R.id.vp_image);
        mIndexText = (TextView) findViewById(R.id.tv_index);
        mImagePager.addOnPageChangeListener(this);
        if (mNeedSaveLocal)
            findViewById(R.id.iv_save).setOnClickListener(this);
        else
            findViewById(R.id.iv_save).setVisibility(View.GONE);

        int len = mImageSources.length;
        if (mCurPosition < 0 || mCurPosition >= len)
            mCurPosition = 0;

        // If only one, we not need the text to show
        if (len == 1)
            mIndexText.setVisibility(View.GONE);

        mImagePager.setAdapter(new ViewPagerAdapter());
        mImagePager.setCurrentItem(mCurPosition);
        // First we call to init the TextView
        onPageSelected(mCurPosition);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_save:
                saveToFile();
                break;
        }
    }

    private void saveToFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,"没有外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        String path = mImageSources[mCurPosition];

        // In this save max image size is source
        final Future<File> future = Glide.with(this).load(path).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);


        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    String extension = PicturesCompress.getExtension(sourceFile.getAbsolutePath());
                    String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + File.separator + "开源中国";
                    File extDirFile = new File(extDir);
                    if (!extDirFile.exists()) {
                        if (!extDirFile.mkdirs()) {
                            callSaveStatus(false, null);
                            return;
                        }
                    }
                    final File saveFile = new File(extDirFile, String.format("IMG_%s.%s", System.currentTimeMillis(), extension));
                    final boolean isSuccess = StreamUtils.copyFile(sourceFile, saveFile);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callSaveStatus(isSuccess, saveFile);
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callSaveStatus(boolean success, File savePath) {
        if (success) {
            // notify
            Uri uri = Uri.fromFile(savePath);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(ImageGalleryActivity.this,"保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ImageGalleryActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
        mIndexText.setText(String.format("%s/%s", (position + 1), mImageSources.length));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private class ViewPagerAdapter extends PagerAdapter implements ImagePreviewView.OnReachBorderListener {

        @Override
        public int getCount() {
            return mImageSources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.lay_gallery_page_item_contener, container, false);
            ImagePreviewView previewView = (ImagePreviewView) view.findViewById(R.id.iv_preview);
            previewView.setOnReachBorderListener(this);
            final Loading loading = (Loading) view.findViewById(R.id.loading);
            Glide.with(ImageGalleryActivity.this).load(mImageSources[position])
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            loading.stop();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            loading.stop();
                            loading.setVisibility(View.GONE);
                            return false;
                        }
                    }).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(previewView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onReachBorder(boolean isReached) {
            mImagePager.isInterceptable(isReached);
        }
    }
}

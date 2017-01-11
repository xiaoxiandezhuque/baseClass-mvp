package com.xuhong.baseclass.template.selectmorepicture.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xuhong.baseclass.ui.activity.BaseActivity;
import com.xuhong.baseclass.template.selectmorepicture.activity.SelectImageActivity;
import com.xuhong.baseclass.template.selectmorepicture.adapter.TweetPicturesPreviewerItemTouchCallback;
import com.xuhong.baseclass.template.selectmorepicture.adapter.TweetSelectImageAdapter;


/**
 * Created by JuQiu
 * on 16/7/18.
 * <p>
 * 动弹发布界面, 图片预览器
 * <p>
 * 提供图片预览/图片操作 返回选中图片等功能
 */

public class TweetPicturesPreviewer extends RecyclerView implements TweetSelectImageAdapter.Callback, SelectImageActivity.Callback {
    private TweetSelectImageAdapter mImageAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private RequestManager mCurImageLoader;

    public TweetPicturesPreviewer(Context context) {
        super(context);
        init();
    }

    public TweetPicturesPreviewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TweetPicturesPreviewer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mImageAdapter = new TweetSelectImageAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        this.setLayoutManager(layoutManager);
        this.setAdapter(mImageAdapter);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);

        ItemTouchHelper.Callback callback = new TweetPicturesPreviewerItemTouchCallback(mImageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    public void set(String[] paths) {
        mImageAdapter.clear();
        for (String path : paths) {
            mImageAdapter.add(path);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreClick() {
        SelectImageActivity.showImage(getContext(), 9, true, mImageAdapter.getPaths(), this);
    }

    @Override
    public RequestManager getImgLoader() {
        if (mCurImageLoader == null) {
            mCurImageLoader = Glide.with(getContext());

        }
        return mCurImageLoader;
    }

    @Override
    public void onStartDrag(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public String[] getPaths() {
        return mImageAdapter.getPaths();
    }

    public void destroy() {
        Context context = getContext();
        if (!(context != null && context instanceof BaseActivity)) {
            mCurImageLoader.onDestroy();
        }
        mCurImageLoader = null;
    }

    @Override
    public void doSelectDone(String[] images) {
        set(images);
    }
}

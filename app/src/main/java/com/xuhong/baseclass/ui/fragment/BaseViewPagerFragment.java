package com.xuhong.baseclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;



/**
 * Created by xuhong on 2017/3/4.
 */

public abstract class BaseViewPagerFragment<V,T> extends BaseFragment{
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    //fragment的布局
    protected View mView;
    /** 标志位，标志已经初始化完成 */
    protected boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    protected boolean mHasLoadedOnce;


    //这个fragment是第几个
    protected int mCurIndex=-1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);

            Bundle bundle = getArguments();
            if (bundle != null) {
                mCurIndex = bundle.getInt("name");
            }
            isPrepared = true;
            if (isVisible){
                ButterKnife.bind(this, mView);
                mPersenter= createdPresenter();
                lazyLoad();
                mHasLoadedOnce = true;
            }

        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup)mView.getParent();
        if(parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {

        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        ButterKnife.bind(this, mView);
        mPersenter= createdPresenter();
        lazyLoad();
        mHasLoadedOnce = true;
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    @Override
    protected void initView() {

    }
}

package com.xuhong.baseclass.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by BHKJ on 2016/5/10.
 */
public abstract class BasePresenter<T> {
    protected Reference<T> mViewRef;

    public BasePresenter(T view){
        mViewRef = new WeakReference<>(view);
    }
    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


}

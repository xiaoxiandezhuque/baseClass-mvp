package com.xuhong.baseclass.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuhong.baseclass.presenter.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by BHKJ on 2016/5/10.
 */
public abstract class BaseFragment<V,T extends BasePresenter<V>> extends Fragment {

    //fragment的布局
    protected View mView;
    /** 标志位，标志已经初始化完成 */
    protected T mPersenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);

            ButterKnife.bind(this, mView);
            mPersenter = createdPresenter();
            initView();
        }

        return mView;
    }


    protected  T createdPresenter(){
        return  null;
    }


    @Override
    public void onDestroy() {
        if (mPersenter!=null){
            mPersenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void initView();
    protected abstract  int  getLayoutId();


}

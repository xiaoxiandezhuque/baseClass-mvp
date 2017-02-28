package com.xuhong.baseclass.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuhong.baseclass.presenter.BasePresenter;
import com.xuhong.baseclass.utils.StatusBarUtils;

import butterknife.ButterKnife;

/**
 * Created by BHKJ on 2016/5/10.
 */
public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {

    protected  T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBarColor();
        mPresenter =createdPresenter();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.detachView();
        }

    }

    protected void setStatusBarColor() {
        StatusBarUtils.setColor(this, Color.parseColor("#f02e2e"));
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected  T createdPresenter(){
        return  null;
    }

}

package com.xuhong.baseclass.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xuhong.baseclass.utils.StatusBarUtils;

import butterknife.ButterKnife;

/**
 * Created by BHKJ on 2016/5/10.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBarColor();
        initView();
    }

    protected void setStatusBarColor() {
        StatusBarUtils.setColor(this, Color.parseColor("#f02e2e"));
    }

    protected abstract int getLayoutId();

    protected abstract void initView();


}

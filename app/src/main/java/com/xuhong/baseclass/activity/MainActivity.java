package com.xuhong.baseclass.activity;

import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.adapter.BaseAdapter;
import com.xuhong.baseclass.adapter.MainAdapter;
import com.xuhong.baseclass.iview.IMainView;
import com.xuhong.baseclass.presenter.MainPresenter;
import com.xuhong.baseclass.view.RefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainView {


    @BindView(R.id.refreshview)
    RefreshView mRefreshview;

    private RecyclerView  mRecyclerView;
    private BaseAdapter mAdapter;
    private List<String> mData;

    private MainPresenter  mMainPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mMainPresenter = new MainPresenter(this);
        mData = new ArrayList<>();

        for (int i = 0; i <20 ; i++) {
            mData.add("第"+i+"条");
        }

        mAdapter = new MainAdapter(this);

        mRecyclerView = mRefreshview.getRecycleView();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        mRefreshview.setOnRefreshListener(new RefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新","刷新");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshview.onRefreshComplete();
                    }
                },3000);
            }
        });
        mRefreshview.onRefreshStart();

    }

    @Override
    protected void onDestroy() {
        if (mMainPresenter!=null){
            mMainPresenter.detachView();
        }
        super.onDestroy();
    }
}

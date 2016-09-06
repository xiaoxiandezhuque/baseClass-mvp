package com.xuhong.baseclass.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xuhong.baseclass.R;

import java.util.List;


/**
 * Created by BHKJ on 2016/6/13.
 */

//主界面的adapter
public class MainAdapter extends BaseAdapter {


    public MainAdapter(Context mContent, List<String> mData) {
        super(mContent, mData);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_main;
    }


    @Override
    public void onMyBindViewHolder(ViewHolder holder, int position) {
        String str = (String) mData.get(position);

        ((TextView)holder.getView(R.id.tv_name)).setText(str);

    }





}

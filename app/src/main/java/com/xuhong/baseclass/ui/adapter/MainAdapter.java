package com.xuhong.baseclass.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.ui.adapter.holder.ViewHolder;


/**
 * Created by BHKJ on 2016/6/13.
 */

//主界面的adapter
public class MainAdapter extends BaseAdapter<String> {


    public MainAdapter(Context context) {
        super(context);
    }

    public MainAdapter(Context context, boolean isOpenAddMore) {
        super(context, isOpenAddMore);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_main;
    }

    @Override
    protected void onMyBindViewHolder(ViewHolder holder, int position, String data) {
        ((TextView) holder.getView(R.id.tv_name)).setText(data);
    }


}

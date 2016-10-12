package com.xuhong.baseclass.template.selectmorepicture.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by huanghaibin
 * on 16-5-9.
 */
public class SpaceGridItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceGridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}

package com.xuhong.baseclass.template.selectmorepicture.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by JuQiu
 * on 16/7/15.
 * 正方形控件
 */
public class SquareLayout extends FrameLayout {

    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightSize == 0) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            return;
        }

        if (widthSize == 0) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            return;
        }

        if (widthSize > heightSize)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        */
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

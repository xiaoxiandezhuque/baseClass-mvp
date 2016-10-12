package com.xuhong.baseclass.template.selectmorepicture.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by thanatos on 16/8/5.
 */
public class PreviewerViewPager extends ViewPager {

    private boolean isInterceptable = false;
    private boolean isTransition = false;

    public PreviewerViewPager(Context context) {
        super(context);
    }

    public PreviewerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            isInterceptable = false;
        }

        boolean b = false;

        if (isTransition){
            MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, ev.getX(), ev.getY(), 0);
            super.onInterceptTouchEvent(event);
            isTransition = false;
        }

        if (ev.getAction() != MotionEvent.ACTION_MOVE || isInterceptable){
            b = super.onInterceptTouchEvent(ev);
        }

        return isInterceptable && b;
    }

    public void isInterceptable(boolean b){
        if (!isInterceptable && b) isTransition = true;
        this.isInterceptable = b;
    }
}

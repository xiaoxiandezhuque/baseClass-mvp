package com.xuhong.baseclass.utils;


import android.util.DisplayMetrics;

import com.xuhong.baseclass.BaseApp;


/**
 * Created by xuhong on 2016/8/7.
 */

public class DensityUtils {
    /**
     * 将dp值转换为px值，保证文字大小不变
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dp值，保证文字大小不变
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 获取高度的英寸数
    public static int getHeightInch(float dpi, float target) {
        DisplayMetrics dm = BaseApp.getInstance().getResources().getDisplayMetrics();
        double heightInch = (double) dm.heightPixels / dpi;
        return (int) (((double) target / heightInch) * dm.heightPixels);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = BaseApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}

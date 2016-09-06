package com.xuhong.baseclass.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.xuhong.baseclass.BaseApp;


/**
 * Created by BHKJ on 2016/5/31.
 */
public class ScreenUtils {

    /**
     * 获取屏幕相关参数   宽高
     * @return
     */
    public static DisplayMetrics  getScreenSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

}

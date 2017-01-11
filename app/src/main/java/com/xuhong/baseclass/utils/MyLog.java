package com.xuhong.baseclass.utils;

import android.util.Log;

/**
 * Created by BHKJ on 2016/8/24.
 */

public class MyLog {

    private  final static  boolean isOpen =true;

    public static void e(String tag,String msg){
        if (isOpen){
            Log.e(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if (isOpen){
            Log.d(tag,msg);
        }
    }
}

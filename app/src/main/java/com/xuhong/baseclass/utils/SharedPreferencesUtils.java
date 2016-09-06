package com.xuhong.baseclass.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xuhong.baseclass.BaseApp;


/**
 * Created by BHKJ on 2016/8/25.
 */

public class SharedPreferencesUtils {
    public static final String DEFAULT_LAYOUT = "default_layout";


    private SharedPreferencesUtils(){

    }
    public static SharedPreferencesUtils  getInstance(){
        return  SingeInstance.INSTANCE;
    }
    private static class SingeInstance {
        private static final SharedPreferencesUtils INSTANCE = new SharedPreferencesUtils();
    }

    public synchronized boolean getDefaultLayout() {
        SharedPreferences sp = BaseApp.getInstance()
                .getSharedPreferences(DEFAULT_LAYOUT, Context.MODE_PRIVATE);
        return sp.getBoolean("key",true);
    }
    public synchronized void setDefaultLayout(boolean bool) {
        SharedPreferences sp = BaseApp.getInstance()
                .getSharedPreferences(DEFAULT_LAYOUT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("key",bool);
        editor.apply();
    }


}

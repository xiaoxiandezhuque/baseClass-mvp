package com.xuhong.baseclass;

import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

import org.litepal.LitePalApplication;

/**
 * Created by BHKJ on 2016/5/10.
 */
//继续litepal的application使数据库的操作不需要传content
public class BaseApp extends LitePalApplication {

    //唯一baseapp的引用
    private static BaseApp baseApp;

    @Override
    public void onCreate() {
        super.onCreate();
//        上传错误日志
//        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(this));

        // 严苛模式---检查内存泄露和资源的关闭
        int appFlags = getApplicationContext().getApplicationInfo().flags;
        if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) { // 在调试和真机运行使用
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedClosableObjects().detectLeakedSqlLiteObjects()
                    .penaltyLog().penaltyDeath().build());
        }

        init();

    }

    private void init() {
        baseApp = this;


    }

    public static BaseApp getInstance() {
        return baseApp;
    }

}

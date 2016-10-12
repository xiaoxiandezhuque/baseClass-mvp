package com.xuhong.baseclass.template.selectmorepicture;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JuQiu
 * on 16/6/24.
 */

public final class AppOperator {
    private static ExecutorService EXECUTORS_INSTANCE;

    public static Executor getExecutor() {
        if (EXECUTORS_INSTANCE == null) {
            synchronized (AppOperator.class) {
                if (EXECUTORS_INSTANCE == null) {
                    EXECUTORS_INSTANCE = Executors.newFixedThreadPool(
                            Runtime.getRuntime().availableProcessors() > 0 ?
                                    Runtime.getRuntime().availableProcessors() : 2);
                }
            }
        }
        return EXECUTORS_INSTANCE;
    }

    public static void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

}

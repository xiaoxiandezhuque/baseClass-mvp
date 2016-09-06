package com.xuhong.baseclass.model;

import com.bumptech.glide.Glide;
import com.xuhong.baseclass.BaseApp;
import com.xuhong.baseclass.utils.Constants;

import java.io.File;
import java.math.BigDecimal;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/9/2.
 */

public class GlideModel {


    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(Action1 action1) {
        Observable.just("")
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Glide.get(BaseApp.getInstance()).clearDiskCache();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);

    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public void getCacheSize(Action1<String> action1) {
        Observable.just(BaseApp.getInstance().getExternalCacheDir() + Constants.CACHE_PATH)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            return getFormatSize(getFolderSize(new File(s)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return "";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);

    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0.00M";
        }
        double megaByte = kiloByte / 1024;

        BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";

//        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }
//
//        double megaByte = kiloByte / 1024;
//        if (megaByte < 1) {
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
//        }
//
//        double gigaByte = megaByte / 1024;
//        if (gigaByte < 1) {
//            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
//            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
//        }
//
//        double teraBytes = gigaByte / 1024;
//        if (teraBytes < 1) {
//            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
//            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
//        }
//        BigDecimal result4 = new BigDecimal(teraBytes);
//
//        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}

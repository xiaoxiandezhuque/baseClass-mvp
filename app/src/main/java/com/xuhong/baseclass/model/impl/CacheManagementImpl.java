package com.xuhong.baseclass.model.impl;

import com.bumptech.glide.Glide;
import com.xuhong.baseclass.BaseApp;
import com.xuhong.baseclass.model.CacheManagement;
import com.xuhong.baseclass.utils.Constants;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by BHKJ on 2017/1/11.
 */

public class CacheManagementImpl implements CacheManagement {

    /**
     * 耗时任务
     *
     * @return
     */
    @Override
    public String getCacheSize() {
        return getFormatSize(getFolderSize(new File(BaseApp.getInstance().getExternalCacheDir()
                + Constants.CACHE_PATH)));
    }
    @Override
    public void cleanCache() {
        Glide.get(BaseApp.getInstance()).clearDiskCache();

    }

    private long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File aFileList : fileList) {
            if (aFileList.isDirectory()) {
                size = size + getFolderSize(aFileList);
            } else {
                size = size + aFileList.length();
            }
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private String getFormatSize(double size) {

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

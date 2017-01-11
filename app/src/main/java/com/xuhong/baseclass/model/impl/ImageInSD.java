package com.xuhong.baseclass.model.impl;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHKJ on 2016/9/20.
 */

public class ImageInSD {


    private List<String> imagePathList = new ArrayList<String>();


    public List<String> getImagePathList() {
        return imagePathList;
    }

    /**
     * 从sd卡获取图片资源
     *
     * @return
     */
    public void getImagePathFromSD() {
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator;
        File file = new File(filePath);
        getImagePath(file);
    }

    private void getImagePath(File fileAll) {
        File[] files = fileAll.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file = files[i];
            if (file.isFile()) {
                if (checkIsImageFile(file.getPath())) {
                    imagePathList.add(file.getPath());
                }
            } else {
                getImagePath(file);
            }
        }

    }


    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
}

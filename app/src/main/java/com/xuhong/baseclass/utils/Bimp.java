package com.xuhong.baseclass.utils;

import android.os.Environment;

import java.io.IOException;

/**
 * Created by BHKJ on 2016/9/9.
 */

public class Bimp {


    public static String revitionImageSize(String path) throws IOException {
        String tempFile = Environment.getExternalStorageDirectory() + "/Travel/" + System.currentTimeMillis() + ".jpg";
        if (PicturesCompress.compressImage(path, tempFile, 512 * 1024, 80, 1280, 1280 * 2, true)){
            return tempFile;
        }

        return  path;
    }


}

package com.xuhong.baseclass.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by BHKJ on 2016/10/11.
 */

public class StreamUtils {


    public static boolean copyFile(final File srcFile, final File saveFile) {
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs())
                return false;
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream, outputStream);
        }
        return true;
    }



    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

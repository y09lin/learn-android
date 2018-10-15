package com.huim_lin.learn.util;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

    public static String getStoragePath(Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getDownloadCacheDirectory().getAbsolutePath();
//            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getFileNameByUrl(String url){
        return url.substring(url.lastIndexOf("/"),url.length());
    }
}

package com.huim_lin.learn.util;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

    public static String getStoragePath(Context context){
        String cachePath;
        cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return cachePath;
    }

    public static String getFileNameByUrl(String url){
        return url.substring(1+url.lastIndexOf("/"),url.length());
    }

    public static String getMp3Path(){
        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return cachePath+"/learn/mp3/";
    }
}

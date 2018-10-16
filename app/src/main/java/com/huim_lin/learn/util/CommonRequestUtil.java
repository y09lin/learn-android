package com.huim_lin.learn.util;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommonRequestUtil {
    public interface RequestCallback{
        void onGetResult(String result);
        void onError();
    }

    public static void sentRequest(final Activity activity, String url, String postData,
                                   String token, final RequestCallback callback){
        OkHttpUtil.OkListener listener = new OkHttpUtil.OkListener() {
            @Override
            public void onGetResult(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetResult(result);
                    }
                });
            }

            @Override
            public void onError() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        };
        if (TextUtils.isEmpty(postData)){
            OkHttpUtil.requestGet(url,token,listener);
        }else{
            OkHttpUtil.requestJsonPost(url, postData, token, listener);
        }
    }

    public static void downloadFile(final Activity activity, final String url, String token,
                                    final RequestCallback callback){
        OkHttpUtil.getFile(url, token, new OkHttpUtil.OkFileListener() {
            @Override
            public void onGetFile(InputStream is, long length) {
                String basePath = FileUtil.getStoragePath(activity);
                final String fileName = FileUtil.getFileNameByUrl(url);
                String dir = basePath+"/learn/mp3";
                File folder = new File(dir);
                if (!folder.exists()){
                    Log.d("DownloadFile",""+folder.mkdirs());
                }
                String path = dir+"/"+fileName;
                FileOutputStream fos = null;
                try {
                    byte[] buf = new byte[2048];
                    int len;
                    File file = new File(path);
                    if (!file.exists()) {
                        fos = new FileOutputStream(file);
                        long sum=0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum+=len;
                            Log.d("DownloadFile","===="+(sum * 1.0f / length * 100));
                        }
                        fos.flush();
                        Log.d("DownloadFile","success");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("DownloadFile","error");
                    path = "";
                } finally {
                    try {
                        if (is!=null){
                            is.close();
                        }
                        if (fos!=null){
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        path = "";
                    }
                }
                final String filePath = path;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(filePath)){
                            callback.onError();
                        }else{
                            callback.onGetResult(filePath);
                        }
                    }
                });
            }

            @Override
            public void onError() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        });
    }
}

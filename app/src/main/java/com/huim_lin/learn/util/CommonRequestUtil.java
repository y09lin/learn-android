package com.huim_lin.learn.util;

import android.app.Activity;
import android.text.TextUtils;

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
}

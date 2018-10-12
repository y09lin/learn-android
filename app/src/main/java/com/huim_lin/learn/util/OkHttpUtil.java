package com.huim_lin.learn.util;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpUtil {

    private static final String TAG="OkHttpUtil";

    public interface OkListener{
        void onGetResult(String result);
        void onError();
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    public static void requestGet(String url,String token,OkListener listener){
        Request.Builder builder=new Request.Builder().addHeader("token",token);
        Request request=builder.url(url).build();
        getResponse(request,listener);
    }

    public static void requestJsonPost(String url, String postData,
                                       String token, OkListener listener){
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(url).addHeader("token", token)
                .post(RequestBody.create(type, postData))
                .build();
        getResponse(request,listener);
    }

    private static void getResponse(final Request request, final OkListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        listener.onError();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) {
                        try {
                            ResponseBody body = response.body();
                            if (body != null){
                                listener.onGetResult(body.string());
                            }else{
                                listener.onError();
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: "+e.getMessage());
                            listener.onError();
                        }
                    }
                });
            }
        }).start();

    }
}

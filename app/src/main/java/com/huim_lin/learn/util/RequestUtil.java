package com.huim_lin.learn.util;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.PageDto;


public class RequestUtil {
    private static final int DEFAULT_SIZE = 20;

    private static final String TOKEN = "this_is_token_test";

    public interface GetArticleListener{
        void onGetList(PageDto<Article> page);
        void onError(long code);
    }

    public static void getArticleList(final Activity activity, int pageIndex,
                                      final GetArticleListener listener){
        JSONObject json = new JSONObject();
        json.put("pageIndex",pageIndex);
        json.put("pageSize",DEFAULT_SIZE);
        String url = Constants.BASE_URL+Constants.GET_ARTICLE;
        CommonRequestUtil.sentRequest(activity, url, json.toString(), TOKEN, new CommonRequestUtil.RequestCallback() {
            @Override
            public void onGetResult(String result) {
                try {
                    JSONObject object = JSON.parseObject(result);
                    long code = object.getLong("code");
                    if (code == 0){
                        JSONObject data = object.getJSONObject("data");
                        PageDto<Article> page = new PageDto<>();
                        page.setPageIndex(data.getInteger("pageIndex"));
                        page.setPageSize(data.getInteger("pageSize"));
                        page.setTotalElement(data.getInteger("totalElement"));
                        page.setTotalPage(data.getInteger("totalPage"));
                        page.setDataList(JSON.parseArray(data.getString("dataList"),Article.class));
                        listener.onGetList(page);
                    }else{
                        listener.onError(code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(-1);
                }
            }

            @Override
            public void onError() {
                listener.onError(-1);
            }
        });

        /*
        OkHttpUtil.requestJsonPost(url, json.toString(), TOKEN, new OkHttpUtil.OkListener() {
            @Override
            public void onGetResult(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = JSON.parseObject(result);
                            long code = object.getLong("code");
                            if (code == 0){
                                JSONObject data = object.getJSONObject("data");
                                PageDto<Article> page = new PageDto<>();
                                page.setPageIndex(data.getInteger("pageIndex"));
                                page.setPageSize(data.getInteger("pageSize"));
                                page.setTotalElement(data.getInteger("totalElement"));
                                page.setTotalPage(data.getInteger("totalPage"));
                                page.setDataList(JSON.parseArray(data.getString("dataList"),Article.class));
                                listener.onGetList(page);
                            }else{
                                listener.onError(code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(-1);
                        }
                    }
                });
            }

            @Override
            public void onError() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(-1);
                    }
                });
            }
        });
        */
    }
}

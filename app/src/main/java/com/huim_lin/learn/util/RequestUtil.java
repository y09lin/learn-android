package com.huim_lin.learn.util;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.ArticleDetail;
import com.huim_lin.learn.bean.PageDto;
import com.huim_lin.learn.bean.Sentence;

import java.io.File;
import java.util.List;


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
        CommonRequestUtil.sentRequest(activity, url, json.toString(), TOKEN,
                new CommonRequestUtil.RequestCallback() {
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
    }

    public interface GetArticleDetailListener{
        void onGetArticle(ArticleDetail detail);
        void onError(long code);
    }

    public static void getArticleDetail(Activity activity, long articleId,
                                        final GetArticleDetailListener listener){
        String url = Constants.BASE_URL + Constants.GET_ARTICLE + "/" + articleId;
        CommonRequestUtil.sentRequest(activity, url, null, TOKEN, new CommonRequestUtil.RequestCallback() {
            @Override
            public void onGetResult(String result) {
                try{
                    JSONObject object = JSON.parseObject(result);
                    long code = object.getLong("code");
                    if (code == 0){
                        JSONObject data = object.getJSONObject("data");
                        ArticleDetail detail = JSON.parseObject(data.toString(),ArticleDetail.class);
                        listener.onGetArticle(detail);
                    } else {
                        listener.onError(code);
                    }
                } catch (Exception e){
                    listener.onError(-1);
                }
            }

            @Override
            public void onError() {
                listener.onError(-1);
            }
        });
    }

    public interface DownloadFileListener{
        void onGetFile(String path);
        void onError();
    }

    public static void downloadFile(Activity activity,String url, final DownloadFileListener listener){
        String path = FileUtil.getMp3Path();
        path+=FileUtil.getFileNameByUrl(url);
        File file = new File(path);
        if (file.exists()){
            listener.onGetFile(path);
        }else{
            CommonRequestUtil.downloadFile(activity, url, TOKEN, new CommonRequestUtil.RequestCallback() {
                @Override
                public void onGetResult(String result) {
                    if (!TextUtils.isEmpty(result)){
                        listener.onGetFile(result);
                    }else {
                        listener.onError();
                    }
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        }
    }

    public interface CommonListener {
        void onSuccess();
        void onError(long code);
    }

    public static void submitSentence(Activity activity, long articleId, List<Sentence> sentenceList,
                                      final CommonListener listerner){
        String url = Constants.BASE_URL + Constants.SET_SENTENCE;
        JSONObject json = new JSONObject();
        json.put("articleId",articleId);
        JSONArray array = new JSONArray();
        for (Sentence s: sentenceList){
            JSONObject object = new JSONObject();
            object.put("sentenceId",s.getSentenceId());
            object.put("beginPoint",s.getBeginPoint());
            object.put("endPoint",s.getEndPoint());
            array.add(object);
        }
        json.put("pointList",array);
        CommonRequestUtil.sentRequest(activity, url, json.toString(), TOKEN, new CommonRequestUtil.RequestCallback() {
            @Override
            public void onGetResult(String result) {
                JSONObject object = JSON.parseObject(result);
                long code = object.getLong("code");
                if (code == 0){
                    listerner.onSuccess();
                }else{
                    listerner.onError(code);
                }
            }

            @Override
            public void onError() {
                listerner.onError(-1);
            }
        });
    }
}

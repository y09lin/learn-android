package com.huim_lin.learn.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.huim_lin.learn.R;
import com.huim_lin.learn.adapter.ArticleAdapter;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.PageDto;
import com.huim_lin.learn.util.RequestUtil;
import com.huim_lin.learn.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "ArticleList";
    private ListView listView;
    private ArticleAdapter articleAdapter;
    private List<Article> list;
    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        list = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this,list,R.layout.item_article);
        listView.setAdapter(articleAdapter);
        initData();
        getData();
    }

    private void initData() {
        pageIndex = 0;
    }

    public void getData() {
        RequestUtil.getArticleList(this,pageIndex, new RequestUtil.GetArticleListener() {
            @Override
            public void onGetList(PageDto<Article> page) {
                Log.d(TAG, "onGetList: " + page.getTotalPage() + " " + page.getTotalElement());
                list.addAll(page.getDataList());
                articleAdapter.notifyDataSetHasChanged();
            }

            @Override
            public void onError(long code) {
                Log.d(TAG,"onError");
                ToastUtil.showText(String.format(getString(R.string.requst_error),code));
            }
        });
    }
}

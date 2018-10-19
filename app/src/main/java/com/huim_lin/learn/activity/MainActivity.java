package com.huim_lin.learn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huim_lin.learn.R;
import com.huim_lin.learn.adapter.ArticleAdapter;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.PageDto;
import com.huim_lin.learn.util.RequestUtil;
import com.huim_lin.learn.util.ToastUtil;
import com.huim_lin.learn.widget.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "ArticleList";
    private ListView listView;
    private ArticleAdapter articleAdapter;
    private List<Article> list;
    private SuperSwipeRefreshLayout swipeLayout;
    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeLayout = findViewById(R.id.swipe_layout);
        listView = findViewById(R.id.list);
        list = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this,list,R.layout.item_article);
        listView.setAdapter(articleAdapter);
        initData();
        initEvent();
        getData();
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ArticleDetailAct.class);
                intent.putExtra(ArticleSetting.EXT_ARTICLE_ID,list.get(i));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ArticleSetting.class);
                intent.putExtra(ArticleSetting.EXT_ARTICLE_ID,list.get(i));
                startActivity(intent);
                return true;
            }
        });
        swipeLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                list.clear();
                getData();
            }

            @Override
            public void onPullDistance(int distance) {
            }

            @Override
            public void onPullEnable(boolean enable) {
            }

        });
        swipeLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageIndex++;
                getData();
            }

            @Override
            public void onPushDistance(int distance) {
            }

            @Override
            public void onPushEnable(boolean enable) {
            }
        });
    }

    private void initData() {
        pageIndex = 0;
    }

    private void endSwipe(){
        swipeLayout.setRefreshing(false);
        swipeLayout.setLoadMore(false);
    }

    public void getData() {
        RequestUtil.getArticleList(this,pageIndex, new RequestUtil.GetArticleListener() {
            @Override
            public void onGetList(PageDto<Article> page) {
                Log.d(TAG, "onGetList: " + page.getTotalPage() + " " + page.getTotalElement());
                list.addAll(page.getDataList());
                articleAdapter.notifyDataSetHasChanged();
                endSwipe();
            }

            @Override
            public void onError(long code) {
                Log.d(TAG,"onError");
                ToastUtil.showText(String.format(getString(R.string.request_error),code));
                endSwipe();
            }
        });
    }
}

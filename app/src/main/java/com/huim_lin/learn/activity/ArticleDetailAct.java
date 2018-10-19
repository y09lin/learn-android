package com.huim_lin.learn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.huim_lin.learn.R;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.ArticleDetail;
import com.huim_lin.learn.fragment.ArticleFrag;
import com.huim_lin.learn.util.RequestUtil;
import com.huim_lin.learn.util.ToastUtil;

import java.util.Objects;

public class ArticleDetailAct extends AppCompatActivity {
    public static final String EXT_ARTICLE_ID = "ext_article_id";
    private ArticleDetail article;

    private ProgressBar bar_article;
    private ImageView image_refresh;

    private ArticleFrag articleFrag;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        bar_article = findViewById(R.id.bar_article);
        image_refresh = findViewById(R.id.image_refresh);

        initData();
        initEvent();
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        image_refresh.setVisibility(View.GONE);
        bar_article.setVisibility(View.VISIBLE);
        RequestUtil.getArticleDetail(this, article.getArticleId(),
                new RequestUtil.GetArticleDetailListener() {
                    @Override
                    public void onGetArticle(ArticleDetail detail) {
                        article = detail;
                        addFrag(0);
                    }

                    @Override
                    public void onError(long code) {
                        ToastUtil.showText(getString(R.string.request_error, code));
                        image_refresh.setVisibility(View.VISIBLE);
                        bar_article.setVisibility(View.GONE);
                    }
                });
    }

    private void addFrag(int id){
        image_refresh.setVisibility(View.GONE);
        bar_article.setVisibility(View.GONE);
        if (articleFrag == null){
            articleFrag = ArticleFrag.newInstance(article);
        }
        if (fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame,articleFrag);
        transaction.commit();
    }

    private void initData() {
        Intent intent = getIntent();
        Article temp = (Article) intent.getSerializableExtra(EXT_ARTICLE_ID);
        if (temp == null) {
            ToastUtil.showText(getString(R.string.article_detail_error));
            finish();
            return;
        }
        article = new ArticleDetail(temp);
    }

    private void initEvent(){
        image_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
}

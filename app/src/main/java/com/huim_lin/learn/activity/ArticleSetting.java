package com.huim_lin.learn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huim_lin.learn.R;
import com.huim_lin.learn.bean.Article;
import com.huim_lin.learn.bean.ArticleDetail;
import com.huim_lin.learn.bean.Paragraph;
import com.huim_lin.learn.bean.Sentence;
import com.huim_lin.learn.util.RequestUtil;
import com.huim_lin.learn.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ArticleSetting extends AppCompatActivity implements View.OnClickListener {
    public static final String EXT_ARTICLE_ID = "ext_article_id";
    private TextView text_sentence,text_begin,text_end;
    private ImageButton btn_pre,btn_begin,btn_play,btn_end,btn_next;
    private ArticleDetail article;
    private List<Sentence> sentenceList;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_setting);
        text_sentence = findViewById(R.id.text_sentence);
        text_begin = findViewById(R.id.text_begin);
        text_end = findViewById(R.id.text_end);
        btn_pre = findViewById(R.id.btn_pre);
        btn_begin = findViewById(R.id.btn_begin);
        btn_play = findViewById(R.id.btn_play);
        btn_end = findViewById(R.id.btn_end);
        btn_next = findViewById(R.id.btn_next);
        initData();
        initEvent();
        getData();
    }

    private void getData() {
        RequestUtil.getArticleDetail(this, article.getArticleId(),
                new RequestUtil.GetArticleDetailListener() {
            @Override
            public void onGetArticle(ArticleDetail detail) {
                article = detail;
                sentenceList = new ArrayList<>();
                if (article.getParagraphList()!=null && article.getParagraphList().size()>0){
                    for (Paragraph p:article.getParagraphList()){
                        if (p.getSentenceList()!=null && p.getSentenceList().size()>0){
                            sentenceList.addAll(p.getSentenceList());
                        }
                    }
                }
                setDate();
            }

            @Override
            public void onError(long code) {
                ToastUtil.showText(getString(R.string.request_error,code));
            }
        });
    }

    private void setDate(){
        text_sentence.setText(sentenceList.get(pos).getContent());
    }

    private void initEvent() {
        btn_pre.setOnClickListener(this);
        btn_begin.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_end.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        Article temp = (Article) intent.getSerializableExtra(EXT_ARTICLE_ID);
        if (temp == null){
            ToastUtil.showText(getString(R.string.article_detail_error));
            finish();
            return;
        }
        article = new ArticleDetail(temp);
        pos = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pre:
                break;
            case R.id.btn_begin:
                break;
            case R.id.btn_play:
                break;
            case R.id.btn_end:
                break;
            case R.id.btn_next:
                break;
            default:
                break;
        }
    }
}

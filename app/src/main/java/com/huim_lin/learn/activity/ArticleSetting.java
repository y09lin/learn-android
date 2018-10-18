package com.huim_lin.learn.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleSetting extends AppCompatActivity implements View.OnClickListener {
    public static final String EXT_ARTICLE_ID = "ext_article_id";
    private TextView text_sentence,text_begin,text_end,text_progress;
    private ImageButton btn_pre,btn_begin,btn_play,btn_reset,btn_end,btn_next;
    private ArticleDetail article;
    private List<Sentence> sentenceList;
    private int pos;
    private String mp3Path;
    private MediaPlayer player;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_setting);
        text_sentence = findViewById(R.id.text_sentence);
        text_begin = findViewById(R.id.text_begin);
        text_end = findViewById(R.id.text_end);
        text_progress = findViewById(R.id.text_progress);
        btn_pre = findViewById(R.id.btn_pre);
        btn_begin = findViewById(R.id.btn_begin);
        btn_play = findViewById(R.id.btn_play);
        btn_reset = findViewById(R.id.btn_reset);
        btn_end = findViewById(R.id.btn_end);
        btn_next = findViewById(R.id.btn_next);
        initData();
        initEvent();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_setting,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RequestUtil.submitSentence(this, article.getArticleId(), sentenceList, new RequestUtil.CommonListener() {
            @Override
            public void onSuccess() {
                //
            }

            @Override
            public void onError(long code) {
            }
        });
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
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
                if (!TextUtils.isEmpty(article.getMp3())){
                    download();
                }
            }

            @Override
            public void onError(long code) {
                ToastUtil.showText(getString(R.string.request_error,code));
            }
        });
    }

    private void download(){
        RequestUtil.downloadFile(this, article.getMp3(), new RequestUtil.DownloadFileListener() {
            @Override
            public void onGetFile(String path) {
                Log.d("download",path);
                mp3Path = path;
                player = new MediaPlayer();
                try {
                    player.setDataSource(mp3Path);
                    player.prepare();
                } catch (IOException e) {
                    ToastUtil.showText(getString(R.string.file_path_error));
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Log.d("download","error");
            }
        });
    }

    private void setDate(){
        Sentence sentence = sentenceList.get(pos);
        text_sentence.setText(sentence.getContent());
        text_begin.setText(getString(R.string.sentence_begin,sentence.getBeginPoint()));
        text_end.setText(getString(R.string.sentence_end,sentence.getEndPoint()));
        text_progress.setText(getString(R.string.sentence_progress,pos+1,sentenceList.size()));
    }

    private void initEvent() {
        btn_pre.setOnClickListener(this);
        btn_begin.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
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
        isPlaying = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pre:
                if (pos==0){
                    ToastUtil.showText(getString(R.string.no_pre_more));
                    break;
                }
                pos--;
                setDate();
                break;
            case R.id.btn_begin:
                int beginPoint = player.getCurrentPosition();
                text_begin.setText(getString(R.string.sentence_begin,beginPoint));
                sentenceList.get(pos).setBeginPoint(beginPoint);
                break;
            case R.id.btn_play:
                if (isPlaying) {
                    player.pause();
                    btn_play.setImageResource(R.drawable.start);
                }else{
                    player.start();
                    btn_play.setImageResource(R.drawable.pause);
                }
                isPlaying = !isPlaying;
                break;
            case R.id.btn_reset:
                player.pause();
                player.seekTo(0);
                isPlaying = false;
                btn_play.setImageResource(R.drawable.start);
                break;
            case R.id.btn_end:
                int endPoint = player.getCurrentPosition();
                text_end.setText(getString(R.string.sentence_end,endPoint));
                sentenceList.get(pos).setEndPoint(endPoint);
                break;
            case R.id.btn_next:
                if (pos==sentenceList.size()-1){
                    ToastUtil.showText(getString(R.string.no_next_more));
                    break;
                }
                pos++;
                setDate();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (player!=null){
            player.stop();
        }
        super.onBackPressed();
    }
}

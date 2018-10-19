package com.huim_lin.learn.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huim_lin.learn.R;
import com.huim_lin.learn.bean.ArticleDetail;
import com.huim_lin.learn.util.FileUtil;
import com.huim_lin.learn.util.TimeUtil;
import com.huim_lin.learn.util.ToastUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ArticleFrag extends Fragment implements View.OnClickListener {
    private static final String ARTICLE_DETAIL = "ARTICLE_DETAIL";
    private final String TAG = "ArticleFrag";

    private ArticleDetail article;

    private TextView text_content,text_progress,text_total;
    private ImageButton btn_play,btn_reset,btn_back,btn_forward;
    private ProgressBar progress;

    private MediaPlayer player;
    private boolean isPlaying;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progress.setProgress(msg.arg1);
                    text_progress.setText(TimeUtil.getPlayTime(msg.arg1));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ArticleFrag() {
    }

    public static ArticleFrag newInstance(ArticleDetail article) {
        ArticleFrag fragment = new ArticleFrag();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE_DETAIL, article);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = (ArticleDetail) getArguments().getSerializable(ARTICLE_DETAIL);
            player = new MediaPlayer();
            try {
                player.setDataSource(article.getMp3());
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.showText(getString(R.string.file_path_error));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_article, container, false);
        text_content = view.findViewById(R.id.text_content);
        text_progress = view.findViewById(R.id.text_progress);
        text_total = view.findViewById(R.id.text_total);
        btn_play = view.findViewById(R.id.btn_play);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_back = view.findViewById(R.id.btn_back);
        btn_forward = view.findViewById(R.id.btn_forward);
        progress = view.findViewById(R.id.progress);

        text_content.setText(Html.fromHtml(article.getContent()));
        text_progress.setText(TimeUtil.getPlayTime(0));
        text_total.setText(TimeUtil.getPlayTime(player.getDuration()));
        isPlaying = false;
        progress.setMax(player.getDuration());
        initEvent();
        return view;
    }

    private void initEvent() {
        btn_play.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                ToastUtil.showText(getString(R.string.mp3_open_error));
                return false;
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(TAG,"onPrepared");
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.arg1 = player.getCurrentPosition();
                        handler.sendMessage(msg);
                    }
                };
                timer.schedule(timerTask,0,100);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player!=null){
            player.stop();
        }
        if (timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            case R.id.btn_back:
                int bp = player.getCurrentPosition() - 5000;
                if (bp<0){
                    bp = 0;
                }
                player.seekTo(bp);
                break;
            case R.id.btn_forward:
                int fp = player.getCurrentPosition() + 5000;
                if (fp>player.getDuration()){
                    fp = player.getDuration();
                }
                player.seekTo(fp);
                break;
        }
    }
}

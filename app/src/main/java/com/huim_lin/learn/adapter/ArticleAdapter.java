package com.huim_lin.learn.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.huim_lin.learn.R;
import com.huim_lin.learn.bean.Article;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;


public class ArticleAdapter extends SuperAdapter<Article> {

    public ArticleAdapter(Context ctx, List<Article> datas, @LayoutRes int layoutId){
        super(ctx,datas,layoutId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Article item) {
        holder.setText(R.id.title,item.getTitle());
        holder.setText(R.id.time,item.getDate());
        holder.setText(R.id.author,item.getAuthor());
        holder.setText(R.id.type,item.getType());
    }
}

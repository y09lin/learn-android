package com.huim_lin.learn.bean;

import java.util.List;

public class ArticleDetail extends Article {
    public ArticleDetail(){}

    public ArticleDetail(Article article){
        setArticleId(article.getArticleId());
        setType(article.getType());
        setTitle(article.getTitle());
        setAuthor(article.getAuthor());
        setDate(article.getDate());
        setSource(article.getSource());
    }

    private String mp3;

    private List<Paragraph> paragraphList;

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public List<Paragraph> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<Paragraph> paragraphList) {
        this.paragraphList = paragraphList;
    }
}

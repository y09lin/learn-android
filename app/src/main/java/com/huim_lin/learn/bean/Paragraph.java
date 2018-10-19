package com.huim_lin.learn.bean;

import java.util.List;

public class Paragraph {
    private long articleId;

    private long paragraphId;

    private List<Sentence> sentenceList;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(long paragraphId) {
        this.paragraphId = paragraphId;
    }

    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    public void setSentenceList(List<Sentence> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public String getContent(){
        if (sentenceList==null || sentenceList.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        for (Sentence s:sentenceList){
            sb.append(s.getContent());
        }
        sb.append("</p>");
        return sb.toString();
    }
}

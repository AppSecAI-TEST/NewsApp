package com.example.h78617.newsapp;

final public class OnlineNews {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mUrl;


    public OnlineNews(String webTitle, String sectionName, String author, String webPublicationDate, String url) {
        mTitle = webTitle;
        mSection = sectionName;
        mAuthor = author;
        mDate = webPublicationDate;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
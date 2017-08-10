package com.example.h78617.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class OnlineNewsLoader extends AsyncTaskLoader<List<OnlineNews>> {

    private String mUrl;

    public OnlineNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<OnlineNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<OnlineNews> onlineNews = QueryUtils.fetchNewsData(mUrl);
        return onlineNews;
    }
}

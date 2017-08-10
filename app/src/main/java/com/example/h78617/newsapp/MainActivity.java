package com.example.h78617.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<OnlineNews>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String ONLINENEWS_REQUEST_URL = "https://content.guardianapis.com/search?&api-key=2a30369a-1355-4437-b500-710e741ef253";
    private static final int NEWS_LOADER_ID = 1;

    private static final String API_KEY = "api-key";

    private static final String KEY = "2a30369a-1355-4437-b500-710e741ef253";

    private OnlineNewsAdapter mAdapter;

    private ListView newsListView;

    private SearchView searchView;

    private String mQuery;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.search_view);

        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.list);

        mAdapter = new OnlineNewsAdapter(this, new ArrayList<OnlineNews>());

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                OnlineNews currentOnlineNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentOnlineNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnected()) {
                    newsListView.setVisibility(View.INVISIBLE);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mQuery = searchView.getQuery().toString();
                    mQuery = mQuery.replace(" ", "+");
                    Log.v(LOG_TAG, mQuery);
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    searchView.clearFocus();
                } else {
                    newsListView.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<OnlineNews>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String searchNews = sharedPrefs.getString(
                getString(R.string.settings_key),
                getString(R.string.settings_news_label));

        Uri baseUri = Uri.parse(ONLINENEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchNews);
        uriBuilder.appendQueryParameter(API_KEY, KEY);

        return new OnlineNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<OnlineNews>> loader, List<OnlineNews> news) {

        mEmptyStateTextView.setText(R.string.no_news);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<OnlineNews>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

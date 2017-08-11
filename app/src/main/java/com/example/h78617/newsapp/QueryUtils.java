package com.example.h78617.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static String response = "response";
    private static String section = "sectionName";
    private static String date = "webPublicationDate";
    private static String title = "webTitle";
    private static String author = "author";
    private static JSONObject authorsArray;
    private static String url = "webUrl";

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static ArrayList<OnlineNews> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }
        ArrayList<OnlineNews> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<OnlineNews> extractFeatureFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<OnlineNews> news = new ArrayList<>();

        try {

            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray newsArray = null;
            if (jsonObj.has("response")) {
                jsonObj = jsonObj.getJSONObject("response");
                if (jsonObj.has("results")) {
                    newsArray = jsonObj.getJSONArray("results");
                }
            }


            for (int i = 0; i < newsArray.length(); i++) {


                JSONObject currentOnlineNews = newsArray.getJSONObject(i);


                String title = currentOnlineNews.getString("webTitle");
                if (currentOnlineNews.has("webTitle")) {
                    title = currentOnlineNews.getString("webTitle");
                }

                String section = currentOnlineNews.getString("sectionName");
                if (currentOnlineNews.has("sectionName")) {
                    section = currentOnlineNews.getString("sectionName");
                }

                String author = "";

                JSONArray authorsArray;

                if (currentOnlineNews.has("authors")) {
                    authorsArray = currentOnlineNews.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        author += (authorsArray.getString(j));
                    }
                } else {
                    author = "N/A";
                }

                String date = currentOnlineNews.getString("webPublicationDate");
                if (currentOnlineNews.has("webPublicationDate")) {
                    date = currentOnlineNews.getString("webPublicationDate");
                }

                String url = currentOnlineNews.getString("webUrl");
                if (currentOnlineNews.has("webUrl")) {
                    url = currentOnlineNews.getString("webUrl");
                }

                OnlineNews onlineNews = new OnlineNews(title, section, author, date, url);


                news.add(onlineNews);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return news;
    }
}
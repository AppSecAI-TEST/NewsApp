package com.example.h78617.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OnlineNewsAdapter extends ArrayAdapter<OnlineNews> {

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView authorTextView;
        private TextView dateTextView;
    }


    public OnlineNewsAdapter(Context context, ArrayList<OnlineNews> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        final OnlineNews currentOnlineNews = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
            holder.sectionTextView = (TextView) listItemView.findViewById(R.id.news_section);
            holder.authorTextView = (TextView) listItemView.findViewById(R.id.news_author);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        holder.titleTextView.setText(currentOnlineNews.getTitle());
        holder.sectionTextView.setText(currentOnlineNews.getSection());
        holder.authorTextView.setText(currentOnlineNews.getAuthor());
        holder.dateTextView.setText(currentOnlineNews.getDate());

        return listItemView;
    }
}
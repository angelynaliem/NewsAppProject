package com.example.android.newsappproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class NewsappAdapter extends ArrayAdapter<Newsapp> {


    public NewsappAdapter(Context context, List<Newsapp> newsapp) {
        super(context, 0, newsapp);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.newsapp_list_item, parent, false);
        }

        Newsapp currentNews = getItem(position);

        TextView sectionView = listItemView.findViewById(R.id.section_textview);
        sectionView.setText(currentNews.getSection());

        TextView titleView = listItemView.findViewById(R.id.title_textview);
        titleView.setText(currentNews.getTitle());

        TextView dateView = listItemView.findViewById(R.id.date_textview);
        dateView.setText(currentNews.getDate());

        TextView authorView = listItemView.findViewById(R.id.author_textview);
        authorView.setText(currentNews.getAuthor());

        return listItemView;
    }
}
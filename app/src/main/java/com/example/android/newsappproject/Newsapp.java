package com.example.android.newsappproject;

public class Newsapp {

    private String mSection;

    private String mTitle;

    private String mDate;

    private String mAuthor;

    private String mUrl;

    public Newsapp(String section, String title, String date,  String author, String url) {
        mSection = section;
        mTitle = title;
        mDate = date;
        mAuthor = author;
        mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

     public String getAuthor() { return mAuthor; }

    public String getUrl() {
        return mUrl;
    }

}

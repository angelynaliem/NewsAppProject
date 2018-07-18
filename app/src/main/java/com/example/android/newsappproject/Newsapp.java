package com.example.android.newsappproject;

public class Newsapp {

    private String mSection;

    private String mTitle;

    private String mDate;

    private String mUrl;

    public Newsapp(String section, String title, String date, String url) {
        mSection = section;
        mTitle = title;
        mDate = date;
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

    public String getUrl() {
        return mUrl;
    }
}

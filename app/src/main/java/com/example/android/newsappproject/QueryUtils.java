package com.example.android.newsappproject;

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
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Newsapp> fetchNewsappData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Newsapp> newsapp = extractResponseFromJson(jsonResponse);

        return newsapp;
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
      Log.e(LOG_TAG, "Problem retrieving the newsapp JSON results.", e);
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

    private static List<Newsapp> extractResponseFromJson(String newsappJSON) {
        if (TextUtils.isEmpty(newsappJSON)) {

            return null;
 }

 List<Newsapp> newsapp = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsappJSON);

            JSONArray newsappArray = baseJsonResponse.getJSONArray("response");

            for (int i = 0; i < newsappArray.length(); i++) {

                JSONObject currentNews = newsappArray.getJSONObject(i);

                JSONObject results = currentNews.getJSONObject("results");

                String section = results.getString("sectionId");

                String title = results.getString("webTitle");

                String date = results.getString("webPublicationDate");

                String url = results.getString("webUrl");

                Newsapp newsapps = new Newsapp(section, title, date, url);

                newsapp.add(newsapps);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the newsapp JSON results", e);
        }

        return newsapp;
    }

}
package com.example.android.newsappproject;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsappActivity extends AppCompatActivity
    implements LoaderCallbacks<List<Newsapp>> {

    public static final String LOG_TAG = NewsappActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?";

    private static final int NEWSAPP_LOADER_ID = 1;

    private NewsappAdapter mAdapter;

    private TextView mEmptyStateTextView;

    ListView newsappListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_activity);

        newsappListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsappListView.setEmptyView(mEmptyStateTextView);

    mAdapter = new NewsappAdapter(this, new ArrayList<Newsapp>());

        newsappListView.setAdapter(mAdapter);

        newsappListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick (AdapterView < ? > adapterView, View view,int position, long l){

        Newsapp currentNews = mAdapter.getItem(position);

        Uri newsappUri = Uri.parse(currentNews.getUrl());

        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsappUri);

        startActivity(websiteIntent);
    }
    });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWSAPP_LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Newsapp>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String numberOfArticles = sharedPreferences.getString(getString(R.string.settings_number_of_articles_key), getString(R.string.settings_number_of_articles_default));

        String orderBy  = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");

        uriBuilder.appendQueryParameter("show-tags", "contributor");

        uriBuilder.appendQueryParameter("page-size", numberOfArticles);

        uriBuilder.appendQueryParameter("order-by", orderBy);

        uriBuilder.appendQueryParameter("api-key", "78bba25c-bc42-400a-8c19-aba969fc0c2b");

        Toast.makeText(this, ""+uriBuilder.toString(), Toast.LENGTH_LONG).show();

        return new NewsappLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Newsapp>> loader, List<Newsapp> newsapp) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_articles);

        if (newsapp != null && !newsapp.isEmpty()) {

            updateUi(newsapp);

        }
    }

    private void updateUi(List<Newsapp> newsapp) {
        mEmptyStateTextView.setVisibility(View.GONE);

        mAdapter.clear();
        mAdapter = new NewsappAdapter(this, newsapp);
        newsappListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Newsapp>> loader) {
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
package com.imams.newsdesk.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.imams.newsdesk.R;
import com.imams.newsdesk.adapter.ArticleAdapter;
import com.imams.newsdesk.configuration.Constants;
import com.imams.newsdesk.model.Article;
import com.imams.newsdesk.model.Source;
import com.imams.newsdesk.model.response.ArticleResponse;
import com.imams.newsdesk.service.ApiClient;
import com.imams.newsdesk.service.RetrofitApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourcesNewsActivity extends AppCompatActivity {

    private static final String API_KEY = Constants.API_KEY;
    public static final String KEY = "source";

    private Source source;
    private ArrayList<Article> articleArrayList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources_news);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            getWindow().setExitTransition(slide);
        }

        source = getIntent().getParcelableExtra(KEY);

        createToolbar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(getApplicationContext(), articleArrayList);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

        getHeadlineNews(source.getId());

        swipeRefreshLayout.setOnRefreshListener(() -> getHeadlineNews(source.getId()));

    }

    private void getHeadlineNews(String id) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitApi retrofitApi = ApiClient.getCacheEnabledRetrofit(getApplicationContext()).create(RetrofitApi.class);
        Call<ArticleResponse> call = retrofitApi.getTopHeadlines(id, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                ArticleResponse articleResponse = response.body();
                articleArrayList.clear();
                if (articleResponse != null) {
                    articleArrayList.addAll(articleResponse.getArticles());
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Toast.makeText(SourcesNewsActivity.this, "Error!",
                        Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void toSearchActivity() {
        Intent intent = new Intent(SourcesNewsActivity.this, SearchActivity.class);
        intent.putExtra(SearchActivity.KEY, source);
        startActivity(intent);
    }

    private void createToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        tvToolbarTitle.setText(source.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                toSearchActivity();
        }
        return super.onOptionsItemSelected(item);
    }

}

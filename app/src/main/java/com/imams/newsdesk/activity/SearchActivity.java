package com.imams.newsdesk.activity;

import android.os.Build;
import android.os.Handler;
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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.imams.newsdesk.R;
import com.imams.newsdesk.adapter.ArticleAdapter;
import com.imams.newsdesk.adapter.CustomRecyclerViewListener;
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

public class SearchActivity extends AppCompatActivity {

    public static final String KEY = "source";
    private static final String API_KEY = Constants.API_KEY;

    private Source source;
    private ArrayList<Article> articleArrayList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editText_search)
    EditText etSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArticleAdapter adapter;

    int pageIndex, totalPages;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            getWindow().setExitTransition(slide);
        }
        createToolbar();

        source = getIntent().getParcelableExtra(KEY);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(getApplicationContext(), articleArrayList);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                query = etSearch.getText().toString().trim();
                getHeadlineNews(query);
                pageIndex = 2;
            }
            return false;
        });

        recyclerView.addOnScrollListener(new CustomRecyclerViewListener() {
            @Override
            public void onLoadMore() {
                loadMoreResults(query, pageIndex);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!query.isEmpty() || !query.equals("")) {
                    getHeadlineNews(query);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getHeadlineNews(String query) {
        recyclerView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        RetrofitApi retrofitApi = ApiClient.getCacheEnabledRetrofit(getApplicationContext()).create(RetrofitApi.class);
        Call<ArticleResponse> call = retrofitApi.getArticles(source.getId(), query, 1, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                ArticleResponse articleResponse = response.body();
                articleArrayList.clear();
                if (articleResponse != null) {
                    articleArrayList.addAll(articleResponse.getArticles());
                    adapter.notifyDataSetChanged();

                    // getting total results and number of pages posibility
                    int totalResults = articleResponse.getTotalResults();
                    int pages = totalResults/30;
                    totalPages = pages;
                    if ((totalResults % 30) > 0) {
                        totalPages = totalPages + 1;
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(SearchActivity.this, "null!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error!", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMoreResults(String query, int page) {
        if (pageIndex <= totalPages) {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                RetrofitApi retrofitApi = ApiClient.getCacheEnabledRetrofit(getApplicationContext()).create(RetrofitApi.class);
                Call<ArticleResponse> call = retrofitApi.getArticles(source.getId(), query, page, API_KEY);
                call.enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        ArticleResponse articleResponse = response.body();
                        if (articleResponse != null) {
                            articleArrayList.addAll(articleResponse.getArticles());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SearchActivity.this, "null!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable t) {
                        Toast.makeText(SearchActivity.this, "Error!", Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }, 1500);
        }
        pageIndex++;
    }

    private void createToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            SearchActivity.this.overridePendingTransition(R.anim.pull_in_left, R.anim.pull_in_left);
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                etSearch.setText("");
                etSearch.requestFocus();
                pageIndex = 2;
                InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                methodManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
                recyclerView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

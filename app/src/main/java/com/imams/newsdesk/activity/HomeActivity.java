package com.imams.newsdesk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imams.newsdesk.R;
import com.imams.newsdesk.adapter.NewsSourceAdapter;
import com.imams.newsdesk.configuration.Constants;
import com.imams.newsdesk.model.Source;
import com.imams.newsdesk.model.response.SourceResponse;
import com.imams.newsdesk.service.ApiClient;
import com.imams.newsdesk.service.RetrofitApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String API_KEY = Constants.API_KEY;
    private ArrayList<Source> sourceArrayList = new ArrayList<>();

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private GridLayoutManager layoutManager;
    private NewsSourceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsSourceAdapter(getApplicationContext(), sourceArrayList);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

        getNewsSource();
    }

    private void getNewsSource() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitApi retrofitApi = ApiClient.getCacheEnabledRetrofit(getApplicationContext()).create(RetrofitApi.class);
        Call<SourceResponse> call = retrofitApi.getAllNewsSources(API_KEY);
        call.enqueue(new Callback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                progressBar.setVisibility(View.GONE);
                SourceResponse sourceResponse = response.body();
                if (sourceResponse != null) {
                    sourceArrayList.addAll(sourceResponse.getSources());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SourceResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error getting News Sources", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}

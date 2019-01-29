package com.imams.newsdesk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.imams.newsdesk.R;
import com.imams.newsdesk.model.Article;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    public static final String KEY = "article";

    @BindView(R.id.web_view_article)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Article article;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        article = getIntent().getParcelableExtra(KEY);
        url = article.getUrl();

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}

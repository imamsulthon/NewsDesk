package com.imams.newsdesk.service;

import android.content.Context;
import android.webkit.WebChromeClient;

public class MyWebChromeClient extends WebChromeClient {

    private Context context;

    public MyWebChromeClient(Context context) {
        super();
        this.context = context;
    }
}

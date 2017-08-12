package com.veeritsolution.android.anivethub.utility;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by ${hitesh} on 1/28/2017.
 */

public class MyWebViewClient extends WebViewClient {

    WebView webView;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        this.webView = view;
        view.loadUrl(url);
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.loadUrl(request.getUrl().toString());
        }
        return true;
    }
}
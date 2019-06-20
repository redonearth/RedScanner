package com.redonearth.redscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

//        WebSettings webSettings = webView.getSettings();
        /*
         * private WebSettings webSettings는 에러 발생.
         * webView.getSettings()로 해야 정상 작동함.
         */
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // 화면 비율
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        // 줌 기능
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getDataString());
        webView.loadUrl(String.valueOf(uri));
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getDataString());

        // 뒤로 가기 누를 때 동작
        if (webView.getOriginalUrl().equalsIgnoreCase(String.valueOf(uri))) {
            super.onBackPressed();
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

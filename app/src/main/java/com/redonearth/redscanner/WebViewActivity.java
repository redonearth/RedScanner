package com.redonearth.redscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class WebViewActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private EditText address;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        address = findViewById(R.id.address_line);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                address.setText(url);
                super.onPageFinished(view, url);
            }
        });
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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back_btn:
                webView.goBack();
                break;

            case R.id.forward_btn:
                webView.goForward();
                break;

            case R.id.refresh_btn:
                String add = address.getText().toString();
                if(!add.contains("http://")) {
                    add = "http://" + add;
                }
                webView.loadUrl(add);
                break;
        }
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

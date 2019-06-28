package com.redonearth.redscanner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class WebViewActivity extends Activity {

    private WebView webView;
    private boolean isLoading = false;

    private ImageButton mBackButton;
    private ImageButton mForwardButton;
    private ImageButton mRefreshButton;
    private ImageButton mEnterButton;
    private EditText mUrlBar;
    private ProgressBar mProgressBar;

    private TextView mCloseButton;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        mForwardButton = findViewById(R.id.forward_btn);
        mBackButton = findViewById(R.id.back_btn);
        mRefreshButton = findViewById(R.id.refresh_btn);
        mEnterButton = findViewById(R.id.enter_btn);
        mUrlBar = findViewById(R.id.url_bar);
        mProgressBar = findViewById(R.id.progress_bar);
        mCloseButton = findViewById(R.id.close_btn);

        mForwardButton.setOnClickListener(onClick);
        mBackButton.setOnClickListener(onClick);
        mRefreshButton.setOnClickListener(onClick);
        mEnterButton.setOnClickListener(onClick);

        mCloseButton.setOnClickListener(onClick);

        mUrlBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mEnterButton.performClick();
                }
                return false;
            }
        });

        webView.setWebViewClient(new webClient());

        /*
         * Progress Bar
         */
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });

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

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.enter_btn:
                    String url;
                    url = mUrlBar.getText().toString();
                    webView.loadUrl(url);
                    break;
                case R.id.refresh_btn:
                    if (isLoading) {
                        webView.stopLoading();
                    } else {
                        webView.reload();
                    }
                    break;
                case R.id.back_btn:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    break;
                case R.id.forward_btn:
                    if (webView.canGoForward()) {
                        webView.goForward();
                    }
                    break;
                case R.id.close_btn:
                    startActivity(new Intent(WebViewActivity.this, ScannerContainerActivity.class));
            }
        }
    };

    class webClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mUrlBar.setText(url);
            mRefreshButton.setBackground(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_block_white_28dp));
            mRefreshButton.setPadding(0, 0, 0, 0);
            mProgressBar.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mRefreshButton.setBackground(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_refresh_white_28dp));
            mRefreshButton.setPadding(0, 0, 0, 0);
            mProgressBar.setVisibility(View.GONE);
            isLoading = false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /*
         * URL 타입별 Url Loading 방식 설정
         */
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
            } else if (url.startsWith("sms:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(intent);
            } else if (url.startsWith("mailto:")) {
                MailTo mailTo = MailTo.parse(url);
                Intent intent = newEmailIntent(WebViewActivity.this, mailTo.getTo(), mailTo.getSubject(), mailTo.getBody(), mailTo.getCc());
                startActivity(intent);
            } else if (url.endsWith(".mp3")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "audio/*");
                view.getContext().startActivity(intent);
            } else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                view.getContext().startActivity(intent);
            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
            }
            return true;
        }

        private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_CC, cc);
            intent.setType("message/rfc822");
            return intent;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getDataString());

        // 뒤로 가기 누를 때 동작
        if (webView.getOriginalUrl().equalsIgnoreCase(String.valueOf(uri))) {
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.app_name)
//                    .setMessage("브라우저를 종료하시겠습니까?")
//                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            startActivity(new Intent(WebViewActivity.this, ScannerContainerActivity.class));
//                        }
//                    })
//                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .show();
            startActivity(new Intent(this, ScannerContainerActivity.class));
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.app_name)
//                    .setMessage("브라우저를 종료하시겠습니까?")
//                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            startActivity(new Intent(WebViewActivity.this, ScannerContainerActivity.class));
//                        }
//                    })
//                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .show();
            startActivity(new Intent(this, ScannerContainerActivity.class));
        }
    }
}

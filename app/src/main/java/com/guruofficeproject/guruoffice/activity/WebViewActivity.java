package com.guruofficeproject.guruoffice.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.guruofficeproject.guruoffice.R;
import com.guruofficeproject.guruoffice.dialog.DialogManager;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    Button btnClose;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        webView = findViewById(R.id.webView);
        btnClose = findViewById(R.id.btn_web_close);
        DialogManager.getInstance().progressLoading(true,WebViewActivity.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        Uri data = getIntent().getData();
        webView.loadUrl(data.toString());
        DialogManager.getInstance().progressLoading(false,WebViewActivity.this);
    }

    public void btnCloseWebView(View v) {
        finish();
    }
}

package com.cov.covinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class datavizu extends AppCompatActivity {
    WebView datavizua;
    ProgressBar datavizpb;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datavizu);
        datavizua = (WebView) findViewById(R.id.datavizua);
        datavizpb = (ProgressBar) findViewById(R.id.datavizpb);
        textView = (TextView) findViewById(R.id.imtsm);
        WebSettings webSettings = datavizua.getSettings();
        webSettings.setJavaScriptEnabled(true);
        datavizua.loadUrl("file:///android_asset/graph.html");
        datavizua.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    //do your task
                    datavizpb.setVisibility(View.GONE);
                    datavizua.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }
}
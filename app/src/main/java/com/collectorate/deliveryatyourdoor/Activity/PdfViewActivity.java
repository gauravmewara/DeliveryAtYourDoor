package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

public class PdfViewActivity extends AppCompatActivity {
    RelativeLayout menuback;
    TextView pagetitle;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        menuback = (RelativeLayout)findViewById(R.id.rl_toolbar2_menu);
        pagetitle = (TextView)findViewById(R.id.tv_toolbar2_heading);
        webView = (WebView)findViewById(R.id.webview_pdf);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new Callback());
        webView.loadUrl(URLs.DEMO_PDF_URL);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

}

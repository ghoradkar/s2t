package com.myhindlab.abkat.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myhindlab.abkat.R;

public class Document extends AppCompatActivity {

    private WebView webview;
    private ProgressBar spi;
    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        webview = (WebView) findViewById(R.id.webview);
        spi = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.textView1);

        String stringVariableName = getIntent().getExtras().getString("URL");

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                spi.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                view.loadUrl(uri);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (spi.isShown()) {
                    spi.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
            }
        });

//        webview.loadUrl("https://www.google.co.in/?gfe_rd=cr&ei=YsFgWd7nCfPI8Aet5bjADA&gws_rd=ssl");
//        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=http://erp.s2infotech.com/S2INTERNATIONALDOCS/eLearning/List of Public Holidays_2018 Innowave.pdf"/* + stringVariableName*/);
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + stringVariableName);
    }
}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_document);
//
//        doc_webview = (WebView) findViewById(R.id.doc_webview);
//        Bundle extras = getIntent().getExtras();
//        String stringVariableName = extras.getString("URL");
//        myUrl = stringVariableName;
//
//        String myPdfUrl = stringVariableName.toString();
//        String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
//        startWebView(url);
//    }
//
//    private void startWebView(String url) {
//        doc_webview.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            public void onLoadResource(WebView view, String url) {
//                progressDialog = new ProgressDialog(Document.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//            }
//
//            public void onPageFinished(WebView view, String url) {
//                progressDialog.dismiss();
//            }
//        });
//
//        doc_webview.getSettings().setJavaScriptEnabled(true);
//        doc_webview.loadUrl(url);
//    }
//}

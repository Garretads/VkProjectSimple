package com.example.garred.vkprojectsimple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;




public class LoginActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;
    private VkWebViewClient vkWebViewClient;
    private String CLIENT_ID = "5931887";
    private String SCOPE = "friends,status,offline";
    private String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    private String RESPONSE_TYPE = "token";
    private String VERSION = "5,68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webView = (WebView) findViewById(R.id.web);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.clearCache(true);
        //WebViewClient webViewClient = new WebViewClientImpl(this);
        vkWebViewClient = new VkWebViewClient();
        webView.setWebViewClient(vkWebViewClient);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("oauth.vk.com")
                .appendPath("authorize")
                .appendQueryParameter("client_id",CLIENT_ID)
                .appendQueryParameter("redirect_uri",REDIRECT_URI)
                .appendQueryParameter("display",Settings.vk_api_display())
                .appendQueryParameter("response_type",RESPONSE_TYPE)
                .appendQueryParameter("scope",SCOPE)
                .appendQueryParameter("v",VERSION);
        webView.loadUrl(builder.toString());
        webView.setVisibility(View.VISIBLE);

    }

    class VkWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            parseUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if( url.startsWith("https://oauth.vk.com/authorize") || url.startsWith("https://oauth.vk.com/oauth/authorize") ) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

    private void parseUrl(String url) {
        try {
            if( url == null ) {
                return;
            }
            if( url.startsWith(Settings.vk_api_redirect_uri()) ) {
                if( !url.contains("error") ) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    webView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    //Строим данные
                    Intent intent = new Intent();
                    intent.putExtra("token", auth[0]);
                    intent.putExtra("uid", auth[1]);

                    //Возвращаем данные
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            } else if( url.contains("error?err") ) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}

package com.example.garred.vkprojectsimple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by garred on 18.03.17.
 */

public class WebViewClientImpl extends WebViewClient {
    private Activity activity = null;
    Intent mIntent;
    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Uri uri = Uri.parse(url);
        String uriHost = uri.getHost();
        String uriLastSegment = uri.getLastPathSegment();
        if(uriHost.equals("oauth.vk.com") && (uriLastSegment.equals("blank.html")))
            if(uri.getQueryParameter("access_token") != null) {
                String accessToken = uri.getQueryParameter("access_token");
                String userId = uri.getQueryParameter("user_id");
                SharedPreferences sharedPref = activity.getSharedPreferences("vk_settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("access_token",accessToken);
                //editor.putString()
            }
            else {
                activity.finish();
                mIntent = new Intent(activity,MainActivity.class);
            }
    }
}

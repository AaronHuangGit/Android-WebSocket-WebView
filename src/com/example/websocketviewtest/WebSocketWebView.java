package com.example.websocketviewtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.websocket.drafts.Draft_17;

/**
 * Created by AaronHuang on 16/6/21.
 *
 * @version 3.0.1
 * @auchor ran.huang
 */
public class WebSocketWebView extends WebView {

    public WebSocketWebView(Context context) {
        super(context);
        init();
    }

    public WebSocketWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebSocketWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public WebSocketWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            }
        });
        setWebViewClient(
                new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return false;
                    }
                });
        addJavascriptInterface(new WebSocketJavaScriptObject(this, new Draft_17()), "WebSocketJavaScriptObject");
    }



}

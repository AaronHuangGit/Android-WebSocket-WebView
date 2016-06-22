package com.example.websocketviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @SuppressLint({"SetJavaScriptEnabled", "addJavascriptInterface"})
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        WebSocketWebView wv = (WebSocketWebView)findViewById(R.id.webview);
		wv.loadUrl("file:///android_asset/www/index.html");
    }

}

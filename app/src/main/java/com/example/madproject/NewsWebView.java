package com.example.madproject;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsWebView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_web_view);
		WebView webView = (WebView) findViewById(R.id.webView);
		String url = getIntent().getExtras().getString(MainActivity.NEWS_URL);
		if(url != null) {
			webView.loadUrl(url);
		}
	}
}

package com.example.madproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LatestNews extends Activity {

	private static final String TAG = LatestNews.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_latest_news);
		Log.d(TAG,"LatestNews activity started");
	}

	
}

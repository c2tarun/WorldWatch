package com.example.madproject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

public class FetchNews extends AsyncTask<String, Void, List<News>> {
	
	protected FetchNewsInterface receiver;
	protected String NEWS_API = "http://ec2-54-191-68-232.us-west-2.compute.amazonaws.com:8080/WorldWatch-1.0/rest/ww/";
	protected String userId;
	protected Context context;
	
	public FetchNews(Context context, FetchNewsInterface receiver, String userId) {
		this.context = context;
		this.receiver = receiver;
		this.userId = userId;
	}
	
	@Override
	protected void onPreExecute() {
		receiver.startFetchNews();
	}

	@Override
	protected List<News> doInBackground(String... params) {
		try {
		List<News> newsList = new ArrayList<News>();
		for(int i = 0;i<params.length;i++) {
			String in = HttpUtil.getStringFromURL(NEWS_API + userId + "/search/"+URLEncoder.encode(params[i]));
			List<News> news = NewsUtil.NewsJSONParser.parseNews(in);
			updatePreferencesValue(params[i], news);
			newsList.addAll(news);
		}
			return newsList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	protected void updatePreferencesValue(String searchText, List<News> newsList) {
		long maxTS = 0;
		for(News news : newsList) {
			if(maxTS < news.getDate().getTime()) {
				maxTS = news.getDate().getTime();
			}
		}
		PreferenceUtils utils = new PreferenceUtils(context);
		utils.save(searchText, maxTS);
	}

	@Override
	protected void onPostExecute(List<News> result) {
		receiver.receiveNews(result);
		receiver.stopFetchNews();
	}

	public interface FetchNewsInterface {
		public void startFetchNews();
		public void receiveNews(List<News> newsList);
		public void stopFetchNews();
	}
}
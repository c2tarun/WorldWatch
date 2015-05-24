package com.example.madproject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;

public class FetchNewNews extends FetchNews {

	public FetchNewNews(Context context, FetchNewsInterface receiver, String userId) {
		super(context, receiver, userId);
		this.context = context;
		this.receiver = receiver;
		this.userId = userId;
	}

	@Override
	protected List<News> doInBackground(String... params) {
		// The params I am getting here are only keywords with new news.
		List<News> newsList = new ArrayList<News>();
		PreferenceUtils utils = new PreferenceUtils(context);
		try {
			for (int i = 0; i < params.length; i++) {
				long oldTs = utils.getTimeStamp(params[i]);
				String in = HttpUtil.getStringFromURL(NEWS_API + userId + "/search/"+URLEncoder.encode(params[i]));
				List<News> newNewsList = NewsUtil.NewsJSONParser.parseNews(in);
				updatePreferencesValue(params[i], newNewsList);
				for(News news : newNewsList) {
					if(news.getDate().getTime() > oldTs) {
						newsList.add(news);
					}
				}
			}
			return newsList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

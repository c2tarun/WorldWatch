package com.example.madproject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NewsUtil {
	
	static public class NewsJSONParser {
		static List<News> parseNews(String in) throws JSONException {
			ArrayList<News> newsList = new ArrayList<News>();
			
			JSONObject root = new JSONObject(in);
			JSONArray newsJSONArray = root.getJSONArray("results");
			for(int i=0; i<newsJSONArray.length();i++) {
				JSONObject newsJSONObject = newsJSONArray.getJSONObject(i);
				News news = News.createNews(newsJSONObject);
				newsList.add(news);
			}
			return newsList;
		}
	}
	
}

package com.example.madproject;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FavNewsActivity extends BaseActivity {
	ParseUser user;
	String objectId;
	List<News> newsList;
	ArrayAdapter<News> adapter;
	ListView listNews;
	String title;
	String imgUrl;
	String appUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		user = ParseUser.getCurrentUser();
		objectId = user.getObjectId();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav_news);
		listNews = (ListView) findViewById(R.id.favList);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FAV_NEWS");
		query.whereEqualTo("userId", objectId);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					newsList = new ArrayList<News>();

					for (ParseObject obj : objects) {
						News objt = new News(obj.getString("newsTitle"), obj.getString("newsUrl"), obj
								.getString("imageUrl"));
						newsList.add(objt);
					}

					adapter = new NewsAdapter(FavNewsActivity.this, R.layout.list_view_layout, newsList);
					listNews.setAdapter(adapter);

				}
			}
		});

		listNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				News favNews;
				favNews = newsList.get(position);
				title = favNews.getTitle();
				ParseQuery<ParseObject> delQuery = new ParseQuery<ParseObject>("FAV_NEWS");
				delQuery.whereEqualTo("newsTitle", title).whereEqualTo("userId", objectId);
				delQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						ParseObject obj = objects.get(0);
						obj.deleteInBackground(new DeleteCallback() {

							@Override
							public void done(ParseException e) {
								Log.d("demo", "success");
								newsList.remove(position);
								adapter = new NewsAdapter(FavNewsActivity.this, R.layout.list_view_layout, newsList);
								listNews.setAdapter(adapter);
							}
						});
					}
				});

				return true;
			}
		});

		listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FavNewsActivity.this, NewsWebView.class);
				intent.putExtra(MainActivity.NEWS_URL, newsList.get(position).getUrl());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if(id == R.id.favourites) {
			Toast.makeText(getApplicationContext(), "You are on Favourites page", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

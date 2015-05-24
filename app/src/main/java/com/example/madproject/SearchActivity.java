package com.example.madproject;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SearchActivity extends BaseActivity implements FetchNews.FetchNewsInterface {

	private static final String SEARCH_HISTORY_TB = "SEARCH_HISTORY";
	private static final String USER_ID = "USER_ID";
	private static final String SEARCH_TEXT = "SEARCH_TEXT";
	private static final String NEWS_JSON_TB = "NEWS_JSON";
	protected static final String MAX_TS = "MAX_TS";
	protected static final String JSON = "JSON";

	String object;
	String objectId;
	EditText searchText;
	String searchString;
	ProgressDialog pd;
	List<News> newsList;
	ListView listingNews;
	ArrayAdapter<News> adapter;
	ParseUser user;
	String NEWS_API;
	String title;
	String imgUrl;
	String appUrl;
	ImageView favButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		user = ParseUser.getCurrentUser();
		objectId = user.getObjectId();
		listingNews = (ListView) findViewById(R.id.newsList);
		listingNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				favButton = (ImageView) view.findViewById(R.id.favImageButton);
				favButton.setImageResource(R.drawable.favorites_yellow);
				News favNews;
				favNews = newsList.get(position);
				title = favNews.getTitle();
				imgUrl = favNews.getIurl();
				appUrl = favNews.getUrl();

				ParseQuery<ParseObject> query = ParseQuery.getQuery("FAV_NEWS");
				query.whereEqualTo("newsUrl", appUrl).whereEqualTo("userId", objectId);
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						if (object == null) {
							ParseObject FavNews = new ParseObject("FAV_NEWS");
							FavNews.put("userId", objectId);
							FavNews.put("newsTitle", title);
							FavNews.put("newsUrl", imgUrl);
							FavNews.put("imageUrl", appUrl);
							FavNews.saveInBackground();
						} else {
							ParseQuery<ParseObject> delQuery = new ParseQuery<ParseObject>("FAV_NEWS");
							delQuery.whereEqualTo("newsTitle", title).whereEqualTo("userId", objectId);
							delQuery.findInBackground(new FindCallback<ParseObject>() {

								@Override
								public void done(List<ParseObject> objects, ParseException e) {
									ParseObject obj = objects.get(0);
									obj.deleteInBackground(new DeleteCallback() {

										@Override
										public void done(ParseException e) {
											adapter = new NewsAdapter(SearchActivity.this, R.layout.list_view_layout,
													newsList);
											listingNews.setAdapter(adapter);
										}
									});
								}
							});
						}
					}
				});

				return true;
			}
		});

		listingNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this, NewsWebView.class);
				intent.putExtra(MainActivity.NEWS_URL, newsList.get(position).getUrl());
				startActivity(intent);
			}
		});
		String searchText = getIntent().getExtras().getString(MainActivity.SEARCH_TEXT_FROM_USER);
		if (searchText != null)
			new FetchNews(getApplicationContext(), this, user.getObjectId()).execute(searchText);
		else {
			List<String> newNewsKeywords = getIntent().getExtras().getStringArrayList(ParseReceiver.NEW_NEWS);
			boolean isManualPush = getIntent().getExtras().getBoolean(ParseReceiver.IS_MANUAL_PUSH);
			String[] params = new String[newNewsKeywords.size()];
			int counter = 0;
			for (String keyword : newNewsKeywords) {
				params[counter++] = keyword;
			}
			if (isManualPush)
				new FetchNewNews(getApplicationContext(), this, user.getObjectId()).execute(params);
			else
				new FetchNews(getApplicationContext(), this, user.getObjectId()).execute(params);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fav_news, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.favourites) {
			Intent intent = new Intent(SearchActivity.this, FavNewsActivity.class);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().length() == 0)
			return true;
		return false;
	}

	@Override
	public void startFetchNews() {
		pd = new ProgressDialog(SearchActivity.this);
		pd.setTitle("Loading");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setMessage("Loading Results...");
		pd.show();
	}

	@Override
	public void receiveNews(List<News> newsList) {
		// TODO Auto-generated method stub
		this.newsList = newsList;
	}

	@Override
	public void stopFetchNews() {
		// TODO Auto-generated method stub
		pd.dismiss();
		Log.d("demo", "newsList" + newsList);
		adapter = new NewsAdapter(SearchActivity.this, R.layout.list_view_layout, newsList);
		listingNews.setAdapter(adapter);
	}

}

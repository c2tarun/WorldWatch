package com.example.madproject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madproject.FetchNews.FetchNewsInterface;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends BaseActivity implements FetchNewsInterface {

	public static final String NEWS_URL = "News URL";
	public static final String SEARCH_TEXT_FROM_USER = "SEARCH TEXT";
	private static final String SEARCH_HISTORY_TB = "SEARCH_HISTORY";
	private static final String USER_ID = "USER_ID";
	private static final String SEARCH_TEXT = "SEARCH_TEXT";
	private static final String NEWS_JSON_TB = "NEWS_JSON";
	protected static final String MAX_TS = "MAX_TS";
	protected static final String JSON = "JSON";
	public static final int SEARCH_CODE = 1234;
	private static final String TAG = MainActivity.class.getSimpleName();
	ProgressDialog pd;

	ListView listView;
	ListAdapter adapter;
	List<News> newsList;
	String title;
	String imgUrl;
	String appUrl;
	ImageView favButton;
	String objectId;
	ParseUser user;
	TextView noSearchHistoryTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listView);
		noSearchHistoryTV = (TextView) findViewById(R.id.noSearchTextView);
		newsList = new CopyOnWriteArrayList<News>();
		user = ParseUser.getCurrentUser();
		objectId = user.getObjectId();

		// Fetching search list from Parse
		fetchNewsBySearchHistory();
		findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText searchEditText = (EditText) findViewById(R.id.searchEditText);
				String searchText = searchEditText.getText().toString();
				if (GeneralUtil.isEmpty(searchText)) {
					Toast.makeText(getApplicationContext(), "Please enter search text", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(MainActivity.this, SearchActivity.class);
					intent.putExtra(SEARCH_TEXT_FROM_USER, searchText);
					startActivityForResult(intent, SEARCH_CODE);
				}
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, NewsWebView.class);
				intent.putExtra(NEWS_URL, newsList.get(position).getUrl());
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final ImageView favButton = (ImageView) view.findViewById(R.id.favImageButton);

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
							FavNews.put("newsUrl", appUrl);
							FavNews.put("imageUrl", imgUrl);
							FavNews.saveInBackground();
							favButton.setImageResource(R.drawable.favorites_yellow);
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
											adapter = new NewsAdapter(MainActivity.this, R.layout.list_view_layout,
													newsList);
											listView.setAdapter(adapter);
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
	}

	private void fetchNewsBySearchHistory() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(SEARCH_HISTORY_TB);
		query.whereEqualTo(USER_ID, user.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null && !objects.isEmpty()) {
					ParseObject obj = objects.get(0);
					String searches = obj.getString(SEARCH_TEXT);
					String[] searchTexts = searches.split(";");
					listView.setVisibility(View.VISIBLE);
					noSearchHistoryTV.setVisibility(View.GONE);
					new FetchNews(getApplicationContext(), MainActivity.this, user.getObjectId()).execute(searchTexts);
				} else {
					listView.setVisibility(View.GONE);
					noSearchHistoryTV.setVisibility(View.VISIBLE);
					Log.d(TAG, "Cannot obtain search history for " + user.getObjectId());
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		fetchNewsBySearchHistory();
	}

	@Override
	public void startFetchNews() {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(MainActivity.this);
		pd.setTitle("Loading");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setMessage("Loading Results...");
		pd.show();
	}

	@Override
	public void receiveNews(List<News> newsList) {
		this.newsList.addAll(newsList);
	}

	@Override
	public void stopFetchNews() {
		Collections.shuffle(newsList);
		adapter = new NewsAdapter(this, R.layout.list_view_layout, newsList);
		listView.setAdapter(adapter);
		pd.dismiss();
	}
}

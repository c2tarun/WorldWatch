package com.example.madproject;

import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SearchHistory extends BaseActivity {
	private static final String SEARCH_HISTORY_TB = "SEARCH_HISTORY";
	private static final String USER_ID = "USER_ID";
	private static final String SEARCH_TEXT = "SEARCH_TEXT";
	private static final String NEWS_JSON_TB = "NEWS_JSON";
	protected static final String MAX_TS = "MAX_TS";
	protected static final String JSON = "JSON";
	private static final String TAG = SearchHistory.class.getSimpleName();
	TextView noSearchTextView;
	ListView searchListView;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_history);

		pd = new ProgressDialog(SearchHistory.this);
		pd.setTitle("Loading");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setMessage("Loading Results...");
		pd.show();
		noSearchTextView = (TextView) findViewById(R.id.noSearchTextView);
		searchListView = (ListView) findViewById(R.id.searchListView);
		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(SEARCH_HISTORY_TB);
		query.whereEqualTo(USER_ID, user.getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				if (objects != null && e == null && objects.size() > 0) {
					ParseObject obj = objects.get(0);
					String searchTexts = obj.getString(SEARCH_TEXT);
					String[] searchArray = searchTexts.split(";");
					List<String> searchList = Arrays.asList(searchArray);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchHistory.this,
							android.R.layout.simple_list_item_1, searchList);
					searchListView.setAdapter(adapter);
					searchListView.setVisibility(View.VISIBLE);
					noSearchTextView.setVisibility(View.GONE);
				} else {
					Log.d(TAG, "Search history failed");
					searchListView.setVisibility(View.GONE);
					noSearchTextView.setVisibility(View.VISIBLE);
				}
				pd.dismiss();
			}
		});

		searchListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				return false;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if(id == R.id.searchHistory) {
			Toast.makeText(getApplicationContext(), "You are on search history page", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

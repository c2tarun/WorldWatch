package com.example.madproject;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/*****************************
 * This class will receive custom push notifications from parse.com. These are
 * different than the "plain" message push notifications.
 * 
 * There must be an action defined within the Intent-Filter for this receiver in
 * the manifest.xml file. And the same action must be specified on the
 * notification when it is pushed.
 * 
 * You can optionally pass JSON data from parse.com which will be avaialable in
 * the onReceive() method here.
 *****************************/
public class ParseReceiver extends ParsePushBroadcastReceiver {
	public static final String IS_MANUAL_PUSH = "is manual push";
	public static final String NEW_NEWS = "New news keywords";
	private final String TAG = "Parse Notification";
	private String msg = "New news available";
	private static ArrayList<String> newKeywords;
	private static final String KEYWORDS = "keywords";

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i(TAG, "PUSH RECEIVED!!!");
		boolean isManualPush = false;
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			JSONObject jsonData = null;
			if(json.has("alert")) {
				jsonData = new JSONObject(json.getString("alert"));
			} else if(json.has(KEYWORDS)){
				jsonData = json;
				isManualPush = true;
			}
			Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
			Iterator itr = jsonData.keys();
			newKeywords = new ArrayList<String>();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				Log.d(TAG, "..." + key + " => " + jsonData.getString(key));
				if (KEYWORDS.equalsIgnoreCase(key)) {
					JSONArray array = jsonData.getJSONArray(key);
					for (int i = 0; i < array.length(); i++) {
						newKeywords.add(array.getString(i));
					}
					break;
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}

		Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ww_icon);

		if (newKeywords == null || newKeywords.isEmpty()) {

		} else {
			Intent launchActivity = new Intent(ctx, SearchActivity.class);
			launchActivity.putExtra(NEW_NEWS, newKeywords);
			launchActivity.putExtra(IS_MANUAL_PUSH, isManualPush);
			PendingIntent pi = PendingIntent.getActivity(ctx, 0, launchActivity, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification noti = new NotificationCompat.Builder(ctx).setContentTitle("PUSH RECEIVED")
					.setContentText(msg).setSmallIcon(R.drawable.ww_icon).setLargeIcon(icon).setContentIntent(pi)
					.setAutoCancel(true).build();

			NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(0, noti);
		}

	}

}

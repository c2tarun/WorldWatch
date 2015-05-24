package com.example.madproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/*
 * Group10B_HW04
 * PreferenceUtils.java
 * Tarun Kumar Mall
 * Pragya Rai
 */
public class PreferenceUtils {
	
	Context context;
	private static final String TAG = PreferenceUtils.class.getSimpleName();
	private static final String WORLD_WATCH = "WorldWatch";
	private static final String DELIM = " ";
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	public PreferenceUtils(Context context) {
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = preferences.edit();
		
	}
	
	public void save(String searchText, long timeStamp) {
		Editor editor = preferences.edit();
		editor.putLong(searchText, timeStamp);
		editor.commit();
	}
	
	public long getTimeStamp(String searchText) {
		return preferences.getLong(searchText, 0);
	}
	
	public void delete(String searchText) {
		Editor editor = preferences.edit();
		editor.remove(searchText);
		editor.commit();
	}
	
}

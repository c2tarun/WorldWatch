package com.example.madproject;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends ArrayAdapter<News> {

	Context context;
	int resourceLayout;
	List<News> newsList;
	String title;
	String objectId;
	ParseUser user;

	public NewsAdapter(Context context, int resource, List<News> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resourceLayout = resource;
		this.newsList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(resourceLayout, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.newsImage = (ImageView) convertView.findViewById(R.id.newsImage);
			holder.title = (TextView) convertView.findViewById(R.id.newsTitle);
			holder.favButton = (ImageView) convertView.findViewById(R.id.favImageButton);

			convertView.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		News news = newsList.get(position);

		user = ParseUser.getCurrentUser();
		objectId = user.getObjectId();
		if (!GeneralUtil.isEmpty(news.getIurl()))
			Picasso.with(context).load(news.getIurl()).into(holder.newsImage);
		else
			holder.newsImage.setImageResource(R.drawable.no_image_available);
		holder.title.setText(news.getTitle());
		if (context instanceof FavNewsActivity) {
			holder.favButton.setImageResource(R.drawable.delete);

		} else {

			ParseQuery<ParseObject> query = ParseQuery.getQuery("FAV_NEWS");
			query.whereEqualTo("newsUrl", news.getUrl());
			query.whereEqualTo("userId", objectId);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (object == null) {
						holder.favButton.setImageResource(R.drawable.ratingnotimportant);
					} else {
						holder.favButton.setImageResource(R.drawable.favorites_yellow);
					}
				}
			});

		}

		return convertView;
	}

	class ViewHolder {
		public ImageView newsImage;
		public TextView title;
		public ImageView favButton;
	}

}

package com.example.madproject;

import java.io.Serializable;
import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class News implements Serializable {
	private String title;
	private String content;
	private String kwic;
	private String url;
	private String iurl;
	private String domain;
	private String author;
	private Boolean isNews;
	private String votes;
	private Date date;
	public static long max;

	public static News createNews(JSONObject json) {
		News news = new News();
		try {
			news.setTitle(json.getString("title"));
			news.setContent(json.getString("content"));
			news.setKwic(json.getString("kwic"));
			news.setUrl(json.getString("url"));
			news.setIurl(json.getString("iurl"));
			news.setDomain(json.getString("domain"));
			news.setAuthor(json.getString("author"));
			news.setIsNews(json.getBoolean("news"));
			news.setVotes(json.getString("votes"));
			news.setDate(json.getLong("date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return news;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKwic() {
		return kwic;
	}

	public void setKwic(String kwic) {
		this.kwic = kwic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIurl() {
		return iurl;
	}

	public void setIurl(String iurl) {
		this.iurl = iurl;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Boolean getIsNews() {
		return isNews;
	}

	public void setIsNews(Boolean isNews) {
		this.isNews = isNews;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(long ts) {
		Date dt = new Date(ts);
		this.date = dt;
	}

	public News() {
	}

	public News(String title, String content, String kwic, String url, String iurl, String domain, String author,
			Boolean isNews, String votes, Date date) {
		super();
		this.title = title;
		this.content = content;
		this.kwic = kwic;
		this.url = url;
		this.iurl = iurl;
		this.domain = domain;
		this.author = author;
		this.isNews = isNews;
		this.votes = votes;
		this.date = date;
	}
	
	public News(String title, String url, String iurl) {
		super();
		this.title = title;		
		this.url = url;
		this.iurl = iurl;
		
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "News [title=" + title + ", content=" + content + ", kwic=" + kwic + ", url=" + url + ", iurl=" + iurl
				+ ", domain=" + domain + ", author=" + author + ", isNews=" + isNews + ", votes=" + votes + ", date="
				+ date + "]";
	}

}

package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RssFeedDAO extends SQLiteOpenHelper{
	public static Feed feed;
	public static String url;	
	
	public RssFeedDAO(Context context) {
		super(context, "vozaide", null, 1);
	}

	
	public boolean insertFeed(Feed feed , String url)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("title", feed.getTitle());
	      contentValues.put("link", feed.getLink());
	      contentValues.put("description", feed.getDescription());	
	      contentValues.put("language", feed.getLanguage());
	      contentValues.put("copyright", feed.getCopyright());
	      contentValues.put("url", url);
	      db.insert("rssfeed", null, contentValues);
	      return true;
	   }

	public Cursor getData(){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from rssfeed", null );
	      return res;
	   }
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(
				      "create table IF NOT EXISTS rssfeed " +
				      "(id integer primary key, title text NOT NULL,link text,description text, language text,copyright text, url text)"
				      );
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS rssfeed");
		      onCreate(db);
		}
}

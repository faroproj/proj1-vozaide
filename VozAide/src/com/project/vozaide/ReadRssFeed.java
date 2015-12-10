package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import com.project.model.Feed;
import com.project.model.RssFeedDAO;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadRssFeed extends ActionBarActivity {
	ListView listView ;
	TextView txt;
	Feed feed;
	ArrayList<String> v;
	ArrayList<String> urls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_rss_feed);
		listView = (ListView) findViewById(R.id.list);
		try{
		RssFeedDAO rssFeedDao = new RssFeedDAO(this);
		Cursor rs = rssFeedDao.getData();
		rs.moveToFirst();
		
		 v= new ArrayList<String>();
		 urls= new ArrayList<String>();
		String data = rs.getString(rs.getColumnIndex("title"));
		String url = rs.getString(rs.getColumnIndex("url"));
		v.add(data);
		urls.add(url);
		
		while(rs.moveToNext()){
			data = rs.getString(rs.getColumnIndex("title"));
			url = rs.getString(rs.getColumnIndex("url"));
			v.add(data);
			urls.add(url);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, v);
		
		listView.setAdapter(adapter); 
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String strUrl ="";
				
					strUrl = urls.get(position);
				
					Toast.makeText(getApplicationContext(), strUrl , Toast.LENGTH_LONG).show();
				Intent intent = new Intent(ReadRssFeed.this, FeedMessagePage.class);
				intent.putExtra("url", strUrl);
				
					startActivity(intent);
				
			}
		});
		}catch(Exception e){
			finish();
			Toast.makeText(getApplicationContext(), "Rss Feed List is empty", Toast.LENGTH_SHORT).show();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_rss_feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.model.Feed;
import com.project.model.FeedMessage;
import com.project.model.FetchFeed;
import com.project.model.FilterRssFeedMessage;
import com.project.model.RssFeedDAO;
import com.project.model.WriteOutputFile;
import com.project.model.XMLFormatter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FilterRssFeed extends ActionBarActivity {
	TextView textView;
	EditText editText;
	ArrayList<String> urls;
	public String keywords;
	Feed feed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_rss_feed);
		textView = (TextView)findViewById(R.id.textViewFilteredFeed);
		editText = (EditText)findViewById(R.id.editTextKeyword);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_rss_feed, menu);
		return true;
	}
	public void filterFeed(View v){
		Feed feed = null;
		try{
			keywords = (editText.getText().toString()).toLowerCase();
			
			
			RssFeedDAO rssFeedDao = new RssFeedDAO(this);
			Cursor rs = rssFeedDao.getData();
				rs.moveToFirst();
				urls= new ArrayList<String>();
			
			String url = rs.getString(rs.getColumnIndex("url"));
			urls.add(url);
			
			while(rs.moveToNext()){
				url = rs.getString(rs.getColumnIndex("url"));
				urls.add(url);
			}
			int index = 0;
			String strUrl = "";
			List<FeedMessage> lMsg = new ArrayList<FeedMessage>();
			while(urls.size() > index){
				strUrl = urls.get(index).toString();
				feed = new FetchFeed().execute(strUrl).get();
				lMsg.addAll( new FilterRssFeedMessage(feed).msg(keywords) );
				index++;
			}
			
			Feed feedCustomized = new Feed(keywords, "Local", "Keywords used to filter: "+keywords, "en", "Customized", DateFormat.getDateTimeInstance().format(new Date()));
			feedCustomized.setEntries(lMsg);
			
			
			
			
			try{
				XMLFormatter.XMLFileWriter(feedCustomized);
				Toast.makeText(getApplicationContext(),
						"XML new Field generated.",
						Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
			
			
			String path = "/sdcard/"+keywords.toString()+".xml";
			Toast.makeText(getApplicationContext(), path , Toast.LENGTH_SHORT).show();
			
			try{
				Intent intent = new Intent(getApplicationContext(), FeedMessagePage.class);
				intent.putExtra("url",path);
				startActivity(intent);
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Problem in activity start." , Toast.LENGTH_SHORT).show();
			}
			String result = "";	
			int pos = 0;
			while(lMsg.size() > pos){
				result = result.concat(lMsg.get(pos).getTitle().toString());
				//System.getProperty("line.separator")
				result = result.concat(System.getProperty("line.separator"));
				pos++;
			}	
				
				
			textView.setText(result); 
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Problem in filtering" , Toast.LENGTH_SHORT).show();
		}
		
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

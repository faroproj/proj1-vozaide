package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.text.Html.ImageGetter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.model.AndroidTTS;
import com.project.model.Feed;
import com.project.model.FeedMessage;
import com.project.model.FetchFeed;
import com.project.model.TextFormattor;
import com.project.model.WriteOutputFile;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class FeedMessagePage extends ActionBarActivity {
	private String strURL;
	TextView feedTitle;
	TextView feedMessageTitle;
	public static int index= 0;
	Feed feed ;
	List<FeedMessage> l;
	Button btnDesc;
	WebView webView;
	static AndroidTTS tts ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_message_page);
		tts = new AndroidTTS(this);
		index= 0;
		Bundle bundle = getIntent().getExtras();
		strURL = bundle.getString("url");
		
		
		
		feedTitle = (TextView)findViewById(R.id.feedTitle);
		feedMessageTitle = (TextView)findViewById(R.id.feedMessageTitle);
		feedMessageTitle.setText("");
		try {
			feed = new FetchFeed().execute(strURL).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
		feedTitle.setText(feed.getTitle());
		l = feed.getEntries();
		Log.d("------>",feed.getTitle() );
		feedMessageTitle.setText(l.get(index).getTitle());
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  tts.speak(l.get(index).getTitle().toString());
		  }
		}, 1100);
		}catch(Exception e){
			finish();
			Toast.makeText(getApplicationContext(), "Rss Feed is not available" , Toast.LENGTH_SHORT).show();
		}
	}
	
	public void nextFeedMessage(View view)
	{
		index++;
		if(l.size() > index){
			tts.stopSpeak();
		feedMessageTitle.setText(l.get(index).getTitle().toString());
		tts.speak(l.get(index).getTitle().toString());
		}else{
			index = l.size()-1;
			Toast.makeText(getApplicationContext(), "Last Feed Message", Toast.LENGTH_SHORT).show();
			tts.speak(" This is Last Feed Message");
		}
	}
	
	public void prevFeedMessage(View view)
	{
		index--;
		if(0 <= index){
			tts.stopSpeak();
		feedMessageTitle.setText(l.get(index).getTitle().toString());
		tts.speak(l.get(index).getTitle().toString());
		}else{
			index = 0;
			Toast.makeText(getApplicationContext(), "First Feed Message", Toast.LENGTH_SHORT).show();
			tts.speak(" This is First Feed Message");
		}
	}
	public void descFeedMessage(View view)
	{
		final String s = TextFormattor.removeAds(l.get(index).getDescription().toString(), this);
		tts.stopSpeak();
		
 		
		Intent intent = new Intent(getApplicationContext(), DescriptionPage.class);
		intent.putExtra("description",s );
		
		startActivity(intent);
		
	}
	public void openLink(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(l.get(index).getLink()));      
		startActivity(intent); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_message_page, menu);
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

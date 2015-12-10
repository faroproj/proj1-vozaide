package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;

import java.util.concurrent.ExecutionException;

import com.project.model.Feed;
import com.project.model.FetchFeed;
import com.project.model.RssFeedDAO;
import com.project.model.WriteOutputFile;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveFeedPage extends ActionBarActivity {
	RssFeedDAO rssFeedDao;
	public static Feed f;
	EditText eTitle;
	EditText eLang;
	EditText eCopy;
	EditText eDesc;
	Feed feedGet;
	
	public static String strURL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_feed_page);
		try {
		Bundle bundle = getIntent().getExtras();
		strURL = bundle.getString("url");
		
		
	
		eTitle = (EditText)findViewById(R.id.editTextTitle);
		eLang = (EditText)findViewById(R.id.editTextLanguage);
		eCopy = (EditText)findViewById(R.id.editTextCopyright);
		eDesc = (EditText)findViewById(R.id.editTextDesc);
		
		
		
			f = new FetchFeed().execute(strURL).get();
		
	
		eTitle.setText(f.getTitle());
		eLang.setText(f.getLanguage());
		eCopy.setText(f.getCopyright());
		eDesc.setText(f.getDescription());
		
		
		rssFeedDao = new RssFeedDAO(this);
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			finish();
			Toast.makeText(getApplicationContext(),
					"Url is invalid",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	public void saveFeedDao(View view)
	{
		if(rssFeedDao.insertFeed(f, strURL)){
		Toast.makeText(getApplicationContext(), "Rss Feed Saved.", Toast.LENGTH_LONG).show();
		}
		
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		
	}
	public void writeOutput(View view){
		try{
			WriteOutputFile.outputFile(this.f);
			Toast.makeText(getApplicationContext(),
					"Done writing...",
					Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_feed_page, menu);
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

package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	}
	
		//setContentView(R.layout.activity_main_page);
	public void addNewRssFeedPage(View view)
	{
		startActivity(new Intent(getApplicationContext(), AddNewRssFeedPage.class));
		//setContentView(R.layout.activity_main_page);
	}
	public void readFeedPage(View view)
	{
		startActivity(new Intent(getApplicationContext(), ReadRssFeed.class));
		
	}
	
	public void filterFeed(View view)
	{
		startActivity(new Intent(getApplicationContext(), FilterRssFeed.class));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

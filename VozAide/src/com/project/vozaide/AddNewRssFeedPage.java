package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewRssFeedPage extends ActionBarActivity {
    EditText eUrl;
    String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_rss_feed_page);
		eUrl = (EditText)findViewById(R.id.editTextURL);
		url = eUrl.getText().toString();
		
		
		
	}

	public void saveRssFeedPage(View view)
	{
		url = eUrl.getText().toString();
		Intent intent = new Intent(getApplicationContext(), SaveFeedPage.class);
		intent.putExtra("url", url);
		startActivity(intent);
		//setContentView(R.layout.activity_main_page);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_rss_feed_page, menu);
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

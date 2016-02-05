package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

import com.project.model.AndroidTTS;
import com.project.model.Feed;
import com.project.model.SystemDAO;
import com.project.model.VoiceCommandFormatter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
	TextView txtStatus;
	Feed feed;
	ArrayList<String> v;
	ArrayList<String> urls;
	SystemDAO rssFeedDao;
	AndroidTTS tts;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_rss_feed);
		listView = (ListView) findViewById(R.id.list);
		txtStatus = (TextView)findViewById(R.id.textViewReadPageStatus);
		tts = new AndroidTTS(this);
		FilterRssFeed.track = false;
		try{
		 rssFeedDao = new SystemDAO(this);
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
		if(MainActivity.voiceActivated){
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  speakActivate();
		  }
		}, 2000);
		}
		txtStatus.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String option = txtStatus.getText().toString().toLowerCase() ;
				if(option.equals("no command found")){
					tts.speak("Command not found speak again after beep");
					final Handler handler1 = new Handler();
					handler1.postDelayed(new Runnable() {
					  @Override
					  public void run() {
						  if(!tts.isSpeaking()){
							  txtStatus.setText("Status: Listening");
								
							  promptSpeechInput();
						  }
					  }
					}, 3000);
				}else{
					int i = 0;
					while(v.size() > i){
						String str = txtStatus.getText().toString();
						if(v.get(i).toLowerCase().equalsIgnoreCase(str)){
							String strUrl ="";
							
							strUrl = urls.get(i);
						
							Intent intent = new Intent(ReadRssFeed.this, FeedMessagePage.class);
							intent.putExtra("url", strUrl);
						
							startActivity(intent);
						}
						i++;
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_rss_feed, menu);
		return true;
	}
	public void speakActivate(){
		 txtStatus.setText("Status: Speaking");
		  tts.speak("Select R S S feed to read headlines    ");
			 
		 int lop = 0;
		 while(v.size() > lop){
			 tts.speak(v.get(lop));
			 tts.speak("    ");
			 
			 lop++;
		 }
		  
		
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  if(!tts.isSpeaking()){
				  txtStatus.setText("Status: Listening");
					
				  promptSpeechInput();
			  }
			  else if(tts.isSpeaking()){
				  handler1.postDelayed(this, 500);
			  }
		  }
		}, 3000);
	}
	private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            txtStatus.setText("Status: Matching Command");
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Not supported",
                    Toast.LENGTH_SHORT).show();
            txtStatus.setText("Status: Voice Recognizer Not Supported");
        }
    }
	/**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
              
                String res = "Problem in match";
                try{
                	res = VoiceCommandFormatter.matchCommand(result , v);
            	}catch(Exception e){
            		 Toast.makeText(getApplicationContext(),
                             e.getMessage(),
                             Toast.LENGTH_SHORT).show();
            	}
                txtStatus.setText(res.toString());
            }
            break;
        }
 
        } 
    }
    @Override
    protected void onStop() {
    	tts.stopSpeak();
    	super.onStop();
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

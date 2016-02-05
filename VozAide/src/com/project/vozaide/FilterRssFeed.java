package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.project.model.AndroidTTS;
import com.project.model.Feed;
import com.project.model.FeedMessage;
import com.project.model.FetchFeed;
import com.project.model.Field;
import com.project.model.FilterRssFeedMessage;
import com.project.model.SystemDAO;
import com.project.model.VoiceCommandFormatter;
import com.project.model.WriteOutputFile;
import com.project.model.XMLFormatter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FilterRssFeed extends ActionBarActivity {
	public static boolean track = false;
	ListView listView ;
	
	AndroidTTS tts;
	TextView txtStatus;
	TextView textView;
	EditText editTextField;
	EditText editTextKeywords;
	Field field;
	ArrayList<String> res;
	String[] keys;
	ArrayList<String> urls;
	public String keywords;
	Feed feed;
	SystemDAO fieldsDao;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_rss_feed);
		tts = new AndroidTTS(this);
		txtStatus = (TextView)findViewById(R.id.textViewFilterPageStatus);
		//textView = (TextView)findViewById(R.id.textViewFilteredFeed);
		editTextField = (EditText)findViewById(R.id.editTextField);
		editTextKeywords = (EditText)findViewById(R.id.editTextFieldKeywrds);
		listView = (ListView) findViewById(R.id.listFields);
		keys = new String[100];
		fieldsDao = new SystemDAO(this);
		track = true;
		try{
			
			Cursor rs = fieldsDao.getFieldsData();
			rs.moveToFirst();
			
			 res= new ArrayList<String>();
			String field = rs.getString(rs.getColumnIndex("field"));
			res.add(field);
		
			String key = rs.getString(rs.getColumnIndex("keywords"));
			keys[0] = key;
			
			int index=1;
			while(rs.moveToNext()){
				field = rs.getString(rs.getColumnIndex("field"));
				res.add(field);
				key = rs.getString(rs.getColumnIndex("keywords"));
				keys[index] = key;
				index++;
			}
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
		              android.R.layout.simple_list_item_1, android.R.id.text1, res);
			
			listView.setAdapter(adapter1); 
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						try{
							filterFeed(res.get(position) , keys[position]);
						}catch(Exception e){
							Toast.makeText(getApplicationContext(), "Problem in filterfeed() function", Toast.LENGTH_SHORT).show();
						}
				}
			});
			}catch(Exception e){
				finish();
				Toast.makeText(getApplicationContext(), "Field List is empty", Toast.LENGTH_SHORT).show();
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
					while(res.size() > i){
						String str = txtStatus.getText().toString();
						if(res.get(i).toLowerCase().equalsIgnoreCase(str)){
							filterFeed(res.get(i) , keys[i]);
							
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
	public void addField(View v){
		String f = editTextField.getText().toString();
		String k = editTextKeywords.getText().toString();
		fieldsDao.insertField(new Field(f, k));
		finish();
		startActivity(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_rss_feed, menu);
		return true;
	} 
	public void filterFeed(String s, String k){
		Feed feed = null;
		try{
			keywords = s.concat(",").concat(k);
			//Toast.makeText(getApplicationContext(), keywords, Toast.LENGTH_SHORT).show();
			
			
			getFeeds();
			
			
			
			
			
			int index = 0;
			String strUrl = "";
			
			List<FeedMessage> lMsg = new ArrayList<FeedMessage>();
			while(urls.size() > index){
				strUrl = urls.get(index).toString();
				feed = new FetchFeed().execute(strUrl).get();
				lMsg.addAll( new FilterRssFeedMessage(feed).msg(keywords) );
				index++;
			}	
			
			
			
			
			Feed feedCustomized = new Feed(s, "Local", "Keywords used to filter: "+keywords.toLowerCase(), "en", "Customized", DateFormat.getDateTimeInstance().format(new Date()));
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
			
			
			String path = "/sdcard/"+s.toString()+".xml";
			
			try{
				Intent intent = new Intent(getApplicationContext(), FeedMessagePage.class);
				intent.putExtra("url",path);
				startActivity(intent);
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Problem in activity start." , Toast.LENGTH_SHORT).show();
			}
		
			
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Problem in filtering" , Toast.LENGTH_SHORT).show();
		}
		
		}
	
	

	private void getFeeds() {
		SystemDAO rssFeedDao = new SystemDAO(this);
		Cursor rs = rssFeedDao.getData();
			rs.moveToFirst();
			urls= new ArrayList<String>();
		
		String url = rs.getString(rs.getColumnIndex("url"));
		urls.add(url);
		
		while(rs.moveToNext()){
			url = rs.getString(rs.getColumnIndex("url"));
			urls.add(url);
		}
		
	}
	public void speakActivate(){
		 txtStatus.setText("Status: Speaking");
		  tts.speak("Select Field to filter headlines    ");
			 
		 int lop = 0;
		 while(res.size() > lop){
			 tts.speak(res.get(lop));
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

               ArrayList<String> result = data
                       .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
             
               String r = "Problem in match";
               try{
               	r = VoiceCommandFormatter.matchCommand(result , this.res);
           	}catch(Exception e){
           		 Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_SHORT).show();
           	}
               txtStatus.setText(r.toString());
           }
           break;
       }

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

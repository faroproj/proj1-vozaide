package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.text.Html.ImageGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.model.AndroidTTS;
import com.project.model.Feed;
import com.project.model.FeedMessage;
import com.project.model.FetchFeed;
import com.project.model.SystemDAO;
import com.project.model.TextFormattor;
import com.project.model.VoiceCommandFormatter;
import com.project.model.WriteOutputFile;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
	private final int REQ_CODE_SPEECH_INPUT = 100;
	public static boolean check=true;
	private TextView txtSpeechInput;
	SystemDAO sysDao;

	//WebView webView;
	static AndroidTTS tts ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_message_page);
		tts = new AndroidTTS(this);
		sysDao = new SystemDAO(getApplicationContext());
		index= 0;
		Bundle bundle = getIntent().getExtras();
		strURL = bundle.getString("url");
		txtSpeechInput = (TextView) findViewById(R.id.textViewFeedMsgPageStatus);
		
		
		feedTitle = (TextView)findViewById(R.id.feedTitle);
		feedMessageTitle = (TextView)findViewById(R.id.feedMessageTitle);
		feedMessageTitle.setText("");
		try {
			feed = new FetchFeed().execute(strURL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		try{
		feedTitle.setText(feed.getTitle());
		l = feed.getEntries();
		feedMessageTitle.setText(l.get(index).getTitle());
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  if(MainActivity.voiceActivated){
					speakActivate();
				}
			 // tts.speak(l.get(index).getTitle().toString());
		  }
		}, 1100); 
		}catch(Exception e){
			finish();
			Toast.makeText(getApplicationContext(), "Rss Feed is not available" , Toast.LENGTH_SHORT).show();
		}
		
txtSpeechInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String option = txtSpeechInput.getText().toString().toLowerCase() ;
				if(option.equals("next"))
				{
					//tts.speak(txtSpeechInput.getText().toString());
					nextFeedMessage(new View(getApplicationContext()));
				}
				else if(option.equals("previous"))
				{
					//tts.speak(txtSpeechInput.getText().toString());
					prevFeedMessage(new View(getApplicationContext()));
				}
				else if(option.equals("description"))
				{
					//tts.speak(txtSpeechInput.getText().toString());
					descFeedMessage(new View(getApplicationContext()));
				}else if(option.equals("home page") || option.equalsIgnoreCase("homepage"))
				{
					MainActivity.voiceActivated=true;
					//tts.speak(txtSpeechInput.getText().toString());
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					
				}else if(option.equals("back page") || option.equalsIgnoreCase("backpage"))
				{
					if(!FilterRssFeed.track){
					startActivity(new Intent(getApplicationContext(), ReadRssFeed.class));
					}else{
						startActivity(new Intent(getApplicationContext(), FilterRssFeed.class));
					}
				}
				else if(option.equals("no command found")){
					tts.speak("Command not found speak again after beep");
					final Handler handler1 = new Handler();
					handler1.postDelayed(new Runnable() {
					  @Override
					  public void run() {
						  if(!tts.isSpeaking()){
							  txtSpeechInput.setText("Status: Listening");
								
							  promptSpeechInput();
						  }
					  }
					}, 3000);
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

		options();
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

		options();
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
	public void options(){
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
		@Override
		public void run() {
		  if(!tts.isSpeaking()){
			  txtSpeechInput.setText("Status: Listening");
			  DescriptionPage.descCheck=true;
			  promptSpeechInput();
		  }
		  else if(tts.isSpeaking()){
			  handler1.postDelayed(this, 500);
		  }
		}
		}, 2000);
	}
	public void speakActivate(){
		txtSpeechInput.setText("Status: Speaking");
		tts.speak("This is    "
		  		+ feed.getTitle().toString()
		  		+"							"
		  		);
		
		tts.speak("-	-	-	-	-	-"
				+ "Select your option after beep"
				+"for every Feed Message 	-	"
				+ "Next  "
				+ "	- 	previous to get previous feed message	"
				+ "			- or	description"
				+ "     -       "
				+ "     -       "
		  		);
		
		tts.speak(" -   "
				+"  -   "
				+"  -   "
		  		+ l.get(index).getTitle().toString()
		  		); 
		options();
		
	}
	
	private void promptSpeechInput() {
		FeedMessagePage.check = false;
		DescriptionPage.descCheck=true;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            txtSpeechInput.setText("Status: Matching Command");
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Not supported",
                    Toast.LENGTH_SHORT).show();
            txtSpeechInput.setText("Status: Voice Recognizer Not Supported");
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
                ArrayList<String> comData = new ArrayList<String>(); 
                try{
                	Cursor rs = sysDao.getCommandData();
                	rs.moveToFirst();
                	String com = rs.getString(rs.getColumnIndex("comName"));
                	comData.add(com);
                	
                	while(rs.moveToNext()){
                		com = rs.getString(rs.getColumnIndex("comName"));
                    	comData.add(com);
            		}
                	
                }catch(Exception e){
                	 Toast.makeText(getApplicationContext(),
                             e.getMessage(),
                             Toast.LENGTH_SHORT).show();
                }
                String res = "Problem in match";
                try{
                	res = VoiceCommandFormatter.matchCommand(result , comData);
            	}catch(Exception e){
            		 Toast.makeText(getApplicationContext(),
                             e.getMessage(),
                             Toast.LENGTH_SHORT).show();
            	}
                txtSpeechInput.setText(res.toString());
            }
            break;
        }
 
        } 
    }
    @Override
    protected void onResume() {
    	if(!DescriptionPage.descCheck && check && txtSpeechInput.getText().toString().equalsIgnoreCase("description")){
    		DescriptionPage.descCheck=true;
    		FeedMessagePage.check=false;
    		options();
    		
    		
    	}
    	super.onResume();
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

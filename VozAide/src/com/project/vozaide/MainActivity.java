package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import com.project.model.AndroidTTS;
import com.project.model.SystemDAO;
import com.project.model.VoiceCommandFormatter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	public static boolean voiceActivated = false;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private TextView txtSpeechInput;
	ImageButton buttonMic;
	AndroidTTS tts;
	SystemDAO sysDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tts = new AndroidTTS(this);
		if(!isConnectedToInternet()){
			tts.speak("Application required a working internet connection.");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Connection error");
			builder.setMessage("Application required a working internet connection.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int id) {
		           finish();
		         }
		      });
			builder.create();
			builder.show();
		}
		
		
		
		
		buttonMic = (ImageButton) findViewById(R.id.btnMic);
		txtSpeechInput = (TextView) findViewById(R.id.textViewStatus);
		sysDao = new SystemDAO(getApplicationContext());
		if(voiceActivated){
			txtSpeechInput.setText("Status: Speaking");
			
			voiceActivated = true;
			speakActivate();
		}
		buttonMic.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txtSpeechInput.setText("Status: Speaking");
				voiceActivated = true;
				speakActivate();
				
			}
		});
		
		 txtSpeechInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String option = txtSpeechInput.getText().toString().toLowerCase() ;
				if(option.equals("read rss feed"))
				{
					
					//tts.speak("Selected option Read R S S Feed");
					readFeedPage(new View(getApplicationContext()));
				}
				else if(option.equals("filter rss feed"))
				{
					//tts.speak("Selected option Filter R S S Feed");
					
					//tts.speak(txtSpeechInput.getText().toString());
					filterFeed(new View(getApplicationContext()));
				}
				else if(option.equals("add new rss feed"))
				{
					//tts.speak("Add New R S S feed page is not voice activated press ok to proceed.");
				
				        	 addNewRssFeedPage(new View(getApplicationContext()));
				        

					//tts.speak("Selected option Add new R S S Feed");
					
					//tts.speak(txtSpeechInput.getText().toString());
					
				}else if(option.equals("text to speech"))
				{
					//tts.speak("Selected option Text to speech");
					
					//tts.speak(txtSpeechInput.getText().toString());
					readTextPage(new View(getApplicationContext()));
				}else if(option.equals("no command found")){
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
	public void speakActivate(){
		tts.speak("Voice commands activated. "
		  		+ "  -    Speak your option after beep. "
		  		+ "   -    Read R S S Feed. "
		  		+ "  -   Filter R S S Feed. "
		  		+ "  -   Add new R S S Feed. "
		  		+ "   -   Text to Speech. ");
		  
		
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  if(!tts.isSpeaking()){
				  txtSpeechInput.setText("Status: Listening");
					
				  promptSpeechInput();
			  }
			  else if(tts.isSpeaking()){
				  handler1.postDelayed(this, 500);
			  }
		  }
		}, 3000);
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
	
	private void promptSpeechInput() {
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
	public void readTextPage(View v){
		startActivity(new Intent(getApplicationContext(), ReadText.class));
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
    public boolean isConnectedToInternet(){
    	ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        
    	if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
 
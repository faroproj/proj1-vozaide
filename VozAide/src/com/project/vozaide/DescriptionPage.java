package com.project.vozaide;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;

import com.project.model.AndroidTTS;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class DescriptionPage extends Activity {
	TextView textView;
	ToggleButton btnToggle;
	Button close;
	String desc;
	AndroidTTS tts;
	public static boolean descCheck = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description_page);
		tts = new AndroidTTS(this);
		close = (Button)findViewById(R.id.buttonDescPageClose);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		getWindow().setLayout(size.x,size.y);
			btnToggle = (ToggleButton)findViewById(R.id.toggleButtonSpeak);
		textView = (TextView)findViewById(R.id.textViewDescPageValue);
		Bundle bundle = getIntent().getExtras();
		desc = bundle.getString("description");
		textView.setText(Html.fromHtml(desc));
		textView.setHeight(size.y-600);
		textView.setMovementMethod(new ScrollingMovementMethod());
		
		
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  tts.speak(desc);
		  }
		}, 500);
		
		final Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  if(!tts.isSpeaking()){
				  btnToggle.setChecked(false);
				  if(MainActivity.voiceActivated){
					  DescriptionPage.descCheck = false;
					  FeedMessagePage.check = true;
					  finish();
					  tts.destroy();
				  }
			  }
			  handler1.postDelayed(this, 500);
		  }
		}, 1000);
		
		
		
		
				
		
		close.setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {	
				DescriptionPage.descCheck = false;
				FeedMessagePage.check = true;
					finish();
				tts.destroy();
			}
		});
		
	}
	
}

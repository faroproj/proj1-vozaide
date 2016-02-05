package com.project.model;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class AndroidTTS implements OnInitListener {
	 
    private TextToSpeech tts;
     
    private boolean ready = false;
     
    private boolean allowed = false;
     
    HashMap<String, String> hash;
    public AndroidTTS(Context context){
        tts = new TextToSpeech(context, this);      
    }   
     
    public boolean isAllowed(){
        return allowed;
    }
     
    public void allow(boolean allowed){
        this.allowed = allowed;
    }
    public void speak(String text){
    	text = text.replace("-", " ");
    	text = text.replace("'", " ");
        if(ready){
            hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM, 
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
        
    }
    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }
    public boolean readyState(){
    	return ready;
    }

    public boolean isSpeaking(){
    	return tts.isSpeaking();
    }
    public void destroy(){
        tts.shutdown();
    }
    public void stopSpeak(){
    	tts.stop();
    }
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if(status == TextToSpeech.SUCCESS){
	        // Change this to match your
	        // locale
	        tts.setLanguage(Locale.US);
	        tts.setSpeechRate(0);
	        
	        ready = true;
	    }else{
	        ready = false;
	    }
	}}


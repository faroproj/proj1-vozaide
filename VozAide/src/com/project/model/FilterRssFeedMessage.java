package com.project.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class FilterRssFeedMessage {
	public Feed feed;
	public FilterRssFeedMessage(Feed f)
	{
		this.feed = f;
	}
	private Feed getFeed(){
		
		
		
		return feed;
	}
	
	public List<FeedMessage> msg(String key){
		String regExp = ".*\\s"+key.toLowerCase()+"\\s.*|^"+key.toLowerCase()+"\\s.*|.*\\s"+key.toLowerCase()+"$";
		List<FeedMessage> feedMsg = feed.getEntries();
		List<FeedMessage> resMsg = new ArrayList<FeedMessage>();
		
		int index = 0;
		
		while(feedMsg.size() > index){
		String check = feedMsg.get(index).getTitle().toLowerCase();
		if(check.matches(regExp)){
			resMsg.add(feedMsg.get(index));
		}
		index++;
		}
		return resMsg;
		
	}
}

package com.project.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

public class WriteOutputFile {
	public static void outputFile(Feed feed){
		StringBuilder writeData = feedToStringSerialize(feed);
		try {
			File myFile = new File("/sdcard/output.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append(writeData);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
			Log.d("Error Writing", "Writter is not working");
		}
	}

	private static StringBuilder feedToStringSerialize(Feed feed) {
		String data ="Feed";
		data = data.concat(System.getProperty("line.separator"));
		data = data.concat("Title: "+feed.getTitle().toString());
		data = data.concat(System.getProperty("line.separator"));
		data =data.concat("Link: "+feed.getLink().toString());
		data = data.concat(System.getProperty("line.separator"));
		data = data.concat("Description: "+feed.getDescription().toString());
		data = data.concat(System.getProperty("line.separator"));
		data = data.concat("Language: "+feed.getLanguage().toString());
		data = data.concat(System.getProperty("line.separator"));
		data = data.concat("Copyright: "+feed.getCopyright().toString());
		data = data.concat(System.getProperty("line.separator"));
		data = data.concat("Publish Date: "+feed.getPubDate().toString());
		
		List<FeedMessage> l ;
		l = feed.getEntries();
		int i=0;
		StringBuilder msg = new StringBuilder();
		msg.append(data);
		while(l.size() > i){
			msg.append(System.getProperty("line.separator"));
			msg.append(System.getProperty("line.separator"));
			msg.append("Feed Message");
			msg.append(System.getProperty("line.separator"));
			msg.append("Title: "+l.get(i).getTitle().toString());
			msg.append(System.getProperty("line.separator"));
			msg.append("Description: "+l.get(i).getTitle().toString());
			msg.append(System.getProperty("line.separator"));
			msg.append("Link: "+l.get(i).getLink().toString());
			msg.append(System.getProperty("line.separator"));
			msg.append("Author: "+(l.get(i).getAuthor()));
			msg.append(System.getProperty("line.separator"));
			msg.append("GUID: "+l.get(i).getGuid());
			msg.append(System.getProperty("line.separator"));
			msg.append("Publish Date: "+l.get(i).getPubDate());
			i++;
		}
		
		
		
		
		
		
		
		
		
		
		
		return msg;
	}	
}

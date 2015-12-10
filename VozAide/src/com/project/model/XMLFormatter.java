package com.project.model;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SerializablePermission;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;


public class XMLFormatter {
	public static void XMLFileWriter(Feed feed){
		
		try {
			File myFile = new File("/sdcard/"+feed.getTitle()+".xml");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			
			XmlSerializer xmlSerializer = Xml.newSerializer();            
		   
			StringWriter writer = new StringWriter();
		    
			
			xmlSerializer.setOutput(writer);
		    xmlSerializer.startDocument("UTF-8", true);
		    xmlSerializer.startTag("", "rss");
		    xmlSerializer.startTag("", "channel");
		    	
		    	xmlSerializer.startTag("", "title");
		        xmlSerializer.text(feed.getTitle());
		    	xmlSerializer.endTag("", "title");
		    	xmlSerializer.startTag("", "link");
		        xmlSerializer.text(feed.getLink());
		    	xmlSerializer.endTag("", "link");
		    
		    	xmlSerializer.startTag("", "description");
		        xmlSerializer.text(feed.getDescription());
		    	xmlSerializer.endTag("", "description");
		    
		    	xmlSerializer.startTag("", "language");
		        xmlSerializer.text(feed.getLanguage());
		    	xmlSerializer.endTag("", "language");
		    	xmlSerializer.startTag("", "copyright");
		        xmlSerializer.text(feed.getCopyright());
		    	xmlSerializer.endTag("", "copyright");
		    	
		    	xmlSerializer.startTag("", "pubDate");
		        xmlSerializer.text(feed.getPubDate());
		    	xmlSerializer.endTag("", "pubDate");
		    
		    int pos = 0;
		    while(feed.getEntries().size() > pos){
		    	xmlSerializer.startTag("", "item");
		    	
		    	xmlSerializer.startTag("", "title");
		        xmlSerializer.text(feed.getEntries().get(pos).getTitle());
		    	xmlSerializer.endTag("", "title");
		    	xmlSerializer.startTag("", "link");
		        xmlSerializer.text(feed.getEntries().get(pos).getLink());
		    	xmlSerializer.endTag("", "link");
		    	xmlSerializer.startTag("", "description");
		        xmlSerializer.text(feed.getEntries().get(pos).getDescription());
		    	xmlSerializer.endTag("", "description");
		    	xmlSerializer.startTag("", "guid");
		        xmlSerializer.text(feed.getEntries().get(pos).getGuid());
		    	xmlSerializer.endTag("", "guid");
		    	xmlSerializer.startTag("", "pubDate");
		        xmlSerializer.text(feed.getEntries().get(pos).getPubDate());
		    	xmlSerializer.endTag("", "pubDate");
		    	
		    	xmlSerializer.endTag("", "item");
		    	pos++;
		    }
		   
		   
		    xmlSerializer.endTag("", "channel");
		    xmlSerializer.endTag("", "rss");
		    xmlSerializer.endDocument();
		    xmlSerializer.flush();
		    String dataWrite = writer.toString();
		    Log.d("------>", dataWrite);
		   // fOut.write(dataWrite.getBytes());
		    fOut.close();
			
		}catch (IllegalStateException e) {
			Log.d("Error in input", "input is not working");
		}catch (Exception e) {
			Log.d("Error Writing", "Writter is not working");
		}
	}
	
}

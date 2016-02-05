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
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
		  	XmlSerializer xmlSerializer = Xml.newSerializer();
		    StringWriter writter = new StringWriter();
		
		    xmlSerializer.setOutput(writter);
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
		    
		    	 xmlSerializer.endDocument();
				   
				    xmlSerializer.flush();
		    	String dataWrite = writter.toString();
				 
			    fOut.write(dataWrite.getBytes());
		    	
		    int pos = 0;
		    int size = feed.getEntries().size();
		    while(size > pos){
		    	XmlSerializer serializer = Xml.newSerializer();
			    StringWriter writer = new StringWriter();
			    serializer.setOutput(writer);
				
		    
		    	serializer.startTag(null, "item");
		    	
		    	serializer.startTag(null, "title");
		        serializer.text(feed.getEntries().get(pos).getTitle());
		    	serializer.endTag(null, "title");
		    	
		    	serializer.startTag(null, "link");
		        serializer.text(feed.getEntries().get(pos).getLink());
		    	serializer.endTag(null, "link");
		    	
		    	serializer.startTag(null, "description");
		        serializer.text(feed.getEntries().get(pos).getDescription());
		    	serializer.endTag(null, "description");
		    	
		    	serializer.startTag(null, "guid");
		        serializer.text(feed.getEntries().get(pos).getGuid());
		    	serializer.endTag(null, "guid");
		    	
		    	serializer.startTag(null, "pubDate");
		        serializer.text(feed.getEntries().get(pos).getPubDate());
		    	serializer.endTag(null, "pubDate");
		    	
		    	serializer.endTag(null, "item");
		    	
		    	serializer.endDocument();
				   
				serializer.flush();
		    	
		    	fOut.write(writer.toString().getBytes());
		    	pos++;
		    }
		   
		   
		   
		   
		    String endData = "<channel><rss>";
		 
		    fOut.write(endData.getBytes());
		    fOut.close();
			
		}catch (IllegalStateException e) {
			Log.d("Error in input", "input is not working");
		}catch (Exception e) {
			Log.d("Error Writing", "Writter is not working");
		}
	}
	
}

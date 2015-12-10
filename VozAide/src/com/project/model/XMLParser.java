package com.project.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XMLParser {
	 private final String ns = null;//we don't use namespace keep it null
	 static String TITLE = null;
	  static String DESCRIPTION = null;
	  static String LANGUAGE = null;
	  static String COPYRIGHT = null;
	  static String LINK = null;
	  static String PUB_DATE = null;
	  
	
	
	
	public Feed parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
        	XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }
	
	private Feed readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "rss");
        String title = null;
        String link = null;
        String desc = null;
        String author = null;
        String pubDate = null;
        String guid =null;
        String dRepeatCheck = null;
        Feed feedValues = null;
        List<FeedMessage> items = new ArrayList<FeedMessage>();
        
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("title")) {
                TITLE = readTitle(parser);
            }else if (tagName.equals("description")) {
            	DESCRIPTION = readDescription(parser);
            }else if (tagName.equals("language")) {
            	LANGUAGE = readLanguage(parser);
            }else if (tagName.equals("copyright")) {
            	COPYRIGHT = readCopy(parser);
            }else if (tagName.equals("link")) {
            	LINK = readLink(parser);
            }else if (tagName.equals("pubDate")) {
            	PUB_DATE = readPubDate(parser);
            }
            if (tagName.equals("item")) {
            	while(parser.next() != XmlPullParser.END_DOCUMENT)
            	{
            		if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
            		String name = parser.getName();
                    if (name.equals("title")) {
                        title = readTitle(parser);
                    }else if (name.equals("link")) {
                        link = readLink(parser);
                    }else if (name.equals("description")) {
                        desc = readDescription(parser);
                    }else if (name.equals("author")) {
                        author = readAuthor(parser);
                    }else if (name.equals("pubDate")) {
                        pubDate = readPubDate(parser);
                    }else if (name.equals("guid")) {
                        guid = readGuid(parser);
                    }
                    if (title != null && link != null && desc != null && desc != dRepeatCheck ) {
                    	FeedMessage item = new FeedMessage(title , desc , link , author , pubDate , guid);
                        items.add(item);//add msg in list
                        dRepeatCheck = desc.toString();
                        title = null;
                        link = null;
                        item = null;
                    }
            	}
            }

            
        }
        feedValues = new Feed(TITLE, LINK, DESCRIPTION, LANGUAGE, COPYRIGHT, PUB_DATE);
        feedValues.setEntries(items);
        return feedValues;
    }
	private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }
 
    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }
    private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String desc = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return desc;
    }
    private String readAuthor(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "author");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "author");
        return author;
    }
    private String readGuid(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "guid");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "guid");
        return guid;
    }
    private String readLanguage(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "language");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "language");
        return guid;
    }
    private String readCopy(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "copyright");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "copyright");
        return guid;
    }
    private String readPubDate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return guid;
    }
// For the tags title and link, extract their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}

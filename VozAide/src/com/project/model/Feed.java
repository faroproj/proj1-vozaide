package com.project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Feed{

  String title;
  final String link;
  final String description;
  final String language;
  final String copyright;
  final String pubDate;

  List<FeedMessage> entries = new ArrayList<FeedMessage>();

  public Feed(String title, String link, String description, String language,
      String copyright, String pubDate) {
    this.title = title;
    this.link = link;
    this.description = description;
    this.language = language;
    this.copyright = copyright;
    this.pubDate = pubDate;
  }

  public void setEntries(List<FeedMessage> l){
	  this.entries = l;
  }
  
  public List<FeedMessage> getEntries() {
	return entries;
}


public List<FeedMessage> getMessages() {
    return entries;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
	    this.title = title;
	  }
  public String getLink() {
    return link;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public String getCopyright() {
    return copyright;
  }

  public String getPubDate() {
    return pubDate;
  }
}
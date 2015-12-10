package com.project.model;

public class FeedMessage {

  String title;
  String description;
  String link;
  String author;
  String guid;
  String pubDate;
  
  public FeedMessage(String title, String description, String link, String author,String pubDate, String guid) {
	super();
	this.title = title;
	this.description = description;
	this.link = link;
	this.author = author;
	this.guid = guid;
	this.pubDate = pubDate;
}

public String getPubDate() {
	return pubDate;
}

public void setPubDate(String pubDate) {
	this.pubDate = pubDate;
}

public FeedMessage(String title, String link , String desc) {
	super();
	this.title = title;
	this.link = link;
	this.description = desc;
}

public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

}
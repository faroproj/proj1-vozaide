package com.project.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;
public class FetchFeed extends AsyncTask<String , Void , Feed>{

	@Override
	protected Feed doInBackground(String... urls) {
		String[] url = urls;
		Feed feed = null;
		XMLParser parser = new XMLParser();
		
		if(url[0].matches("^http.*")){
		try {
			InputStream in = getInputStream(url[0]);
			feed = parser.parse(in);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}else {
			File file = new File(url[0].toString());
			try {
				InputStream fileInputStream = new FileInputStream(file);
				feed = parser.parse(fileInputStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feed;
	}
	
	public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            InputStream in = url.openConnection().getInputStream();
            return in;
        } catch (IOException e) {
            return null;
        }
    }
	@Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
	@Override
	protected void onPostExecute(Feed f) {
    }
}

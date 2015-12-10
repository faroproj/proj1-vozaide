package com.project.model;

import android.content.Context;
import android.util.Log;

public class TextFormattor {
	public static String removeAds(String str , Context context)
	{
		String[] splitString = str.split("<br");
		if(splitString[0].toString().isEmpty())
		{
			splitString[0] = "Description is not available.";
		}
		
		return splitString[0].toString();
	}
}

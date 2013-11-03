package com.officialwebsite.traveltogether.Data;

import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

public class RouteData {
	public static String COLUMN_ID = "id";
	public static String COLUMN_START_TIME = "starttime";
	public static String COLUMN_ACTIVITY_NAME = "activityname";
	public static String COLUMN_LOCATION_NAME = "locationname";
	public static String COLUMN_PICTURE = "picture";
	
	public String id;
	public String startTime;
	public String activityName;
	public String locationName;
	public String picture;
	
	public void setProperty(String propertyName, String value){
		propertyName = propertyName.toLowerCase(Locale.US);
		if (propertyName.equals(COLUMN_ID.toLowerCase(Locale.US))) {
			id = value;
		} else if (propertyName.equals(COLUMN_START_TIME.toLowerCase(Locale.US))) {
			startTime = value;
		} else if (propertyName.equals(COLUMN_ACTIVITY_NAME.toLowerCase(Locale.US))) {
			activityName = value;
		} else if (propertyName.equals(COLUMN_LOCATION_NAME.toLowerCase(Locale.US))) {
			locationName = value;
		} else if (propertyName.equals(COLUMN_PICTURE.toLowerCase(Locale.US))) {
			picture = value;
		} else {
			Log.d("SpreadsheetHelper", "invalid column " + propertyName);
		}
	}
	
	public HashMap <String, String> getRecordsMap(){
		HashMap <String, String> records = new HashMap <String, String>();
		if (id != null) { 
			records.put(COLUMN_ID, id);
		}
		if (startTime != null) { 
			records.put(COLUMN_START_TIME, startTime);
		}
		if (activityName != null) {
			records.put(COLUMN_ACTIVITY_NAME, activityName);
		}
		if (locationName != null) {
			records.put(COLUMN_LOCATION_NAME, locationName);
		}
		if (picture != null) {
			records.put(COLUMN_PICTURE, picture);
		}
		
		return records;
	}
	
	public void print(){
		Log.d("SpreadsheetHelper", "[RouteData] id = " + id + ", startTime = " + startTime
				+ ", activityName = " + activityName
				+ ", locationName = " + locationName + ", picture = " + picture);
	}
}

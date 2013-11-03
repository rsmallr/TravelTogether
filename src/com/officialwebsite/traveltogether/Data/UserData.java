package com.officialwebsite.traveltogether.Data;

import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

public class UserData {
	public static String COLUMN_ID = "id";
	public static String COLUMN_NAME = "name";
	public static String COLUMN_PICTURE = "picture";
	
	public String id;
	public String name;
	public String picture;
	
	public void setProperty(String propertyName, String value){
		propertyName = propertyName.toLowerCase(Locale.US);
		if (propertyName.equals(COLUMN_ID.toLowerCase(Locale.US))) {
			id = value;
		} else if (propertyName.equals(COLUMN_NAME.toLowerCase(Locale.US))) {
			name = value;
		} else if (propertyName.equals(COLUMN_PICTURE.toLowerCase(Locale.US))) {
			picture = value;
		}
	}
	
	public HashMap <String, String> getRecordsMap(){
		HashMap <String, String> records = new HashMap <String, String>();
		if (id != null) {
			records.put(COLUMN_ID, id);
		}
		if (name != null) {
			records.put(COLUMN_NAME, name);
		}
		if (picture != null) {
			records.put(COLUMN_PICTURE, picture);
		}
		
		return records;
	}
	
	public void print(){
		Log.d("SpreadsheetHelper", "[UserData] id = " + id + ", name = " + name + "," +
				" picture = " + picture);
	}
}

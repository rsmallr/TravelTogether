package com.officialwebsite.traveltogether.Data;

import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

public class TravelData {
	public static String COLUMN_ID = "id";
	public static String COLUMN_PEOPLE_ID = "peopleid";
	public static String COLUMN_NAME = "name";
	public static String COLUMN_START_LOCATION = "startlocation";
	public static String COLUMN_TRAVEL_LOCATION = "travellocation";
	public static String COLUMN_START_TIME = "starttime";
	public static String COLUMN_END_TIME = "endtime";
	public static String COLUMN_BUDGET = "budget";
	public static String COLUMN_ROUTE_ID = "routeid";
	public static String COLUMN_VEHICLE = "vehicle";
	public static String COLUMN_PICTURE = "picture";
	
	public String id;
	public String travelName;
	public String startLocation;
	public String travelLocation;
	public String peopleId;
	public String startTime;
	public String endTime;
	public String budget;
	public String routeID;
	public String vehicle;
	public String picture;
	
	public void setProperty(String propertyName, String value){
		if (propertyName.equals(COLUMN_ID.toLowerCase(Locale.US))) {
			id = value;
		} else if (propertyName.equals(COLUMN_PEOPLE_ID.toLowerCase(Locale.US))) {
			peopleId = value;
		} else if (propertyName.equals(COLUMN_NAME.toLowerCase(Locale.US))) {
			travelName = value;
		} else if (propertyName.equals(COLUMN_START_LOCATION.toLowerCase(Locale.US))) {
			startLocation = value;
		} else if (propertyName.equals(COLUMN_TRAVEL_LOCATION.toLowerCase(Locale.US))) {
			travelLocation = value;
		} else if (propertyName.equals(COLUMN_START_TIME.toLowerCase(Locale.US))) {
			startTime = value;
		} else if (propertyName.equals(COLUMN_END_TIME.toLowerCase(Locale.US))) {
			endTime = value;
		} else if (propertyName.equals(COLUMN_BUDGET.toLowerCase(Locale.US))) {
			budget = value;
		} else if (propertyName.equals(COLUMN_ROUTE_ID.toLowerCase(Locale.US))) {
			routeID = value;
		} else if (propertyName.equals(COLUMN_VEHICLE.toLowerCase(Locale.US))) {
			vehicle = value;
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
		if (peopleId != null) {
			records.put(COLUMN_PEOPLE_ID, peopleId);
		}
		if (travelName != null) {
			records.put(COLUMN_NAME, travelName);
		}
		if (startLocation != null) {
			records.put(COLUMN_START_LOCATION, startLocation);
		}
		if (travelLocation != null) {
			records.put(COLUMN_TRAVEL_LOCATION, travelLocation);
		}
		if (startTime != null) {
			records.put(COLUMN_START_TIME, startTime);
		}
		if (endTime != null) {
			records.put(COLUMN_END_TIME, endTime);
		}
		if (budget != null) {
			records.put(COLUMN_BUDGET, budget);
		}
		if (routeID != null) {
			records.put(COLUMN_ROUTE_ID, routeID);
		}
		if (vehicle != null) {
			records.put(COLUMN_VEHICLE, vehicle);
		}
		if (picture != null) {
			records.put(COLUMN_PICTURE, picture);
		}
		
		return records;
	}
	
	public void print(){
		Log.d("SpreadsheetHelper", "[TravelData] id = " + id + ", peopleId = " + peopleId + ", "
				+ ", travelName = " + travelName + ", startLocation = " + startLocation
				+ ", travelLocation = " + travelLocation + " startTime = " + startTime
				+ ", endTime = " + endTime + ", budget = " + budget
				+ ", routeID = " + routeID + ", vehicle = " + vehicle + ", picture = " + picture);
	}
}

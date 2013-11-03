package com.officialwebsite.traveltogether;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TravelData;
import com.officialwebsite.traveltogether.Data.UserData;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.pras.WorkSheet;
import com.pras.WorkSheetCell;
import com.pras.WorkSheetRow;
import com.pras.conn.HttpConHandler;

public class SpreadsheetHelper {
	private static String ACCOUNT = "xxxxxxxx@gmail.com";
	private static String PASSWORD = "xxxxxxxx";
	
	private static SpreadsheetHelper factory;
	private static SpreadSheetFactory spreadSheetFactory;
	private static String spreadSheetKey;
	ArrayList<WorkSheetRow> rows;
	String[] cols;
	
	public static SpreadsheetHelper getInstance(){
		if (factory == null)
			factory = new SpreadsheetHelper();
		return factory;
	}
	
	private SpreadsheetHelper(){
		spreadSheetFactory = SpreadSheetFactory.getInstance(ACCOUNT, PASSWORD);
	}
	
	private static WorkSheet getTravelWorkSheet(){
		return SpreadsheetHelper.getWorkSheetByIndex(0);
	}
	
	private static WorkSheet getRouteWorkSheet(){
		return SpreadsheetHelper.getWorkSheetByIndex(1);
	}
	
	private static WorkSheet getUserWorkSheet(){
		return SpreadsheetHelper.getWorkSheetByIndex(2);
	}
	
	private static WorkSheet getWorkSheetByIndex(int index){
		ArrayList<SpreadSheet> sps = spreadSheetFactory.getAllSpreadSheets(true, "TravelTogether", true);
    	for(int i=0; i<sps.size(); i++){
    		SpreadSheet sp = sps.get(i);
//    		Log.d("SpreadsheetHelper", "SpreadsheetHelper sp title = " + sp.getTitle());
    	}
    	
		SpreadSheet sp = sps.get(0);
		spreadSheetKey = sp.getKey();
//		Log.d("SpreadsheetHelper", "spreadSheetKey = " + spreadSheetKey);
    	
    	ArrayList<WorkSheet> workSheets = sp.getAllWorkSheets();
    	
    	for(int i=0; i<workSheets.size(); i++){
    		WorkSheet wk = workSheets.get(i);
//    		Log.d("SpreadsheetHelper", "SpreadsheetHelper wk title = " + wk.getTitle());
    	}
    	
		return sp.getAllWorkSheets().get(index);
	}
	
	public static ArrayList<TravelData> getAllTravels(){    	
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper getAllTravel rows = " + rows + ", cols :" + cols);
		
		StringBuffer record = new StringBuffer();
		ArrayList<TravelData> travels = new ArrayList<TravelData>();
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			record.append("[ Row ID "+ (i + 1) +" ]\n");
			
			ArrayList<WorkSheetCell> cells = row.getCells();
			
			TravelData data = new TravelData();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				data.setProperty(cell.getName(), cell.getValue());
			}
			
			travels.add(data);
		}
		
		return travels;
	}
	
	public static ArrayList<TravelData> getTravelsByUserId(String userId){    	
		ArrayList<TravelData> allTravels = getAllTravels();
		ArrayList<TravelData> userTravels = new ArrayList<TravelData>();
		
		for (int i=0; i<allTravels.size(); i++) {
			String[] peopleIds = allTravels.get(i).peopleId.split(",");
			for (String id : peopleIds) {
				if (id.equals(userId)) {
					userTravels.add(allTravels.get(i));
				}
			}
		}
		
		return userTravels;
	}
	
	private static Date getDate(String dateString){    	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yyyy");
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<TravelData> searchTravels(String name, String startLocation, String travelLocation, String startTime, String endTime, String budget, String vehicle){    	
		ArrayList<TravelData> allTravels = getAllTravels();
		ArrayList<TravelData> results = new ArrayList<TravelData>();
		
		for (int i=allTravels.size()-1; i>=0; i--) {
			boolean isFound = true;
			TravelData data = allTravels.get(i);
			
			if (name != null && data.travelName != null) {
				if (data.travelName.indexOf(name) == -1) {
					isFound = false;
				}
			}
			if (startLocation != null && data.startLocation != null) {
				if (data.startLocation.indexOf(startLocation) == -1) {
					isFound = false;
				}
			}
			if (travelLocation != null && data.travelLocation != null) {
				if (data.travelLocation.indexOf(travelLocation) == -1) {
					isFound = false;
				}
			}
			if (startTime != null && data.startTime != null) {
				Date sourceDate = getDate(startTime);
				Date targetDate = getDate(data.startTime);
				if (sourceDate == null) {
					Log.w("SpreadsheetHelper", "Invalid date format: " + sourceDate);
				}
				if (targetDate == null) {
					Log.w("SpreadsheetHelper", "Invalid date format: " + targetDate);
				}
				
				if (sourceDate != null && targetDate != null) {
					if (sourceDate.getTime() > targetDate.getTime()) {
						isFound = false;
					} 
				}
			}
			if (endTime != null && data.endTime != null) {
				Date sourceDate = getDate(endTime);
				Date targetDate = getDate(data.endTime);
				if (sourceDate == null) {
					Log.w("SpreadsheetHelper", "Invalid date format: " + sourceDate);
				}
				if (targetDate == null) {
					Log.w("SpreadsheetHelper", "Invalid date format: " + targetDate);
				}
				
				if (sourceDate != null && targetDate != null) {
					if (sourceDate.getTime() < targetDate.getTime()) {
						isFound = false;
					} 
				}
			}
			if (budget != null && data.budget != null) {
				if (Integer.valueOf(data.budget) > Integer.valueOf(budget)) {
					isFound = false;
				} 
			}
			if (vehicle != null && data.vehicle != null) {
				if (data.vehicle.indexOf(vehicle) == -1) {
					isFound = false;
				}
			}
			
			if (isFound) {
				results.add(data);
			}
		}
		
		return results;
	}
	
	public static ArrayList<UserData> getAllUsers(){    	
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper getAllUsers rows = " + rows + ", cols :" + cols);
		
		StringBuffer record = new StringBuffer();
		ArrayList<UserData> users = new ArrayList<UserData>();
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			record.append("[ Row ID "+ (i + 1) +" ]\n");
			
			ArrayList<WorkSheetCell> cells = row.getCells();
			
			UserData data = new UserData();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				data.setProperty(cell.getName(), cell.getValue());
			}
			
			users.add(data);
		}
		
		return users;
	}
	
	public static ArrayList<TravelData> getTravelById(String travelId){
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(travelId),null);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper getRoutesById rows = " + rows + ", cols :" + cols);
		
		StringBuffer record = new StringBuffer();
		ArrayList<TravelData> routes = new ArrayList<TravelData>();
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			record.append("[ Row ID "+ (i + 1) +" ]\n");
			
			ArrayList<WorkSheetCell> cells = row.getCells();
			
			TravelData data = new TravelData();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				data.setProperty(cell.getName(), cell.getValue());
			}
			
			routes.add(data);
		}
		
		return routes;
	}
	
	public static ArrayList<RouteData> getRoutesById(String routeId){
		WorkSheet wk = SpreadsheetHelper.getRouteWorkSheet();
		
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(routeId),null);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper getRoutesById rows = " + rows + ", cols :" + cols);
		
		StringBuffer record = new StringBuffer();
		ArrayList<RouteData> routes = new ArrayList<RouteData>();
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			record.append("[ Row ID "+ (i + 1) +" ]\n");
			
			ArrayList<WorkSheetCell> cells = row.getCells();
			
			RouteData data = new RouteData();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				data.setProperty(cell.getName(), cell.getValue());
			}
			
			routes.add(data);
		}
		
		return routes;
	}
	
	public static UserData getUserById(String userId){
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(userId),null);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper getUserById rows = " + rows + ", cols :" + cols);
		
		StringBuffer record = new StringBuffer();
		if (rows.size() > 0) {
			ArrayList<WorkSheetCell> cells = rows.get(0).getCells();
			
			UserData user = new UserData();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				user.setProperty(cell.getName(), cell.getValue());
			}
			
			return user;
		} else {
			return null;
		}
	}
	
	public void addUserData(UserData data){    	
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		HashMap <String, String> records = data.getRecordsMap();
		wk.addListRow(records);
		Log.d("SpreadsheetHelper", "Add user " + data.name);
	}
	
	public void addTravel(TravelData data){    	
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		HashMap <String, String> records = data.getRecordsMap();
		wk.addListRow(records);
		Log.d("SpreadsheetHelper", "Add travel successfully");
	}
	
	public void deleteTravelById(String travelId){    	
		if (SpreadsheetHelper.spreadSheetKey == null) {
			Log.w("SpreadsheetHelper", "Cannot get spreadSheetKey");
		}
		
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(travelId),null);
		
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			wk.deleteListRow(SpreadsheetHelper.spreadSheetKey, row);
			Log.d("SpreadsheetHelper", "Delete travel " + travelId + " successfully");
		}
	}
	
	public static void addUserToTravel(String travelId, String userId){
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		
		if (SpreadsheetHelper.spreadSheetKey == null) {
			Log.w("SpreadsheetHelper", "Cannot get spreadSheetKey");
		}
		
		ArrayList<WorkSheetRow> rows;
		String[] cols;
		cols = wk.getColumns();
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(travelId),null);
		
		Log.d("SpreadsheetHelper", "SpreadsheetHelper addUserToTravel rows = " + rows + ", cols :" + cols);
		
		if (rows.size() > 0) {
			WorkSheetRow row = rows.get(0);
			ArrayList<WorkSheetCell> cells = row.getCells();
			HashMap <String, String> records = new HashMap <String, String>();
			
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				String value = cell.getValue();
				if (cell.getName().equals(TravelData.COLUMN_PEOPLE_ID.toLowerCase(Locale.US))) {
					value = value + "," +userId;
				}
				
				records.put(cell.getName(), value);
			}

			wk.updateListRow(SpreadsheetHelper.spreadSheetKey, row, records);
		}
	}
	
	public void addRoute(RouteData data){    	
		WorkSheet wk = SpreadsheetHelper.getRouteWorkSheet();
		HashMap <String, String> records = data.getRecordsMap();
		wk.addListRow(records);
		Log.d("SpreadsheetHelper", "Add route successfully");
	}
	
	public void deleteRouteById(String routeId){    	
		if (SpreadsheetHelper.spreadSheetKey == null) {
			Log.w("SpreadsheetHelper", "Cannot get spreadSheetKey");
		}
		
		WorkSheet wk = SpreadsheetHelper.getRouteWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(routeId),null);
		
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			wk.deleteListRow(SpreadsheetHelper.spreadSheetKey, row);
			Log.d("SpreadsheetHelper", "Delete route " + routeId + " successfully");
		}
	}
	
	public void addUser(UserData data){    	
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		HashMap <String, String> records = data.getRecordsMap();
		wk.addListRow(records);
		Log.d("SpreadsheetHelper", "Add user " + data.name + " successfully");
	}
	
	public void deleteUserById(String userId){    	
		if (SpreadsheetHelper.spreadSheetKey == null) {
			Log.w("SpreadsheetHelper", "Cannot get spreadSheetKey");
		}
		
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, HttpConHandler.encode("\"id\"") + "=" + HttpConHandler.encode(userId),null);
		
		for(int i=0; i<rows.size(); i++){
			WorkSheetRow row = rows.get(i);
			wk.deleteListRow(SpreadsheetHelper.spreadSheetKey, row);
			Log.d("SpreadsheetHelper", "Delete user " + userId + " successfully");
		}
	}
	
	public String createNewTravelId(){    	
		WorkSheet wk = SpreadsheetHelper.getTravelWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, null, "column:id");
		
		int newId = -1;
		StringBuffer record = new StringBuffer();
		if (rows.size() > 0) {
			ArrayList<WorkSheetCell> cells = rows.get(rows.size()-1).getCells();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				if (cell.getName().equals(TravelData.COLUMN_ID.toLowerCase(Locale.US))) {
					newId = Integer.valueOf(cell.getValue());
					break;
				}
			}
		}
		
		Log.d("SpreadsheetHelper", "NewTravelId = " + String.valueOf(newId + 1));
		return String.valueOf(newId + 1);
	}
	
	public String createNewRouteId(){    	
		WorkSheet wk = SpreadsheetHelper.getRouteWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, null, "column:id");
		
		int newId = -1;
		StringBuffer record = new StringBuffer();
		if (rows.size() > 0) {
			ArrayList<WorkSheetCell> cells = rows.get(rows.size()-1).getCells();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				if (cell.getName().equals(UserData.COLUMN_ID.toLowerCase(Locale.US))) {
					newId = Integer.valueOf(cell.getValue());
					break;
				}
			}
		}
		
		Log.d("SpreadsheetHelper", "NewRouteId = " + String.valueOf(newId + 1));
		return String.valueOf(newId + 1);
	}
	
	public String createNewUserId(){    	
		WorkSheet wk = SpreadsheetHelper.getUserWorkSheet();
		ArrayList<WorkSheetRow> rows;
		rows = wk.getData(false, false, null, "column:id");
		
		int newId = -1;
		StringBuffer record = new StringBuffer();
		if (rows.size() > 0) {
			ArrayList<WorkSheetCell> cells = rows.get(rows.size()-1).getCells();
			for(int j=0; j<cells.size(); j++){
				WorkSheetCell cell = cells.get(j);
				record.append(cell.getName() +" = "+ cell.getValue() +"\n"); 
				if (cell.getName().equals(UserData.COLUMN_ID.toLowerCase(Locale.US))) {
					newId = Integer.valueOf(cell.getValue());
					break;
				}
			}
		}
		
		Log.d("SpreadsheetHelper", "NewUserId = " + String.valueOf(newId + 1));
		return String.valueOf(newId + 1);
	}
}

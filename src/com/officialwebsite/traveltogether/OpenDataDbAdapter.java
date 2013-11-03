package com.officialwebsite.traveltogether;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.officialwebsite.traveltogether.Data.ICultureOpenData;
import com.officialwebsite.traveltogether.Data.TaipeiOpenData;

public class OpenDataDbAdapter {

	private static OpenDataDbAdapter diaryDbAdapter;

	public static final String TAG = OpenDataDbAdapter.class.getName();

	public static final String _ID = "_id";
	public static final String COUNT 	= "count(*)";
	
	// TaipeiOpenData Table Columns
	public static final String TABLE_TAIPEI = "TaipeiOpenData";
	public static final String stitle 	= "stitle";
	public static final String xbody	= "xbody";
	public static final String info		= "info";
	public static final String address	= "address";
	public static final String img 		= "img";
	
	// iCulture Table Columns
		public static final String TABLE_ICULTURE 			= "iCulture";
		public static final String title					= "title";
		public static final String category					= "category";
		public static final String descriptionFilterHtml 	= "descriptionFilterHtml";
		public static final String imageUrl					= "imageUrl";
		public static final String startDate 				= "startDate";
		public static final String endDate 					= "endDate";
		
		public static final String location 				= "location";
		public static final String locationName 			= "locationName";
		public static final String latitude 				= "latitude";
		public static final String longitude 				= "longitude";

	public static final String CREATE_TABLE_ICULTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_ICULTURE + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			title + " TEXT, " +
			category + " TEXT, " +
			imageUrl + " TEXT, " +
			descriptionFilterHtml + " TEXT, " +
			startDate + " TEXT, " +
			endDate + " TEXT, " +
			location + " TEXT, " +
			locationName + " TEXT, " +
			latitude + " TEXT, " +
			longitude + " TEXT);";

	public static final String CREATE_TABLE_TAIPEI = "CREATE TABLE IF NOT EXISTS " + TABLE_TAIPEI + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			stitle + " TEXT, " +
			xbody + " TEXT, " +
			info + " TEXT, " +
			img + " TEXT, " +
			address + " TEXT);";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public final static String DATABASE_NAME = "OpenDataˊ.db";
		private final static int DATABASE_VERSION = 1;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_ICULTURE);
			db.execSQL(CREATE_TABLE_TAIPEI);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion > oldVersion) {

			}
		}
	}

	private OpenDataDbAdapter(Context context) {
		mDbHelper = new DatabaseHelper(context);
		mDb = mDbHelper.getWritableDatabase();
	}

	public void deleteDB(Context context) {
		context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
		Toast.makeText(context, "Delete Done!", Toast.LENGTH_SHORT).show();
	}
	
	public void browseDB(Activity context) {
		OpenDataHelper t = new OpenDataHelper();
		t.init(context);
	}

	public static OpenDataDbAdapter getInstance(Context context) {
		
		if (diaryDbAdapter == null) {
			diaryDbAdapter = new OpenDataDbAdapter(context);
		}
		
		return diaryDbAdapter;
	}

	public void close()	{
		mDb.close();
		mDbHelper.close();
	}

	public void addICultureData(ArrayList<ICultureOpenData> list) {
		for (ICultureOpenData data : list) {
			addICultureData(data);
		}
	}
	
	public void addICultureData(ICultureOpenData data) {
		ContentValues values = new ContentValues();
		
		values.put(category, data.category);
		values.put(descriptionFilterHtml, data.xbody);
		values.put(endDate, data.endDate);
		values.put(imageUrl, data.imageUrl);
		values.put(latitude, data.latitude);
		values.put(location, data.address);
		values.put(locationName, data.locationName);
		values.put(longitude, data.longitude);
		values.put(startDate, data.startDate);
		values.put(title, data.stitle);

		long insert = mDb.insert(TABLE_ICULTURE, null, values);
		Log.d(TAG, "addICultureData() insert: " + insert);
}

	public void addTaipeiOpenData(ArrayList<TaipeiOpenData> list) {

		for (int i = 0; i < list.size(); i++) {
			addTaipeiOpenData(list.get(i));
		}
	}

	public void addTaipeiOpenData(TaipeiOpenData data) {
			ContentValues values = new ContentValues();
			values.put(stitle, data.stitle);
			values.put(xbody, data.xbody);
			values.put(info, data.info);
			values.put(address, data.address);
			values.put(img, data.imageUrl);

			long insert = mDb.insert(TABLE_TAIPEI, null, values);

			Log.d(TAG, "addTaipeiOpenData() insert: " + insert);
	}
	
	public ArrayList<TaipeiOpenData> searchTaipeiOpenDataByKeyword(String keyword) {
		ArrayList<TaipeiOpenData> list = new ArrayList<TaipeiOpenData>();

		String[] columns = {_ID, stitle, xbody, info, address, img};

		Cursor cursor = mDb.query(TABLE_TAIPEI, columns, address+" LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null);

		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();

			do {
				TaipeiOpenData data = new TaipeiOpenData();
				data.stitle 	= cursor.getString(cursor.getColumnIndex(stitle));
				data.xbody 		= cursor.getString(cursor.getColumnIndex(xbody));
				data.info 		= cursor.getString(cursor.getColumnIndex(info));
				data.address	= cursor.getString(cursor.getColumnIndex(address));
				data.imageUrl	= cursor.getString(cursor.getColumnIndex(img));

				list.add(data);

			} while (cursor.moveToNext());
		}
		cursor.close();

		return list;
	}
	
	public ArrayList<ICultureOpenData> searchICultureOpenDataWithCategoryIdAndLocation(String categoryId, String keyLocation) {
		ArrayList<ICultureOpenData> list = new ArrayList<ICultureOpenData>();

		String[] columns = {_ID, category, descriptionFilterHtml, endDate,
							imageUrl, latitude, location, locationName, longitude,
							startDate, title};

		Cursor cursor = mDb.query(TABLE_ICULTURE, columns, location+" LIKE ? AND " + category + " = ? ", new String[]{"%" + keyLocation + "%", categoryId}, null, null, null);

		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();

			do {
				ICultureOpenData data = new ICultureOpenData();
				data.category 	= cursor.getString(cursor.getColumnIndex(category));
				data.xbody 		= cursor.getString(cursor.getColumnIndex(descriptionFilterHtml));
				data.endDate 		= cursor.getString(cursor.getColumnIndex(endDate));
				data.imageUrl	= cursor.getString(cursor.getColumnIndex(imageUrl));
				data.latitude 	= cursor.getString(cursor.getColumnIndex(latitude));
				data.address 		= cursor.getString(cursor.getColumnIndex(location));
				data.locationName 		= cursor.getString(cursor.getColumnIndex(locationName));
				data.longitude	= cursor.getString(cursor.getColumnIndex(longitude));
				data.startDate	= cursor.getString(cursor.getColumnIndex(startDate));
				data.stitle	= cursor.getString(cursor.getColumnIndex(title));

				list.add(data);

			} while (cursor.moveToNext());
		}
		cursor.close();

		return list;
	}

	public int getTotalTaipeiOpenDataCount()	{
		String[] columns = { _ID };
		Cursor cursor = mDb.query(TABLE_TAIPEI, columns, null, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	public int getCountOfTaipeiOpenDataByAddress(String keyword) {
		String[] columns = { COUNT, _ID, address };
		Cursor cursor = mDb.query(TABLE_TAIPEI, columns, address + " LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null);

		int count = cursor.getCount();
		cursor.close();

		return count;
	}
	
	public int getTotalICultureOpenDataCount()	{
		String[] columns = { _ID };
		Cursor cursor = mDb.query(TABLE_ICULTURE, columns, null, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	public int getCountOfICultureOpenDataByAddress(String keyword) {
		String[] columns = { COUNT, _ID, location };
		Cursor cursor = mDb.query(TABLE_ICULTURE, columns, location + " LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null);

		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	public String convertDateToFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	public Date convertStringToDate(String dateString) {
		int year = Integer.valueOf(dateString.substring(0, 4));
		int month = Integer.valueOf(dateString.substring(4, 6));
		int day = Integer.valueOf(dateString.substring(6, 8));

		return new Date(year-1900, month - 1, day);
	}

}

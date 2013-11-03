package com.officialwebsite.traveltogether;

import java.util.HashMap;
import java.util.List;

import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TravelData;
import com.officialwebsite.traveltogether.Data.UserData;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TravelListView extends Activity {
	
	public static String mSearchUserId;
	public static String mSearchName;
	public static String mSearchStartLocation;
	public static String mSearchTravelLocation;
	public static String mSearchStartTime;
	public static String mSearchEndTime;
	public static String mSearchBudget;
	public static String mSearchVehicle;
	
	private Typeface mTypeface;
	private ListView mMyTravelListView;
	private List<TravelData> mMyTravelDataList;
	private List<UserData> mUserDataList;
	private Activity mThis = this;
	private HashMap<String, Bitmap> mImageHashMap = new HashMap<String, Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.my_travel);

    	mTypeface = Typeface.createFromAsset(TravelListView.this.getAssets(), "fonts/pen.ttf");
    	
    	mMyTravelListView = (ListView) findViewById(R.id.my_travel_list);
    	mMyTravelListView.setAdapter(mMyTravelListAdapter);
    	mMyTravelListView.setOnItemClickListener(mOnItemClickListener);
    	
        // Sample code to read SpreadSheet from Google server
        // Do this in non-UI thread!
    	Utility.showHideLoadingView(mThis, true);
        runOnUiThread(new Runnable(){
        	public void run(){
        		new MyTask().execute();
        	}
        });
    }
	
	public List<TravelData> retrieveMyTravelDataList() {
		return testTravelData.getTestTravelData1();
	}

    private BaseAdapter mMyTravelListAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			if (mMyTravelDataList == null)
				return 0;
			else
				return mMyTravelDataList.size();
		}

		@Override
		public Object getItem(int position) {
			if (mMyTravelDataList == null)
				return null;
			else
				return mMyTravelDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if (null == view) {
				view = LayoutInflater.from(TravelListView.this).inflate(R.layout.my_travel_list_item, null);
				
				((TextView) view.findViewById(R.id.textTravleTitle)).setTypeface(mTypeface, Typeface.BOLD);
				((TextView) view.findViewById(R.id.textOrganizer)).setTypeface(mTypeface, Typeface.BOLD);
				((TextView) view.findViewById(R.id.textTravelTime)).setTypeface(mTypeface, Typeface.BOLD);
				((TextView) view.findViewById(R.id.textTravelLocation)).setTypeface(mTypeface, Typeface.BOLD);
				((TextView) view.findViewById(R.id.textPartner)).setTypeface(mTypeface, Typeface.BOLD);
			}
			
			TravelData travel = mMyTravelDataList.get(position);
			((TextView) view.findViewById(R.id.textTravleTitle)).setText(travel.travelName);
			((TextView) view.findViewById(R.id.textTravelTime)).setText(getString(R.string.startTime_) + travel.startTime + "  " + getString(R.string.endTime_) + travel.endTime);
			((TextView) view.findViewById(R.id.textTravelLocation)).setText(getString(R.string.from) + travel.startLocation + getString(R.string.departure) + getString(R.string.arrival) + travel.travelLocation);
			((TextView) view.findViewById(R.id.textPartner)).setText(getString(R.string.partner_) + getUserNameList(travel.peopleId));
			((TextView) view.findViewById(R.id.textOrganizer)).setText(getString(R.string.organizer_) + getFirstUserName(travel.peopleId));
			
			Bitmap image = null;
			if (travel.picture != null && !travel.picture.isEmpty()) {
				image = mImageHashMap.get(travel.picture);
			}
			if (image == null) {
				((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.route_default);
			} else {
				((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(image);
			}
			
			return view;
		}
    	
    };
    
    private String getFirstUserName(String userIds) {
    	String[] userIdArray = userIds.split(",");
    	
    	if (userIdArray.length > 0) {
    		String userId = userIdArray[0];
    		
    		for (UserData user : mUserDataList) {
    			if (user.id.equals(userId)) {
    				return user.name;
    			}
    		}
    	}
    	
    	return "";
    }
    
    private String getUserNameList(String userIds) {
    	StringBuilder result = null;
    	String[] userIdArray = userIds.split(",");
    	for (String userId : userIdArray) {
    		
    		for (UserData user : mUserDataList) {
    			if (user.id.equals(userId)) {
    				if (result == null) {
    					result = new StringBuilder();
    					result.append(user.name);
    				}
    				else {
    					result.append(getString(R.string.serial_comma));
    					result.append(user.name);
    				}
    			}
    		}
    	}
    	return (result == null ? "" : result.toString());
    }
    
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			TravelDetailView.mSelectTravelData = mMyTravelDataList.get(position);
			TravelDetailView.mUserDataList = mUserDataList;
			
			Intent intent = new Intent();
			intent.setClass(TravelListView.this, TravelDetailView.class);
			TravelListView.this.startActivity(intent);
			
		}
		
	};
	
	private class MyTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... params) {
			SpreadsheetHelper helper = SpreadsheetHelper.getInstance();
			
			if (mSearchUserId != null) {
				mMyTravelDataList = helper.getTravelsByUserId(mSearchUserId);
			} else {
				mMyTravelDataList = helper.searchTravels(mSearchName, mSearchStartLocation, mSearchTravelLocation, mSearchStartTime, mSearchEndTime, mSearchBudget, mSearchVehicle);
			}
			
			for (TravelData travel : mMyTravelDataList) {
				if (travel.picture != null && !travel.picture.isEmpty()) {
					Bitmap image = Utility.loadBitmap(travel.picture);
					mImageHashMap.put(travel.picture, image);
				}
			}
			
			mUserDataList = helper.getAllUsers();
//			ArrayList<RouteData> datas = helper.getRoutesById(travelData.get(0).routeID);
			
			TravelListView.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mMyTravelListAdapter.notifyDataSetChanged();
				}
			});
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Utility.showHideLoadingView(mThis, false);
	    }
	}
}

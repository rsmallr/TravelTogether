package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TravelData;
import com.officialwebsite.traveltogether.Data.UserData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TravelDetailView extends Activity {
	
	public static TravelData mSelectTravelData;
	public static List<UserData> mUserDataList;
	
	private Typeface mTypeface;
	private ArrayList<RouteData> mRouteDataList;
	private Button mButtonJoin;
	private Activity mThis = this;
	private HashMap<String, Bitmap> mImageHashMap = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_detail);
        
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/pen.ttf");
		((TextView) findViewById(R.id.textTravleTitle)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textOrganizer)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textTravelTime)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textTravelLocation)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textPartner)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textTravelVehicle)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textTravelBudget)).setTypeface(mTypeface, Typeface.BOLD);
		((TextView) findViewById(R.id.textRouterListTitle)).setTypeface(mTypeface, Typeface.BOLD);
		((Button) findViewById(R.id.button_join)).setTypeface(mTypeface, Typeface.BOLD);
		
		mButtonJoin = ((Button) findViewById(R.id.button_join));
		mButtonJoin.setOnClickListener(mOnJoinClickListener);
        
		updateTravelData();
    	
    	Utility.showHideLoadingView(mThis, true);
        runOnUiThread(new Runnable(){
        	public void run(){
        		new MyTask().execute();
        	}
        });
    }
    
    private boolean isJoined() {
    	if (mSelectTravelData == null)
    		return false;
    	
    	String userIds = mSelectTravelData.peopleId;
    	String[] userIdArray = userIds.split(",");
    	for (String userId : userIdArray) {
    		if (userId.endsWith(Utility.USER_ID))
    			return true;
    	}
    	
    	return false;
    }
    
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
	
	private class MyTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... params) {
			SpreadsheetHelper helper = SpreadsheetHelper.getInstance();
			
			mRouteDataList = helper.getRoutesById(mSelectTravelData.routeID);
			
			for (RouteData route : mRouteDataList) {
				if (route.picture != null && !route.picture.isEmpty()) {
					Bitmap image = Utility.loadBitmap(route.picture);
					mImageHashMap.put(route.picture, image);
				}
			}
			
			TravelDetailView.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateRouteList();
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
	
	private void updateTravelData() {
		TravelData travel = mSelectTravelData;
		if (travel != null) {
			((TextView) findViewById(R.id.textTravleTitle)).setText(travel.travelName);
			((TextView) findViewById(R.id.textTravelTime)).setText(getString(R.string.startTime_) + travel.startTime + "  " + getString(R.string.endTime_) + travel.endTime);
			((TextView) findViewById(R.id.textTravelLocation)).setText(getString(R.string.from) + travel.startLocation + getString(R.string.departure) + getString(R.string.arrival) + travel.travelLocation);
			((TextView) findViewById(R.id.textPartner)).setText(getString(R.string.partner_) + getUserNameList(travel.peopleId));
			((TextView) findViewById(R.id.textOrganizer)).setText(getString(R.string.organizer_) + getFirstUserName(travel.peopleId));
			((TextView) findViewById(R.id.textTravelVehicle)).setText(getString(R.string.vehicle_) + travel.vehicle);
			((TextView) findViewById(R.id.textTravelBudget)).setText(getString(R.string.budget_) + travel.budget);

			mButtonJoin.setVisibility(isJoined() ? View.INVISIBLE : View.VISIBLE);
		}
	}
	
	private void updateRouteList() {
		ViewGroup viewRouteList = ((ViewGroup) findViewById(R.id.viewRouteList));
		
		for (RouteData route : mRouteDataList) {

			View view = LayoutInflater.from(this).inflate(R.layout.travel_detail_route_item, null);
			
			((TextView) view.findViewById(R.id.textRouteTime)).setTypeface(mTypeface, Typeface.BOLD);
			((TextView) view.findViewById(R.id.textRouteActivityName)).setTypeface(mTypeface, Typeface.BOLD);
			((TextView) view.findViewById(R.id.textRouteName)).setTypeface(mTypeface, Typeface.BOLD);
			((TextView) view.findViewById(R.id.textRouteAddress)).setTypeface(mTypeface, Typeface.BOLD);

			((TextView) view.findViewById(R.id.textRouteTime)).setText(route.startTime);
			((TextView) view.findViewById(R.id.textRouteActivityName)).setText(route.activityName);
			((TextView) view.findViewById(R.id.textRouteName)).setText(route.locationName);
			
			Bitmap image = null;
			if (route.picture != null && !route.picture.isEmpty()) {
				image = mImageHashMap.get(route.picture);
			}
			if (image == null) {
				((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.route_default);
			} else {
				((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(image);
			}
			
			viewRouteList.addView(view);
		}
	}

	private OnClickListener mOnJoinClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
	    	Utility.showHideLoadingView(mThis, true);
    		new JoinTask().execute();
		}
		
	};
	
	private class JoinTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... params) {
			SpreadsheetHelper helper = SpreadsheetHelper.getInstance();
			
			helper.addUserToTravel(mSelectTravelData.id, Utility.USER_ID);
			
			// Update current travel data
			ArrayList<TravelData> newTravels = helper.getTravelById(mSelectTravelData.id);
			if (newTravels != null && !newTravels.isEmpty()) {
				mSelectTravelData = newTravels.get(0);
			}
			
			TravelDetailView.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateTravelData();
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

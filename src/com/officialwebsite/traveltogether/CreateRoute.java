package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.officialwebsite.traveltogether.CreateTravel.ListData;

//import com.officialwebsite.traveltogether.CreateTravel.ListData;

import com.officialwebsite.traveltogether.Data.ICultureOpenData;
import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TaipeiOpenData;
import com.officialwebsite.traveltogether.Data.TravelData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CreateRoute extends Activity{

	
//	class ListData{
//		public String main;
//		public String sub;
//	}
	
	public static Map<String, String> mTravelMap = new HashMap<String, String>();
	private CLViewPager ctViewPager;  
	private CLPagerAdapter cdPagerAdapter; 
	private LayoutInflater mInflater; 
	
	private TravelAdapter mRouteAdapter;
	private DescriptionAdapter mDataBaseAdapter;
	private DescriptionAdapter mAttractionAdapter;
	private DescriptionAdapter mActivityAdapter;
	private Animation mSlideOutAnim = null;
	private Typeface mTypeface;
	
	public static TravelData mTravelData; 
	
	private static final String[] mRouteStrings = new String[] {
		 "08:00\n    |\n10:00", "10:00\n    |\n12:00", "12:00\n    |\n14:00", "14:00\n    |\n16:00", "16:00\n    |\n18:00", "18:00\n    |\n20:00", "20:00\n    |\n22:00"};
	
	public static final String[] mDataBaseStrings = new String[] {
		 "台北open data", "iCulture"};
	
	public static final String[] mActiveTypeStrings = new String[] {
		 "景點", "活動"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mTypeface = Typeface.createFromAsset(getAssets(), "fonts/pen.ttf"); 
		
		setContentView(R.layout.create_travel);
		
		mInflater = getLayoutInflater(); 
		
		ctViewPager = new CLViewPager(this);
		ctViewPager.setPageMargin((int) (-800));
		
		initialListView();
		
		ViewGroup routeGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_grouplist, null));
		ListView groupList = (ListView)routeGroup.findViewById(R.id.group_listview);
		groupList.setAdapter(mRouteAdapter);
		
		
//		groupList.setDivider(null);
		groupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				ctViewPager.enableAppearingAnimator(1000);
				onItemSelect(view, pos);
			}
		});
		
		
		cdPagerAdapter = new CLPagerAdapter(this);
		cdPagerAdapter.addView(routeGroup);
		
//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view1, null));
//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view2, null));
//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view3, null));
		
        ctViewPager.setAdapter(cdPagerAdapter);
        ctViewPager.setCurrentItem(0, true);
        
//        ListView mainList = new ListView(this);//(ListView) findViewById(R.id.group_listview);
        
        Button nextBtn = (Button) routeGroup.findViewById(R.id.groupt_btn);
		nextBtn.setText("成團~!");
		nextBtn.setTypeface(mTypeface, Typeface.BOLD);
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("","groupt_btn");
				
				runOnUiThread(new Runnable(){
					public void run(){
						new MyTask().execute();
					}
				});
			}
		});
        
        ((ViewGroup)findViewById(R.id.rootLayout)).addView(ctViewPager);
        
//		for(int i=0;i<CreateTravel.mTravelStrings.length;i++){
//			String title = CreateTravel.mTravelMap.get(CreateTravel.mTravelStrings[i]);
//			Log.e("wula", "title: "+title);
//		}
	}
	
	public void initialListView(){
		
		ArrayList<ListData> routeArrayList = new ArrayList<ListData>();
		
		
		for(int i=0;i<mRouteStrings.length;i++){
			ListData listdata = new ListData();
			listdata.main = mRouteStrings[i];
			routeArrayList.add(listdata);
		}
		mRouteAdapter = new TravelAdapter(this, routeArrayList);
		
		ArrayList<ListData> databaseArrayList = new ArrayList<ListData>();  
		
		for(int i=0;i<mDataBaseStrings.length;i++){
			ListData listdata = new ListData();
			listdata.location = mDataBaseStrings[i];
			if(i == 0)
				listdata.image_id = R.drawable.infomation_logo;
			else if(i == 1)
				listdata.image_id = R.drawable.culture_logo;
			databaseArrayList.add(listdata);
		}
		mDataBaseAdapter = new DescriptionAdapter(this, databaseArrayList);
//		
		
		
		
	}
	
	public void onItemSelect(final View rootView, final int rootPos){
//		ctViewPager.setPageMargin((int) (-1000));
		ViewGroup locateGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaultlist, null));
		ListView locateList = (ListView)locateGroup.findViewById(R.id.default_listview);
		locateList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String selectitem = (String) ((TextView)arg1.findViewById(R.id.location)).getText();
					if(selectitem.equals(mDataBaseStrings[0])){
						//景點 --> taipei open data
						Log.e("wula", "getTaipeiOpenData");
						getTaipeiOpenData(rootView, rootPos);
						
					}
					else if(selectitem.equals(mDataBaseStrings[1])){
						//活動 --> iculture
						getICulture(rootView, rootPos);
					}
					
					
				}
			});
			locateList.setAdapter(mDataBaseAdapter);
		cdPagerAdapter.addView(locateGroup);
		ctViewPager.setCurrentItem(0, true);
		cdPagerAdapter.notifyDataSetChanged();
		
	}
	
	public void actAnimation(){
		if(mSlideOutAnim != null)
			mSlideOutAnim.reset();
		mSlideOutAnim = new TranslateAnimation(0,800,0,0);
		mSlideOutAnim.setAnimationListener(new Animation.AnimationListener(){
	
			@Override
			public void onAnimationEnd(Animation animation) {
				cdPagerAdapter.removeView(ctViewPager, 2);
				cdPagerAdapter.removeView(ctViewPager, 1);
			}
	
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
	
			@Override
			public void onAnimationStart(Animation animation) {
			}
				
		});
//		setBusy(true);
		mSlideOutAnim.setDuration(500);
		mSlideOutAnim.setRepeatCount(0);
		ctViewPager.getChildAt(1).setAnimation(mSlideOutAnim);
		mSlideOutAnim.startNow();
	}
	
	public void getTaipeiOpenData(final View view, final int pos){
		String[] Attractionitem = mActiveTypeStrings;
			
//		Activityitem = getJackie
		ArrayList<TaipeiOpenData> taipeiopendata = OpenDataDbAdapter.getInstance(this).searchTaipeiOpenDataByKeyword("臺北");
		ArrayList<ListData> AttractionArrayList = new ArrayList<ListData>();  
		for(int i=0;i<taipeiopendata.size();i++){
			ListData listdata = new ListData();
			listdata.title = taipeiopendata.get(i).stitle;
			listdata.location = taipeiopendata.get(i).address;
			listdata.locationName = taipeiopendata.get(i).stitle;
			listdata.description = taipeiopendata.get(i).xbody;
			listdata.image_uri = taipeiopendata.get(i).imageUrl;
			AttractionArrayList.add(listdata);
		}
		mAttractionAdapter = new DescriptionAdapter(this, AttractionArrayList);
		
		ViewGroup Group = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaultlist, null));
		ListView List = (ListView)Group.findViewById(R.id.default_listview);
		List.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String text = (String) ((TextView)arg1.findViewById(R.id.title)).getText();
					((TextView)view.findViewById(R.id.selectName)).setText(text);
					
					

					((ListData)mRouteAdapter.getItem(pos)).sub = text;
					((ListData)mRouteAdapter.getItem(pos)).locationName = ((ListData)mAttractionAdapter.getItem(arg2)).locationName;
					((ListData)mRouteAdapter.getItem(pos)).image_uri = ((ListData)mAttractionAdapter.getItem(arg2)).image_uri;
					ctViewPager.setCurrentItem(0, true);
					actAnimation();
				}
			});
		List.setAdapter(mAttractionAdapter);
		cdPagerAdapter.addView(Group);
		ctViewPager.setCurrentItem(1, true);
		
		cdPagerAdapter.notifyDataSetChanged();
		
	}
	
	public void getICulture(final View view, final int pos){
		String[] Activityitem = mActiveTypeStrings;
		
//		Activityitem = getTim
		ArrayList<ICultureOpenData> iculture = OpenDataDbAdapter.getInstance(this).searchICultureOpenDataWithCategoryIdAndLocation("2", "台北");
		ArrayList<ListData> ActivityArrayList = new ArrayList<ListData>();  
		for(int i=0;i<iculture.size();i++){
			ListData listdata = new ListData();
			listdata.title = iculture.get(i).stitle;
			listdata.location = iculture.get(i).address;
			listdata.locationName = iculture.get(i).locationName;
			listdata.description = iculture.get(i).xbody;
			listdata.image_uri = iculture.get(i).imageUrl;
			ActivityArrayList.add(listdata);
		}
		mActivityAdapter = new DescriptionAdapter(this, ActivityArrayList);
		
		ViewGroup Group = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaultlist, null));
		ListView List = (ListView)Group.findViewById(R.id.default_listview);
		List.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String text = (String) ((TextView)arg1.findViewById(R.id.title)).getText();
					((TextView)view.findViewById(R.id.selectName)).setText(text);
					((ListData)mRouteAdapter.getItem(pos)).sub = text;
					((ListData)mRouteAdapter.getItem(pos)).locationName = ((ListData)mActivityAdapter.getItem(arg2)).locationName;
					((ListData)mRouteAdapter.getItem(pos)).image_uri = ((ListData)mActivityAdapter.getItem(arg2)).image_uri;
					ctViewPager.setCurrentItem(0, true);
					actAnimation();
				}
			});
		List.setAdapter(mActivityAdapter);
		cdPagerAdapter.addView(Group);
		ctViewPager.setCurrentItem(1, true);
		cdPagerAdapter.notifyDataSetChanged();
		
	}
	
	private class MyTask extends AsyncTask{
		@Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	 
	        Utility.showHideLoadingView(CreateRoute.this, true);
	    }
		
		@Override
		protected Object doInBackground(Object... params) {
			SpreadsheetHelper helper = SpreadsheetHelper.getInstance();
			String routeId = helper.createNewRouteId();
			
			ArrayList<RouteData> routeDatasList = new ArrayList<RouteData>();
			String travelPicture = null;
			for (int i=0; i<mRouteAdapter.getCount(); i++) {
//				Log.d("","ListData = " + ((ListData)mRouteAdapter.getItem(i)).main + ", " + ((ListData)mRouteAdapter.getItem(i)).sub + ", " + ((ListData)mRouteAdapter.getItem(i)).image_uri);
				Log.d("","ListData = " + ((ListData)mRouteAdapter.getItem(i)).image_uri);
				String activityName = ((ListData)mRouteAdapter.getItem(i)).sub;
				String locationName = ((ListData)mRouteAdapter.getItem(i)).locationName;
				String image_uri = ((ListData)mRouteAdapter.getItem(i)).image_uri;
				
				if (travelPicture == null && image_uri != null) {
					travelPicture = image_uri;
				}
				
				if (activityName != null || locationName!= null) {
					String timeStr = ((ListData)mRouteAdapter.getItem(i)).main;
					String startTime = ((ListData)mRouteAdapter.getItem(i)).main.substring(0, timeStr.indexOf("\n"));
					
					RouteData data = new RouteData();
					data.id = routeId;
					data.activityName = activityName;
					data.locationName = locationName;
					data.startTime = startTime;
					data.picture = image_uri;
					routeDatasList.add(data);
				}
			}
			
			for (RouteData data : routeDatasList) {
				data.print();
				helper.addRoute(data);
			}
			
			String travelId = helper.createNewRouteId();
			mTravelData.id = travelId;
			mTravelData.routeID = routeId;
			mTravelData.picture = travelPicture;
			mTravelData.print();
			helper.addTravel(mTravelData);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Utility.showHideLoadingView(CreateRoute.this, false);

			CreateRoute.this.finish();
	    }
	}
	
}

package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.officialwebsite.traveltogether.Data.TravelData;




import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;

public class CreateTravel extends Activity {

	public static  class ListData extends Object{
		public String main;
		public int image_id;
		public String image_uri;
		public String sub;

		public String title;
		public String location;
		public String locationName;
		public String description;
	}

	public static Map<String, String> mTravelMap = new HashMap<String, String>();
	private CLViewPager ctViewPager;  
	private CLPagerAdapter cdPagerAdapter; 
	private LayoutInflater mInflater; 

	private TravelAdapter mGroupAdapter;
	private TravelAdapter mLocateAdapter;
	private TravelAdapter mTransportationAdapter;

	private Animation mSlideOutAnim = null;
	private Typeface mTypeface;

	public static final String[] mTravelStrings = new String[] {
		"新增團名", "出發地點", "旅遊地點", "出發日期", "結束日期", "預算","人數", "交通"};

	public static final String[] mLocateStrings = new String[] {
		"台北", "桃園", "新竹", "台中", "台南", "高雄", "墾丁"};

	public static final String[] mTransportationStrings = new String[] {
		"公車", "捷運", "汽車", "機車", "飛機"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("wula", "onCreate");
		
		mTypeface = Typeface.createFromAsset(getAssets(), "fonts/pen.ttf"); 
		
		setContentView(R.layout.create_travel);
		mInflater = getLayoutInflater(); 
		//		ListView mainList = (ListView) findViewById(R.id.mainListView);
		//		Log.e("wula", "mainList: "+mainList);
		//		mainList.setAdapter(new ArrayAdapter<String>(this,
		//				 android.R.layout.simple_list_item_1, mStrings));


		ctViewPager = new CLViewPager(this);
		ctViewPager.setPageMargin((int) (-800));

		//		mainList.setOnItemClickListener(new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> adapter, View view, int pos,
		//					long id) {
		//				
		//				onItemSelect(view, pos);
		//			}
		//		});



		initialListView();

		//		ctViewPager.setCurrentItem(item, true)
		ViewGroup groupGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_grouplist, null));
		ListView groupList = (ListView)groupGroup.findViewById(R.id.group_listview);
		groupList.setAdapter(mGroupAdapter);

		//		groupList.setDivider(null);
		groupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				ctViewPager.enableAppearingAnimator(1000);
				onItemSelect(view, pos);
			}
		});


		Button nextBtn = (Button) groupGroup.findViewById(R.id.groupt_btn);
		nextBtn.setText("規劃行程");
		nextBtn.setTypeface(mTypeface, Typeface.BOLD);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TravelData data = new TravelData();
				data.peopleId = Utility.USER_ID;
				
				for(int i=0;i<mGroupAdapter.getCount();i++){
					String title = ((ListData)mGroupAdapter.getItem(i)).main;
					String value = ((ListData)mGroupAdapter.getItem(i)).sub;
					Log.e("wula", "title: "+title+",  value: "+value);
					mTravelMap.put(title, value);
					
					if (title.equals("出發地點")) {
						data.startLocation = value;
					} else if (title.equals("旅遊地點")) {
						data.travelLocation = value;
					} else if (title.equals("出發日期")) {
						data.startTime = value;
					} else if (title.equals("結束日期")) {
						data.endTime = value;
					} else if (title.equals("預算")) {
						data.budget = value;
					} else if (title.equals("交通")) {
						data.vehicle = value;
					} else if (title.equals("新增團名")) {
						data.travelName = value;
					}
				}
				CreateRoute.mTravelData = data;

				Intent toNextPage = new Intent(getBaseContext(), CreateRoute.class); 
				startActivityForResult(toNextPage,0);
			}
		});


		cdPagerAdapter = new CLPagerAdapter(this);
		cdPagerAdapter.addView(groupGroup);

		//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view1, null));
		//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view2, null));
		//		cdPagerAdapter.addView(mInflater.inflate(R.layout.view3, null));

		ctViewPager.setAdapter(cdPagerAdapter);
		ctViewPager.setCurrentItem(0, true);

		//        ListView mainList = new ListView(this);//(ListView) findViewById(R.id.group_listview);



		((ViewGroup)findViewById(R.id.rootLayout)).addView(ctViewPager);


		//        setContentView(ctViewPager);
	}

	public void onItemSelect(final View view, final int pos){
		//		ctViewPager.setPageMargin((int) (-1000));
		if(pos == 0){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(((TextView)view.findViewById(R.id.itemName)).getText());
			//			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = String.valueOf(input.getText());

					Log.e("wula", "value : "+value);

					((TextView)view.findViewById(R.id.selectName)).setText(value);
					((ListData)mGroupAdapter.getItem(pos)).sub = value;
					// Do something with value!
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});

			alert.show();
		}
		else if(pos == 1 || pos == 2){

			ViewGroup locateGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaultlist, null));
			ListView locateList = (ListView)locateGroup.findViewById(R.id.default_listview);
			locateList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Log.e("wula", "text : "+((TextView)arg1.findViewById(R.id.itemName)).getText());
					String text = (String) ((TextView)arg1.findViewById(R.id.itemName)).getText();
					((TextView)view.findViewById(R.id.selectName)).setText(text);
					((ListData)mGroupAdapter.getItem(pos)).sub = text;
					//						mGroupData[pos] = text;
					actAnimation();



				}
			});
			locateList.setAdapter(mLocateAdapter);
			cdPagerAdapter.addView(locateGroup);
			//	ctViewPager.setCurrentItem(0, true);
			cdPagerAdapter.notifyDataSetChanged();
		}
		else if(pos == 7){
			ViewGroup transportationGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaultlist, null));
			ListView transportationList = (ListView)transportationGroup.findViewById(R.id.default_listview);
			transportationList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.e("wula", "text : "+((TextView)arg1.findViewById(R.id.itemName)).getText());
					String text = (String) ((TextView)arg1.findViewById(R.id.itemName)).getText();
					((TextView)view.findViewById(R.id.selectName)).setText(text);
					((ListData)mGroupAdapter.getItem(pos)).sub = text;
					//						ListData listdata = new ListData();
					//						((ListData)mGroupAdapter.getItem(arg2)).sub = (String) ((TextView)arg1.findViewById(R.id.itemName)).getText();
					actAnimation();



				}
			});
			transportationList.setAdapter(mTransportationAdapter);
			cdPagerAdapter.addView(transportationGroup);
			ctViewPager.setCurrentItem(0, true);
			cdPagerAdapter.notifyDataSetChanged();
		}

		else if(pos == 5 || pos == 6){
			/*ViewGroup editBoxGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaulteditbox, null));
			cdPagerAdapter.addView(editBoxGroup);
			ctViewPager.setCurrentItem(0, true);
			cdPagerAdapter.notifyDataSetChanged();*/
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(((TextView)view.findViewById(R.id.itemName)).getText());
			//			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = String.valueOf(input.getText());

					Log.e("wula", "value : "+value);

					((TextView)view.findViewById(R.id.selectName)).setText(value);
					((ListData)mGroupAdapter.getItem(pos)).sub = value;
					// Do something with value!
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});

			alert.show();
		}
		else if(pos == 3 || pos == 4){
			/*ViewGroup editBoxGroup = ((ViewGroup)mInflater.inflate(R.layout.create_travel_defaulteditbox, null));
			cdPagerAdapter.addView(editBoxGroup);
			ctViewPager.setCurrentItem(0, true);
			cdPagerAdapter.notifyDataSetChanged();*/
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			//			alert.setTitle(((TextView)view.findViewById(R.id.itemName)).getText());
			//			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final DatePicker datePicker = new DatePicker(this);
			alert.setView(datePicker);


			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String year = String.valueOf(datePicker.getYear());
					String month = String.valueOf(datePicker.getMonth()+1);
					String date = String.valueOf(datePicker.getDayOfMonth());
					String time = month+"/"+date+"/"+year;
					//			Log.e("wula", "value : "+value);

					((TextView)view.findViewById(R.id.selectName)).setText(time);
					((ListData)mGroupAdapter.getItem(pos)).sub = time;
					// Do something with value!
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});

			alert.show();
		}

		//		cdPagerAdapter.addView(mInflater.inflate(R.layout.activity_main, null));
		//		cdPagerAdapter.addView(mInflater.inflate(R.layout.activity_main, null));
		//		cdPagerAdapter.notifyDataSetChanged();
	}

	public void initialListView(){

		ArrayList<ListData> groupArrayList = new ArrayList<ListData>();


		for(int i=0;i<mTravelStrings.length;i++){
			ListData listdata = new ListData();
			listdata.main = mTravelStrings[i];
			//			listdata.image_id = R.drawable.culture_logo;
			groupArrayList.add(listdata);
		}
		mGroupAdapter = new TravelAdapter(this, groupArrayList);

		ArrayList<ListData> locateArrayList = new ArrayList<ListData>();  

		for(int i=0;i<mLocateStrings.length;i++){
			ListData listdata = new ListData();
			listdata.main = mLocateStrings[i];
			locateArrayList.add(listdata);
		}
		mLocateAdapter = new TravelAdapter(this, locateArrayList);

		ArrayList<ListData> mTransportationArrayList = new ArrayList<ListData>();  
		for(int i=0;i<mTransportationStrings.length;i++){
			ListData listdata = new ListData();
			listdata.main = mTransportationStrings[i];
			mTransportationArrayList.add(listdata);
		}
		mTransportationAdapter = new TravelAdapter(this, mTransportationArrayList);


	}


	public void actAnimation(){
		if(mSlideOutAnim != null)
			mSlideOutAnim.reset();
		mSlideOutAnim = new TranslateAnimation(0,800,0,0);
		mSlideOutAnim.setAnimationListener(new Animation.AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
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

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CreateTravel.this.finish();
	}

}

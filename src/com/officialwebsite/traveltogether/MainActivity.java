package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TaipeiOpenData;
import com.officialwebsite.traveltogether.Data.TravelData;
import com.officialwebsite.traveltogether.Data.UserData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class MainActivity extends Activity {
	AnimatorSet mAnimatorSet = null;
	Timer mTimer = null;
	TimerTask mTimerTask1 = new TimerTask(){

		@Override
		public void run() {
			animView(MainActivity.this.findViewById(R.id.lookForGroup),0,2000,null);
		}

	}; 
	TimerTask mTimerTask2 = new TimerTask(){

		@Override
		public void run() {
			animView(MainActivity.this.findViewById(R.id.dragGroup),0,2000,null);
		}

	}; 
	TimerTask mTimerTask3 = new TimerTask(){

		@Override
		public void run() {
			animView(MainActivity.this.findViewById(R.id.travel),0,2000,null);
		}

	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		mTimer = new Timer();
		mTimer.schedule(mTimerTask1, 0, 5000);
		mTimer.schedule(mTimerTask2, 2500, 5000);
		mTimer.schedule(mTimerTask3, 0, 5000);
		//OpenDataDbAdapter.getInstance(this).rebrowseDB(this);
		// Sample code to read SpreadSheet from Google server
		// Do this in non-UI thread!
		runOnUiThread(new Runnable(){
			public void run(){
				new MyTask().execute();
			}
		});
	}

	public void animView(final View v, final long delay, final long duration, final Runnable callBack){
		MainActivity.this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mAnimatorSet != null && mAnimatorSet.isRunning() /*&& callBack != null*/){
					//mAnimatorSet.cancel();
				}
				mAnimatorSet = new AnimatorSet();
				mAnimatorSet.playSequentially(
						ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 10.0f),
						ObjectAnimator.ofFloat(v, "rotationX", 10.0f, 0.0f)
						);

				mAnimatorSet.addListener(new Animator.AnimatorListener(){

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						if(callBack != null)
							callBack.run();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationStart(Animator animation) {
					}

				});

				mAnimatorSet.setStartDelay(delay);
				mAnimatorSet.setDuration(duration).start();
			}

		});

	}

	public void onBtnClick(View v){
		Log.d("","onBtnClick "+v.getId());
		switch(v.getId()){
		case R.id.imageButton2:
			animView(MainActivity.this.findViewById(R.id.lookForGroup),0,300,new Runnable(){

				@Override
				public void run() {
					Intent toNextPage = new Intent(getBaseContext(), SearchTravel.class); 
					startActivity(toNextPage);
				}
				
			});
			break;
		case R.id.imageButton4:
			animView(MainActivity.this.findViewById(R.id.dragGroup),0,300,new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent toNextPage = new Intent(getBaseContext(), CreateTravel.class); 
					startActivity(toNextPage);
				}
				
			});
			break;
		case R.id.imageButton3:
			animView(MainActivity.this.findViewById(R.id.travel),0,300,new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TravelListView.mSearchUserId = Utility.USER_ID;
					Intent toNextPage = new Intent(getBaseContext(), TravelListView.class); 
					startActivity(toNextPage);
				}
				
			});
			break;
		case R.id.deleteDB:
			OpenDataDbAdapter.getInstance(this).deleteDB(this);
			break;
		case R.id.loadDB:
			OpenDataDbAdapter.getInstance(this).browseDB(this);
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class MyTask extends AsyncTask{
		@Override
		protected Object doInBackground(Object... params) {
			SpreadsheetHelper helper = SpreadsheetHelper.getInstance();
			
//			helper.deleteUserById("3");
//			helper.addUserToTravel("1", "3");	
			
//			ArrayList<TravelData> datas = helper.getAllTravels();
//			ArrayList<TravelData> datas = helper.searchTravels(null, null, null, null, null, "7000", null);
//			ArrayList<TravelData> datas = helper.getTravelsByUserId("1");
//			ArrayList<UserData> datas = helper.getAllUsers();	
//			ArrayList<RouteData> datas = helper.getRoutesById("2");
//			for(int i=0; i<datas.size(); i++){
//				datas.get(i).print();
//			}

			//			UserData data = helper.getUserById("3");
			//			data.print();


			// Get new id before add data
			//			helper.createNewTravelId();
			//			helper.createNewRouteId();
			//			helper.createNewUserId();

			// Sample code to add data
			/*UserData user = new UserData();
			user.id = "3";
			user.name = "???S??";
			user.picture = "";
			helper.addUser(user);

			RouteData newRoute = new RouteData();
			newRoute.id = "3";
			newRoute.startTime = "10:00:00";
			newRoute.locationName = "?U?~?j??";
			helper.addRoute(newRoute);

			TravelData newTravel = new TravelData();
			newTravel.id = "2";
			newTravel.peopleId = "3";
			newTravel.startTime = "2013-10-10";
			newTravel.endTime = "2013-10-10";
			newTravel.budget = "12000";
			newTravel.routeID = "2";
			helper.addTravel(newTravel);*/
			
			
			return null;
		}
	}
}

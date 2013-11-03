package com.officialwebsite.traveltogether;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CLPagerAdapter extends PagerAdapter {

	static final String TAG = "CLPagerAdapter";
	private Activity mActivity;
	public static final float FULL_PAGE_RATIO = 1.0f;
	public static final float RIGHT_PAGE_RATIO = 0.6f;
	public static final float LEFT_PAGE_RATIO = 0.4f;
	public ArrayList<View> mViewList = null;
	private int mViewCount = 0;
	private int mPageCount = 0;
	
	//flag
	private boolean allowNotify = true;
	
	public CLPagerAdapter(final Activity activity){
		mActivity = activity;
		mViewList = new ArrayList<View>();
		mViewCount = 0;
	}
	
	@Override
	public int getCount() {
		return mPageCount;
	}
	
	public int getViewCount() {
		return mViewCount;
	}
	
	public View getViewAt(int position) {
		return mViewList.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override  
    public Object instantiateItem(ViewGroup container, int position) {  
		Log.d(TAG,"instantiateItem "+position);
		View view = mViewList.get(position);
		if(view != null){
			if(view.getParent() != null)
				((ViewGroup) view.getParent()).removeView(view);
			container.addView(view);
		}
        
        return view;  
    }
	
	@Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object) {  
		container.removeView ((View) object);
    }  

    @Override  
    public int getItemPosition(Object object) {  

    	Log.d(TAG,"getItemPosition");
        int index = mViewList.indexOf((View) object);
        if (index == -1)
          return POSITION_NONE;
        else
          return index;
    }

	public int addView(View v) {
		return addView (v, mViewList.size());
	}
	
	public int addView (View v, int position)
	{
		Log.d(TAG,"addView "+position);
		
		++mViewCount;
		try{
			mViewList.add(position, v);
		}catch(IndexOutOfBoundsException e){
	    	Log.e(TAG,"addView error!");
	    	e.printStackTrace();
	    }
		
		this.notifyDataSetChanged();
		return position;
	}
	
	public int removeView (ViewPager pager, View v)
	{
		return removeView (pager, mViewList.indexOf(v));
	}
	 
	public int removeView (ViewPager pager, final int position)
	{
		Log.d(TAG,"removeView "+position);
		
	    --mViewCount;
	    
	    try{
	    	mViewList.remove(position);
	    }catch(IndexOutOfBoundsException e){
	    	Log.e(TAG,"removeView error!");
	    	e.printStackTrace();
	    }
	    
		notifyDataSetChanged();

	    return position;
	}
	
	public View getView (int position)
	{
		Log.d(TAG,"getView "+position);
		return mViewList.get (position);
	}
	
	@Override		
	public float getPageWidth(int position) {
		return FULL_PAGE_RATIO;
	}
	
	@Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        Log.d(TAG, "restoreState");
    }
 
    @Override
    public Parcelable saveState() {
        return null;
    }

	public void disallowNotifyDataSetChanged() {
		allowNotify = false;
	}
	
	public void allowNotifyDataSetChanged() {
		allowNotify = true;
	}
	
	@Override
	public void notifyDataSetChanged(){
		Log.d(TAG,"notifyDataSetChanged "+mPageCount+" "+mViewCount);
		if(allowNotify){
			Log.d(TAG,"allowNotify "+mPageCount+" "+mViewCount);
			mPageCount = mViewCount;
			super.notifyDataSetChanged();
		}
	}

}

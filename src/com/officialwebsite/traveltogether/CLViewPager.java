package com.officialwebsite.traveltogether;

import java.lang.reflect.Field;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CLViewPager extends ViewPager {
	private static final String TAG = "CLViewPager";
	private CLViewPager THIS = this;
	private int mDuration = 300;
	private Runnable mAppearingEndCallBack = null;

	public CLViewPager(final Context context) {
		super(context);
		init();
	}
	
	public CLViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		LayoutTransition transitioner = new LayoutTransition();
	
		Interpolator sInterpolator = new AccelerateInterpolator();
		try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.getContext(), sInterpolator);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
	}
	
	public void enableAppearingAnimator(float length) {
		Log.d(TAG,"enableAnimator");
		LayoutTransition transitioner = new LayoutTransition();
		
		Animator customAppearingAnim = ObjectAnimator.ofFloat(null, "translationX", length, 0f).
                setDuration(mDuration);
        customAppearingAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
            	Log.d(TAG,"Animation APPEARING");
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setTranslationX(0f);
                if(mAppearingEndCallBack != null)
                	view.post(mAppearingEndCallBack);
            }
        });
        
		transitioner.setAnimator(LayoutTransition.APPEARING, customAppearingAnim);
		transitioner.setAnimator(LayoutTransition.DISAPPEARING, null);
		transitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
		transitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
		
		this.setLayoutTransition(transitioner);
	}
	
	public void disableAppearingAnimator() {
		LayoutTransition transitioner  = this.getLayoutTransition();
		if(transitioner == null)
			return;
		transitioner.setDuration(0);
		this.setLayoutTransition(transitioner);
	}
	
	public void setAppearingDuration(int duration) {
		mDuration = duration;
	}
	
	public void setAppearingEndCallBack(Runnable appearingEndCallBack) {
		mAppearingEndCallBack = appearingEndCallBack;
	}
	
//	@Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return false;
//    }
//	
//	@Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return false;
//    }
	
	public class FixedSpeedScroller extends Scroller {

	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }

	    public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

	    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	    	Log.d(TAG,"startScroll");
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	    	Log.d(TAG,"startScroll");
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	}
}

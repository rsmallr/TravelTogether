package com.officialwebsite.traveltogether;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Utility {
	public static int LOADING_VIEW_ID = 1000;
	
	public static String USER_ID = "5";

	public static void showHideLoadingView(Activity activity, boolean isShow) {
		RelativeLayout rl = (RelativeLayout) activity.findViewById(R.id.root_layout);
		if (rl == null) {
			Log.d("Utility", "showHideLoadingView cannot found root view");
			return;
		}
		
		if (isShow) {
	        // Creating a new TextView
	        ImageView imageView = new ImageView(activity.getApplicationContext());
	        imageView.setImageResource(R.drawable.loading);  
			AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();  
			animationDrawable.start();
			imageView.setId(LOADING_VIEW_ID);
	
	        // Defining the layout parameters of the TextView
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	                RelativeLayout.LayoutParams.WRAP_CONTENT,
	                RelativeLayout.LayoutParams.WRAP_CONTENT);
	        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
	
	        // Setting the parameters on the TextView
	        imageView.setLayoutParams(lp);
	        
	        rl.addView(imageView);
		} else {
			View loadingView = (View) activity.findViewById(LOADING_VIEW_ID);
			if (loadingView == null) {
				Log.d("Utility", "showHideLoadingView cannot found loadingView");
				return;
			}
			
			rl.removeView(loadingView);
		}
	}
	
	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream out = null;
		
		try {
            URLConnection conn = (new URL(url)).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            
    		out = new ByteArrayOutputStream();
    		copy(is, out);

            byte[] data = out.toByteArray();
            
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, options);

			options.inJustDecodeBounds = false;
	        int sample  = options.outWidth * options.outHeight / 400 / 400;
	        if (sample < 3)
	        	options.inSampleSize = sample;
	        else if (sample < 6)
	        	options.inSampleSize = 4;
	        else
	        	options.inSampleSize = 8;
	        
	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			
		} catch (IOException e) {
			Log.e("TravelTogether", "Could not load Bitmap from: " + url);
		} finally {
			closeStream(is);
			closeStream(bis);
		}
		
		return bitmap;
	}
	
	private static void copy(InputStream in, ByteArrayOutputStream out) throws IOException {

		int nRead = -1;
		byte[] data = new byte[10240];
		
		while (true) {
			if ((nRead = in.read(data, 0, data.length)) != -1) {
				out.write(data, 0, nRead);
			}
			else {
				break;
			}
		}
		
	}
	
	private static byte[] toByteArray(InputStream is) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int nRead = -1;
		byte[] data = new byte[16384];
		
		while (true) {
			
			try {
				nRead = is.read(data, 0, data.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (nRead == -1)
				break;
			
			buffer.write(data, 0, nRead);
		}
		
		try {
			buffer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer.toByteArray();
	}
	
	private static void closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}

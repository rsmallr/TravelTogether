package com.officialwebsite.traveltogether;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import com.officialwebsite.traveltogether.CreateTravel.ListData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DescriptionAdapter extends BaseAdapter{

	private ArrayList<ListData> data; 
	private LayoutInflater layoutInflater;  
	private Context context; 
	Typeface tf = null;
	
	public DescriptionAdapter(Context context,ArrayList<ListData> data)
	{
		this.context = context;  
		tf = Typeface.createFromAsset(context.getAssets(), "fonts/pen.ttf");
        this.data = data;  
        this.layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView==null)
				convertView = layoutInflater.inflate(R.layout.listview_detail, null);
			
//			TextView itemName = (TextView) convertView.findViewById(R.id.itemName); 
//			TextView selectName = (TextView) convertView.findViewById(R.id.selectName); 
			final ImageView imageIcon = (ImageView) convertView.findViewById(R.id.iconImage);
			TextView title = (TextView) convertView.findViewById(R.id.title); 
			TextView location = (TextView) convertView.findViewById(R.id.location); 
			TextView description = (TextView) convertView.findViewById(R.id.description); 
			
//			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/pen.ttf");
			if(title.getTypeface() != tf)
				title.setTypeface(tf, Typeface.BOLD);
			if(location.getTypeface() != tf)
				location.setTypeface(tf, Typeface.BOLD);
			if(description.getTypeface() != tf)
				description.setTypeface(tf, Typeface.BOLD);
			
			title.setText(data.get(position).title);
			location.setText(data.get(position).location);
			description.setText(data.get(position).description);
		
//			itemName.setText(data.get(position).main);
//			selectName.setText(data.get(position).sub);
//			if(data.get(position).image_id != 0)
//				imageIcon.setImageDrawable(convertView.getResources().getDrawable(data.get(position).image_id));
			
			imageIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.culture_logo));
			if(data.get(position).image_id != 0)
				imageIcon.setImageDrawable(convertView.getResources().getDrawable(data.get(position).image_id));
			
//			Log.e("wula", "uri: "+data.get(position).image_uri.toString());
			if(data.get(position).image_uri != null){
				Log.e("wula", "uri: "+data.get(position).image_uri.toString());
				
				Thread thread = new Thread(){
					public void run(){
						final Bitmap bmp;
						bmp = Utility.loadBitmap(data.get(position).image_uri);
						imageIcon.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								imageIcon.setImageBitmap(bmp);
							}
							
						});
					}
				};
				thread.start();
			}
				
			
		return convertView;
	}
	
	
	private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
       } catch (IOException e) {
       }
       return bm;
    } 

}

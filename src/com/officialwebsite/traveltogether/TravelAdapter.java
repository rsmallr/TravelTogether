package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.HashMap;

import com.officialwebsite.traveltogether.CreateTravel.ListData;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TravelAdapter extends BaseAdapter{

	private ArrayList<ListData> data; 
	private LayoutInflater layoutInflater;  
	private Context context; 
	Typeface tf = null;
	
	public TravelAdapter(Context context,ArrayList<ListData> data)
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
	public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null)
				convertView = layoutInflater.inflate(R.layout.listview_item, null);
			
			TextView itemName = (TextView) convertView.findViewById(R.id.itemName); 
			TextView selectName = (TextView) convertView.findViewById(R.id.selectName); 
			ImageView imageIcon = (ImageView) convertView.findViewById(R.id.iconImage);
			
			if(itemName.getTypeface() != tf)
				itemName.setTypeface(tf, Typeface.BOLD);
			if(selectName.getTypeface() != tf)
				selectName.setTypeface(tf, Typeface.BOLD);
			
			itemName.setText(data.get(position).main);
			selectName.setText(data.get(position).sub);
			if(data.get(position).image_id != 0)
				imageIcon.setImageDrawable(convertView.getResources().getDrawable(data.get(position).image_id));
			
		return convertView;
	}

}

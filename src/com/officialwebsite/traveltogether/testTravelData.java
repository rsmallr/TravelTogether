package com.officialwebsite.traveltogether;

import java.util.ArrayList;
import java.util.List;

import com.officialwebsite.traveltogether.Data.RouteData;
import com.officialwebsite.traveltogether.Data.TravelData;

public class testTravelData {
	
	public static List<TravelData> getTestTravelData1() {
		ArrayList<TravelData> list = new ArrayList<TravelData>();
		
		for (int i = 0; i < 10; i++) {
			TravelData data = new TravelData();
			
			data.travelName = "�E���B���ʥۤ@��C";
			data.startTime = "2013�~11��16��";
			data.endTime = "2013�~11��16��";
			
			ArrayList<RouteData> spots = new ArrayList<RouteData>();
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "�����ժ��]";
//				spot.address = "�s�_����ڰϪ��ʥ۪�����8��";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "�Ӥl���]";
//				spot.descript = "���ʥۤӤl���]���s�_����ڰϪ��������A��褸1922�~�ɬ����Ѥ饻�L�M�ӤӤl�ӥx�����q���ɩҫت��馡���]�A�S�١u�Ӥl���]�v�C";
//				spot.address = "�s�_����ڰϪ��ʥ۪�����8��";
//				spot.image = "";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "�E���¹D��s��";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "���a������";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "���K���V�׶�";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "�@�����|";
			}
			
//			data.spots = spots;
			
			list.add(data);
		}
		
		return list;
	}

}

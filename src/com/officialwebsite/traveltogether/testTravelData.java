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
			
			data.travelName = "九份、金瓜石一日遊";
			data.startTime = "2013年11月16日";
			data.endTime = "2013年11月16日";
			
			ArrayList<RouteData> spots = new ArrayList<RouteData>();
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "黃金博物館";
//				spot.address = "新北市瑞芳區金瓜石金光路8號";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "太子賓館";
//				spot.descript = "金瓜石太子賓館位於新北市瑞芳區的金光路，於西元1922年時為提供日本昭和皇太子來台視察礦產時所建的日式行館，又稱「太子賓館」。";
//				spot.address = "新北市瑞芳區金瓜石金光路8號";
//				spot.image = "";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "九份舊道基山街";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "阿柑姨芋圓";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "金枝紅糟肉圓";
			}
			
			{
				RouteData spot = new RouteData();
				spot.locationName = "昇平戲院";
			}
			
//			data.spots = spots;
			
			list.add(data);
		}
		
		return list;
	}

}

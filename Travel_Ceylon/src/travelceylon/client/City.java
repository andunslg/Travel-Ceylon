package travelceylon.client;

import com.google.android.maps.GeoPoint;

public class City {
	String City_Name,Latitude,Longitude;
	GeoPoint city;
	public City(String cName,String lat,String lng,GeoPoint pnt) {
		City_Name=cName;
		Latitude=lat;
		Longitude=lng;
		city=pnt;
	}
}

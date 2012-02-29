package travelceylon.client;

import com.google.android.maps.GeoPoint;

public class Important_Place {
	String Place_Name,Category,Description,Latitude,Longitude;
	City closeTo;
	GeoPoint imp;
	public Important_Place(String pName,String cat,String des,String lat,String lng,City cls,GeoPoint p) {
		Place_Name=pName;
		Category=cat;
		Description=des;
		Latitude=lat;
		Longitude=lng;
		closeTo=cls;
		imp=p;
	}
}

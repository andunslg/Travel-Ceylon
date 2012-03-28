package travelceylon.client;

import com.google.android.maps.GeoPoint;

/**
 * This class holds the data of a important place in the Travel Ceylon App It
 * have data like Place_Name,Category,Description,Latitude,Longitude; Also it
 * have a geo-point object which represent the location on a google map
 * 
 * @author ASLG
 * 
 */
public class Important_Place {
	String Place_Name, Category, Description, Latitude, Longitude;
	City closeTo;
	GeoPoint imp;

	public Important_Place(String pName, String cat, String des, String lat,
			String lng, City cls, GeoPoint p) {
		Place_Name = pName;
		Category = cat;
		Description = des;
		Latitude = lat;
		Longitude = lng;
		closeTo = cls;
		imp = p;
	}
}

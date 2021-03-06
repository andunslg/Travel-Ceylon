package travelceylon.client;

import com.google.android.maps.GeoPoint;

/**
 * This is the class to represent cities in the application It holds
 * City_Name,Latitude,Longitude Also especially a geo-point object which
 * represent the position in a google map
 * 
 * @author ASLG
 * 
 */
public class City {
	String City_Name, Latitude, Longitude;
	GeoPoint city;

	public City(String cName, String lat, String lng, GeoPoint pnt) {
		City_Name = cName;
		Latitude = lat;
		Longitude = lng;
		city = pnt;
	}
}

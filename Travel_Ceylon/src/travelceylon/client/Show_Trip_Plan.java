package travelceylon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import route.*;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Show_Trip_Plan extends MapActivity {
	private Show_Trip_Plan stp;
	private MapView mapView;
	private Road mRoad;
	private ArrayList<City> cityArray;
	private ArrayList<Important_Place> placeArray;
	private boolean streetview;
	private LocationManager locationManager;
	private boolean currentUpdated;
	private String tripPathDes;
	private Bundle params;
	private boolean[] cityAlertShown;
	private int currentCity;
	private String ns;
	private NotificationManager mNotificationManager;
	private Context context;
	private Intent notificationIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_trip_plan);

		stp = this;
		streetview = true;
		currentUpdated = false;
		tripPathDes = "";

		params = getIntent().getExtras();
		String tripPlan = params.getString("TripPlan");
		String cities[] = tripPlan.split(";");
	
		final Button buttonChagngeView = (Button) findViewById(R.id.buttonChagngeView);
		buttonChagngeView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (!streetview) {
					mapView.setStreetView(true);
					streetview = true;
					buttonChagngeView.setText("Change to satelite view");
					mapView.invalidate();

				} else {
					mapView.setStreetView(true);
					streetview = false;
					buttonChagngeView.setText("Change to street view");
					mapView.invalidate();

				}
			}
		});

		final Button buttonShowTripPathDes = (Button) findViewById(R.id.buttonShowTripPathDes);
		buttonShowTripPathDes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(stp);
				dialog.setTitle("Your Trip Path Is This, ");
				dialog.setMessage(tripPathDes);
				dialog.setIcon(R.drawable.icon1);
				dialog.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

				dialog.show();
			}
		});

		mapView = (MapView) findViewById(R.id.mapviewFullTrip);
		mapView.setBuiltInZoomControls(true);
		cityArray = new ArrayList<City>();
		placeArray = new ArrayList<Important_Place>();

		for (int i = 0; i < cities.length; i++) {
			String city = cities[i];
			String cityDetails[] = city.split(":");
			GeoPoint p = new GeoPoint(
					(int) (Double.parseDouble(cityDetails[1]) * 1E6),
					(int) (Double.parseDouble(cityDetails[2]) * 1E6));
			City ct = new City(cityDetails[0], cityDetails[1], cityDetails[2],
					p);
			cityArray.add(ct);
			if (cityDetails.length > 3) {
				String places[] = cityDetails[3].split("#");
				for (int j = 0; j < places.length; j++) {
					String implace[] = places[j].split("\\|");
					GeoPoint p1 = new GeoPoint(
							(int) (Double.parseDouble(implace[3]) * 1E6),
							(int) (Double.parseDouble(implace[4]) * 1E6));
					Important_Place impPlace = new Important_Place(implace[0],
							implace[1], implace[2], implace[3], implace[4], ct,
							p1);
					placeArray.add(impPlace);
				}
			}
		}

		for (int i = 0; i < cityArray.size(); i++) {
			Drawable marker = getResources().getDrawable(R.drawable.marker);
			int markerWidth = marker.getIntrinsicWidth();
			int markerHeight = marker.getIntrinsicHeight();
			marker.setBounds(0, markerHeight, markerWidth, 0);

			MyOverlay myItemizedOverlay = new MyOverlay(marker);
			myItemizedOverlay.context = this;
			myItemizedOverlay.isCity = true;
			myItemizedOverlay.myNum = i;
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(cityArray.get(i).city,
					cityArray.get(i).City_Name,
					"This is the City " + cityArray.get(i).City_Name
							+ ". Longitude :" + cityArray.get(i).Longitude
							+ ". Latitude :" + cityArray.get(i).Latitude);
		}
		for (int i = 0; i < placeArray.size(); i++) {
			Drawable marker = getResources().getDrawable(R.drawable.marker1);
			int markerWidth = marker.getIntrinsicWidth();
			int markerHeight = marker.getIntrinsicHeight();
			marker.setBounds(0, markerHeight, markerWidth, 0);

			MyOverlay myItemizedOverlay = new MyOverlay(marker);
			myItemizedOverlay.context = this;
			myItemizedOverlay.myNum = i;
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(
					placeArray.get(i).imp,
					placeArray.get(i).Place_Name,
					"This is " + placeArray.get(i).Place_Name + ". Longitude :"
							+ placeArray.get(i).Longitude + ". Latitude :"
							+ placeArray.get(i).Latitude
							+ ". This has a importants in "
							+ placeArray.get(i).Category + ". "
							+ placeArray.get(i).Description);

		}
		mapView.invalidate();

		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < cityArray.size() - 1; i++) {
					double fromlat = cityArray.get(i).city.getLatitudeE6() / 1e6;
					double fromlon = cityArray.get(i).city.getLongitudeE6() / 1e6;
					double tolat = cityArray.get(i + 1).city.getLatitudeE6() / 1e6;
					double tolon = cityArray.get(i + 1).city.getLongitudeE6() / 1e6;
					String url = RoadProvider.getUrl(fromlat, fromlon, tolat,
							tolon);
					InputStream is = getConnection(url);
					mRoad = RoadProvider.getRoute(is);
					currentCity=i;
					mHandler.sendEmptyMessage(0);
				}

			}
		}.start();
		cityAlertShown=new boolean[cityArray.size()];
		
		ns = Context.NOTIFICATION_SERVICE;
		context = getApplicationContext();
		mNotificationManager = (NotificationManager) getSystemService(ns);
		notificationIntent=getIntent();
		
		GeoUpdateHandler guh=new GeoUpdateHandler();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200,guh );
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tripPathDes += "Follow this road to go to "+cityArray.get(currentCity+1).City_Name+" from "+cityArray.get(currentCity).City_Name+".\n "+mRoad.mName + " " + mRoad.mDescription + "\n\n";
			MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
			List<Overlay> listOfOverlays = mapView.getOverlays();
			listOfOverlays.add(mapOverlay);
			mapView.invalidate();
		};
	};

	private InputStream getConnection(String url) {
		InputStream is = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			is = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected boolean isLocationDisplayed() {
		return false;
	}

	class MyOverlay extends ItemizedOverlay<OverlayItem> {
		Context context;
		boolean isCity;
		int myNum;
		private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

		public MyOverlay(Drawable marker) {
			super(boundCenterBottom(marker));
			populate();
		}

		public void addItem(GeoPoint p, String title, String snippet) {
			OverlayItem newItem = new OverlayItem(p, title, snippet);
			overlayItemList.add(newItem);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {

			return overlayItemList.get(i);
		}

		@Override
		public int size() {

			return overlayItemList.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = overlayItemList.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			if (isCity) {
				dialog.setIcon(R.drawable.icon1);
				dialog.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

			} else {
				dialog.setIcon(R.drawable.icon2);
				dialog.setNeutralButton("Show path",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								Intent i = new Intent(Show_Trip_Plan.this,
										Show_Path_To_Place.class);
								Bundle bundle = new Bundle();
								bundle.putString("fromLat",
										placeArray.get(myNum).closeTo.Latitude);
								bundle.putString("fromLng",
										placeArray.get(myNum).closeTo.Longitude);
								bundle.putString("toLat",
										placeArray.get(myNum).Latitude);
								bundle.putString("toLng",
										placeArray.get(myNum).Longitude);
								bundle.putString("cityName",
										placeArray.get(myNum).closeTo.City_Name);
								bundle.putString("placeName",
										placeArray.get(myNum).Place_Name);
								bundle.putString("des",
										placeArray.get(myNum).Description);
								bundle.putString("cat",
										placeArray.get(myNum).Category);
								bundle.putString("TripPlan",
										stp.params.getString("TripPlan"));
								i.putExtras(bundle);

								startActivity(i);
							}
						});
				dialog.setNegativeButton("Go Back",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
			}

			dialog.show();
			return true;

		}

	}

	public class GeoUpdateHandler implements LocationListener {
		double currentLat;
		double currentLng;

		@Override
		public void onLocationChanged(Location location) {
			currentLat = location.getLatitude();
			currentLng = location.getLongitude();
			for (int i = 0; i < cityArray.size(); i++) {
				final City temp = cityArray.get(i);
				if (((Double.parseDouble(temp.Latitude) - currentLat) >= -0.0400)
						&& ((Double.parseDouble(temp.Latitude) - currentLat) <= 0.0400)) {
					if (((Double.parseDouble(temp.Longitude) - currentLng) >= -0.0400)
							&& ((Double.parseDouble(temp.Longitude) - currentLng)) <= 0.0400) {
						if (!cityAlertShown[i]) {
							cityAlertShown[i]=true;

							int icon = R.drawable.icon2;
							CharSequence tickerText = "You are reaching a city";
							long when = System.currentTimeMillis();

							Notification notification = new Notification(icon, tickerText, when);
							
							CharSequence contentTitle = "You are reaching "+temp.City_Name;
							CharSequence contentText = "Please check the map to see what are places in the trip plan";
							notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
							PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

							notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
							notification.defaults |= Notification.DEFAULT_SOUND;
							notification.defaults |= Notification.DEFAULT_VIBRATE;
							notification.flags =Notification.FLAG_AUTO_CANCEL;
							
							int HELLO_ID = 100;							
							mNotificationManager.notify(HELLO_ID, notification);
							
							AlertDialog.Builder dialog = new AlertDialog.Builder(
									stp);
							dialog.setTitle("Your are close to "
									+ temp.City_Name);
							dialog.setMessage("Please check the map to see what are places in the trip plan");
							dialog.setIcon(R.drawable.icon1);
							dialog.setNeutralButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface arg0, int arg1) {

											GeoPoint point = new GeoPoint(
													(int) (Double
															.parseDouble(temp.Latitude) * 1E6),
													(int) (Double
															.parseDouble(temp.Longitude) * 1E6));
											MapController mapController = mapView
													.getController();
											mapController.setZoom(16);
											mapController.animateTo(point);

										}
									});

							dialog.show();
							
						}
					}

				}
			}

			Drawable marker = getResources().getDrawable(R.drawable.marker2);
			int markerWidth = marker.getIntrinsicWidth();
			int markerHeight = marker.getIntrinsicHeight();
			marker.setBounds(0, markerHeight, markerWidth, 0);

			GeoPoint current = new GeoPoint(
					(int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));
			MyOverlay myItemizedOverlay = new MyOverlay(marker);
			myItemizedOverlay.context = stp;
			myItemizedOverlay.isCity = true;
			if (currentUpdated) {
				mapView.getOverlays().remove(mapView.getOverlays().size() - 1);
			}
			currentUpdated = true;
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(current, "Your Current Postion",
					"Please watch the map Carfully");
			mapView.invalidate();
			MapController mapController = mapView.getController();
			mapController.animateTo(current);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}

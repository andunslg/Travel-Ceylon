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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Show_Trip_Plan extends MapActivity {
	Show_Trip_Plan stp;
	LinearLayout linearLayout;
	MapView mapView;
	private Road mRoad;
	MapController mc;
	ArrayList<City> cityArray;
	ArrayList<Important_Place> placeArray;
	boolean streetview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_trip_plan);
		Log.d("Startin City", "Starts");
		streetview=true;
		stp = this;

		final Button b = (Button) findViewById(R.id.buttonChagngeView);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(!streetview){
					mapView.setStreetView(true);
					streetview=true;
					b.setText("Change to satelite view");
					mapView.invalidate();

				}
				else{
					mapView.setStreetView(true);
					streetview=false;
					b.setText("Change to street view");
					mapView.invalidate();

				}
			}
		});

		mapView = (MapView) findViewById(R.id.mapviewFullTrip);
		mapView.setBuiltInZoomControls(true);
		cityArray = new ArrayList<City>();
		placeArray = new ArrayList<Important_Place>();


		Bundle params = getIntent().getExtras();
		String tripPlan = params.getString("TripPlan");
		String cities[] = tripPlan.split(";");

		for (int i = 0; i < cities.length; i++) {
			Log.d("City", cities[i]);
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
					Log.d("Place Details", places[j]);
					String implace[] = places[j].split("\\|");
					Log.d("length", Integer.toString(implace[3].length()));
					Log.d("lat", implace[3]);
					Log.d("lng", implace[4]);
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
			myItemizedOverlay.myNum=i;
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
			myItemizedOverlay.myNum=i;
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
					mHandler.sendEmptyMessage(0);
				}

			}
		}.start();

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TextView textView = (TextView) findViewById(R.id.textViewDescriptionofTrip);
			textView.setText(mRoad.mName + " " + mRoad.mDescription);
			MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
			List<Overlay> listOfOverlays = mapView.getOverlays();
			// listOfOverlays.clear();
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
		// TODO Auto-generated method stub
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
								startActivity(i);
								Bundle bundle = new Bundle();
							    bundle.putString("fromLat",placeArray.get(myNum).closeTo.Latitude);
							    bundle.putString("fromLng", placeArray.get(myNum).closeTo.Longitude);
							    bundle.putString("toLat", placeArray.get(myNum).Latitude);
							    bundle.putString("toLng", placeArray.get(myNum).Longitude);
							    bundle.putString("cityName", placeArray.get(myNum).closeTo.City_Name);
							    bundle.putString("placeName", placeArray.get(myNum).Place_Name);
							    bundle.putString("des", placeArray.get(myNum).Description);
							    bundle.putString("cat", placeArray.get(myNum).Category);
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
}

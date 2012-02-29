package travelceylon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import route.*;

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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_trip_plan);
		Log.d("Startin City","Starts");

		stp = this;

		Button b = (Button) findViewById(R.id.buttonShowPathToPlace);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Show_Trip_Plan.this,
						Show_Path_To_Place.class);
				startActivity(i);
			}
		});

		mapView = (MapView) findViewById(R.id.mapviewFullTrip);
		mapView.setBuiltInZoomControls(true);
		cityArray = new ArrayList<City>();
		placeArray = new ArrayList<Important_Place>();
		
		int cityCount = 0;
		int placeCount = 0;
		Bundle params = getIntent().getExtras();
        String tripPlan= params.getString("TripPlan");
        String cities[]=tripPlan.split(";");
        
        for(int i=0;i<cities.length;i++){
        	Log.d("City",cities[i]);
        	String city=cities[i];
        	String cityDetails[]=city.split(":");
        	GeoPoint p = new GeoPoint((int) (Double.parseDouble(cityDetails[1]) * 1E6), (int) (Double.parseDouble(cityDetails[2]) * 1E6));
        	City ct=new City(cityDetails[0],cityDetails[1],cityDetails[2],p);
        	cityArray.add(ct);
        	if(cityDetails.length>3){
        		String places[]=cityDetails[3].split("#");
        		for(int j=0;j<places.length;j++){
                	Log.d("Place Details",places[j]);
        			String implace[]=places[j].split("\\|");
        			Log.d("length",Integer.toString(implace[3].length()));
        			Log.d("lat",implace[3]);
        			Log.d("lng",implace[4]);
        			GeoPoint p1 = new GeoPoint((int) (Double.parseDouble(implace[3]) * 1E6), (int) (Double.parseDouble(implace[4]) * 1E6));
        			Important_Place impPlace=new Important_Place(implace[0], implace[1], implace[2], implace[3], implace[4], ct,p1);
        			placeArray.add(impPlace);
        		}
        	}
        }
        
		List<Overlay> listOfOverlays = mapView.getOverlays();
		for (int i = 0; i < cityArray.size(); i++) {
			MapOverlay_City mapOverlay = new MapOverlay_City();
			mapOverlay.num = cityCount;
			cityCount++;
			listOfOverlays.add(mapOverlay);
		}
		for (int i = 0; i < placeArray.size(); i++) {
			MapOverlay_Important_Place mapOverlay = new MapOverlay_Important_Place();
			mapOverlay.num = placeCount;
			placeCount++;
			listOfOverlays.add(mapOverlay);
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

	class MapOverlay_City extends com.google.android.maps.Overlay {
		int num;

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(cityArray.get(num).city, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.marker);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}
	}
	class MapOverlay_Important_Place extends com.google.android.maps.Overlay {
		int num;

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(placeArray.get(num).imp, screenPts);

			// ---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.marker1);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}
	}
}

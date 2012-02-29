package travelceylon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import route.*;
import travelceylon.client.Show_Trip_Plan.MyOverlay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Show_Path_To_Place extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	private Road mRoad;
	Show_Path_To_Place spt;
	Bundle params ;

	public boolean onKeyDown(int keyCode, KeyEvent event){
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	            Intent i = new Intent(this, Show_Trip_Plan.class);   
	            Bundle bundle = new Bundle();
				bundle.putString("TripPlan",this.params.getString("TripPlan"));
				i.putExtras(bundle);
	            startActivity(i);          
	            finish();
	            return true;
	    }
	    return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_path_to_place);
		spt = this;
		mapView = (MapView) findViewById(R.id.mapviewShowPathToPlace);
		mapView.setBuiltInZoomControls(true);
		params = getIntent().getExtras();
		new Thread() {
			@Override
			public void run() {

				double fromlat = Double
						.parseDouble(params.getString("fromLat"));
				double fromlon = Double
						.parseDouble(params.getString("fromLng"));
				double tolat = Double.parseDouble(params.getString("toLat"));
				double tolon = Double.parseDouble(params.getString("toLng"));

				GeoPoint city = new GeoPoint((int) (fromlat * 1E6),
						(int) (fromlon * 1E6));

				GeoPoint place = new GeoPoint((int) (tolat * 1E6),
						(int) (tolon * 1E6));

				Drawable marker = getResources().getDrawable(R.drawable.marker);
				int markerWidth = marker.getIntrinsicWidth();
				int markerHeight = marker.getIntrinsicHeight();
				marker.setBounds(0, markerHeight, markerWidth, 0);

				MyOverlay myItemizedOverlay = new MyOverlay(marker);
				myItemizedOverlay.context = spt;
				myItemizedOverlay.isCity = true;
				mapView.getOverlays().add(myItemizedOverlay);
				myItemizedOverlay.addItem(city, params.getString("cityName"),
						"This is the City " + params.getString("cityName")
								+ ". Longitude :" + params.getString("fromLng")
								+ ". Latitude :" + params.getString("fromLat"));

				marker = getResources().getDrawable(R.drawable.marker1);
				marker.setBounds(0, markerHeight, markerWidth, 0);

				myItemizedOverlay = new MyOverlay(marker);
				myItemizedOverlay.context = spt;
				mapView.getOverlays().add(myItemizedOverlay);
				myItemizedOverlay.addItem(
						place,
						params.getString("placeName"),
						"This is " + params.getString("placeName")
								+ ". Longitude :" + params.getString("toLng")
								+ ". Latitude :" + params.getString("toLat")
								+ ". This has a importants in "
								+ params.getString("cat") + ". "
								+ params.getString("des"));

				mapView.invalidate();

				String url = RoadProvider
						.getUrl(fromlat, fromlon, tolat, tolon);
				InputStream is = getConnection(url);
				mRoad = RoadProvider.getRoute(is);
				mHandler.sendEmptyMessage(0);

			}
		}.start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TextView textView = (TextView) findViewById(R.id.textViewDescriptionofPathToPlace);
			textView.setText(mRoad.mName + " " + mRoad.mDescription);
			MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
			List<Overlay> listOfOverlays = mapView.getOverlays();
			// listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
			mapView.getController().setZoom(12);
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

			} else {
				dialog.setIcon(R.drawable.icon2);

			}
			dialog.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});

			dialog.show();
			return true;

		}

	}
}

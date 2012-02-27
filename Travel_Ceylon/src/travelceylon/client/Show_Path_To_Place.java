package travelceylon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import route.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Show_Path_To_Place extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	private Road mRoad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_path_to_place);
		mapView = (MapView) findViewById(R.id.mapviewShowPathToPlace);
		mapView.setBuiltInZoomControls(true);

		new Thread() {
			@Override
			public void run() {
				double fromLat = 6.93408, fromLon =79.8502 ; 
			    double toLat =6.85132, toLon =79.866 ;

				String url = RoadProvider
						.getUrl(fromLat, fromLon, toLat, toLon);
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
			listOfOverlays.clear();
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
			Log.d("Exce",e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("Exce",e.toString());
		}
		return is;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}

package travelceylon.client;


import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Show_Cities extends MapActivity {

	private Show_Cities sc;
	private MapView mapView;
	private ArrayList<City> cityArray;

	private String METHOD_NAME = "";
	// our webservice method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webservice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// you must use ipaddress here, don�t use Hostname or localhost

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_cities);

		sc = this;

		mapView = (MapView) findViewById(R.id.mapviewShowCities);
		mapView.setBuiltInZoomControls(true);

		METHOD_NAME = "getCityList";
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			String temp = result.toString();
			String cityList[] = temp.split(";");

			cityArray = new ArrayList<City>();
			for (String c : cityList) {
				String lngi = "";
				String lati = "";

				METHOD_NAME = "getLongitude_City";
				request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("city", c);
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				result = envelope.getResponse();
				lngi = result.toString();

				METHOD_NAME = "getLatitude_City";
				request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("city", c);
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				result = envelope.getResponse();
				lati = result.toString();

				GeoPoint p = new GeoPoint(
						(int) (Double.parseDouble(lati) * 1E6),
						(int) (Double.parseDouble(lngi) * 1E6));

				cityArray.add(new City(c, lati, lngi, p));
			}

		} catch (Exception E) {
			E.printStackTrace();

		}

		for (int i = 0; i < cityArray.size(); i++) {
			Drawable marker = getResources().getDrawable(R.drawable.marker);
			int markerWidth = marker.getIntrinsicWidth();
			int markerHeight = marker.getIntrinsicHeight();
			marker.setBounds(0, markerHeight, markerWidth, 0);

			MyOverlay myItemizedOverlay = new MyOverlay(marker);
			myItemizedOverlay.context = this;
			mapView.getOverlays().add(myItemizedOverlay);
			myItemizedOverlay.addItem(cityArray.get(i).city,
					cityArray.get(i).City_Name,
					"This is the City " + cityArray.get(i).City_Name
							+ ". Longitude :" + cityArray.get(i).Longitude
							+ ". Latitude :" + cityArray.get(i).Latitude);
		}
	
		MapController mc=mapView.getController();
		mc.setZoom(10);
		mapView.invalidate();

	}

	class MyOverlay extends ItemizedOverlay<OverlayItem> {
		Context context;
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

			dialog.setIcon(R.drawable.icon1);
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

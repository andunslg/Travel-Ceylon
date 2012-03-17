package travelceylon.client;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class Add_travel_details_observing extends Activity
{
	Add_travel_details_observing atd;
	ArrayList<String> cities;

	AutoCompleteTextView oa1;
	AutoCompleteTextView oa2;
	AutoCompleteTextView oa3;
	AutoCompleteTextView oa4;
	AutoCompleteTextView oa5;

	String startCity = "";
	String destCity = "";
	String interests = "";
	String duration = "";
	String shouldVisitCities = "";
	String shouldAvoidCities = "";
	String observingCities = "";
	Bundle params;

	private String METHOD_NAME = "";
	// our webservice method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webservice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// you must use ipaddress here, don’t use Hostname or localhost

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_observing_cities);
		atd = this;

		params = getIntent().getExtras();
		startCity = params.getString("Start_City");
		destCity = params.getString("Dest_City");
		duration = params.getString("Due");
		interests = params.getString("Interests");
		shouldVisitCities = params.getString("Should_Visit_Cities");
		shouldAvoidCities = params.getString("Should_Avoid_Cities");


		oa1 = (AutoCompleteTextView) findViewById(R.id.autocompleteOC1);
		oa2 = (AutoCompleteTextView) findViewById(R.id.autocompleteOC2);
		oa3 = (AutoCompleteTextView) findViewById(R.id.autocompleteOC3);
		oa4 = (AutoCompleteTextView) findViewById(R.id.autocompleteOC4);
		oa5 = (AutoCompleteTextView) findViewById(R.id.autocompleteOC5);

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

			cities = new ArrayList<String>();
			for (String c : cityList) {
				cities.add(c);
			}

			String[] saCities = params.getString("Should_Avoid_Cities").split(
					";");
			for (String c : saCities) {
				cities.remove(cities.indexOf(c));
			}
			cityList = new String[cities.size()];
			int i = 0;
			for (String c : cities) {
				cityList[i] = c;
				i++;
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.list, cityList);
			oa1.setAdapter(adapter);
			oa2.setAdapter(adapter);
			oa3.setAdapter(adapter);
			oa4.setAdapter(adapter);
			oa5.setAdapter(adapter);

		} catch (Exception E) {
			E.printStackTrace();

		}

		Button b = (Button) findViewById(R.id.ButtonSendTavelDetailsWithOC);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if (oa1.getText().toString().equals("")
						&& oa2.getText().toString().equals("")
						&& oa3.getText().toString().equals("")
						&& oa4.getText().toString().equals("")
						&& oa5.getText().toString().equals("")) {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("Please fill at least one field before submit");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				} else if ((!cities.contains(oa1.getText().toString()) && !oa1
						.getText().toString().equals(""))
						|| (!cities.contains(oa2.getText().toString()) && !oa2
								.getText().toString().equals(""))
						|| (!cities.contains(oa3.getText().toString()) && !oa3
								.getText().toString().equals(""))
						|| (!cities.contains(oa4.getText().toString()) && !oa4
								.getText().toString().equals(""))
						|| (!cities.contains(oa5.getText().toString()) && !oa5
								.getText().toString().equals(""))) {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("Please choose a City Name sugessted.");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				} else {
					observingCities = "";
					if (!oa1.getText().toString().equals("")) {
						observingCities += oa1.getText().toString() + ";";
					}
					if (!oa2.getText().toString().equals("")) {
						observingCities += oa2.getText().toString() + ";";
					}
					if (!oa3.getText().toString().equals("")) {
						observingCities += oa3.getText().toString() + ";";
					}
					if (!oa4.getText().toString().equals("")) {
						observingCities += oa4.getText().toString() + ";";
					}
					if (!oa5.getText().toString().equals("")) {
						observingCities += oa5.getText().toString() + ";";
					}

					METHOD_NAME = "planTheTrip";
					try {
						SoapObject request = new SoapObject(NAMESPACE,
								METHOD_NAME);
						request.addProperty("startC", startCity);
						request.addProperty("desC", destCity);
						request.addProperty("duration", duration);
						request.addProperty("interests", interests);
						request.addProperty("shouldInclude", shouldVisitCities);
						request.addProperty("shouldAvoid", shouldAvoidCities);
						request.addProperty("observing", observingCities);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(
								URL);
						androidHttpTransport.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();

						Intent i = new Intent(
								Add_travel_details_observing.this,
								Show_Trip_Plan.class);
						Bundle bundle = new Bundle();
						bundle.putString("TripPlan", result.toString());
						i.putExtras(bundle);
						startActivity(i);

					} catch (Exception E) {
						E.printStackTrace();
						Log.d("Error of SOAP", E.toString());

					}

				}

			}
		});

	}
}

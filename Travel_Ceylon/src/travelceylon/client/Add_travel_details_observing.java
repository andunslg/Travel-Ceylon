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

/**
 * This class get a list of cities from the user which are marked as observing
 * cities. Those cities will be only considered to be observing cities that
 * means only on those cites user will stop for see places.
 * 
 * @author ASLG
 * 
 */
public class Add_travel_details_observing extends Activity {
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
	// Travel web service method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in web service with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// Location of the wsdl file

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

		/*
		 * This get the city list from the web service That list will be assign
		 * as adapters to auto complete text views
		 */
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

			/*
			 * The cities which are marked as avoiding cities are removed form
			 * the city list which will be suggested in the auto complete text
			 * boxes
			 */
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

		/*
		 * This button will request trip plan from the web service
		 */
		Button b = (Button) findViewById(R.id.ButtonSendTavelDetailsWithOC);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				/*
				 * This if will check weather at least one city is added If not
				 * it will show a message box
				 */
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
				}
				/*
				 * This if condition check weather user has given a input which
				 * is not a suggested city from the application
				 */
				else if ((!cities.contains(oa1.getText().toString()) && !oa1
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
				}
				/*
				 * If all the inputs are according to the requirement they will
				 * be sent to the web service
				 */
				else {
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

					/*
					 * This send the trip details to the web service to
					 * processing The field not are applicable at this moment
					 * are sent as ""
					 */
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

						/*
						 * This alert box is shown to verify the data which is
						 * sent to the next screen
						 */
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

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
 * This class gets the list of cities which should be included in the trip plan
 * for sure If user only want to add that filter only user can get the trip at
 * this moment If user want to add more filters he can go forward
 * 
 * @author ASLG
 * 
 */
public class Add_travel_details_should_visit extends Activity {
	Add_travel_details_should_visit atd;
	ArrayList<String> cities;

	AutoCompleteTextView sv1;
	AutoCompleteTextView sv2;
	AutoCompleteTextView sv3;
	AutoCompleteTextView sv4;
	AutoCompleteTextView sv5;

	String startCity = "";
	String destCity = "";
	String interests = "";
	String duration = "";
	String shouldVisitCities = "";
	Bundle params;

	private String METHOD_NAME = "";
	// Travel Ceylon web service method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webs ervice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// Location of the wsdl file

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_advanced);
		atd = this;
		params = getIntent().getExtras();

		sv1 = (AutoCompleteTextView) findViewById(R.id.autocompleteSV1);
		sv2 = (AutoCompleteTextView) findViewById(R.id.autocompleteSV2);
		sv3 = (AutoCompleteTextView) findViewById(R.id.autocompleteSV3);
		sv4 = (AutoCompleteTextView) findViewById(R.id.autocompleteSV4);
		sv5 = (AutoCompleteTextView) findViewById(R.id.autocompleteSV5);

		/*
		 * This will get the city list from the webs service Then it set that
		 * city list as adapters to the auto complete text views
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
			String sCity = params.getString("Start_City");
			startCity = sCity;
			String dCity = params.getString("Dest_City");
			destCity = dCity;
			interests = params.getString("Interests");
			duration = params.getString("Due");

			/*
			 * The starting city and dest city will be removed form the list
			 */
			cities.remove(cities.indexOf(sCity));
			cities.remove(cities.indexOf(dCity));
			cityList = new String[cities.size()];
			int i = 0;
			for (String c : cities) {
				cityList[i] = c;
				i++;
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.list, cityList);

			sv1.setAdapter(adapter);
			sv2.setAdapter(adapter);
			sv3.setAdapter(adapter);
			sv4.setAdapter(adapter);
			sv5.setAdapter(adapter);

		} catch (Exception E) {
			E.printStackTrace();

		}

		/*
		 * This will guide user to add more filters to the trip plan
		 */
		Button b = (Button) findViewById(R.id.ButtonGoTShouldAvoid);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				/*
				 * This if will check weather at least one city is added If not
				 * it will show a message box
				 */
				if (sv1.getText().toString().equals("")
						&& sv2.getText().toString().equals("")
						&& sv3.getText().toString().equals("")
						&& sv4.getText().toString().equals("")
						&& sv5.getText().toString().equals("")) {
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
				else if ((!cities.contains(sv1.getText().toString()) && !sv1
						.getText().toString().equals(""))
						|| (!cities.contains(sv2.getText().toString()) && !sv2
								.getText().toString().equals(""))
						|| (!cities.contains(sv3.getText().toString()) && !sv3
								.getText().toString().equals(""))
						|| (!cities.contains(sv4.getText().toString()) && !sv4
								.getText().toString().equals(""))
						|| (!cities.contains(sv5.getText().toString()) && !sv5
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
				 * If all the inputs are in order the data are sent to the next
				 * screen in a bundle object
				 */
				else {
					shouldVisitCities = "";
					if (!sv1.getText().toString().equals("")) {
						shouldVisitCities += sv1.getText().toString() + ";";
					}
					if (!sv2.getText().toString().equals("")) {
						shouldVisitCities += sv2.getText().toString() + ";";
					}
					if (!sv3.getText().toString().equals("")) {
						shouldVisitCities += sv3.getText().toString() + ";";
					}
					if (!sv4.getText().toString().equals("")) {
						shouldVisitCities += sv4.getText().toString() + ";";
					}
					if (!sv5.getText().toString().equals("")) {
						shouldVisitCities += sv5.getText().toString() + ";";
					}

					final Intent i = new Intent(
							Add_travel_details_should_visit.this,
							Add_travel_details_should_avoid.class);
					Bundle bundle = params;
					bundle.putString("Should_Visit_Cities", shouldVisitCities);
					i.putExtras(bundle);
					/*
					 * this mesaage box is shown to to verify the data which
					 * will be sent to the next screen
					 */
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					String tsvC = shouldVisitCities.replace(";", " ");
					alertbox.setMessage("Your list of should visit cities is "
							+ tsvC + ".Proceed further ?");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									startActivity(i);
								}
							});
					alertbox.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				}

			}
		});

		/*
		 * This button will request trip plan form the web service
		 */

		Button b1 = (Button) findViewById(R.id.ButtonSendTavelDetailsWithSVC);
		b1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				/*
				 * This if will check weather at least one city is added If not
				 * it will show a message box
				 */
				if (sv1.getText().toString().equals("")
						&& sv2.getText().toString().equals("")
						&& sv3.getText().toString().equals("")
						&& sv4.getText().toString().equals("")
						&& sv5.getText().toString().equals("")) {
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
				else if ((!cities.contains(sv1.getText().toString()) && !sv1
						.getText().toString().equals(""))
						|| (!cities.contains(sv2.getText().toString()) && !sv2
								.getText().toString().equals(""))
						|| (!cities.contains(sv3.getText().toString()) && !sv3
								.getText().toString().equals(""))
						|| (!cities.contains(sv4.getText().toString()) && !sv4
								.getText().toString().equals(""))
						|| (!cities.contains(sv5.getText().toString()) && !sv5
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
					shouldVisitCities = "";
					if (!sv1.getText().toString().equals("")) {
						shouldVisitCities += sv1.getText().toString() + ";";
					}
					if (!sv2.getText().toString().equals("")) {
						shouldVisitCities += sv2.getText().toString() + ";";
					}
					if (!sv3.getText().toString().equals("")) {
						shouldVisitCities += sv3.getText().toString() + ";";
					}
					if (!sv4.getText().toString().equals("")) {
						shouldVisitCities += sv4.getText().toString() + ";";
					}
					if (!sv5.getText().toString().equals("")) {
						shouldVisitCities += sv5.getText().toString() + ";";
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
						request.addProperty("shouldAvoid", "");
						request.addProperty("observing", "");
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(
								URL);
						androidHttpTransport.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();

						/*
						 * The generated trip plan from the web service is sent
						 * to the map view activity
						 */
						Intent i = new Intent(
								Add_travel_details_should_visit.this,
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

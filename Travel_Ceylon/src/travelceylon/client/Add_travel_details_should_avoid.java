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
 * This class will get the cities user need to avoided from the trip plan. Those
 * cities will not be available for the trip plan for sure Also User can get the
 * trip plan at this moment
 * 
 * @author ASLG
 */
public class Add_travel_details_should_avoid extends Activity {
	Add_travel_details_should_avoid atd;
	ArrayList<String> cities;

	AutoCompleteTextView sa1;
	AutoCompleteTextView sa2;
	AutoCompleteTextView sa3;
	AutoCompleteTextView sa4;
	AutoCompleteTextView sa5;

	String startCity = "";
	String destCity = "";
	String interests = "";
	String duration = "";
	String shouldVisitCities = "";
	String shouldAvoidCities = "";
	Bundle params;

	private String METHOD_NAME = "";
	// Travel Ceylon webservice method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webservice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// Location of the wsdl file

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_should_avoid);
		atd = this;

		params = getIntent().getExtras();
		startCity = params.getString("Start_City");
		destCity = params.getString("Dest_City");
		duration = params.getString("Due");
		interests = params.getString("Interests");
		shouldVisitCities = params.getString("Should_Visit_Cities");

		sa1 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA1);
		sa2 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA2);
		sa3 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA3);
		sa4 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA4);
		sa5 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA5);

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

			String sCity = params.getString("Start_City");
			String dCity = params.getString("Dest_City");

			cities.remove(cities.indexOf(sCity));
			cities.remove(cities.indexOf(dCity));
			String[] svCities = params.getString("Should_Visit_Cities").split(
					";");
			/*
			 * This removes the start,dest and should ilculde cities form the
			 * city list given for avoid cities
			 */
			for (String c : svCities) {
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
			sa1.setAdapter(adapter);
			sa2.setAdapter(adapter);
			sa3.setAdapter(adapter);
			sa4.setAdapter(adapter);
			sa5.setAdapter(adapter);

		} catch (Exception E) {
			E.printStackTrace();

		}

		/*
		 * This button will request trip plan from the web service
		 */
		Button b = (Button) findViewById(R.id.ButtonSendTravelDetailsWithSVCandSAC);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				/*
				 * This if will check weather at least one city is added If not
				 * it will show a message box
				 */
				if (sa1.getText().toString().equals("")
						&& sa2.getText().toString().equals("")
						&& sa3.getText().toString().equals("")
						&& sa4.getText().toString().equals("")
						&& sa5.getText().toString().equals("")) {
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
				else if ((!cities.contains(sa1.getText().toString()) && !sa1
						.getText().toString().equals(""))
						|| (!cities.contains(sa2.getText().toString()) && !sa2
								.getText().toString().equals(""))
						|| (!cities.contains(sa3.getText().toString()) && !sa3
								.getText().toString().equals(""))
						|| (!cities.contains(sa4.getText().toString()) && !sa4
								.getText().toString().equals(""))
						|| (!cities.contains(sa5.getText().toString()) && !sa5
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
					shouldAvoidCities = "";

					/*
					 * This if ladder get at least one city form the list of
					 */
					if (!sa1.getText().toString().equals("")) {
						shouldAvoidCities += sa1.getText().toString() + ";";
					}
					if (!sa2.getText().toString().equals("")) {
						shouldAvoidCities += sa2.getText().toString() + ";";
					}
					if (!sa3.getText().toString().equals("")) {
						shouldAvoidCities += sa3.getText().toString() + ";";
					}
					if (!sa4.getText().toString().equals("")) {
						shouldAvoidCities += sa4.getText().toString() + ";";
					}
					if (!sa5.getText().toString().equals("")) {
						shouldAvoidCities += sa5.getText().toString() + ";";
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
								Add_travel_details_should_avoid.this,
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

		/*
		 * This button will guide the user to select further more filters to the
		 * trip plan
		 */
		Button b1 = (Button) findViewById(R.id.buttonGoToObserving);
		b1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				/*
				 * This if ladder check at least one city is typed in the text
				 * box If not a message box will be shown to complain
				 */
				if (sa1.getText().toString().equals("")
						&& sa2.getText().toString().equals("")
						&& sa3.getText().toString().equals("")
						&& sa4.getText().toString().equals("")
						&& sa5.getText().toString().equals("")) {
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
				 * This will check weather the users has input some city name
				 * which is not suggested by the app
				 */
				else if ((!cities.contains(sa1.getText().toString()) && !sa1
						.getText().toString().equals(""))
						|| (!cities.contains(sa2.getText().toString()) && !sa2
								.getText().toString().equals(""))
						|| (!cities.contains(sa3.getText().toString()) && !sa3
								.getText().toString().equals(""))
						|| (!cities.contains(sa4.getText().toString()) && !sa4
								.getText().toString().equals(""))
						|| (!cities.contains(sa5.getText().toString()) && !sa5
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
					shouldAvoidCities = "";
					if (!sa1.getText().toString().equals("")) {
						shouldAvoidCities += sa1.getText().toString() + ";";
					}
					if (!sa2.getText().toString().equals("")) {
						shouldAvoidCities += sa2.getText().toString() + ";";
					}
					if (!sa3.getText().toString().equals("")) {
						shouldAvoidCities += sa3.getText().toString() + ";";
					}
					if (!sa4.getText().toString().equals("")) {
						shouldAvoidCities += sa4.getText().toString() + ";";
					}
					if (!sa5.getText().toString().equals("")) {
						shouldAvoidCities += sa5.getText().toString() + ";";
					}

					final Intent i = new Intent(
							Add_travel_details_should_avoid.this,
							Add_travel_details_observing.class);
					Bundle bundle = params;
					bundle.putString("Should_Avoid_Cities", shouldAvoidCities);
					i.putExtras(bundle);
					/*
					 * This alert box is shown to verify the data which is sent
					 * to the next screen
					 */
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					String taC = shouldAvoidCities.replace(";", " ");
					alertbox.setMessage("Your list of should avoid cities is "
							+ taC + ".Proceed further ?");
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

	}

}

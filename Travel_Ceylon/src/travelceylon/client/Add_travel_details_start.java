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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is the first UI of set UIs which gets the Trip Plan details from
 * the user This class is responsible for get the Start Location and Destination
 * Location and Trip Duration from user After getting the details those are
 * passed to the next activity
 * 
 * @author ASLG
 * 
 */
public class Add_travel_details_start extends Activity {
	Add_travel_details_start atd;
	Intent i;
	ArrayList<String> cities;

	private String METHOD_NAME = "";
	// Travel Ceylon web service method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in web service with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// Location of the wsdl

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_start);
		atd = this;

		/*
		 * This code section will get the City list from the web service Those
		 * cities will be suggested when the user type city names in the text
		 * fields
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

			AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.autocomplete_StartingCity);
			AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.autocomplete_DestinationCity);
			ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,
					R.layout.list, cityList);
			textView1.setAdapter(cityAdapter);
			textView2.setAdapter(cityAdapter);

		} catch (Exception E) {
			E.printStackTrace();

		}

		/*
		 * When the user click this button it will show the selection of
		 * interests screen
		 */
		Button b = (Button) findViewById(R.id.ButtonGoToSelectInterests);
		b.setOnClickListener(new View.OnClickListener() {
			AutoCompleteTextView sCity = (AutoCompleteTextView) findViewById(R.id.autocomplete_StartingCity);
			AutoCompleteTextView dCity = (AutoCompleteTextView) findViewById(R.id.autocomplete_DestinationCity);
			EditText du = (EditText) findViewById(R.id.EditTextDuration);
			String startCity = "";
			String destCity = "";
			String duration = "";

			public void onClick(View arg0) {

				/*
				 * This if condition check for invalid inputs or empty text
				 * fields in the UI. If it found it will show a message box.
				 */
				if (sCity.getText().toString().equals("")
						|| dCity.getText().toString().equals("")
						|| du.getText().toString().equals("")) {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("Please fill all the fields before submit");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				}
				/*
				 * This if condition check weather user has input a city name
				 * which was not suggested by the Travel Ceylon. If it is it
				 * will show a error message
				 */
				else if (!cities.contains(sCity.getText().toString())
						|| !cities.contains(dCity.getText().toString())) {
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
				 * If user has input all the valid inputs then this if condition
				 * will send those data to the select interest Activity
				 */
				else {
					startCity = sCity.getText().toString();
					destCity = dCity.getText().toString();
					duration = du.getText().toString();

					i = new Intent(Add_travel_details_start.this,
							Add_travel_details_interest.class);
					Bundle bundle = new Bundle();
					bundle.putString("Start_City", startCity);
					bundle.putString("Dest_City", destCity);
					bundle.putString("Due", duration);
					i.putExtras(bundle);

					/*
					 * This message box is shown to verify the city data which
					 * was send to the next dialog
					 */
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("You have selected " + startCity
							+ " as your starting city and " + destCity
							+ " as your destination city.Proceed further ?");
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

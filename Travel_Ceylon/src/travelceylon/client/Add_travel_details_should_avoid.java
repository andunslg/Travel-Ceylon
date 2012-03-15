package travelceylon.client;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Add_travel_details_should_avoid extends Activity {
	Add_travel_details_should_avoid atd;

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
	// our webservice method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webservice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// you must use ipaddress here, don’t use Hostname or localhost

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_should_avoid);
		atd = this;

		params = getIntent().getExtras();
		startCity = params.getString("Start_City");
		destCity = params.getString("Dest_City");
		duration = params.getString("Due");
		interests=params.getString("Interests");
		shouldVisitCities=params.getString("Should_Visit_Cities");
				
		sa1 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA1);
		sa2 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA2);
		sa3 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA3);
		sa4 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA4);
		sa5 = (AutoCompleteTextView) findViewById(R.id.autocompleteSA5);

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
			String cities = result.toString();
			String cityList[] = cities.split(";");
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

		Button b = (Button) findViewById(R.id.ButtonSendTravelDetails1);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
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
				} else {
					shouldAvoidCities="";
					if(!sa1.getText().toString().equals("")){
						shouldAvoidCities+=sa1.getText().toString()+";";
					}
					if(!sa2.getText().toString().equals("")){
						shouldAvoidCities+=sa2.getText().toString()+";";
					}
					if(!sa3.getText().toString().equals("")){
						shouldAvoidCities+=sa3.getText().toString()+";";
					}
					if(!sa4.getText().toString().equals("")){
						shouldAvoidCities+=sa4.getText().toString()+";";
					}
					if(!sa5.getText().toString().equals("")){
						shouldAvoidCities+=sa5.getText().toString()+";";
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
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(
								URL);
						androidHttpTransport.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();

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

	}

}

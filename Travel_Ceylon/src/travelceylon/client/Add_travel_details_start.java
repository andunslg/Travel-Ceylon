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

public class Add_travel_details_start extends Activity {
	Add_travel_details_start atd;
	Intent i;
	
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
		setContentView(R.layout.add_travel_details_start);
		atd = this;

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
			AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.autocomplete_StartingCity);
			AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.autocomplete_DestinationCity);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.list, cityList);
			textView1.setAdapter(adapter);
			textView2.setAdapter(adapter);

		} catch (Exception E) {
			E.printStackTrace();

		}

		Button b = (Button) findViewById(R.id.ButtonGoToSelectInterests);
		b.setOnClickListener(new View.OnClickListener() {
			AutoCompleteTextView sCity = (AutoCompleteTextView) findViewById(R.id.autocomplete_StartingCity);
			AutoCompleteTextView dCity = (AutoCompleteTextView) findViewById(R.id.autocomplete_DestinationCity);
			EditText du = (EditText) findViewById(R.id.EditTextDuration);
			String startCity = "";
			String destCity = "";
			String duration = "";

			public void onClick(View arg0) {

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
				} else {
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
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("You have selected "+startCity+" as your starting city and "+destCity+" as your destination city.Proceed further ?");
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
										int arg1) {						}
							});
					alertbox.show();
					

				}

			}
		});

	}

}

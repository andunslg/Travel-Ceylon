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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Important_place_add_Activity extends Activity {
	Important_place_add_Activity imp;
	private String METHOD_NAME = "";
	// our webservice method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in webservice with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// you must use ipaddress here, don’t use Hostname or localhost
	Spinner spinner, spinner3, spinner2;
	AutoCompleteTextView textView1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		imp = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.important_place_add);
		// imp=this;
		METHOD_NAME = "getCategories";
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			String categories = result.toString();
			categories = categories + "Select a category";
			String categoriesList[] = categories.split(";");

			ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(
					this, R.layout.list, categoriesList);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner = (Spinner) findViewById(R.id.spinnerCategory1);
			spinner.setAdapter(adapter1);
			spinner.setSelection(categoriesList.length - 1);

			spinner2 = (Spinner) findViewById(R.id.spinnerCategory2);
			spinner2.setAdapter(adapter1);
			spinner2.setSelection(categoriesList.length - 1);

			spinner3 = (Spinner) findViewById(R.id.spinnerCategory3);
			spinner3.setAdapter(adapter1);
			spinner3.setSelection(categoriesList.length - 1);

		} catch (Exception E) {
			E.printStackTrace();

		}

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
			textView1 = (AutoCompleteTextView) findViewById(R.id.autocomplete_ClosetotheCity);
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
					R.layout.list, cityList);
			textView1.setAdapter(adapter1);

		} catch (Exception E) {
			E.printStackTrace();

		}
		Button b = (Button) findViewById(R.id.buttonSubmitData);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String placename = "";
				String longitude = "";
				String latitude = "";
				String des = "";
				String cat1 = "";
				String cat2 = "";
				String cat3 = "";
				String city = "";
				String dis = "";

				EditText pname = (EditText) findViewById(R.id.editTextPlaceName);
				EditText description = (EditText) findViewById(R.id.editTextDescription);
				EditText lng = (EditText) findViewById(R.id.editTextLongditude);
				EditText lat = (EditText) findViewById(R.id.editTextLatitude);
				EditText distance = (EditText) findViewById(R.id.editTexDistance);

				placename = pname.getText().toString();
				des = description.getText().toString();
				longitude = lng.getText().toString();
				latitude = lat.getText().toString();
				dis = distance.getText().toString();

				cat1 = spinner.getSelectedItem().toString();
				cat2 = spinner2.getSelectedItem().toString();
				cat3 = spinner3.getSelectedItem().toString();
				
				city = textView1.getText().toString();
				if ((placename != null && placename.trim().length() > 0)
						&& (des != null && des.trim().length() > 0)
						&& (longitude != null && longitude.trim().length() > 0)
						&& (latitude != null && latitude.trim().length() > 0)
						&& (cat1!="Select a category" ||cat2!="Select a category"||cat2!="Select a category")
						&& (city != null && city.trim().length() > 0)
						&& (dis != null && dis.trim().length() > 0)) {
					METHOD_NAME = "insertImportantPlace";
					try {
						SoapObject request = new SoapObject(NAMESPACE,
								METHOD_NAME);
						request.addProperty("placeName", placename);
						request.addProperty("longitude", longitude);
						request.addProperty("latitude", latitude);
						request.addProperty("des", des);
						String cat="";
						if(cat1!="Select a category"){
							cat=cat1;
						}
						if(cat1!="Select a category"&&cat2!="Select a category"){
							cat+=","+cat2;
						}
						if(cat2!="Select a category"&&cat1=="Select a category"){
							cat=cat2;
						}
						if(cat3!="Select a category"&&cat1=="Select a category"&&cat2=="Select a category"){
							cat=cat3;
						}
						if(cat3!="Select a category"&&(cat1!="Select a category"||cat2!="Select a category")){
							cat=","+cat3;
						}
						request.addProperty("cat", cat);
						request.addProperty("city", city);
						request.addProperty("dis", dis);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(
								URL);
						androidHttpTransport.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								imp);
						// Set the message to display
						alertbox.setMessage("Details have been submited ="
								+ result.toString());
						// Add a neutral button to the alert box and assign a
						// click listener
						alertbox.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {
									// Click listener on the neutral button of
									// alert box
									public void onClick(DialogInterface arg0,
											int arg1) {
										// The neutral button was clicked
										setResult(RESULT_OK);
										finish();
									}
								});
						// show the alert box
						alertbox.show();
					} catch (Exception E) {
						E.printStackTrace();
						System.out.println(E);
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								imp);
						// Set the message to display
						alertbox.setMessage("ERROR:" + E.getClass().getName()
								+ ":" + E.getMessage());
						// Add a neutral button to the alert box and assign a
						// click listener
						alertbox.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {
									// Click listener on the neutral button of
									// alert box
									public void onClick(DialogInterface arg0,
											int arg1) {
										// The neutral button was clicked
									}
								});
						// show the alert box
						alertbox.show();

					}
				} else {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(imp);
					// Set the message to display
					alertbox.setMessage("Please fill all the fields before submit");
					// Add a neutral button to the alert box and assign a
					// click listener
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								// Click listener on the neutral button of
								// alert box
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					// show the alert box
					alertbox.show();
				}
			}
		});
	}
}

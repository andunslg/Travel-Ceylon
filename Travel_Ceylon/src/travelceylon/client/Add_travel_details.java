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

public class Add_travel_details extends Activity {
	Add_travel_details atd;
	int catCount = 0;
	CheckBox cb1;
	CheckBox cb2;
	CheckBox cb3;
	CheckBox cb4;
	CheckBox cb5;
	CheckBox cb6;
	CheckBox cb7;
	CheckBox cb8;
	CheckBox cb9;
	CheckBox cb10;

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
		setContentView(R.layout.add_travel_details);
		atd=this;
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

		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb1.setVisibility(View.INVISIBLE);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb2.setVisibility(View.INVISIBLE);
		cb3 = (CheckBox) findViewById(R.id.checkBox3);
		cb3.setVisibility(View.INVISIBLE);
		cb4 = (CheckBox) findViewById(R.id.checkBox4);
		cb4.setVisibility(View.INVISIBLE);
		cb5 = (CheckBox) findViewById(R.id.checkBox5);
		cb5.setVisibility(View.INVISIBLE);
		cb6 = (CheckBox) findViewById(R.id.checkBox6);
		cb6.setVisibility(View.INVISIBLE);
		cb7 = (CheckBox) findViewById(R.id.checkBox7);
		cb7.setVisibility(View.INVISIBLE);
		cb8 = (CheckBox) findViewById(R.id.checkBox8);
		cb8.setVisibility(View.INVISIBLE);
		cb9 = (CheckBox) findViewById(R.id.checkBox9);
		cb9.setVisibility(View.INVISIBLE);
		cb10 = (CheckBox) findViewById(R.id.checkBox10);
		cb10.setVisibility(View.INVISIBLE);

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
			Log.d("Categories",categories);
			String categoriesList[] = categories.split(";");
			catCount = categoriesList.length;
			Log.d("Cat Caount",Integer.toString(catCount));
			if (catCount >= 1) {
				cb1.setText(categoriesList[0]);
				cb1.setVisibility(View.VISIBLE);
			}
			if (catCount >= 2) {
				cb2.setText(categoriesList[1]);
				cb2.setVisibility(View.VISIBLE);
			}

			if (catCount >= 3) {
				cb3.setText(categoriesList[2]);
				cb3.setVisibility(View.VISIBLE);
			}

			if (catCount >= 4) {
				cb4.setText(categoriesList[3]);
				cb4.setVisibility(View.VISIBLE);
			}

			if (catCount >= 5) {
				cb5.setText(categoriesList[4]);
				cb5.setVisibility(View.VISIBLE);
			}

			if (catCount >= 6) {
				cb6.setText(categoriesList[5]);
				cb6.setVisibility(View.VISIBLE);
			}

			if (catCount >= 7) {
				cb7.setText(categoriesList[6]);
				cb7.setVisibility(View.VISIBLE);
			}

			if (catCount >= 8) {
				cb8.setText(categoriesList[7]);
				cb8.setVisibility(View.VISIBLE);
			}

			if (catCount >= 9) {
				cb9.setText(categoriesList[8]);
				cb9.setVisibility(View.VISIBLE);
			}

			if (catCount >= 10) {
				cb10.setText(categoriesList[9]);
				cb10.setVisibility(View.VISIBLE);
			}

		} catch (Exception E) {
			E.printStackTrace();

		}
		
		

		Button b = (Button) findViewById(R.id.ButtonSendTravelDetails);
		b.setOnClickListener(new View.OnClickListener() {
			AutoCompleteTextView sCity=(AutoCompleteTextView) findViewById(R.id.autocomplete_StartingCity);
			AutoCompleteTextView dCity=(AutoCompleteTextView) findViewById(R.id.autocomplete_DestinationCity);
			EditText du=(EditText) findViewById(R.id.EditTextDuration);
			String startCity="";
			String destCity="";
			String interests="";
			String duration="";
			public void onClick(View arg0) {
				Log.d("Cliked","Clicked");
				if(sCity.getText().toString().equals("")||dCity.getText().toString().equals("")||du.getText().toString().equals("")||(!cb1.isChecked()&&!cb2.isChecked()&&!cb3.isChecked()&&!cb4.isChecked()&&!cb5.isChecked()&&!cb6.isChecked()&&!cb7.isChecked()&&!cb8.isChecked()&&!cb9.isChecked()&&!cb10.isChecked())){
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
				else{
					startCity=sCity.getText().toString();
					destCity=dCity.getText().toString();
					duration=du.getText().toString();
					if (catCount >= 1) {
						if(cb1.isChecked()){
							interests+=cb1.getText()+";";
						}
					}
					if (catCount >= 2) {
						if(cb2.isChecked()){
							interests+=cb2.getText()+";";
						}
					}

					if (catCount >= 3) {
						if(cb3.isChecked()){
							interests+=cb3.getText()+";";
						}
					}

					if (catCount >= 4) {
						if(cb4.isChecked()){
							interests+=cb4.getText()+";";
						}
					}

					if (catCount >= 5) {
						if(cb5.isChecked()){
							interests+=cb5.getText()+";";
						}
					}

					if (catCount >= 6) {
						if(cb6.isChecked()){
							interests+=cb6.getText()+";";
						}
					}

					if (catCount >= 7) {
						if(cb7.isChecked()){
							interests+=cb7.getText()+";";
						}
					}

					if (catCount >= 8) {
						if(cb8.isChecked()){
							interests+=cb8.getText()+";";
						}
					}

					if (catCount >= 9) {
						if(cb9.isChecked()){
							interests+=cb9.getText()+";";
						}
					}

					if (catCount >= 10) {
						if(cb10.isChecked()){
							interests+=cb10.getText()+";";
						}
					}
					
					METHOD_NAME = "planTheTrip";
					try {
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty("startC",startCity );
						request.addProperty("desC",destCity );
						request.addProperty("duration",duration );
						request.addProperty("interests",interests);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
						androidHttpTransport.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();
						Log.d("Done",result.toString());
						Intent i = new Intent(Add_travel_details.this,
								Show_Trip_Plan.class);
						Bundle bundle = new Bundle();
					    bundle.putString("TripPlan", result.toString());
					    i.putExtras(bundle);
					    startActivity(i);

					} catch (Exception E) {
						E.printStackTrace();
						Log.d("Error of SOAP",E.toString());

					}
					
				}				

			}
		});

	}
}

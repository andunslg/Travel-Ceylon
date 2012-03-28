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
import android.widget.Button;
import android.widget.CheckBox;

/**
 * This class will get the user's interests on the trip The starting city and
 * dest city and interest is enough to get a travel plan So in this activity
 * user can request a travel plan Also if the user want to add some more filters
 * to the trip he can go further to add them If user go forward all the data is
 * send forward as a bundle
 * 
 * @author ASLG
 * 
 */
public class Add_travel_details_interest extends Activity {
	Add_travel_details_interest atd;

	String startCity = "";
	String destCity = "";
	String interests = "";
	String duration = "";

	Bundle params;

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
	CheckBox advOption;

	private String METHOD_NAME = "";
	// Travel Ceylon web service method name
	private String NAMESPACE = "http://ws.travel_ceylon.web.org";
	// Here package name in web service with reverse order.
	private String SOAP_ACTION = NAMESPACE + METHOD_NAME;
	// NAMESPACE + method name
	private static final String URL = "http://10.0.2.2:8080/Travel_Ceylon_Web_Service/services/Travel_Ceylon_Web_Service?wsdl";

	// Location for the wsdl file

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.add_travel_details_interests);
		atd = this;
		params = getIntent().getExtras();
		startCity = params.getString("Start_City");
		destCity = params.getString("Dest_City");
		duration = params.getString("Due");

		/*
		 * The list of interest is dynamic So the check boxes shown are also
		 * dynamic So the mechanism implemented is showing predefined number of
		 * check boxes and hide the extra ones So here ten check boxes are shown
		 * and after getting the list of interests from the web services
		 * remaining check boxes will be hide
		 */
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

		/*
		 * This web service call will get the interest category list from the
		 * web service
		 */
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
			Log.d("Categories", categories);
			String categoriesList[] = categories.split(";");
			catCount = categoriesList.length;

			/*
			 * This if ladder will show needed number of check boxes according
			 * to the interest list from the web service
			 */
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

		/*
		 * This button will guide user to the next screen which shows advanced
		 * options
		 */
		Button b1 = (Button) findViewById(R.id.ButtonGoToAdvancedOptions);
		b1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				/*
				 * This if will check weather at least one interest is selected
				 * If not it will show a message box
				 */
				if ((!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()
						&& !cb4.isChecked() && !cb5.isChecked()
						&& !cb6.isChecked() && !cb7.isChecked()
						&& !cb8.isChecked() && !cb9.isChecked() && !cb10
						.isChecked())) {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("Please tick at least one Interest");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				}
				/*
				 * If at least one interest is selected then the data is sent to
				 * the nest screen in a Bundle
				 */
				else {
					interests = "";
					if (catCount >= 1) {
						if (cb1.isChecked()) {
							interests += cb1.getText() + ";";
						}
					}
					if (catCount >= 2) {
						if (cb2.isChecked()) {
							interests += cb2.getText() + ";";
						}
					}

					if (catCount >= 3) {
						if (cb3.isChecked()) {
							interests += cb3.getText() + ";";
						}
					}

					if (catCount >= 4) {
						if (cb4.isChecked()) {
							interests += cb4.getText() + ";";
						}
					}

					if (catCount >= 5) {
						if (cb5.isChecked()) {
							interests += cb5.getText() + ";";
						}
					}

					if (catCount >= 6) {
						if (cb6.isChecked()) {
							interests += cb6.getText() + ";";
						}
					}

					if (catCount >= 7) {
						if (cb7.isChecked()) {
							interests += cb7.getText() + ";";
						}
					}

					if (catCount >= 8) {
						if (cb8.isChecked()) {
							interests += cb8.getText() + ";";
						}
					}

					if (catCount >= 9) {
						if (cb9.isChecked()) {
							interests += cb9.getText() + ";";
						}
					}

					if (catCount >= 10) {
						if (cb10.isChecked()) {
							interests += cb10.getText() + ";";
						}
					}
					final Intent i = new Intent(
							Add_travel_details_interest.this,
							Add_travel_details_should_visit.class);
					Bundle bundle = params;
					bundle.putString("Interests", interests);
					i.putExtras(bundle);

					/*
					 * This Message box is shown to verify the sent data by the
					 * user
					 */
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					String tInt = interests.replace(";", " ");
					alertbox.setMessage("Your list of interest is " + tInt
							+ ".Proceed further ?");
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
		 * This button will send data to the web service to get trip plans After
		 * getting the trip plan user will be guided to the map
		 */
		Button b = (Button) findViewById(R.id.ButtonSendTravelDetails);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				/*
				 * This if will check weather at least one interest is selected
				 * If not it will show a message box
				 */
				if ((!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()
						&& !cb4.isChecked() && !cb5.isChecked()
						&& !cb6.isChecked() && !cb7.isChecked()
						&& !cb8.isChecked() && !cb9.isChecked() && !cb10
						.isChecked())) {
					AlertDialog.Builder alertbox = new AlertDialog.Builder(atd);
					alertbox.setMessage("Please tick at least one Interest");
					alertbox.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					alertbox.show();
				}
				/*
				 * If at least one interest is selected then the data is sent to
				 * the web service for get the trip plan
				 */
				else {
					if (catCount >= 1) {
						if (cb1.isChecked()) {
							interests += cb1.getText() + ";";
						}
					}
					if (catCount >= 2) {
						if (cb2.isChecked()) {
							interests += cb2.getText() + ";";
						}
					}

					if (catCount >= 3) {
						if (cb3.isChecked()) {
							interests += cb3.getText() + ";";
						}
					}

					if (catCount >= 4) {
						if (cb4.isChecked()) {
							interests += cb4.getText() + ";";
						}
					}

					if (catCount >= 5) {
						if (cb5.isChecked()) {
							interests += cb5.getText() + ";";
						}
					}

					if (catCount >= 6) {
						if (cb6.isChecked()) {
							interests += cb6.getText() + ";";
						}
					}

					if (catCount >= 7) {
						if (cb7.isChecked()) {
							interests += cb7.getText() + ";";
						}
					}

					if (catCount >= 8) {
						if (cb8.isChecked()) {
							interests += cb8.getText() + ";";
						}
					}

					if (catCount >= 9) {
						if (cb9.isChecked()) {
							interests += cb9.getText() + ";";
						}
					}

					if (catCount >= 10) {
						if (cb10.isChecked()) {
							interests += cb10.getText() + ";";
						}
					}

					/*
					 * This is the web service call for get the trip plan All
					 * the requested properties are added to the SOAP object The
					 * properties which are not applicable on this point will be
					 * sent as "" strings. Those will be manupilated at the wqeb
					 * service
					 */
					METHOD_NAME = "planTheTrip";
					try {
						SoapObject request = new SoapObject(NAMESPACE,
								METHOD_NAME);
						request.addProperty("startC", startCity);
						request.addProperty("desC", destCity);
						request.addProperty("duration", duration);
						request.addProperty("interests", interests);
						request.addProperty("shouldInclude", "");
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
						 * When the web service sends the trip plan it is passed
						 * to the trip plan showing UI
						 */
						Intent i = new Intent(Add_travel_details_interest.this,
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

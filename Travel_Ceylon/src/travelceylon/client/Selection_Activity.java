package travelceylon.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This is the main selection screen of the Travel Ceylon According to the users
 * choice they will guided to different interfaces.
 * 
 * @author ASLG
 * 
 */
public class Selection_Activity extends Activity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.selection_screen);

		Button b = (Button) findViewById(R.id.buttonSubmitPlaceDetails);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Selection_Activity.this,
						Important_place_add_Activity.class);
				startActivity(i);
			}
		});
		Button b1 = (Button) findViewById(R.id.buttonPlanATravel);
		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Selection_Activity.this,
						Plan_a_trip_Guide.class);
				startActivity(i);
			}
		});
		Button b2 = (Button) findViewById(R.id.buttonShowCities);
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Selection_Activity.this,
						Show_Cities.class);
				startActivity(i);
			}
		});
		Button b3 = (Button) findViewById(R.id.buttonHelp);
		b3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Selection_Activity.this, Help.class);
				startActivity(i);
			}
		});
	}
}

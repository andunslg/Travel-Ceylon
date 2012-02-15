package travelceylon.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Selection_Activity extends Activity {
	 public void onCreate(Bundle icicle)
	   {
	      super.onCreate(icicle);
	      setContentView(R.layout.selection_screen);
	     

	      Button b = (Button) findViewById(R.id.buttonSubmitPlaceDetails);
	        b.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View arg0) {
	           Intent i = new Intent(Selection_Activity.this, Important_place_add_Activity.class);
	           startActivity(i);
	           }
	        });
	        Button b1 = (Button) findViewById(R.id.buttonPlanATravel);
	        b1.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View arg0) {
	           Intent i = new Intent(Selection_Activity.this, Plan_a_trip_Guide.class);
	           startActivity(i);
	           }
	        });
	   }
}



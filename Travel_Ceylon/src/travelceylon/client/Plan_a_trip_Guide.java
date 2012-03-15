package travelceylon.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Plan_a_trip_Guide  extends Activity  {
	public void onCreate(Bundle icicle)
	   {
	      super.onCreate(icicle);
	      setContentView(R.layout.plan_a_trip_guide);
	      Button b = (Button) findViewById(R.id.buttonPlantheTravel);
	        b.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View arg0) {
	           Intent i = new Intent(Plan_a_trip_Guide.this, Add_travel_details_start.class);
	           startActivity(i);
	           }
	        });
	   }
}

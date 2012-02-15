package travelceylon.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Add_travel_details  extends Activity  {
	public void onCreate(Bundle icicle)
	   {
	      super.onCreate(icicle);
	      setContentView(R.layout.add_travel_details);
	      Button b = (Button) findViewById(R.id.ButtonSendTravelDetails);
	        b.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View arg0) {
	           Intent i = new Intent(Add_travel_details.this, Important_place_addActivity.class);
	           startActivity(i);
	           }
	        });
	   }
}

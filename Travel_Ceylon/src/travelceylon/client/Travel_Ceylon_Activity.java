package travelceylon.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Travel_Ceylon_Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button b = (Button) findViewById(R.id.buttonEnterTravelCeylon);
        b.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
           Intent i = new Intent(Travel_Ceylon_Activity.this, Selection_Activity.class);
           i.putExtra("name","Andun");
           startActivity(i);
           }
        });
    }
}
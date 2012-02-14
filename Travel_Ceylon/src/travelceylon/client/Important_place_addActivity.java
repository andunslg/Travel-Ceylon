package travelceylon.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Important_place_addActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.important_place_add);

	    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.category_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
	    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
	            this, R.array.category_array, android.R.layout.simple_spinner_item);
	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner2.setAdapter(adapter2);
	    
	    Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
	    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
	            this, R.array.category_array, android.R.layout.simple_spinner_item);
	    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner3.setAdapter(adapter3);
	}
}

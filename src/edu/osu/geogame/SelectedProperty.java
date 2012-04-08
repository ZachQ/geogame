package edu.osu.geogame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class SelectedProperty extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.selectedproperty);
	    
	    Bundle extras = getIntent().getExtras();
	    if(extras !=null) {
	    	String value = extras.getString("name");
	    	
	    	Toast.makeText(getApplicationContext(),
					value, Toast.LENGTH_SHORT).show();
	    }
	}

}

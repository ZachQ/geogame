package edu.osu.geogame;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class TransactionActivity extends Activity {

	int id, action;
	TextView titleView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.transaction);
	    
	    // Get the data from the market intent
	    Bundle extras = getIntent().getExtras(); 
	    if(extras != null)
	    {
	    	id = extras.getInt("id");
	    	action = extras.getInt("action");
	    }
	    
	    String title = "";
	    
	    switch (action) {
	    	case 0: title = "Buying "; break;
	    	case 1: title = "Selling "; break;
	    	case 2: title = "Auctioning "; break;
	    }
	    
	    switch (id) {
	    	case 0: title += "SeedLR"; break;
	    	case 1: title += "SeedHYC"; break;
	    	case 2: title += "Fertilizer"; break;
	    	case 3: title += "Water"; break;
	    	case 4: title += "Oxen"; break;
	    	case 5: title += "Labor"; break;
	    }
	    
	    // Set the text of the title
	    titleView = (TextView) findViewById(R.id.transaction_title);
	    titleView.setText(title);
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

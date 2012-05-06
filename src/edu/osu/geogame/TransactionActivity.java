package edu.osu.geogame;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class TransactionActivity extends Activity {

	int id, action;
	TextView titleView, amount;
	SeekBar bar;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.transaction);
	    
	    amount = (TextView)findViewById(R.id.market_amount);
	    
	    // Grab the bar
	    bar = (SeekBar)findViewById(R.id.market_progressBar);
	    // Set listener to update cost/amount
	    bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				amount.setText(String.valueOf(progress));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
	    
	    // Get the data from the market intent
	    Bundle extras = getIntent().getExtras(); 
	    if(extras != null)
	    {
	    	id = extras.getInt("id");
	    }
	    
	    // Set the screen title
	    String title = "";
	    int cost = 1;
	    switch (id) {
	    	case 0: 
	    		title = "SeedLR"; 
	    		cost = GeoGame.costSeedLR;
	    		break;
	    	case 1: title = "SeedHYC"; break;
	    	case 2: title = "Fertilizer"; break;
	    	case 3: title = "Water"; break;
	    	case 4: title = "Oxen"; break;
	    	case 5: title = "Labor"; break;
	    }
	    
	    // Set the text of the title
	    titleView = (TextView) findViewById(R.id.transaction_title);
	    titleView.setText(title);
	    
	    // Calculate the range for the slider
	    try {	    	
	    	int max = new Integer(GeoGame.money) / cost;
	    	bar.setMax(max);
	    } catch (Exception e) {}
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

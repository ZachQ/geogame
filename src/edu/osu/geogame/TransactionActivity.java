package edu.osu.geogame;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TransactionActivity extends Activity {

	private int id, action;
	private TextView titleView, amount;
	private SeekBar bar;
	private Button buyButton, sellButton, auctionButton;
	private LinearLayout buttonArea;
	private Handler mHandler;
	private String category;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.transaction);
	    mHandler = new Handler();
	    
	    // Get the data from the market intent
	    Bundle extras = getIntent().getExtras(); 
	    if(extras != null)
	    {
	    	id = extras.getInt("id");
	    }
	    
	    // Create the buttons
	    buildButtons();
	    // Find where buttons go
	    buttonArea = (LinearLayout)findViewById(R.id.market_buttonArea);
	    // Get resource cost
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
	    
	    // Set the screen attributes
	    int cost = 1;
	    switch (id) {
	    	case 0: 
	    		category = "SeedLR"; 
	    		cost = GeoGame.costSeedLR;
	    		buttonArea.addView(buyButton);
	    		break;
	    	case 1: category = "SeedHYC"; break;
	    	case 2: category = "Fertilizer"; break;
	    	case 3: category = "Water"; break;
	    	case 4: category = "Oxen"; break;
	    	case 5: category = "Labor"; break;
	    }
	    
	    // Set the text of the title
	    titleView = (TextView) findViewById(R.id.transaction_title);
	    titleView.setText(category);
	    
	    // Calculate the range for the slider
	    try {	    	
	    	int max = new Integer(GeoGame.money) / cost;
	    	bar.setMax(max);
	    } catch (Exception e) {}
	}
	
	/**
	 * 
	 */
	private void buildButtons() {
		// Create the buttons
		buyButton = new Button(this);
		buyButton.setText("Buy");
		buyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create the thread
				Thread commit = new commitThread();
			    // Run the thread
			    commit.start();
			}
		});
		
		buyButton.setVisibility(View.VISIBLE);
		
		sellButton = new Button(this);
		sellButton.setText("Sell");
		sellButton.setVisibility(View.VISIBLE);
		
	}
	
	public class commitThread extends Thread {
		@Override
		public void run() {
			// Build URL for buy
			RestClient client = new RestClient(
					GeoGame.URL_MARKET + "Buy/" + GeoGame.currentGameId + "/" + 
					category + "/-1/" + String.valueOf(bar.getProgress()));
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONObject a;
			try {
				client.Execute(RequestMethod.POST);
			} catch (Exception e) {} finally {
				try {
					j = new JSONObject(client.getResponse());
					a = (JSONObject) j.get("dash");
					
					// Update the global resource values
					GeoGame.money = a.getString("money");
					GeoGame.seedLR = a.getString("seedLR");
					GeoGame.seedHYC = a.getString("seedHYC");
					GeoGame.fertilizer = a.getString("fertilizer");
					GeoGame.water = a.getString("water");
					
					mHandler.post(Success);
					if (j.getBoolean("success") == true) {
						
					} else {	
						// Failure
						mHandler.post(showErrorMessage);
					}
				} catch (Exception e) {}
			}
        }
    };

	private Runnable Success = new Runnable(){
        public void run(){
        	Toast.makeText(getApplicationContext(),
					"Success", Toast.LENGTH_SHORT).show();
        	
        	finish();
        }
    };
    
    private Runnable showErrorMessage = new Runnable(){
        public void run(){
        	Toast.makeText(getApplicationContext(),
					"Error", Toast.LENGTH_SHORT).show();
        }
    };
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

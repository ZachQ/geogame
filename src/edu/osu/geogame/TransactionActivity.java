package edu.osu.geogame;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionActivity extends Activity {

	private int id, cost = 1;
	private TextView titleView, amount;
	private SeekBar bar;
	private Button commitButton;
	private Handler mHandler;
	private String category, resourceCount;
	private Context myContext;
	private boolean radioBuy;
	RadioButton rBuy, rSell;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.transaction);
	    mHandler = new Handler();
	    myContext = this;
	    rBuy = (RadioButton)findViewById(R.id.radioBuy);
	    rSell = (RadioButton)findViewById(R.id.radioSell);
	    
	    // Get the data from the market intent
	    Bundle extras = getIntent().getExtras(); 
	    if(extras != null)
	    {
	    	id = extras.getInt("id");
	    }
	    
	    // Create the button listener
	    buildButtons();

	    // Get resource transaction cost
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
	    switch (id) {
	    	case 0: 
	    		category = "SeedLR"; 
	    		cost = GeoGame.costSeedLR;
	    		resourceCount = GeoGame.seedLR;
	    		rBuy.toggle();
	    		break;
	    	case 1: category = "SeedHYC";
		    	cost = GeoGame.costSeedHYC;
		    	resourceCount = GeoGame.seedHYC;
		    	rBuy.toggle();
	    		break;
	    	case 2: category = "Fertilizer";
		    	cost = GeoGame.costFertilizer;
		    	resourceCount = GeoGame.fertilizer;
		    	rBuy.toggle();
		    	break;
	    	case 3: category = "Water"; 
		    	cost = GeoGame.costWater;
		    	resourceCount = GeoGame.water;
		    	rBuy.toggle();
		    	break;
	    	case 4: category = "Oxen"; 
		    	cost = GeoGame.costOxen;
		    	resourceCount = GeoGame.oxen;
		    	rBuy.toggle();
		    	break;
	    	case 5: category = "Labor";
	    		// Labor is weird since we have to sell whole people.
		    	rSell.setEnabled(false);
		    	rBuy.setEnabled(false);
	    		break;
	    	case 6: category = "GrainLR"; 
		    	cost = GeoGame.costGrainLR;
		    	resourceCount = GeoGame.grainLR;
		    	rSell.toggle();
		    	rBuy.setEnabled(false);
		    	break;
	    	case 7: category = "GrainHYC"; 
		    	cost = GeoGame.costGrainHYC;
		    	resourceCount = GeoGame.grainHYC;
		    	rSell.toggle();
		    	rBuy.setEnabled(false);
		    	break;
	    }
	    
	    // Set the title
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
		commitButton = (Button)findViewById(R.id.market_commitButton);
		commitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Check for buy or sell
				if (radioBuy) {// Do Buy
					// Create the thread
					Thread commit = new CommitBuyThread();
				    // Run the thread
				    commit.start();
				} else {// Do Sell
					// Get the asking price
					AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
					alert.setTitle("Auction");
					alert.setMessage("Enter selling price:");
					// Set an EditText view to get user input 
					final EditText input = new EditText(getApplicationContext());
					alert.setView(input);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString();
							
							/* **************************************
							* TODO Check Legal value here
							******************************************/
							
						  	// Create the thread
							Thread commit = new CommitSellThread(value);
						    // Run the thread
						    commit.start();
						  }});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled
						    mHandler.post(finish);
						  }});
					alert.show();
				}
			}
		});
	    
	    // Add listeners
	    rBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					radioBuy = true;
					// Calculate the range for the slider
				    try {	    	
				    	int max = new Integer(GeoGame.money) / cost;
				    	bar.setMax(max);
				    } catch (Exception e) {}
				}
			}
		});
	    rSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					radioBuy = false;
					// Calculate the range for the slider
				    try {	    	
				    	int max = new Integer(resourceCount);
				    	bar.setMax(max);
				    } catch (Exception e) {}
				}
			}
		});
	}
	
	public class CommitBuyThread extends Thread {
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
    
    public class CommitSellThread extends Thread {
    	String price;
    	public CommitSellThread(String price) {
    		this.price = price;
    	}
		@Override
		public void run() {	
			// Build URL for buy
			RestClient client = new RestClient(
					GeoGame.URL_MARKET + "Sell/" + GeoGame.currentGameId + "/" + 
					category + "/" + String.valueOf(bar.getProgress()) + "/0");
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
    
    private Runnable finish = new Runnable(){
        public void run(){
        	finish();
        }
    };
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

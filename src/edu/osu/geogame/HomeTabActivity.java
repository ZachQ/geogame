package edu.osu.geogame;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class HomeTabActivity extends Activity {
	TextView name, money, adults, labor, seedLR, seedHYC, fertilizer, water, grainLR, grainHYC, oxen, 
			timerTV, turnTV;
	private Handler mHandler;
	private RestClient gameTimeUpdater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_tab);
		
		// Get references
		name = (TextView) findViewById(R.id.textViewName);
		money = (TextView) findViewById(R.id.textViewMoney);
		adults = (TextView) findViewById(R.id.textViewAdults);
		labor = (TextView) findViewById(R.id.textViewLabor);
		seedLR = (TextView) findViewById(R.id.textViewSeedLR);
		seedHYC = (TextView) findViewById(R.id.textViewSeedHYC);
		fertilizer = (TextView) findViewById(R.id.textViewFertilizer);
		water = (TextView) findViewById(R.id.textViewWater);
		grainLR = (TextView) findViewById(R.id.textViewGrainLR);
		grainHYC = (TextView) findViewById(R.id.textViewGrainHYC);
		oxen = (TextView) findViewById(R.id.textViewOxen);
		timerTV = (TextView) findViewById(R.id.gameTimer);
		turnTV = (TextView) findViewById(R.id.gameTurn);
		
		// Get the game data, then update the home display
		mHandler = new Handler();
		//Thread update = new updateThread();
		//update.start();
	}

	public class updateThread extends Thread {
		@Override
		public void run() {
			RestClient g_client = new RestClient(GeoGame.URL_GAME + "Dash/" + GeoGame.currentGameId);
			RestClient m_client = new RestClient(GeoGame.URL_MARKET + "myBank/" + GeoGame.currentGameId);
			RestClient cost_client = new RestClient(GeoGame.URL_MARKET + "Get/" + GeoGame.currentGameId + "/bank");
			RestClient game_time = new RestClient(GeoGame.URL_TIME + GeoGame.currentGameId);
			g_client.addCookie(GeoGame.sessionCookie);
			m_client.addCookie(GeoGame.sessionCookie);
			cost_client.addCookie(GeoGame.sessionCookie);
			game_time.addCookie(GeoGame.sessionCookie);
			gameTimeUpdater = game_time;
			try {
				g_client.Execute(RequestMethod.POST);
				m_client.Execute(RequestMethod.POST);
				cost_client.Execute(RequestMethod.POST);
				game_time.Execute(RequestMethod.POST);
			} catch (Exception e) {
				Log.i("HomeTabActivity", "Post Failed");
			} finally {
				JSONObject j;
				JSONObject data, market, family, cost;
				String time = null, turn = null, status;
				try {
					// Get the data
					j = new JSONObject(g_client.getResponse());
					data = (JSONObject) j.get("data");
					j = new JSONObject(m_client.getResponse());
					market = (JSONObject) j.get("data");
					family = (JSONObject) j.get("family");
					j = new JSONObject(cost_client.getResponse());
					cost = (JSONObject) j.get("bank");
					
					// Time results
					j = new JSONObject(game_time.getResponse());
					status = j.getString("status");
					// if status = "end" then the other variables will not be returned
					if (!status.equals("end")) {
						turn = j.getString("turn");
						time = j.getString("timer");
					}
					
					String timeReadable = convertTime(time);
					
					// Update the global resource values
					GeoGame.familyName = family.getString("name");
					GeoGame.money = data.getString("money");
					GeoGame.adults = family.getString("adults");
					GeoGame.seedLR = market.getString("SeedLR");
					GeoGame.seedHYC = market.getString("SeedHYC");
					GeoGame.fertilizer = market.getString("Fertilizer");
					GeoGame.grainLR = market.getString("GrainLR");
					GeoGame.grainHYC = market.getString("GrainHYC");
					GeoGame.water = market.getString("Water");
					GeoGame.oxen = market.getString("Oxen");
					GeoGame.timer = timeReadable;
					GeoGame.turn = turn;
					GeoGame.status = status;
					
					// Do the actual screen update
					mHandler.post(showUpdate);
					mHandler.removeCallbacks(updateTime);
			        mHandler.postDelayed(updateTime, 1000);

					
					// Store the prices
					GeoGame.costSeedLR = cost.getInt("SeedLR");
					GeoGame.costSeedHYC = cost.getInt("SeedHYC");
					GeoGame.costFertilizer = cost.getInt("Fertilizer");
					GeoGame.costWater = cost.getInt("Water");
					GeoGame.costOxen = cost.getInt("Oxen");
					
				} catch (Exception e) {
					Log.i("HomeTabActivity","Failed to get JSON");
				}
			}
		}
	}
	
	private Runnable showUpdate = new Runnable(){
        public void run(){
			// Update
			name.setText(GeoGame.familyName + " Family");
			money.setText("$" + GeoGame.money);
			adults.setText(GeoGame.adults);
			// TODO: labor needs to be added
			seedLR.setText(GeoGame.seedLR);
			seedHYC.setText(GeoGame.seedHYC);
			fertilizer.setText(GeoGame.fertilizer);
			water.setText(GeoGame.water);
			grainLR.setText(GeoGame.grainLR);
			grainHYC.setText(GeoGame.grainHYC);
			oxen.setText(GeoGame.oxen);
			timerTV.setText("Time left: "+GeoGame.timer);
			turnTV.setText("Turn: "+GeoGame.turn);
        }
    };
    
    /**
     * This is used to update the timer in real time without updating the entire page
     * to see the current time left in game.
     */
    private Runnable updateTime = new Runnable(){
    	public void run(){
    		String time;
    		timerTV.setText("Time left: "+GeoGame.timer);
    		try {
				gameTimeUpdater.Execute(RequestMethod.POST);
			} catch (Exception e) {
				Log.i("gameTimeUpdater","Failed to POST");
			} finally {
				JSONObject j;
				try {
					j = new JSONObject(gameTimeUpdater.getResponse());
					time = j.getString("timer");
					
					String timeReadable = convertTime(time);
					
					GeoGame.timer = timeReadable;
				} catch (JSONException e) {
				}
				
			}
    		mHandler.postAtTime(showUpdate, 1000);
    	}
    };
    
    @Override
    protected void onResume() {
    	// Update the home page every time the user looks at it
    	// From server
    	super.onResume();
    	Thread update = new updateThread();
		update.start();
    }
    
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
	/**
	 * This method converts the miliseconds into readable format
	 * @param time
	 * @return
	 */
	private String convertTime(String time) {
		String result = "00:00:00";
		NumberFormat formatter = new DecimalFormat("#");
		double temp = Double.parseDouble(time);
		
		long millis = Long.parseLong(formatter.format(temp).toString());
	    int seconds = (int) (millis / 1000);
	    int minutes = seconds / 60;
	    int hours = minutes / 60;
	    seconds = seconds % 60;
	    minutes = minutes % 60;
	    
	    if(hours < 10){
	    	result = "0" + hours + ":";
	    } else {
	    	result = "" + hours + ":";
	    }
	    
	    if(minutes < 10){
	    	result = result + "0" + minutes + ":";
	    } else {
	    	result = result + "" + minutes + ":";
	    }
	    
	    if(seconds < 10){
	    	result = result + "0" + seconds;
	    } else {
	    	result = result + "" + seconds;
	    }
	    return result;
	}
	
}

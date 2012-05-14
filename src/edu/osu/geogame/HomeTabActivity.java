package edu.osu.geogame;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class HomeTabActivity extends Activity {
	TextView name, money, adults, labor, seedLR, seedHYC, fertilizer, water, grainLR, grainHYC, oxen;
	private Handler mHandler;
	
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
			g_client.addCookie(GeoGame.sessionCookie);
			m_client.addCookie(GeoGame.sessionCookie);
			cost_client.addCookie(GeoGame.sessionCookie);
			try {
				g_client.Execute(RequestMethod.POST);
				m_client.Execute(RequestMethod.POST);
				cost_client.Execute(RequestMethod.POST);
			} catch (Exception e) {} finally {
				JSONObject j;
				JSONObject data, market, family, cost;
				try {
					// Get the data
					j = new JSONObject(g_client.getResponse());
					data = (JSONObject) j.get("data");
					j = new JSONObject(m_client.getResponse());
					market = (JSONObject) j.get("data");
					family = (JSONObject) j.get("family");
					j = new JSONObject(cost_client.getResponse());
					cost = (JSONObject) j.get("bank");
					
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
					
					// Do the actual screen update
					mHandler.post(showUpdate);
					
					// Store the prices
					GeoGame.costSeedLR = cost.getInt("SeedLR");
					GeoGame.costGrainHYC = cost.getInt("SeedHYC");
					GeoGame.costFertilizer = cost.getInt("Fertilizer");
					GeoGame.costWater = cost.getInt("Water");
					GeoGame.costOxen = cost.getInt("Oxen");
					
				} catch (Exception e) {}
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
}

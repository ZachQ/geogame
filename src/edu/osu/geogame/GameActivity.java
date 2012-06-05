package edu.osu.geogame;


import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TabHost;

/**
 * This is the in-game screen.  It hosts 5 tabs (see below) 
 *
 */
public class GameActivity extends TabActivity {

	private TabHost tabs;
	private Handler mHandler;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		tabs = getTabHost();
		TabHost.TabSpec tabSpec;
		
		//Add Overview Tab
		tabSpec = 	tabs.newTabSpec("Home")
					.setIndicator("Home", getResources().getDrawable(R.drawable.home))
					.setContent( new Intent( this, HomeTabActivity.class) );
		tabs.addTab(tabSpec);
		
		//Add Market Tab
		tabSpec = 	tabs.newTabSpec("Market")
					.setIndicator("Market", getResources().getDrawable(R.drawable.market))
					.setContent( new Intent( this, MarketTabActivity.class) );
		tabs.addTab(tabSpec);
		
		//Add Map Tab
		tabSpec = 	tabs.newTabSpec("Map")
					.setIndicator("Map", getResources().getDrawable(R.drawable.map))
					.setContent( new Intent( this, MapTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		//Add Scoreboard Tab
		tabSpec = 	tabs.newTabSpec("Score")
					.setIndicator("Score", getResources().getDrawable(R.drawable.trophy))
					.setContent( new Intent( this, ScoreboardTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		tabSpec = 	tabs.newTabSpec("Forum")
				.setIndicator("Forum", getResources().getDrawable(R.drawable.forum))
				.setContent( new Intent( this, ForumTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		tabs.setCurrentTab(1);
		
		Thread update = new updateThread();
		update.start();
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
					
					// Store the prices
					GeoGame.costSeedLR = cost.getInt("SeedLR");
					GeoGame.costSeedHYC = cost.getInt("SeedHYC");
					GeoGame.costFertilizer = cost.getInt("Fertilizer");
					GeoGame.costWater = cost.getInt("Water");
					GeoGame.costOxen = cost.getInt("Oxen");
				} catch (Exception e) {}
			}
		}
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
}

package edu.osu.geogame;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class HomeTabActivity extends Activity {
	TextView name, money, adults, labor, seedLR, seedHYC, fertilizer, water, grainLR, grainHYC, oxen;
	GeoGame game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_tab);
		game = (GeoGame)getApplicationContext();
		
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
		
		update();
	}

	private void update() {
		RestClient g_client = new RestClient(game.URL_GAME + "Dash/" + game.currentGameId);
		RestClient m_client = new RestClient(game.URL_MARKET + "myBank/" + game.currentGameId);
		g_client.addCookie(game.sessionCookie);
		m_client.addCookie(game.sessionCookie);
		try {
			g_client.Execute(RequestMethod.POST);
			m_client.Execute(RequestMethod.POST);
		} catch (Exception e) {} finally {
			JSONObject j;
			JSONObject data, market, family;
			
			try {
				j = new JSONObject(g_client.getResponse());
				data = (JSONObject) j.get("data");
				j = new JSONObject(m_client.getResponse());
				market = (JSONObject) j.get("data");
				family = (JSONObject) j.get("family");
				
				// Update
				name.setText(family.getString("name") + " Family");
				money.setText("$" + data.getString("money"));
				adults.setText(family.getString("adults"));
				//labor
				seedLR.setText(market.getString("SeedLR"));
				seedHYC.setText(market.getString("SeedHYC"));
				fertilizer.setText(market.getString("Fertilizer"));
				water.setText(market.getString("Water"));
				grainLR.setText(market.getString("GrainLR"));
				grainHYC.setText(market.getString("GrainHYC"));
				oxen.setText(market.getString("Oxen"));
			} catch (Exception e) {}
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

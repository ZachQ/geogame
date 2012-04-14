package edu.osu.geogame;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Home extends Activity {
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
		RestClient client = new RestClient(game.URL_GAME + "/Dash/" + game.currentGameId);
		client.addCookie(game.sessionCookie);
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {} finally {
			JSONObject j;
			JSONObject data;
			
			try {
				j = new JSONObject(client.getResponse());
				data = (JSONObject) j.get("data");
				
				// Update Fields
				name.setText(game.username);
				money.setText("$" + data.getString("money"));
				//adults
				//labor
				seedLR.setText(data.getString("seedLR"));
				seedHYC.setText(data.getString("seedHYC"));
				fertilizer.setText(data.getString("fertilizer"));
				water.setText(data.getString("water"));
				//grainLR
				//grainHYC
				//oxen

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

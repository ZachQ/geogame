package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Home extends Activity {

	Button map, myProperty, market, family;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO:: Here we should check if the player needs to select their family
		
		setContentView(R.layout.home);
		
		// Get references
		map = (Button) findViewById(R.id.buttonMap);
		myProperty = (Button) findViewById(R.id.buttonMyProperty);
		market = (Button) findViewById(R.id.buttonMarket);
		family = (Button) findViewById(R.id.buttonFamily);
		
		// Add ActionListeners to buttons
		map.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Map.class);
                startActivityForResult(myIntent, 0);
			}
		});
		// Add ActionListeners to buttons
		myProperty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyProperty.class);
                startActivityForResult(myIntent, 0);
			}
		});
		// Add ActionListeners to buttons
		market.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Market.class);
                startActivityForResult(myIntent, 0);
			}
		});
		// Add ActionListeners to buttons
		family.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Family.class);
                startActivityForResult(myIntent, 0);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * This will change the color format of the Activity so that
	 * the background gradient will be very smooth.  Without this
	 * it has noticeable color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

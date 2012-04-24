package edu.osu.geogame;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MarketTabActivity extends Activity implements OnClickListener {
	
	private EditText quantity_SeedLR, quantity_SeedHYC, 
					quantity_Fertilizer, quantity_Water, quantity_Oxen = null;
	
	private TextView price_SeedLR, price_SeedHYC, price_quantity_Fertilizer,
						price_Water, price_Oxen = null;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_tab);
		
		/*
		 * Produce the TextViews that holds the prices of the items, so that
		 * they can be set to the prices retrieved from the server
		 */
		price_SeedLR = (TextView) findViewById(R.id.SeedLRPrice);
		price_SeedLR = (TextView) findViewById(R.id.SeedLRPrice);
		price_SeedLR = (TextView) findViewById(R.id.SeedLRPrice);
		price_SeedLR = (TextView) findViewById(R.id.SeedLRPrice);
		price_SeedLR = (TextView) findViewById(R.id.SeedLRPrice);
		
		
		/*
		 * Produce the EditTexts that the user enters purchase quantities into,
		 * so that the app can react appropriately
		 */
		quantity_SeedLR = (EditText) findViewById(R.id.SeedLRQuantity);
		quantity_SeedHYC = (EditText) findViewById(R.id.SeedHYCQuantity);
		quantity_Fertilizer = (EditText) findViewById(R.id.FertilizerQuantity);
		quantity_Water = (EditText) findViewById(R.id.WaterQuantity);
		quantity_Oxen = (EditText) findViewById(R.id.OxenQuantity);
		
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
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
    protected void onResume() {
		super.onResume();
	}

	@Override
    protected void onStop() {
		super.onStop();
	}

	@Override
    protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
			case R.id.SeedLRBuy:
				//do something
			case R.id.SeedHYCBuy:
				//do something
			case R.id.FertilizerBuy:
				//do something
			case R.id.WaterBuy:
				//do something
			case R.id.OxenBuy:
				//do something
		}
		
	}
	
	
}

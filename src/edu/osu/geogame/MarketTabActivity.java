package edu.osu.geogame;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MarketTabActivity extends Activity {
	
	private ViewPager vPager;
	private ScrollerAdapter sAdapter;
	private View buyMarketView = null;
	private View playerMarketView = null;
	private View sellItemsView = null;
	
	private ImageButton seedLR, seedHYC, fertilizer, water, ox, labor;
		
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_scroller_mech);
	
		vPager = (ViewPager) findViewById(R.id.viewpager);
		sAdapter = new ScrollerAdapter();
		vPager.setAdapter(sAdapter);		
		vPager.setCurrentItem(1);
		
		// Create views
		buyMarketView = marketView();
		playerMarketView = playerMarketView();
		sellItemsView = sellItemsView();
	}
	
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
	
	/*
	 * The view where users buy stuff from the computerized market
	 */
	private View marketView() {
		
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View marketView = inflater.inflate(R.layout.market_tab_1,null);
		
		seedLR = (ImageButton) marketView.findViewById(R.id.SeedLRButton);
		seedHYC = (ImageButton) marketView.findViewById(R.id.SeedHYCButton);
		fertilizer = (ImageButton) marketView.findViewById(R.id.FertilizerButton);
		water = (ImageButton) marketView.findViewById(R.id.WaterButton);
		ox  = (ImageButton) marketView.findViewById(R.id.OxButton);
		labor = (ImageButton) marketView.findViewById(R.id.LaborButton);
		
		// Add onClick with button ID to pass
		seedLR.setOnClickListener(new MarketListener(0,0));
		seedHYC.setOnClickListener(new MarketListener(1,0));
		fertilizer.setOnClickListener(new MarketListener(2,0));
		water.setOnClickListener(new MarketListener(3,0));
		ox.setOnClickListener(new MarketListener(4,0));
		labor.setOnClickListener(new MarketListener(5,0));

		return marketView;
	}
	
	private class MarketListener implements OnClickListener {
		private int id, action;

		/**
		 * Creates custom action listeners that will pass info to the next screen.
		 * @param id is the ID to pass to the buy screen to buy the correct item.
		 * @param type is the action to perform (buy / sell / auction).
		 */
		public MarketListener(int id, int action) {
			this.id = id;
			this.action = action;
		}
		
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(v.getContext(), TransactionActivity.class);
			myIntent.putExtra("id", id);
			myIntent.putExtra("action", action);
            startActivity(myIntent);
		}
	}
	
	
	/**
	 * The View of the market where users buy items that other users have put up for sale 
	 * @return
	 */
	private View playerMarketView() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View playerMarketView = inflater.inflate(R.layout.market_tab_0,null);
		
		
		//playerMarketView.setOnClickListener( new PlayerMarketListener() );
		return playerMarketView;
	}

	
	
	
	/**
	 * The View where users put items up on the playerMarket
	 * @return
	 */
	private View sellItemsView() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View sellItemsView = inflater.inflate(R.layout.market_tab_2,null);
		
		sellItemsView.setOnClickListener( new SellItemsListener() );
		return sellItemsView;
	}
	/**
	 * The listener of the buttons in the sellItems View
	 * @author danielfischer
	 *
	 */
	private class SellItemsListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	
	
	
	/**
	 * Changes views upon swiping
	 * @author danielfischer
	 *
	 */
	private class ScrollerAdapter extends PagerAdapter {

		private final int NUMBER_OF_SCREENS = 3;
		
		public ScrollerAdapter() {
			super();
		}
		
		@Override
		public int getCount() {
			Log.d("getCount",Integer.toString(NUMBER_OF_SCREENS));
			return NUMBER_OF_SCREENS;
		}
		
		@Override 
		public Object instantiateItem( ViewGroup container, int position ) {
			Log.d("instantiateItem",Integer.toString(position));
			View screen = null;
			switch( position ) {
			case 0: screen = playerMarketView; break;
			case 1: screen = buyMarketView; break;
			case 2: screen = sellItemsView; break;
			default: screen = playerMarketView;
			}
			Log.d("number",Integer.toString(position));
			((ViewPager) container).addView(screen,0);
			return screen;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			Log.d("isViewFromObject","isViewFromObject");
			return ( view == ((View)object) );
		}
		
		@Override
		public void destroyItem( ViewGroup container, int position, Object object ) {
			container.removeView((View)object);
			Log.d("destroyItem","destroyItem");
		}
		
		@Override
		public void finishUpdate( ViewGroup container ) {
			Log.d("finishUpdate","finishUpdate");
		}
		
		@Override
		public void restoreState( Parcelable state, ClassLoader loader ) {
			Log.d("restoreState","restoreState");
		}
		
		@Override
		public Parcelable saveState() {
			Log.d("saveState","saveState");
			return null;
		}
		
		@Override
		public void startUpdate( View container ) {
			Log.d("stateUpdate","startUpdate");
		}
	}
	

		
}

package edu.osu.geogame;


import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MarketTabActivity extends Activity {
	
	private ViewPager vPager;
	private ScrollerAdapter sAdapter;
	private View marketView = null;
	private View playerMarketView = null;
	private View sellItemsView = null;
	
	
		
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.market_scroller_mech);
		
		marketView = marketView();
		playerMarketView = playerMarketView();
		sellItemsView = sellItemsView();
	
		vPager = (ViewPager) findViewById(R.id.why_wont_you_look_at_me);

		sAdapter = new ScrollerAdapter();
		
		vPager.setAdapter(sAdapter);		
		vPager.setCurrentItem(1);

		
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
	
	private View marketView() {
		return new MarketView(this);
	}
	
	private View playerMarketView() {
		return new PlayerMarketView(this);
	}
	
	private View sellItemsView() {
		return new SellItemsView(this);
	}
	
	
	private class ScrollerAdapter extends PagerAdapter {

		private final int NUMBER_OF_SCREENS = 3;
		
		public ScrollerAdapter() {
			super();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("getCount",Integer.toString(NUMBER_OF_SCREENS));
			return NUMBER_OF_SCREENS;
		}
		
		@Override 
		public Object instantiateItem( ViewGroup container, int position ) {
			Log.d("instantiateItem",Integer.toString(position));
			View screen = null;
			switch( position ) {
			case 0: screen = playerMarketView; break;
			case 1: screen = sellItemsView; break;
			case 2: screen = marketView; break;
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
	
	
	
	private class PlayerMarketView extends View implements OnClickListener {
	
		public PlayerMarketView( Context context ) {
			super(context);
			setContentView(R.layout.register);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	private class SellItemsView extends View implements OnClickListener {
		 
		public SellItemsView( Context context ) {
			super(context);
			setContentView(R.layout.home_tab);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	private class MarketView extends View implements OnClickListener {
		
		private EditText quantity_SeedLR, quantity_SeedHYC, 
					quantity_Fertilizer, quantity_Water, quantity_Oxen = null;
		
		private TextView price_SeedLR, price_SeedHYC, price_quantity_Fertilizer,
					price_Water, price_Oxen = null;
		
		public MarketView( Context context ) {
			super(context);
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
	
	
	
	
}

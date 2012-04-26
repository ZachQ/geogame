package edu.osu.geogame;


import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

public class MarketTabActivity extends Activity {
	
	private ViewPager vPager; 
	
	
		
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.market_scroller);
	
		vPager = (ViewPager) findViewById(R.id.pager);

		
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
		return null;
	}
	
	private View playerMarketView() {
		return null;
	}
	
	private View putItemsOnMarketView() {
		return null;
	}
	
	
	private class ScrollerAdapter extends PagerAdapter {

		public ScrollerAdapter() {
			super();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override 
		public Object instantiateItem( ViewGroup container, int position ) {
			
			return null;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void destroyItem( ViewGroup container, int position, Object object ) {
			
		}
		
		@Override
		public void finishUpdate( ViewGroup container ) {
			
		}
		
		@Override
		public void restoreState( Parcelable state, ClassLoader loader ) {
			
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public void startUpdate( View container ) {
			
		}
	}
	
	
	
	private class PlayerMarketView extends View implements OnClickListener {
	
		public PlayerMarketView( Context context ) {
			super(context);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	private class SellItemsView extends View implements OnClickListener {
		 
		public SellItemsView( Context context ) {
			super(context);
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
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.market_tab, null);
			
			/*
			 * Produce the TextViews that holds the prices of the items, so that
			 * they can be set to the prices retrieved from the server
			 */
			price_SeedLR = (TextView) v.findViewById(R.id.SeedLRPrice);
			price_SeedLR = (TextView) v.findViewById(R.id.SeedLRPrice);
			price_SeedLR = (TextView) v.findViewById(R.id.SeedLRPrice);
			price_SeedLR = (TextView) v.findViewById(R.id.SeedLRPrice);
			price_SeedLR = (TextView) v.findViewById(R.id.SeedLRPrice);
			
			/*
			 * Produce the EditTexts that the user enters purchase quantities into,
			 * so that the app can react appropriately
			 */
			quantity_SeedLR = (EditText) v.findViewById(R.id.SeedLRQuantity);
			quantity_SeedHYC = (EditText) v.findViewById(R.id.SeedHYCQuantity);
			quantity_Fertilizer = (EditText) v.findViewById(R.id.FertilizerQuantity);
			quantity_Water = (EditText) v.findViewById(R.id.WaterQuantity);
			quantity_Oxen = (EditText) v.findViewById(R.id.OxenQuantity);
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

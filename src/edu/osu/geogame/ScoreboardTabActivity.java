package edu.osu.geogame;

import java.util.ArrayList;


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
import android.widget.TextView;

public class ScoreboardTabActivity extends Activity {

	private ArrayList<ScoreboardDataContainer> scoreboards;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_scroller_mech);
		scoreboards = new ArrayList<ScoreboardDataContainer>();
		populateScoreboard.run();		
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
	
	
	private Thread populateScoreboard = new Thread() {
		public void run() {
			RestClient sClient = new RestClient(GeoGame.URL_GAME + "Scoreboard/" + GeoGame.currentGameId);
			sClient.addCookie(GeoGame.sessionCookie);
			Log.d("SCOREBOARD URL",GeoGame.URL_GAME + "Scoreboard/" + GeoGame.currentGameId);
			try {
				sClient.Execute(RequestMethod.POST);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	
	
	
	
	
	
	/**
	 * Changes views upon swiping
	 * @author danielfischer
	 *
	 */
	private class ScrollerAdapter extends PagerAdapter {
		
		public ScrollerAdapter() {
			super();
		}
		
		@Override
		public int getCount() {
			Log.d("getCount",Integer.toString(scoreboards.size()));
			return scoreboards.size();
		}
		
		@Override 
		public Object instantiateItem( View container, int position ) {
			Log.d("instantiateItem",Integer.toString(position));
			Object screen = scoreboardView( scoreboards.get(position), position );
			((ViewPager) container).addView((View)screen,0);
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
	
	
	
	/**
	 * The View of the market where users buy items that other users have put up for sale 
	 * @return
	 */
	private View scoreboardView( ScoreboardDataContainer data, int turnNumber ) {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.scoreboard_tab,null);
		
		TextView turn = (TextView) v.findViewById(R.id.turn);
		
		TextView savingsFromPreviousYear = (TextView) v.findViewById(R.id.savings_from_previous_year_value);
		TextView numberOfAdults = (TextView) v.findViewById(R.id.number_of_adults_value);
		TextView numberOfChildren = (TextView) v.findViewById(R.id.number_of_children_value);
		TextView totalConsumption = (TextView) v.findViewById(R.id.total_consumption_value);
		TextView totalLand = (TextView) v.findViewById(R.id.total_land_value);
		TextView totalSeededLand = (TextView) v.findViewById(R.id.total_seeded_land_value);
		TextView weather = (TextView) v.findViewById(R.id.weather_value);
		TextView wheatPrice = (TextView) v.findViewById(R.id.wheat_price_value);
		TextView lrYield = (TextView) v.findViewById(R.id.lr_yield_value);
		TextView hycYield = (TextView) v.findViewById(R.id.hyc_yield_value);
		TextView totalYield = (TextView) v.findViewById(R.id.total_yield_value);
		TextView totalYieldConsumption = (TextView) v.findViewById(R.id.total_yield_consumption_value);
		TextView wheatCrops = (TextView) v.findViewById(R.id.wheat_crops_value);
		TextView labourWages = (TextView) v.findViewById(R.id.labour_wages_value);
		TextView assetTrade = (TextView) v.findViewById(R.id.asset_trade_value);
		TextView totalIncome = (TextView) v.findViewById(R.id.total_income_value);
		TextView seeds = (TextView) v.findViewById(R.id.seeds_value);
		TextView fertilizers = (TextView) v.findViewById(R.id.fertilizers_value);
		TextView water = (TextView) v.findViewById(R.id.water_value);
		TextView labour = (TextView) v.findViewById(R.id.labour_value);
		TextView oxen = (TextView) v.findViewById(R.id.oxen_value);
		TextView land = (TextView) v.findViewById(R.id.land_value);
		TextView totalCosts = (TextView) v.findViewById(R.id.total_costs_value);
		TextView balance = (TextView) v.findViewById(R.id.balance_value);
		
		
		turn.setText("Turn " + Integer.toString(turnNumber));
		
		savingsFromPreviousYear.setText(data.savingsFromPreviousYear());
		numberOfAdults.setText(data.adults());
		numberOfChildren.setText(data.children());
		totalConsumption.setText(data.totalConsumption());
		totalLand.setText(data.totalLand());
		totalSeededLand.setText(data.seededLand());
		weather.setText(data.weather());
		wheatPrice.setText(data.wheatPrice());
		lrYield.setText(data.yieldLR());
		hycYield.setText(data.yieldHYC());
		totalYield.setText(data.totalYield());
		totalYieldConsumption.setText(data.totalYieldMinusConsumption());
		wheatCrops.setText(data.wheatSellIncome());
		labourWages.setText(data.laborWagesIncome());
		assetTrade.setText(data.assetSellIncome());
		totalIncome.setText(data.totalIncome());
		seeds.setText(data.seedBuyCost());
		fertilizers.setText(data.fertilizerBuyCost());
		water.setText(data.waterBuyCost());
		labour.setText(data.laborBuyCost());
		oxen.setText(data.oxenBuyCost());
		land.setText(data.landBuyCost());
		totalCosts.setText(data.totalCosts());
		balance.setText(data.balance());
		
		return v;
	}

}
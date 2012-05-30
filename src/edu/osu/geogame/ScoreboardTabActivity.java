package edu.osu.geogame;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONTokener;


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
	private ViewPager vPager;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoreboard_scroller_mech);
		scoreboards = new ArrayList<ScoreboardDataContainer>();
		populateScoreboards.run();		
		vPager = (ViewPager) findViewById(R.id.viewpager);
		vPager.setAdapter(new ScrollerAdapter());
		vPager.setCurrentItem(scoreboards.size()-1);
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
		scoreboards.clear();
		populateScoreboards.run();
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
	
	
	private Thread populateScoreboards = new Thread() {
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
			
			
			JSONTokener tokenizer = new JSONTokener(sClient.getResponse());
			try {
				
				//Check that there is forum content to display
				tokenizer.nextTo(':');
				tokenizer.next();
				if( !tokenizer.nextTo(',').equals("true") ) {
					return;
				}
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				if( !tokenizer.nextTo('"').equals("data") ) {
					return;
				}
				
				tokenizer.nextTo('{');
				tokenizer.next(2);
				
				tokenizer.nextTo('{');
				tokenizer.next(2);
				
				
				//Begin retrieving forum content
				ScoreboardDataContainer scoreboard;
				do {
					scoreboard = new ScoreboardDataContainer();
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int turnNumber = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setTurnNumber(turnNumber);
					
					tokenizer.nextTo(':');
					tokenizer.next(2);
					String fateCard = tokenizer.nextTo('"');
					scoreboard.setFateCard(fateCard);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int money = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setMoney(money);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int adults = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setAdults(adults);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int children = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setChildren(children);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int totalConsumption = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setTotalConsumption(totalConsumption);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					float totalLand = Float.parseFloat(tokenizer.nextTo(','));
					scoreboard.setTotalLand(totalLand);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int totalSeededLand = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setSeededLand(totalSeededLand);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int weather = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setWeather(weather);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int wheatPrice = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setWheatPrice(wheatPrice);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int yieldLR = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setYieldLR(yieldLR);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int yieldHYC = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setYieldHYC(yieldHYC);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int assetSellIncome = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setAssetSellIncome(assetSellIncome);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int laborWagesIncome = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setLaborWagesIncome(laborWagesIncome);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int wheatSellIncome = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setWheatSellIncome(wheatSellIncome);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int landBuyCost = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setLandBuyCost(landBuyCost);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int seedBuyCost = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setSeedBuyCost(seedBuyCost);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int fertilizerBuyCost = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setFertilizerBuyCost(fertilizerBuyCost);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int waterBuyCost = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setWaterBuyCost(waterBuyCost);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int laborBuyCost = Integer.parseInt(tokenizer.nextTo(','));
					scoreboard.setLaborBuyCost(laborBuyCost);
					
					tokenizer.nextTo(':');
					tokenizer.next();
					int oxenBuyCost = Integer.parseInt(tokenizer.nextTo('}'));
					scoreboard.setOxenBuyCost(oxenBuyCost);
					
					scoreboards.add(scoreboard);
					
					tokenizer.next();
					if( tokenizer.next() != ',' ) {
						return;
					}
					
					
				} while( true );
						
			} catch( JSONException ex ) {
				Log.d("EXC",ex.getMessage());
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
			Object screen = scoreboardView( scoreboards.get(position), position+1 );
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
		
		TextView fateCard = (TextView) v.findViewById(R.id.fate_card_value);
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
		
		fateCard.setText(data.fateCard());
		savingsFromPreviousYear.setText("$"+Integer.toString(data.savingsFromPreviousYear()));
		numberOfAdults.setText(Integer.toString(data.adults()));
		numberOfChildren.setText(Integer.toString(data.children()));
		totalConsumption.setText(Integer.toString(data.totalConsumption()));
		totalLand.setText(Float.toString(data.totalLand()));
		totalSeededLand.setText(Float.toString(data.seededLand()));
		weather.setText(Integer.toString(data.weather()));
		wheatPrice.setText("$"+Integer.toString(data.wheatPrice()));
		lrYield.setText(Integer.toString(data.yieldLR()));
		hycYield.setText(Integer.toString(data.yieldHYC()));
		totalYield.setText(Integer.toString(data.totalYield()));
		totalYieldConsumption.setText(Integer.toString(data.totalYieldMinusConsumption()));
		wheatCrops.setText("$"+Integer.toString(data.wheatSellIncome()));
		labourWages.setText("$"+Integer.toString(data.laborWagesIncome()));
		assetTrade.setText("$"+Integer.toString(data.assetSellIncome()));
		totalIncome.setText("$"+Integer.toString(data.totalIncome()));
		seeds.setText("$"+Integer.toString(data.seedBuyCost()));
		fertilizers.setText("$"+Integer.toString(data.fertilizerBuyCost()));
		water.setText("$"+Integer.toString(data.waterBuyCost()));
		labour.setText("$"+Integer.toString(data.laborBuyCost()));
		oxen.setText("$"+Integer.toString(data.oxenBuyCost()));
		land.setText("$"+Integer.toString(data.landBuyCost()));
		totalCosts.setText("$"+Integer.toString(data.totalCosts()));
		balance.setText("$"+Integer.toString(data.balance()));
		
		return v;
	}

}
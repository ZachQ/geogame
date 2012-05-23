package edu.osu.geogame;


import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MarketTabActivity extends Activity {
	
	private ViewPager vPager;
	private ScrollerAdapter sAdapter;
	private View buyMarketView = null;
	private View playerMarketView = null;
	private ListView buyAuctionView = null;
	
	private ImageButton seedLR, seedHYC, grainLR, grainHYC, fertilizer, water, ox, labor;
	
	private Handler mHandler;
	SimpleAdapter adapter;
	private Vector<HashMap<String, String>> data;
		
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_scroller_mech);
	
		// Create views
		buyMarketView = marketView();
		playerMarketView = playerMarketView();
		buyAuctionView = buyAuctionsView();
		
		vPager = (ViewPager) findViewById(R.id.viewpager);
		sAdapter = new ScrollerAdapter();
		vPager.setAdapter(sAdapter);		
		vPager.setCurrentItem(1);
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
		
		// Add onClick with button ID to pass
		seedLR.setOnClickListener(new MarketListener(0));
		seedHYC.setOnClickListener(new MarketListener(1));
		fertilizer.setOnClickListener(new MarketListener(2));
		water.setOnClickListener(new MarketListener(3));

		return marketView;
	}
	
	private class MarketListener implements OnClickListener {
		private int id;

		/**
		 * Creates custom action listeners that will pass info to the next screen.
		 * @param id is the ID to pass to the buy screen to buy the correct item.
		 * @param type is the action to perform (buy / sell / auction).
		 */
		public MarketListener(int id) {
			this.id = id;
		}
		
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(v.getContext(), TransactionActivity.class);
			myIntent.putExtra("id", id);
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
		View marketView = inflater.inflate(R.layout.market_tab_0,null);
		
		grainLR = (ImageButton) marketView.findViewById(R.id.GrainLRButton);
		grainHYC = (ImageButton) marketView.findViewById(R.id.GrainHYCButton);
		ox  = (ImageButton) marketView.findViewById(R.id.OxButton);
		labor = (ImageButton) marketView.findViewById(R.id.LaborButton);
		
		// Add onClick with button ID to pass
		grainLR.setOnClickListener(new MarketListener(6));
		grainHYC.setOnClickListener(new MarketListener(7));
		ox.setOnClickListener(new MarketListener(4));
		labor.setOnClickListener(new MarketListener(5));
		
		return marketView;
	}
	
	/**
	 * The View where users put items up on the playerMarket
	 * @return
	 */
	private ListView buyAuctionsView() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		ListView auctionView = new ListView(this);
		//ListView auctionView = (ListView) inflater.inflate(R.layout.market_tab_2, null);
				
		data = new Vector<HashMap<String, String>>();
		
		adapter = new SimpleAdapter(
				this,
				data,
				R.layout.game_row,
				new String[] {"title","one","two","three"},
				new int[] {R.id.text1,R.id.text2,R.id.text3,R.id.text4});
		
		// Load data
		auctionView.setAdapter(adapter);
		
		// Create the list of properties
		mHandler = new Handler();
		Thread thread = new populateList();
		thread.start();
		
		return auctionView;
	}	
	
	public class populateList extends Thread {
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_MARKET + "Get/" + GeoGame.currentGameId + "/seller");
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONArray a;
			try {
				client.Execute(RequestMethod.POST);
			} catch (Exception e) {} finally {
				try {
					j = new JSONObject(client.getResponse());
					a = (JSONArray) j.get("seller");
					
					if (a.length() == 0 && j.getBoolean("success") == true) {
						// No open games
						mHandler.post(showMessage);
					} else {	
						// Import Games
						for (int i = 0; i < a.length(); i++) {
							HashMap<String,String> temp = new HashMap<String,String>();
							temp.put("auctionID", a.getJSONObject(i).getString("ID"));
							temp.put("sellerID", a.getJSONObject(i).getString("Seller"));
							temp.put("title", a.getJSONObject(i).getString("Category"));
							temp.put("one", "$" + a.getJSONObject(i).getString("Price"));
							temp.put("two", "Qty: " + a.getJSONObject(i).getString("Quantity"));
							temp.put("three", a.getJSONObject(i).getString("Seller"));
							data.add(temp);
						}
						
						// Complete = notify
						mHandler.post(showUpdate);
					}
				} catch (Exception e) {}
			}
		}
	}
	
	private Runnable showUpdate = new Runnable(){
        public void run(){
        	// Add listeners
        	buyAuctionView.setTextFilterEnabled(true);
        	buyAuctionView.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				
    				//todo
    			}
    		});
        }
    };
    
    private Runnable showMessage = new Runnable(){
        public void run(){
        	//TextView v = (TextView)buyAuctionView.findViewById(R.id.market_errorText);
			//v.setText("No Auctions");
			//ProgressBar p = (ProgressBar)buyAuctionView.findViewById(R.id.market_progress);
			//p.setVisibility(View.INVISIBLE);
        }
    };
	
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
		public Object instantiateItem( View container, int position ) {
			Log.d("instantiateItem",Integer.toString(position));
			Object screen = null;
			switch( position ) {
			case 0: 
				{
					screen = playerMarketView;
					((ViewPager) container).addView((View)screen,0);
					break;
				}
			case 1: 
				{
					screen = buyMarketView;
					((ViewPager) container).addView((View)screen,0);
					break;
				}
			case 2:
				{
					screen = buyAuctionView;
					((ViewPager) container).addView((ListView)screen,0);
					break;
				}
			}
			
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

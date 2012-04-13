package edu.osu.geogame;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class GameActivity extends TabActivity {

	private TabHost tabs;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		tabs = getTabHost();
		TabHost.TabSpec tabSpec;
		
		//Add Market Tab
		tabSpec = 	tabs.newTabSpec("Market")
					.setIndicator("Market")
					.setContent( new Intent( this, MarketTabActivity.class) );
		tabs.addTab(tabSpec);
		
		//Add Map Tab
		tabSpec = 	tabs.newTabSpec("Map")
					.setIndicator("Map")
					.setContent( new Intent( this, MapTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		//Add Scoreboard Tab
		tabSpec = 	tabs.newTabSpec("Scoreboard")
					.setIndicator("Scoreboard")
					.setContent( new Intent( this, ScoreboardTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		
	}
	
	
	
}

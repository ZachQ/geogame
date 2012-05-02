package edu.osu.geogame;


import android.app.TabActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;


public class GameActivity extends TabActivity {

	private TabHost tabs;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		tabs = getTabHost();
		TabHost.TabSpec tabSpec;
		
		//Add Overview Tab
		tabSpec = 	tabs.newTabSpec("Home")
					.setIndicator("Home")
					.setContent( new Intent( this, HomeTabActivity.class) );
		tabs.addTab(tabSpec);
		
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
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
}

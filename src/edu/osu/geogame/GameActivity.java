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
					.setIndicator("Home", getResources().getDrawable(R.drawable.home))
					.setContent( new Intent( this, HomeTabActivity.class) );
		tabs.addTab(tabSpec);
		
		//Add Market Tab
		tabSpec = 	tabs.newTabSpec("Market")
					.setIndicator("Market", getResources().getDrawable(R.drawable.market))
					.setContent( new Intent( this, MarketTabActivity.class) );
		tabs.addTab(tabSpec);
		
		//Add Map Tab
		tabSpec = 	tabs.newTabSpec("Map")
					.setIndicator("Map", getResources().getDrawable(R.drawable.map))
					.setContent( new Intent( this, MapTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		//Add Scoreboard Tab
		tabSpec = 	tabs.newTabSpec("Score")
					.setIndicator("Score", getResources().getDrawable(R.drawable.trophy))
					.setContent( new Intent( this, ScoreboardTabActivity.class ) );
		tabs.addTab(tabSpec);
		
		tabSpec = 	tabs.newTabSpec("Forum")
				.setIndicator("Forum", getResources().getDrawable(R.drawable.forum))
				.setContent( new Intent( this, ForumTabActivity.class ) );
	tabs.addTab(tabSpec);
	
	tabs.setCurrentTab(2);
	
		
		
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
}

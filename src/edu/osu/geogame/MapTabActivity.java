package edu.osu.geogame;


import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class MapTabActivity extends Activity {

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		//LayoutInflater inflater = LayoutInflater.from(this);
		//View mapTabView = inflater.inflate(R.layout.map_tab, null);
		//setContentView(mapTabView);
		setContentView(R.layout.map_tab);
		
		
		
		RestClient client = new RestClient("http://arcsrv.rolltherock.net/ArcGIS/rest/services/India_Gameboard/MapServer");
		try {
			client.Execute(RequestMethod.POST);
		} catch( Exception ex ) {
			
		}
		
		String temp = client.getResponse();
		Log.d(temp,"RESPONSE");

		
		
		
		//Retrieve the map and initial extent from XML layout
		MapView mMapView = (MapView) findViewById(R.id.map);
				
				//Add dynamic layer to MapView
		//mMapView.addLayer(new ArcGISTiledMapServiceLayer(
			//	temp));
		ArcGISDynamicMapServiceLayer dMap = new ArcGISDynamicMapServiceLayer("http://128.146.194.14/ArcGIS/rest/services/India/MapServer");
		ArcGISTiledMapServiceLayer tMap = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		mMapView.addLayer(dMap);
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
	
	
	
	
}

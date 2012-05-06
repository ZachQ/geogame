package edu.osu.geogame;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MapTabActivity extends Activity {

	private MapView mapView;
	private Point pointClicked;
	private ArcGISFeatureLayer featureLayer;
	private TextView plotData;
	private GeoGame game;
	private static String id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		game = (GeoGame)getApplicationContext();
		// LayoutInflater inflater = LayoutInflater.from(this);
		// View mapTabView = inflater.inflate(R.layout.map_tab, null);
		// setContentView(mapTabView);
		setContentView(R.layout.map_tab);
		
		plotData = (TextView) findViewById(R.id.plot_id);
		
		plotData.setText("text");

		RestClient client = new RestClient(
				"http://arcsrv.rolltherock.net/ArcGIS/rest/services/India_Gameboard/MapServer");
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception ex) {

		}

		String temp = client.getResponse();
		Log.d(temp, "RESPONSE");

		// Retrieve the map and initial extent from XML layout
		mapView = (MapView) findViewById(R.id.map);

		// Create the base map
		ArcGISDynamicMapServiceLayer dMap = new ArcGISDynamicMapServiceLayer(
				"http://128.146.194.14/ArcGIS/rest/services/India/MapServer");
		mapView.addLayer(dMap);

		// Add the click-able feature layer
		featureLayer = new ArcGISFeatureLayer(
				"http://128.146.194.14/ArcGIS/rest/services/India/MapServer/0",
				MODE.SNAPSHOT);

		// /////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////

		// Set tap listener for MapView
		mapView.setOnSingleTapListener(new FingerTapListener( plotData ) );
	}
	
	private class FingerTapListener implements OnSingleTapListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private TextView view;
		public FingerTapListener( TextView view ) {
			this.view = view;
			plotData.setText("test");
		}
		
		@Override
		public void onSingleTap(float x, float y) {
			//Log.d("test outside", game.test);
			plotData.setText("Loading...");

			// convert event into screen click
			pointClicked = mapView.toMapPoint(x, y);
			Log.d(Float.toString(x),"x");
			Log.d(Float.toString(y),"y");

			// build a query to select the clicked feature
			Query query = new Query();
			query.setOutFields(new String[] { "*" });
			query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
			query.setGeometry(pointClicked);
			query.setInSpatialReference(mapView.getSpatialReference());

			// call the select features method and implement the
			// callbacklistener
			
			MyCallBackListener myCBListener = new MyCallBackListener();
			featureLayer.selectFeatures(query,
					ArcGISFeatureLayer.SELECTION_METHOD.NEW,
					myCBListener); 
			Log.d("now","now");
			Log.d(myCBListener.getId(),"IDsdaf");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				Log.d("sleeping errror","sleeping error");
				System.exit(1);
			}
			String display = "Plot ID = " + myCBListener.getId();

			plotData.setText(display);
		}
		
		
		
	}
	
	private class MyCallBackListener implements CallbackListener<FeatureSet> {
		
		@Override
		public void onCallback( FeatureSet queryResults ) {
			Log.d("In the method","blah");
			if (queryResults.getGraphics().length > 0) {
				Log.d(id,"rrrrID");
				id = " ID:" + queryResults.getGraphics()[0].getAttributeValue(featureLayer.getObjectIdField());
				// ontap
				Log.d("1","1");
				Log.d("2","2");
				// Forward to a property select screen
			}
		}

		@Override
		public void onError(Throwable e) {
			GeoGame.test = "fail";
			Log.d("test fail",GeoGame.test);
			// Cant toast here???
		}
		
		public String getId() {
			return id;
		}
		
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
	 * (non-Javadoc) This will change the color format of the Activity so that
	 * the background gradient will be very smooth. Without this it has
	 * noticeable color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

}
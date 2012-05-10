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

import org.json.JSONException;
import org.json.JSONTokener;

import edu.osu.geogame.exception.ParcelNotFoundException;

public class MapTabActivity extends Activity {

	private MapView mapView;
	private Point pointClicked;
	private ArcGISFeatureLayer featureLayer;
	private TextView plotId;
	private TextView plotArea;
	private TextView plotOther;
	private GeoGame game;
	private static String id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		game = (GeoGame)getApplicationContext();

		setContentView(R.layout.map_tab);
		
		plotId = (TextView) findViewById(R.id.plot_id);
		plotArea = (TextView) findViewById(R.id.plot_area);
		plotOther = (TextView) findViewById(R.id.plot_price_or_plot_owner);
		
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
		mapView.setOnSingleTapListener(new FingerTapListener( plotId ) );
		
		}
	
	private class FingerTapListener implements OnSingleTapListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private TextView view;
		public FingerTapListener( TextView view ) {
			this.view = view;
		}
		
		@Override
		public void onSingleTap(float x, float y) {
			PlotDataThread retrievePlotData = new PlotDataThread(x,y);
			retrievePlotData.run();
		}
		
		
		
	}
	
	private class MyCallBackListener implements CallbackListener<FeatureSet> {
		
		@Override
		public void onCallback( FeatureSet queryResults ) {
			Log.d("In the method","blah");
			if (queryResults.getGraphics().length > 0) {
				Log.d(id,"rrrrID");
				id = ""+queryResults.getGraphics()[0].getAttributeValue(featureLayer.getObjectIdField());
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
	
	private class PlotDataThread extends Thread {
		
		private float x;
		private float y;
		
		public PlotDataThread( float x, float y ) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void run() {
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
			//String display = "Plot ID = " + myCBListener.getId();

			//plotId.setText(display);
			
			 String url = GeoGame.URL_GAME + "/India/" + GeoGame.currentGameId
				+ "/Info/" + myCBListener.getId();
			 
			 Log.d("URL",url);
			
			RestClient client = new RestClient(url);
			client.addCookie(GeoGame.sessionCookie);
			try {
				client.Execute(RequestMethod.GET);
			} catch (Exception e) {
				Log.d("ERROR5","454");
			}
			ParcelPacket packet = parseResponse(client.getResponse());	
			setUIWithPacket(packet);

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
	
	private void setUIWithPacket( ParcelPacket packet ) {
		if( packet.parecelType() == ParcelType.FOR_SALE ) {
			plotId.setText("Plot ID: " + packet.plotID());
			plotArea.setText("Plot Area: " + packet.area());
			plotOther.setText("Price:  $" + packet.price());
		} else if( packet.parecelType() == ParcelType.OWNED_BY_USER ) {
			plotId.setText("");
			plotArea.setText("");
			plotOther.setText("");
		} else {
			plotId.setText("Plot ID: " + packet.plotID());
			plotArea.setText("Plot Area: " + packet.area());
			plotOther.setText("Owner" + packet.opponentOwner());
		}
	}
	
	private ParcelPacket parseResponse( String json ) {
		JSONTokener tokenizer = new JSONTokener(json);
		ParcelPacket parcelPacket = new ParcelPacket();
		try {
			
			//Success status
		tokenizer.nextTo(':');
		tokenizer.next();
		if( !tokenizer.nextTo(',').equals("true") ) {
			throw new ParcelNotFoundException();
		}
		
		
		//Parcel type
		tokenizer.nextTo(':');
		tokenizer.next();
		String type = tokenizer.nextTo(',');
		if( type.equals("0") ) {
			parcelPacket.setType(ParcelType.FOR_SALE);
		} else if( type.equals("1") ) {
			parcelPacket.setType(ParcelType.OWNED_BY_USER);
		} else if( type.equals("2") ) {
			parcelPacket.setType(ParcelType.OWNED_BY_OPPONENT);
		} else {
			throw new Exception("Unknow parcel key");
		}
		
		//ID
		tokenizer.nextTo('{');
		tokenizer.nextTo(':');
		tokenizer.next();
		int parcelId = Integer.parseInt(tokenizer.nextTo(','));
		parcelPacket.setPlotID(parcelId);
		
		
		//area
		tokenizer.nextTo(':');
		tokenizer.next();
		float area = Float.parseFloat(tokenizer.nextTo(','));
		parcelPacket.setArea(area);
		
		//The rest; depends on the parcel type
		if( parcelPacket.parecelType() == ParcelType.FOR_SALE ) {
			//price
			tokenizer.nextTo(':');
			tokenizer.next();
			int price = Integer.parseInt(tokenizer.nextTo('}'));
			parcelPacket.setPrice(price);
		} else if( parcelPacket.parecelType() == ParcelType.OWNED_BY_USER ) {
			//seed
			tokenizer.nextTo(':');
			tokenizer.next();
			int seed = Integer.parseInt(tokenizer.nextTo(','));
			parcelPacket.setSeed(seed);
			//fertilizer
			tokenizer.nextTo(':');
			tokenizer.next();
			int fertilizer = Integer.parseInt(tokenizer.nextTo(','));
			parcelPacket.setSeed(fertilizer);
			//irrigation
			tokenizer.nextTo(':');
			tokenizer.next();
			int irrigation = Integer.parseInt(tokenizer.nextTo(')'));
			parcelPacket.setPrice(irrigation);
		} else {
			//owner
			tokenizer.nextTo(':');
			tokenizer.next(2);
			String owner = tokenizer.nextTo('"');
			parcelPacket.setOpponentOwner(owner);
		}
		
		} catch( JSONException ex ) {
			
		} catch( ParcelNotFoundException ex ) {
			
		} catch( Exception ex ) {
			Log.d("error",ex.getMessage());
			System.exit(1);
		}
		
		return parcelPacket;
	}

}
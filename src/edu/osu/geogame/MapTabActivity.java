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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.osu.geogame.exception.ParcelNotFoundException;

/**
 * This tab holds the map.  The map is an ArcGIS map and right now is set to India.  
 *
 */
public class MapTabActivity extends Activity implements OnClickListener {

	/*
	 * The section of the UI that holds the ArcGIS map
	 */
	private MapView mapView;
	
	/*
	 * Set to the coordinates on the map that the user taps
	 */
	private Point pointClicked;
	
	/*
	 * The interactive layer of the map that's clickable and returns data
	 */
	private ArcGISFeatureLayer featureLayer;
	
	/*
	 * The section of the UI displaying the plot id of the plot a user selects
	 */
	private TextView plotId;
	
	/*
	 * The section of the UI displaying the plot area of the plot a user selects
	 */
	private TextView plotArea;
	
	/*
	 * If for sale, this section of the UI displays the price; if owned by a different player,
	 * it displays the family that owns it
	 */
	private TextView plotOther;
	
	/*
	 * If a selected plot is for sale, this text view (which is clickable and really acts
	 * as a button) allows a player to buy the plot.
	 */
	private TextView buyPlot;
	
	/*
	 * Id of the currently selected plot
	 */
	private int currentPlotId;
	
	/*
	 * String representation of the id of the currently selected plot
	 */
	private static String id = "";
	
	private Context mapContext;

	/**
	 * UI created; map fetched from the server; listeners assigned
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map_tab);
		
		mapContext = this;

		plotId = (TextView) findViewById(R.id.plot_id);
		plotArea = (TextView) findViewById(R.id.plot_area);
		plotOther = (TextView) findViewById(R.id.plot_price_or_plot_owner);
		
		buyPlot = (TextView) findViewById(R.id.purchase_land);
		buyPlot.setOnClickListener(this);
		//The Buy! text view (which acts as a button) is only visible when the selected
		//plot is for sale
		buyPlot.setVisibility(View.GONE);

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
		mapView.setOnSingleTapListener(new FingerTapListener());

	}

	/**
	 * A listener for the map that listens for finger taps
	 *
	 */
	private class FingerTapListener implements OnSingleTapListener {

		/*
		 * Disregard this
		 */
		private static final long serialVersionUID = 1L;
		
		/*
		 * Listens for the data that the map returns when it is tapped
		 */
		private MyCallBackListener myCBListener = new MyCallBackListener();

		/**
		 * Constructor
		 */
		public FingerTapListener() {
		}

		/**
		 * When the map is tapped, hand over myCBListener and the coordinates to
		 * a PlotDataThread to do some work
		 */
		@Override
		public void onSingleTap(float x, float y) {
			/*
			 * Thread loadingThread = new Thread() { public void run() {
			 * plotId.setText("Plot Id:  Loading..");
			 * plotArea.setText("Plot Area:  Loading..");
			 * plotOther.setText("Loading.."); } }; loadingThread.run();
			 */
			PlotDataThread retrievePlotData = new PlotDataThread(x, y,
					myCBListener);
			retrievePlotData.run();

			/*
			 * pointClicked = mapView.toMapPoint(x, y);
			 * 
			 * // build a query to select the clicked feature Query query = new
			 * Query(); query.setOutFields(new String[] { "*" });
			 * query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
			 * query.setGeometry(pointClicked);
			 * query.setInSpatialReference(mapView.getSpatialReference());
			 * 
			 * // call the select features method and implement the //
			 * callbacklistener
			 * 
			 * featureLayer.selectFeatures(query,
			 * ArcGISFeatureLayer.SELECTION_METHOD.NEW, myCBListener);
			 * 
			 * try { Thread.sleep(3500); } catch (InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 * 
			 * Log.d(myCBListener.getId(),"ID after sleep");
			 * 
			 * //String display = "Plot ID = " + myCBListener.getId();
			 * 
			 * //plotId.setText(display);
			 * 
			 * String url = GeoGame.URL_GAME + "/India/" + GeoGame.currentGameId
			 * + "/Info/" + myCBListener.getId();
			 * 
			 * myCBListener.finish();
			 * 
			 * 
			 * RestClient client = new RestClient(url);
			 * client.addCookie(GeoGame.sessionCookie); try {
			 * client.Execute(RequestMethod.GET); } catch (Exception e) {
			 * Log.d("ERROR5","454"); } ParcelPacket packet =
			 * parseResponse(client.getResponse()); setUIWithPacket(packet);
			 */
		}

	}

	private class MyCallBackListener implements CallbackListener<FeatureSet> {

		// boolean idSet = false;
		String idPrevious = "";
		int pCounter = 0;

		@Override
		public void onCallback(FeatureSet queryResults) {
			if (queryResults.getGraphics().length > 0) {
				pCounter = 0;
				Log.d(id, "ID before change");
				id = ""
						+ queryResults.getGraphics()[0]
								.getAttributeValue(featureLayer
										.getObjectIdField());
				// Log.d(id,idPrevious);
				// ontap
				// Forward to a property select screen
			}
		}

		@Override
		public void onError(Throwable e) {
			GeoGame.test = "fail";
			Log.d("test fail", GeoGame.test);
			// Cant toast here???
		}

		public String getId() {
			while (id.equals(idPrevious) && pCounter < 100000) {
				Log.d(id, idPrevious);
				pCounter++;
			}
			return id;
		}

		public void finish() {
			idPrevious = id;
		}

	}

	/*
	private class MapThread extends Thread {

		@Override
		public void run() {
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

		}
	}
	*/

	private class PlotDataThread extends Thread {

		private float x;
		private float y;
		private MyCallBackListener myCBListener;

		public PlotDataThread(float x, float y, MyCallBackListener myCBListener) {
			super();
			this.x = x;
			this.y = y;
			this.myCBListener = myCBListener;
		}

		@Override
		public void run() {
			pointClicked = mapView.toMapPoint(x, y);

			// build a query to select the clicked feature
			Query query = new Query();
			query.setOutFields(new String[] { "*" });
			query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
			query.setGeometry(pointClicked);
			query.setInSpatialReference(mapView.getSpatialReference());

			// call the select features method and implement the
			// callbacklistener

			featureLayer.selectFeatures(query,
					ArcGISFeatureLayer.SELECTION_METHOD.NEW, myCBListener);
			/*
			 * try { Thread.sleep(3500); } catch (InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */
			Log.d(myCBListener.getId(), "ID after sleep");

			// String display = "Plot ID = " + myCBListener.getId();

			// plotId.setText(display);

			String url = GeoGame.URL_GAME + "/India/" + GeoGame.currentGameId
					+ "/Info/" + myCBListener.getId();

			myCBListener.finish();

			RestClient client = new RestClient(url);
			client.addCookie(GeoGame.sessionCookie);
			try {
				client.Execute(RequestMethod.POST);
			} catch (Exception e) {
				Log.d("ERROR5", "454");
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

	private class OwnedParcelDialog extends Dialog implements OnClickListener {

		private int seedType = 0;
		private int fertilizerLevel = 0;
		private int irrigationLevel = 0;

		private int id;

		public OwnedParcelDialog(Context context, float area, int id) {
			super(context);
			this.setContentView(R.layout.owned_parcel_dialog);

			this.id = id;

			TextView areaValue = (TextView) findViewById(R.id.area_value);

			Spinner seedPicker = (Spinner) findViewById(R.id.seed_picker);
			ArrayAdapter<CharSequence> seedPickerAdapter = ArrayAdapter
					.createFromResource(this.getContext(), R.array.seed_type,
							android.R.layout.simple_spinner_item);
			seedPickerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			seedPicker.setAdapter(seedPickerAdapter);
			seedPicker
					.setOnItemSelectedListener(new SpinnerOptionSelectedListener());

			Spinner fertilizerPicker = (Spinner) findViewById(R.id.fertilizer_picker);
			ArrayAdapter<CharSequence> fertilizerPickerAdapter = ArrayAdapter
					.createFromResource(this.getContext(),
							R.array.fertilizer_level,
							android.R.layout.simple_spinner_item);
			fertilizerPickerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			fertilizerPicker.setAdapter(fertilizerPickerAdapter);
			fertilizerPicker
					.setOnItemSelectedListener(new SpinnerOptionSelectedListener());

			Spinner irrigationPicker = (Spinner) findViewById(R.id.irrigation_picker);
			ArrayAdapter<CharSequence> irrigationPickerAdapter = ArrayAdapter
					.createFromResource(this.getContext(),
							R.array.irrigation_level,
							android.R.layout.simple_spinner_item);
			irrigationPickerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			irrigationPicker.setAdapter(irrigationPickerAdapter);
			irrigationPicker
					.setOnItemSelectedListener(new SpinnerOptionSelectedListener());

			Button submit = (Button) findViewById(R.id.submit);
			submit.setOnClickListener(this);
			Button cancel = (Button) findViewById(R.id.cancel);
			cancel.setOnClickListener(this);

			areaValue.setText(Float.toString(area));

			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View v) {
			String seedMessage = null;
			String fertilizerMessage = null;
			String irrigationMessage = null;
			Log.d("InOnClick", "onclick");
			switch (v.getId()) {
			case R.id.submit:
				Log.d("SUBMIT","SUBMIT");
				RestClient performAction;
				if (seedType > 0) {
					performAction = new RestClient(GeoGame.URL_GAME + "India/"
							+ GeoGame.currentGameId + "/Plant/"
							+ Integer.toString(id) + "/"
							+ Integer.toString(seedType));
					performAction.addCookie(GeoGame.sessionCookie);
					try {
						performAction.Execute(RequestMethod.POST);
						JSONTokener tokenizer = new JSONTokener(performAction.getResponse());
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.next();
						boolean hasMessage = (tokenizer.nextTo('"').equals("message"));
						if( hasMessage ) {
						tokenizer.nextTo(":");
						tokenizer.next();
						tokenizer.next();
						seedMessage = tokenizer.nextTo('"');
						Log.d("seedMessage",seedMessage);
						}
						Log.d("ActionResponse",performAction.getResponse());
						Log.d("ActionError",performAction.getErrorMessage());
					} catch (Exception e) {
						Log.d("SeedActionError", "SeedActionError");
					}
				}
				if (fertilizerLevel > 0) {
					performAction = new RestClient(GeoGame.URL_GAME + "India/"
							+ GeoGame.currentGameId + "/Fertilize/"
							+ Integer.toString(id) + "/"
							+ Integer.toString(fertilizerLevel));
					performAction.addCookie(GeoGame.sessionCookie);
					try {
						performAction.Execute(RequestMethod.POST);
						JSONTokener tokenizer = new JSONTokener(performAction.getResponse());
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.next();
						boolean hasMessage = (tokenizer.nextTo('"').equals("message"));
						if( hasMessage ) {
						tokenizer.nextTo(":");
						tokenizer.next();
						tokenizer.next();
						fertilizerMessage = tokenizer.nextTo('"');
						Log.d("fertilizerMessage",fertilizerMessage);
						}
						Log.d("ActionResponse",performAction.getResponse());
						Log.d("ActionError",performAction.getErrorMessage());
						
					} catch (Exception e) {
						Log.d("SeedActionError", "SeedActionError");
					}
				}
				if (irrigationLevel > 0) {
					performAction = new RestClient(GeoGame.URL_GAME + "India/"
							+ GeoGame.currentGameId + "/Irrigate/"
							+ Integer.toString(id) + "/"
							+ Integer.toString(irrigationLevel));
					performAction.addCookie(GeoGame.sessionCookie);
					try {
						performAction.Execute(RequestMethod.POST);
						JSONTokener tokenizer = new JSONTokener(performAction.getResponse());
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.nextTo(':');
						tokenizer.next();
						tokenizer.next();
						boolean hasMessage = (tokenizer.nextTo('"').equals("message"));
						if( hasMessage ) {
						tokenizer.nextTo(":");
						tokenizer.next();
						tokenizer.next();
						irrigationMessage = tokenizer.nextTo('"');
						Log.d("irrigationMessage",irrigationMessage);
						}
						Log.d("ActionResponse",performAction.getResponse());
						Log.d("ActionError",performAction.getErrorMessage());
					} catch (Exception e) {
						Log.d("SeedActionError", "SeedActionError");
					}
				}
				
				/*
				 * /Game/India/{id}/Fertilize/{parcelID}/{fertilizationLevel}
				 * /Game/India/{id}/Irrigate/{parcelID}/{irrigationLevel}
				 */
				/*
				if( seedMessage != null ) {
					Toast.makeText(this.getContext(), seedMessage, Toast.LENGTH_SHORT);
				}
				if( fertilizerMessage != null ) {
					Toast.makeText(this.getContext(), fertilizerMessage, Toast.LENGTH_SHORT);
				}
				if( irrigationMessage != null ) {
					Toast.makeText(this.getContext(), irrigationMessage, Toast.LENGTH_SHORT);
				}
				*/
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
				if( seedMessage != null || fertilizerMessage != null || irrigationMessage != null ) {
					String message = "";
					if( seedMessage != null ) {
						message = message.concat(seedMessage);
					} 
					if( fertilizerMessage != null ) {
						message = message.concat("\n"+fertilizerMessage);
					}
					if( irrigationMessage != null ) {
						message = message.concat("\n"+irrigationMessage);
					}
					builder.setMessage(message);
				       builder.setCancelable(false)
				       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.dismiss();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
				}
				this.dismiss();
				break;
			case R.id.cancel:
				Log.d("DISMISS", "DIALOG");
				this.dismiss();
				break;
			}
		}

		private class SpinnerOptionSelectedListener implements
				OnItemSelectedListener {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				switch (parent.getId()) {
				case R.id.seed_picker:
					seedType = pos;
					break;
				case R.id.fertilizer_picker:
					fertilizerLevel = pos;
					break;
				case R.id.irrigation_picker:
					irrigationLevel = pos;
					break;
				}
			}

			public void onNothingSelected(AdapterView parent) {
				// do nothing
			}
		}
	}

	
	private void setUIWithPacket(ParcelPacket packet) {
		if (packet.parecelType() == ParcelType.FOR_SALE) {
			plotId.setText("Plot ID: " + packet.plotID());
			currentPlotId = packet.plotID();
			buyPlot.setVisibility(View.VISIBLE);
			plotArea.setText("Plot Area: " + packet.area());
			plotOther.setText("Price:  $" + packet.price());
		} else if (packet.parecelType() == ParcelType.OWNED_BY_USER) {
			buyPlot.setVisibility(View.GONE);
			plotId.setText("");
			plotArea.setText("");
			plotOther.setText("");
			Context mContext = this;
			OwnedParcelDialog dialog = new OwnedParcelDialog(mContext,
					packet.area(), packet.plotID());
			dialog.show();

		} else {
			buyPlot.setVisibility(View.GONE);
			plotId.setText("Plot ID: " + packet.plotID());
			plotArea.setText("Plot Area: " + packet.area());
			String owner = (packet.opponentOwner()==null)?"":packet.opponentOwner();
			plotOther.setText("Owner: " + owner);
		}
	}

	private ParcelPacket parseResponse(String json) {
		JSONTokener tokenizer = new JSONTokener(json);
		ParcelPacket parcelPacket = new ParcelPacket();
		try {

			// Success status
			tokenizer.nextTo(':');
			tokenizer.next();
			if (!tokenizer.nextTo(',').equals("true")) {
				throw new ParcelNotFoundException();
			}

			// Parcel type
			tokenizer.nextTo(':');
			tokenizer.next();
			String type = tokenizer.nextTo(',');
			if (type.equals("0")) {
				parcelPacket.setType(ParcelType.FOR_SALE);
			} else if (type.equals("1")) {
				parcelPacket.setType(ParcelType.OWNED_BY_USER);
			} else if (type.equals("2")) {
				parcelPacket.setType(ParcelType.OWNED_BY_OPPONENT);
			} else {
				throw new Exception("Unknow parcel key");
			}

			// ID
			tokenizer.nextTo('{');
			tokenizer.nextTo(':');
			tokenizer.next();
			int parcelId = Integer.parseInt(tokenizer.nextTo(','));
			parcelPacket.setPlotID(parcelId);

			// area
			tokenizer.nextTo(':');
			tokenizer.next();
			float area = Float.parseFloat(tokenizer.nextTo(','));
			parcelPacket.setArea(area);

			// The rest; depends on the parcel type
			if (parcelPacket.parecelType() == ParcelType.FOR_SALE) {
				// price
				tokenizer.nextTo(':');
				tokenizer.next();
				int price = Integer.parseInt(tokenizer.nextTo('}'));
				parcelPacket.setPrice(price);
			} else if (parcelPacket.parecelType() == ParcelType.OWNED_BY_USER) {
				// seed
				tokenizer.nextTo(':');
				tokenizer.next();
				int seed = Integer.parseInt(tokenizer.nextTo(','));
				parcelPacket.setSeed(seed);
				// fertilizer
				tokenizer.nextTo(':');
				tokenizer.next();
				int fertilizer = Integer.parseInt(tokenizer.nextTo(','));
				parcelPacket.setSeed(fertilizer);
				// irrigation
				tokenizer.nextTo(':');
				tokenizer.next();
				int irrigation = Integer.parseInt(tokenizer.nextTo(')'));
				parcelPacket.setPrice(irrigation);
			} else {
				// owner
				tokenizer.nextTo(':');
				tokenizer.next(2);
				String owner = tokenizer.nextTo('"');
				parcelPacket.setOpponentOwner(owner);
			}

		} catch (JSONException ex) {

		} catch (ParcelNotFoundException ex) {

		} catch (NullPointerException ex) {

		} catch (Exception ex) {
			// Log.d("error",ex.getMessage());
			// System.exit(1);
		}

		return parcelPacket;
	}

	@Override
	public void onClick(View view) {
		switch( view.getId() ) {
		case R.id.purchase_land:
			Log.d("PURCHASING LAND","PURCHASING LAND");
			RestClient client = new RestClient(GeoGame.URL_GAME + "India/" + GeoGame.currentGameId + "/BuyParcel/" +
												Integer.toString(currentPlotId));
			client.addCookie(GeoGame.sessionCookie);
			Toast.makeText(this, "You purchased plot number " + Integer.toString(currentPlotId) + "!", Toast.LENGTH_SHORT);
			JSONObject j = null;
			try {
				client.Execute(RequestMethod.POST);
				JSONTokener tokenizer = new JSONTokener(client.getResponse());
				tokenizer.nextTo(':');
				tokenizer.next();
				tokenizer.nextTo(':');
				tokenizer.next();
				tokenizer.nextTo(':');
				tokenizer.next();
				tokenizer.next();
				String message = tokenizer.nextTo('.');
				Log.d("message",message);
				if( message.equals("You need more money to make this purchase") ) {
					throw new Exception("You don't have enough money!");
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("You purchased plot number " + Integer.toString(currentPlotId) + "!")
				       .setCancelable(false)
				       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.dismiss();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			} catch (Exception e) {
				Toast.makeText(this, "Error purchasing the land", Toast.LENGTH_SHORT);
				Log.d("But it didn't work","whoops");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				if( e.getMessage().equals("You don't have enough money!") ) {
					builder.setMessage("You don't have enough money!");
				} else {
					builder.setMessage("Error purchasing the land");
				}
				       builder.setCancelable(false)
				       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.dismiss();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
			Log.d("Response",client.getResponse());
			Log.d("Error", client.getErrorMessage());
			
		}
	}

	

}
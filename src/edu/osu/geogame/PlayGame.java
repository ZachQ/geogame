package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayGame extends ListActivity {
	private Vector<HashMap<String, String>> data;
	private Handler mHandler;
	SimpleAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.joingame);
	    data = new Vector<HashMap<String, String>>();
		
		adapter = new SimpleAdapter(
				this,
				data,
				R.layout.game_row,
				new String[] {"title","one","two","three"},
				new int[] {R.id.text1,R.id.text2,R.id.text3,R.id.text4});
		
		// Create the list of properties
		mHandler = new Handler();
		populateList.start();
	}

	private Thread populateList = new Thread() {
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_GAME + "YourGames");
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONArray a;
			try {
				client.Execute(RequestMethod.GET);
			} catch (Exception e) {} finally {
				try {
					Log.d("games",client.getResponse());
					j = new JSONObject(client.getResponse());
					a = (JSONArray) j.get("data");
					
					if (a.length() == 0 && j.getBoolean("success") == true) {
						// No open games
						mHandler.post(showMessage);
					} else {	
						// Import Games
						for (int i = 0; i < a.length(); i++) {
							HashMap<String,String> temp = new HashMap<String,String>();
							temp.put("gameID", a.getJSONObject(i).getString("gameID"));
							temp.put("title", a.getJSONObject(i).getString("location"));
							temp.put("one", "Seats: " + a.getJSONObject(i).getString("seats"));
							temp.put("two", "Started: " + a.getJSONObject(i).getString("started"));
							temp.put("three", "Turn: " + a.getJSONObject(i).getString("turn") + "/" + a.getJSONObject(i).getString("numberOfTurns"));
							data.add(temp);
						}
						
						// Complete = notify
						mHandler.post(showUpdate);
					}
				} catch (Exception e) {}
			}
		}
	};
    
    private Runnable showUpdate = new Runnable(){
        public void run(){
        	setListAdapter(adapter);
        	
        	// Add listeners
    		ListView listView = getListView();
    		listView.setTextFilterEnabled(true);
    		listView.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				
    				// Set current game
    				GeoGame.currentGameId = data.elementAt(position).get("gameID");
    				
    				Intent i= new Intent(getApplicationContext(), GameActivity.class);
    				startActivity(i);
    			}
    		});
        }
    };
    
    private Runnable showMessage = new Runnable(){
        public void run(){
        	TextView v = (TextView) findViewById(R.id.joingame_errorText);
			v.setText("No Open Games");
			ProgressBar p = (ProgressBar) findViewById(R.id.joingame_progress);
			p.setVisibility(View.INVISIBLE);
        }
    };
    
    @Override
	protected void onPause() {
		// This will "pause" the activity aka kill itself so
		// skips to last screen opened.
		super.onPause();
		finish();
	}
    
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class JoinGame extends ListActivity {
	GeoGame game;
	private Vector<HashMap<String, String>> data;
	private Handler mHandler;
	SimpleAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.joingame);
	    game = (GeoGame)getApplicationContext();
	    data = new Vector<HashMap<String, String>>();
		
	    adapter = new SimpleAdapter(
				this,
				data,
				R.layout.game_row,
				new String[] {"title","one","two","three"},
				new int[] {R.id.text1,R.id.text2,R.id.text3,R.id.text4});
		
		// get the list of games
		mHandler = new Handler();
		populateList.start();
	}
	
	private Thread populateList = new Thread() {
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_GAME + "OpenGames");
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONArray a;
			try {
				client.Execute(RequestMethod.POST);
			} catch (Exception e) {} finally {
				String response = client.getResponse();
				
				try {
					j = new JSONObject(response);
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
							temp.put("two", "Duration: " + a.getJSONObject(i).getString("turnDuration"));
							temp.put("three", "Turns: " + a.getJSONObject(i).getString("numberOfTurns"));
							data.add(temp);
						}
						
						// Complete = notify
						mHandler.post(showUpdate);
					}
				} catch (Exception e) {
					int test = 9;
				}
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
    			    // Join the game
    				RestClient client = new RestClient(GeoGame.URL_GAME + "Join");
    				
    				// Back to Menu
    				Intent i= new Intent(getApplicationContext(), Menu.class);
    				i.putExtra("gameID", data.elementAt(position).get("gameID"));
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
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

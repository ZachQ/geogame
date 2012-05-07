package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.SimpleAdapter;

public class ForumTabActivity extends Activity {
	GeoGame game;
	private Handler mHandler;
	SimpleAdapter adapter;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.forum_tab);
	    game = (GeoGame)getApplicationContext();
		
		// get the list of games
		mHandler = new Handler();
	}
	
	/*
	private Thread populateList = new Thread() {
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_FORUM + "Get/Threads/");
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
	*/
	
	
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




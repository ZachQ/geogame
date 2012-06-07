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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * When the user clicks the "join Game" button from the menu this screen is
 * displayed where the user can select a game to join. If there are no open
 * games, no games will show and it will state "No open games"
 * 
 */
public class JoinGame extends ListActivity {
	
	/**
	 * The game data that will be fed to adapter, in turn feeding it to the UI
	 */
	private Vector<HashMap<String, String>> data;
	
	/**
	 * Manages threads
	 */
	private Handler mHandler;
	
	/**
	 * This UIs ListView uses this adapter to display data
	 */
	private SimpleAdapter adapter;

	/**
	 * Create the UI; intialize the adapter; run the thread that populates data
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joingame);
		data = new Vector<HashMap<String, String>>();

		adapter = new SimpleAdapter(this, data, R.layout.game_row,
				new String[] { "title", "one", "two", "three" }, new int[] {
						R.id.text1, R.id.text2, R.id.text3, R.id.text4 });

		// get the list of games
		mHandler = new Handler();
		populateList.start();
	}

	/**
	 * Populates the data object for later use by the UIs ListView
	 */
	private Thread populateList = new Thread() {
		/**
		 * Run this thread
		 */
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_GAME + "OpenGames");
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONArray a;
			try {
				client.Execute(RequestMethod.POST);
			} catch (Exception e) {
			} finally {
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
							HashMap<String, String> temp = new HashMap<String, String>();
							temp.put("gameID",
									a.getJSONObject(i).getString("gameID"));
							temp.put("title",
									a.getJSONObject(i).getString("location"));
							temp.put("one", "Seats: "
									+ a.getJSONObject(i).getString("seats"));
							temp.put(
									"two",
									"Duration: "
											+ a.getJSONObject(i).getString(
													"turnDuration"));
							temp.put(
									"three",
									"Turns: "
											+ a.getJSONObject(i).getString(
													"numberOfTurns"));
							data.add(temp);
						}

						// Complete = notify
						mHandler.post(showUpdate);
					}
				} catch (Exception e) {
				}
			}
		}
	};

	/**
	 * The adapter updates the UI
	 */
	private Runnable showUpdate = new Runnable() {
		/**
		 * Run this thread
		 */
		public void run() {
			setListAdapter(adapter);

			// Add listeners
			ListView listView = getListView();
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Join the game
					RestClient client = new RestClient(GeoGame.URL_GAME
							+ "Join");

					// Back to Menu
					Intent i = new Intent(getApplicationContext(), Menu.class);
					i.putExtra("gameID", data.elementAt(position).get("gameID"));
					startActivity(i);
				}
			});
		}
	};

	/**
	 * If there are no open games, a message saying so is displayed
	 */
	private Runnable showMessage = new Runnable() {
		/**
		 * Run this thread
		 */
		public void run() {
			TextView v = (TextView) findViewById(R.id.joingame_errorText);
			v.setText("No Open Games");
			ProgressBar p = (ProgressBar) findViewById(R.id.joingame_progress);
			p.setVisibility(View.INVISIBLE);
		}
	};

	/**
	 * 
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class JoinGame extends ListActivity {
	GeoGame game;
	private Vector<HashMap<String, String>> data;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.joingame);
	    game = (GeoGame)getApplicationContext();
	    data = new Vector<HashMap<String, String>>();
		
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				data,
				R.layout.game_row,
				new String[] {"title","one","two","three"},
				new int[] {R.id.text1,R.id.text2,R.id.text3,R.id.text4});
		
		// Create the list of properties
		populateList();
	    
		setListAdapter(adapter);
		
		// Add listeners
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // Join the game
				RestClient client = new RestClient(game.URL_GAME + "Join");
				
				// Back to Menu
				Intent i= new Intent(getApplicationContext(), SelectedProperty.class);
				i.putExtra("gameID",data.elementAt(position).get("gameID"));
				startActivity(i);
			}
		});
	}

	private void populateList() {
		RestClient client = new RestClient(game.URL_GAME + "OpenGames");
		client.addCookie(game.sessionCookie);
		JSONObject j;
		JSONArray a;
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {} finally {
			String response = client.getResponse();
			
			try {
				j = new JSONObject(response);
				a = (JSONArray) j.get("data");
				
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
			} catch (Exception e) {}
		}
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

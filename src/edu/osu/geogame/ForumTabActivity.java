package edu.osu.geogame;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.SimpleAdapter;

import org.json.JSONTokener;

import edu.osu.geogame.exception.NoThreadsExistException;

public class ForumTabActivity extends Activity {
	private Handler mHandler;
	private SimpleAdapter adapter;
	private Map<Integer,ForumThreadTuple> forumContent;

	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		Log.d("In onCreate","Forum");
	    setContentView(R.layout.forum_tab);
	    forumContent = new HashMap<Integer,ForumThreadTuple>();
	    
		// get the list of games
		mHandler = new Handler();
		populateList.run();
	}
	
	
	private Thread populateList = new Thread() {
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_FORUM + "Get/Threads/"
					+ GeoGame.currentGameId);
			client.addCookie(GeoGame.sessionCookie);
			JSONObject j;
			JSONArray a;
			
			try {
				client.Execute(RequestMethod.GET);
				parseResponse(client.getResponse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	
	
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
	
	private void parseResponse( String json ) {
		JSONTokener tokenizer = new JSONTokener(json);
		try {
			
			//Check that there is forum content to display
			String temp = tokenizer.next(11);
			if( !temp.equals("{success\":") ) { 
				throw new JSONException("success var");
			}
			
			temp = tokenizer.next(4);
			if( !temp.equals("true") ) {
				throw new JSONException("success var: not true");
			}
			
			temp = tokenizer.next(11);
			if( !temp.equals(",\"status\":\"") ) {
				throw new JSONException("status var");
			}
			
			temp = tokenizer.next(7);
			if( !temp.equals("threads") ) {
				throw new JSONException("status var: not threads");
			}
			
			temp = tokenizer.next(13);
			if( !temp.equals("\",\"threads\":[") ) {
				throw new NoThreadsExistException();
			}
			
			//Begin retrieving forum content
			boolean cont = true;
			do {
				String threadId;
				String title;
				String message;
				String family;
				String timestamp;
				String count;
				
				
				
				
				
				
				
			} while( cont );
			
			
			
					
				
				
				
		} catch( JSONException ex ) {
			
		} catch( NoThreadsExistException ex ) {
			
		}
	}
	
}




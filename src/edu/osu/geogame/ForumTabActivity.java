package edu.osu.geogame;

import java.util.HashMap;
import java.util.Iterator;
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
			
			try {
				client.Execute(RequestMethod.GET);
				parseThreadResponse(client.getResponse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Iterator<Integer> ids = forumContent.keySet().iterator();
			while( ids.hasNext() ) {
			
				int currentId = ids.next();
				
				client = new RestClient(GeoGame.URL_FORUM + "Get/Comments/"
					+ Integer.toString(currentId));
				
			client.addCookie(GeoGame.sessionCookie);
			
			try {
				client.Execute(RequestMethod.GET);
				parseCommentResponse(client.getResponse(), currentId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	
	private void parseThreadResponse( String json ) {
		JSONTokener tokenizer = new JSONTokener(json);
		//Log.d("JSON",json);
		try {
			
			//Check that there is forum content to display
			tokenizer.nextTo(':');
			tokenizer.next();
			if( !tokenizer.nextTo(',').equals("true") ) {
				return;
			}
			
			tokenizer.nextTo(':');
			tokenizer.next(2);
			if( !tokenizer.nextTo('"').equals("threads") ) {
				return;
			}
			
			tokenizer.nextTo('{');
			tokenizer.next(2);
			
			
			//Begin retrieving forum content
			do {
				int id = -1;
				ForumThreadTuple threadInfo = new ForumThreadTuple();
				
				tokenizer.nextTo(':');
				tokenizer.next();
				id = Integer.parseInt(tokenizer.nextTo(','));
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				threadInfo.setTitle(tokenizer.nextTo('"'));
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				threadInfo.setMessage(tokenizer.nextTo('"'));
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				threadInfo.setFamily(tokenizer.nextTo('"'));
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				threadInfo.setTimestamp(tokenizer.nextTo('"'));
				
				tokenizer.nextTo(':');
				tokenizer.next(2);
				threadInfo.setCount(tokenizer.nextTo('}'));
				
				forumContent.put(id, threadInfo);
				
				tokenizer.next();
				if( tokenizer.next() != ',' ) {
					return;
				}
				
				
			} while( true );
					
		} catch( JSONException ex ) {
			Log.d("EXC",ex.getMessage());
		}
	}
	
	private void parseCommentResponse( String json, int parentId ) {
		JSONTokener tokenizer = new JSONTokener(json);

		try {
		Log.d("comment",json);
		
		//Check that there is forum content to display
		tokenizer.nextTo(':');
		tokenizer.next();
		if( !tokenizer.nextTo(',').equals("true") ) {
			return;
		}
		
		tokenizer.nextTo(':');
		tokenizer.next(2);
		if( !tokenizer.nextTo('"').equals("comments") ) {
			return;
		}
		
		tokenizer.nextTo('{');
		tokenizer.next(2);
		
		
		//Begin retrieving forum content
		do {

			CommentThreadTuple commentInfo = new CommentThreadTuple();
			
			tokenizer.nextTo(':');
			tokenizer.next();
			commentInfo.setId(Integer.parseInt(tokenizer.nextTo(',')));
			
			tokenizer.nextTo(':');
			tokenizer.next(2);
			commentInfo.setMessage(tokenizer.nextTo('"'));
			
			tokenizer.nextTo(':');
			tokenizer.next(2);
			commentInfo.setFamily(tokenizer.nextTo('"'));
			
			tokenizer.nextTo(':');
			tokenizer.next(2);
			commentInfo.setTimestamp(tokenizer.nextTo('"'));
						
			forumContent.get(parentId).addComment(commentInfo);
			
			tokenizer.next(2);
			if( tokenizer.next() != ',' ) {
				return;
			}
			
		} while( true );
	} catch( JSONException ex ) {
		Log.d("EXC_COM",ex.getMessage());
	}
	
	}
}




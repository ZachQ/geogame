package edu.osu.geogame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONTokener;

import edu.osu.geogame.exception.NoThreadsExistException;

/**
 * This is the Forum tab.  Posts are displayed and, if clicked, their comments are displayed (CommentPageActivity
 *  handles this task).  Posts can be created by clicking the Create Post button; CreatePostDialog (defined in this
 *  class) handles this task.
 *
 */
public class ForumTabActivity extends ListActivity implements OnClickListener {
	
	/*
	 * 
	 */
	private MyForumAdapter<ForumThreadTuple> threadAdapter;
	
	/*
	 * 
	 */
	private Map<Integer,ForumThreadTuple> forumContent;
	
	/*
	 * 
	 */
	private ArrayList<Integer> auxilaryIds;
	
	/*
	 * 
	 */
	private ArrayList<ForumThreadTuple> auxilaryData;
	
	/*
	 * 
	 */
	private Context forumContext;
	

	
	/**
	 * 
	 */
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		Log.d("In onCreate","Forum");
	    setContentView(R.layout.forum_tab_v2);
	    forumContent = new HashMap<Integer,ForumThreadTuple>();
	    auxilaryData = new ArrayList<ForumThreadTuple>();
	    auxilaryIds = new ArrayList<Integer>();
	    forumContext = this;
	    threadAdapter = new MyForumAdapter<ForumThreadTuple>(forumContext,R.layout.forum_row,R.id.threadInfo);
		// get the list of games
		populateList.run();
		
		Iterator<Integer> forumIt = forumContent.keySet().iterator();
		while( forumIt.hasNext() ) {
			int currentId = forumIt.next();
			auxilaryIds.add(currentId);
			auxilaryData.add(forumContent.get(currentId));
			threadAdapter.add(auxilaryData.get(auxilaryData.size()-1));
		}
		
		
		Button createPostButton = (Button) findViewById(R.id.create_post);
		createPostButton.setOnClickListener(this);
		
		//submitPostButton.setVisibility(View.GONE);
		
		//cancelPostButton.setVisibility(View.GONE);
		
		showThreads.run();
	}
	
	/*
	 * 
	 */
	private Thread populateList = new Thread() {
		/**
		 * 
		 */
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_FORUM + "Get/Threads/"
					+ GeoGame.currentGameId);
			client.addCookie(GeoGame.sessionCookie);
			
			try {
				client.Execute(RequestMethod.POST);
				parseThreadResponse(client.getResponse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d("here","9");
						
		}
	};
	
	/*
	 * 
	 */
	private Runnable showThreads = new Runnable(){
		/**
		 * 
		 */
        public void run(){
        	setListAdapter(threadAdapter);
        	
        	// Add listeners
    		ListView listView = getListView();
    		listView.setTextFilterEnabled(true);
    		listView.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				
    				// Set current game
    				
    				Log.d(Integer.toString(auxilaryIds.get(position)),Integer.toString(auxilaryIds.size()));
    				Intent i= new Intent(getApplicationContext(), CommentPageActivity.class);
    				i.putExtra("thread_index",auxilaryIds.get(position));
    				Log.d(Integer.toString(auxilaryIds.get(position)),Integer.toString(auxilaryIds.size()));
    				
    				startActivity(i);
    			}
    		});
        }
	};
	
	
	/**
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		threadAdapter = new MyForumAdapter<ForumThreadTuple>(forumContext,R.layout.forum_row,R.id.threadInfo);
		
		forumContent.clear();
		auxilaryIds.clear();
		auxilaryData.clear();
		
		populateList.run();		
		
		Iterator<Integer> forumIt = forumContent.keySet().iterator();
		while( forumIt.hasNext() ) {
			int currentId = forumIt.next();
			auxilaryIds.add(currentId);
			auxilaryData.add(forumContent.get(currentId));
			threadAdapter.add(auxilaryData.get(auxilaryData.size()-1));
		}
		
		showThreads.run();
		
	}
	
	/**
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
	
	/**
	 * 
	 * @param json
	 */
	private void parseThreadResponse( String json ) {
		JSONTokener tokenizer = new JSONTokener(json);
		Log.d("JSON",json);
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
	
	
	/**
	 * 
	 *
	 * @param <ForumThreadTuple>
	 */
	private class MyForumAdapter<ForumThreadTuple> extends ArrayAdapter<ForumThreadTuple> {

		/**
		 * 
		 * @param context
		 * @param resource
		 * @param textViewResourceId
		 */
		public MyForumAdapter(Context context, int resource, int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}
		
		/**
		 * 
		 */
		public View getView (int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.forum_row, null);
            TextView threadInfo = (TextView) v.findViewById(R.id.threadInfo);
            threadInfo.setText(Html.fromHtml(auxilaryData.get(position).toString()));
            return v;
		}
		
	}

	
	/**
	 * 
	 */
	@Override
	public void onClick(View v) {
		Log.d("Forum_Click","Forum_Click");
		switch( v.getId() ) {
		case R.id.create_post:
			Log.d("Forum_Click_Dialog","Forum_Click_Dialog");
			Context mContext = this;
			CreatePostDialog dialog = new CreatePostDialog(mContext);
			dialog.show();	
		}
	}
	
	/**
	 * 
	 *
	 */
	private class CreatePostDialog extends Dialog implements OnClickListener {

		/*
		 * 
		 */
		private EditText title;
		
		/*
		 * 
		 */
		private EditText message;
		
		/*
		 * 
		 */
		private Button create;
		
		/*
		 * 
		 */
		private Button cancel;
		
		/*
		 * 
		 */
		private Context parentContext;
		
		/**
		 * 
		 * @param context
		 */
		public CreatePostDialog(Context context) {
			super(context);
			parentContext = context;
			this.setContentView(R.layout.create_post_dialog);
			
			title = (EditText) findViewById(R.id.post_title);
			message = (EditText) findViewById(R.id.write_post);
			
			create = (Button) findViewById(R.id.create_post);
			create.setOnClickListener(this);
			
			cancel = (Button) findViewById(R.id.cancel_post);
			cancel.setOnClickListener(this);
			
		}

		/**
		 * 
		 */
		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_post:
				publishPost( title.getText().toString(), message.getText().toString() );
				this.dismiss();
				break;
			case R.id.cancel_post:
				this.dismiss();
				break;
			}
		}
		
		/**
		 * 
		 * @param title
		 * @param message
		 * @return
		 */
		private boolean publishPost( String title, String message ) {
			try {
			RestClient client = new RestClient(GeoGame.URL_FORUM+"New/Thread/"+GeoGame.currentGameId);
			Log.d("FORUM URL",GeoGame.URL_FORUM+"New/Thread/"+GeoGame.currentGameId);
			client.AddParam("title", title);
			client.AddParam("message", message);
			client.addCookie(GeoGame.sessionCookie);
			client.Execute(RequestMethod.POST);
			Log.d("Response",client.getResponse());
			Log.d("Response Code",Integer.toString(client.getResponseCode()));
			Log.d("Error",client.getErrorMessage());
			return true;
			} catch( Exception ex ) {
				Toast.makeText(parentContext, "Error: your post was not created", 2);
				Log.d("Post","False");
				return false;
			}
		}
		
	}
	
	
	
}



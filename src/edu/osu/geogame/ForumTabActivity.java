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

import org.json.JSONTokener;

import edu.osu.geogame.exception.NoThreadsExistException;

public class ForumTabActivity extends ListActivity implements OnClickListener {
	private Handler mHandler;
	private MyForumAdapter<ForumThreadTuple> threadAdapter;
	private Map<Integer,ForumThreadTuple> forumContent;
	
	private ArrayList<Integer> auxilaryIds;
	private ArrayList<ForumThreadTuple> auxilaryData;

	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		Log.d("In onCreate","Forum");
	    setContentView(R.layout.forum_tab);
	    forumContent = new HashMap<Integer,ForumThreadTuple>();
	    auxilaryData = new ArrayList<ForumThreadTuple>();
	    auxilaryIds = new ArrayList<Integer>();
	    Context context = this;
	    threadAdapter = new MyForumAdapter<ForumThreadTuple>(context,R.layout.forum_row,R.id.threadInfo);
		// get the list of games
		mHandler = new Handler();
		populateList.run();
		
		Iterator<Integer> forumIt = forumContent.keySet().iterator();
		while( forumIt.hasNext() ) {
			int currentId = forumIt.next();
			auxilaryIds.add(currentId);
			auxilaryData.add(forumContent.get(currentId));
			threadAdapter.add(auxilaryData.get(auxilaryData.size()-1));
		}
		
		
		//submitPostButton.setVisibility(View.GONE);
		
		//cancelPostButton.setVisibility(View.GONE);
		
		showThreads.run();
	}
	
	
	private Thread populateList = new Thread() {
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
	
	private Runnable showThreads = new Runnable(){
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
	
	
	
	private class MyForumAdapter<ForumThreadTuple> extends ArrayAdapter<ForumThreadTuple> {

		public MyForumAdapter(Context context, int resource, int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}
		
		public View getView (int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.forum_row, null);
            TextView threadInfo = (TextView) v.findViewById(R.id.threadInfo);
            threadInfo.setText(Html.fromHtml(auxilaryData.get(position).toString()));
            return v;
		}
		
	}



	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
		case R.id.create_post:
			Context mContext = this;
			CreatePostDialog dialog = new CreatePostDialog(mContext);
			dialog.show();	
		}
	}
	
	
	
	
	
	
	private class CreatePostDialog extends Dialog implements OnClickListener {

		EditText writeComment;
		Button create;
		Button cancel;
		
		public CreatePostDialog(Context context) {
			super(context);
			this.setContentView(R.layout.create_post_dialog);
			
			writeComment = (EditText) findViewById(R.id.write_post);
			
			create = (Button) findViewById(R.id.create_post);
			create.setOnClickListener(this);
			
			cancel = (Button) findViewById(R.id.cancel_post);
			cancel.setOnClickListener(this);
			
		}

		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_post:
				publishPost( writeComment.getText().toString() );
				break;
			case R.id.cancel_post:
				this.dismiss();
				break;
			}
		}
		
		
		private boolean publishPost( String post ) {
			
			return true;
		}
		
	}



	
	
	
	
}



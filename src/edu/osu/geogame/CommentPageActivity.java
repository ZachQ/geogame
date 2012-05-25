package edu.osu.geogame;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONTokener;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CommentPageActivity extends ListActivity implements OnClickListener {
		
		private MyCommentAdapter<CommentThreadTuple> commentAdapter;
		private ArrayList<CommentThreadTuple> comments;
		private int threadId;
		
		private Button createComment;
		
		@Override
		public void onCreate( Bundle savedInstanceState ) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.comments_page);
		    Bundle extras = getIntent().getExtras();
			threadId = extras.getInt("thread_index");
			commentAdapter = new MyCommentAdapter<CommentThreadTuple>(this,R.layout.forum_row,R.id.threadInfo);
			comments = new ArrayList<CommentThreadTuple>();
			populateList.run();
			showComments.run();
			Log.d("what is", "going on");
			
			for( int i = 0; i < comments.size(); i++ ) {
				commentAdapter.add(comments.get(i));
			}
			
			createComment = (Button) findViewById(R.id.create_comment);
			createComment.setOnClickListener(this);
			
		}
		
		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_comment:
				CreateCommentDialog dialog = new CreateCommentDialog(this);
				dialog.show();
			}
		}
		
		private Thread populateList = new Thread() {
			public void run() {
									
				RestClient client = new RestClient(GeoGame.URL_FORUM + "Get/Comments/"
						+ Integer.toString(threadId));
					
				client.addCookie(GeoGame.sessionCookie);
				
				try {
					client.Execute(RequestMethod.POST);
					parseCommentResponse(client.getResponse(), threadId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d("here","9");
				
			}
		};
		
		
		
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
							
				comments.add(commentInfo);
				
				tokenizer.next(2);
				if( tokenizer.next() != ',' ) {
					return;
				}
				
			} while( true );
		} catch( JSONException ex ) {
			Log.d("EXC_COM",ex.getMessage());
		}
		
		}
		
		
		private Runnable showComments = new Runnable(){
	        public void run(){
	        	setListAdapter(commentAdapter);
	        	
	        	// Add listeners
	    		ListView listView = getListView();
	    		//listView.setTextFilterEnabled(true);
	    		listView.setEnabled(false);
	        }
		};
		
		
	
	private class MyCommentAdapter<CommentThreadTuple> extends ArrayAdapter<CommentThreadTuple> {

		public MyCommentAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	private class CreateCommentDialog extends Dialog implements OnClickListener {

		EditText writeComment;
		Button create;
		Button cancel;
		
		public CreateCommentDialog(Context context) {
			super(context);
			this.setContentView(R.layout.create_comment_dialog);
			
			writeComment = (EditText) findViewById(R.id.write_message);
			
			create = (Button) findViewById(R.id.create_message);
			create.setOnClickListener(this);
			
			cancel = (Button) findViewById(R.id.cancel_message);
			cancel.setOnClickListener(this);
			
		}

		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_message:
				publishComment( writeComment.getText().toString() );
				break;
			case R.id.cancel_message:
				this.dismiss();
				break;
			}
		}
		
		
		private boolean publishComment( String post ) {
			
			return true;
		}
		
	}

}
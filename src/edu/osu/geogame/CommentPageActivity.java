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
import android.widget.Toast;


/**
 * This page lists comments made under a specific post.  That is, it's launched from the ForumTabActivity.
 * Users can read comments and create comments of their own by clicking the Create Comment button. 
 *
 */
public class CommentPageActivity extends ListActivity implements OnClickListener {
		
		/*
		 * This adapter is hooked up to the ListView of this activity's UI.  It's used to display
		 * the comments retrieved from the server.
		 */
		private MyCommentAdapter<CommentThreadTuple> commentAdapter;
		
		/*
		 * Collection of all comments made under this post.  (threadId uniquely identifies the post,
		 *  see below).  Comments are fed from this collection to commentAdapter to be displayed. 
		 */
		private ArrayList<CommentThreadTuple> comments;
		
		/*
		 * The unique id of the post that is hosting these comments
		 */
		private int threadId;
		
		/*
		 * UI button that allows a comment to be written and posted.  A CreateCommentDialog is 
		 * spawned to handle this task; see the inner class defined below.
		 */
		private Button createComment;
		
		/*
		 * The context of this activity; referenced in CreateCommentDialog
		 */
		private Context parentContext;
		
		/**
		 * commentAdapter is defined, and the comments are fetched from the server (this is done in a separate thread) and
		 * fed to the commentAdapter to be displayed by the UI.
		 * @param savedInstanceState
		 */
		@Override
		public void onCreate( Bundle savedInstanceState ) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.comments_page_v2);
		    parentContext = this;
		    Bundle extras = getIntent().getExtras();
			threadId = extras.getInt("thread_index");
			commentAdapter = new MyCommentAdapter<CommentThreadTuple>(this,R.layout.forum_row,R.id.threadInfo);
			comments = new ArrayList<CommentThreadTuple>();
			populateList.run();
			Log.d("what is", "going on");
			
			for( int i = 0; i < comments.size(); i++ ) {
				commentAdapter.add(comments.get(i));
			}
			showComments.run();
			createComment = (Button) findViewById(R.id.create_comment);
			createComment.setOnClickListener(this);
			
		}
		
		/**
		 * The comments need to be once again fetched from the server and fed to the commentAdapter.
		 */
		@Override
		public void onResume() {
			super.onResume();
			commentAdapter = new MyCommentAdapter<CommentThreadTuple>(this,R.layout.forum_row,R.id.threadInfo);
			comments.clear();
			populateList.run();
			for( int i = 0; i < comments.size(); i++ ) {
				commentAdapter.add(comments.get(i));
			}
			showComments.run();
		}
		
		/**
		 * If the Create Comment button is pushed, a CreateCommentDialog is launched.
		 */
		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_comment:
				CreateCommentDialog dialog = new CreateCommentDialog(this);
				dialog.show();
			}
		}
		
		/**
		 * The task of retrieving all comments for this  post is run in this thread.
		 */
		private Thread populateList = new Thread() {
			public void run() {
									
				//RestClient goes to the Comments url
				RestClient client = new RestClient(GeoGame.URL_FORUM + "Get/Comments/"
						+ Integer.toString(threadId));				
				client.addCookie(GeoGame.sessionCookie);
				
				try {
					client.Execute(RequestMethod.POST);
					
					//The task of interpreting the JSON response is passed onto parseCommentReponse()
					parseCommentResponse(client.getResponse());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Log.d("here","9");
				
			}
		};
		
		
		/**
		 * The JSON response is interpreted by this method.  Here the 'comments' variable (see above)
		 * will be fed CommentThreadTuples; these are objects that store comment data.
		 * @param json  the response from the server, containing comments of this post
		 */
		private void parseCommentResponse( String json ) {
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
		
		/*
		 * In this thread commentAdapter is simply up to the ListView of the 
		 * UI, and comments become viewable
		 */
		private Runnable showComments = new Runnable(){
			/**
			 * Run this thread
			 */
	        public void run(){
	        	setListAdapter(commentAdapter);
	        	
	        	// Add listeners
	    		ListView listView = getListView();
	    		//listView.setTextFilterEnabled(true);
	    		listView.setEnabled(false);
	        }
		};
		
		
	/**
	 * The ArrayAdapter to be passed to this UIs ListView 
	 *
	 * @param <CommentThreadTuple>
	 */
	private class MyCommentAdapter<CommentThreadTuple> extends ArrayAdapter<CommentThreadTuple> {

		public MyCommentAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	/**
	 * This dialog handles the task of creating a comment; it's comprised of a text box
	 * and a submit and cancel button.
	 *
	 */
	private class CreateCommentDialog extends Dialog implements OnClickListener {

		/*
		 * The text box where users write their comment
		 */
		EditText writeComment;
		
		/*
		 * Publishes the comment (and destroys the dialog box)
		 */
		Button create;
		
		/*
		 * Cancels the comment (and destroys the dialog box)
		 */
		Button cancel;
		
		/**
		 * Set the dialog interface and listeners
		 * @param context  the parent View (that of CommentPageActivity) will need to be given
		 */
		public CreateCommentDialog(Context context) {
			super(context);
			this.setContentView(R.layout.create_comment_dialog);
			
			writeComment = (EditText) findViewById(R.id.write_message);
			
			create = (Button) findViewById(R.id.create_message);
			create.setOnClickListener(this);
			
			cancel = (Button) findViewById(R.id.cancel_message);
			cancel.setOnClickListener(this);
			
		}

		/**
		 * Set button listeners
		 * @param v  the view for whom to specify action
		 */
		@Override
		public void onClick(View v) {
			switch( v.getId() ) {
			case R.id.create_message:
				//when the Create button is pushed, the text in the text box is retrieved
				// and publishComment() sends it to the server
				publishComment( writeComment.getText().toString() );
				this.dismiss();
				break;
			case R.id.cancel_message:
				this.dismiss();
				break;
			}
		}
		
		/**
		 * Send the comment to the server
		 * @param message  the message retrieved from the text box
		 * @return boolean  true if the comment was successfully published
		 * 
		 * TODO: Verify the 'success' response from the server
		 */
		private boolean publishComment( String message ) {
			try {
				RestClient client = new RestClient(GeoGame.URL_FORUM+"New/Comment/"+threadId);
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
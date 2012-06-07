package edu.osu.geogame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONTokener;


/**
 * This is the Forum tab. Posts are displayed and, if clicked, their comments
 * are displayed (CommentPageActivity handles this task). Posts can be created
 * by clicking the Create Post button; CreatePostDialog (defined in this class)
 * handles this task.
 * 
 */
public class ForumTabActivity extends ListActivity implements OnClickListener {

	/**
	 * This adapter is passed in threads (posts) retrieved from the server, and
	 * in turn passes these to the ListView of this activity's UI.
	 */
	private MyForumAdapter<ForumThreadTuple> threadAdapter;

	/**
	 * Maps thread ids to thread content
	 */
	private Map<Integer, ForumThreadTuple> forumContent;

	/**
	 * It's necessary to have the following linear and indexable structures in
	 * order to correctly display the UI.
	 */
	private ArrayList<Integer> auxilaryIds;
	private ArrayList<ForumThreadTuple> auxilaryData;

	/**
	 * The context of this activity
	 */
	private Context forumContext;

	/**
	 * Set the UI; create the adapter; gather the threads from the server;
	 * create auxiliary structures; set listeners; display the posts
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("In onCreate", "Forum");
		setContentView(R.layout.forum_tab_v2);
		forumContent = new HashMap<Integer, ForumThreadTuple>();
		auxilaryData = new ArrayList<ForumThreadTuple>();
		auxilaryIds = new ArrayList<Integer>();
		forumContext = this;
		threadAdapter = new MyForumAdapter<ForumThreadTuple>(forumContext,
				R.layout.forum_row, R.id.threadInfo);
		// get the threads
		populateList.run();

		Iterator<Integer> forumIt = forumContent.keySet().iterator();
		while (forumIt.hasNext()) {
			int currentId = forumIt.next();
			auxilaryIds.add(currentId);
			auxilaryData.add(forumContent.get(currentId));
			threadAdapter.add(auxilaryData.get(auxilaryData.size() - 1));
		}

		Button createPostButton = (Button) findViewById(R.id.create_post);
		createPostButton.setOnClickListener(this);

		showThreads.run();
	}

	/**
	 * In this separate thread, collect the threads (posts) from the server and
	 * feed them to the adapter
	 */
	private Thread populateList = new Thread() {
		/**
		 * Run the thread
		 */
		public void run() {
			RestClient client = new RestClient(GeoGame.URL_FORUM
					+ "Get/Threads/" + GeoGame.currentGameId);
			client.addCookie(GeoGame.sessionCookie);

			try {
				client.Execute(RequestMethod.POST);
				parseThreadResponse(client.getResponse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d("here", "9");

		}
	};

	/**
	 * In this separate thread, kook up threadAdapter to the ListView and
	 * display the threads
	 */
	private Runnable showThreads = new Runnable() {
		/**
		 * Run the thread
		 */
		public void run() {
			setListAdapter(threadAdapter);

			// Add listeners
			ListView listView = getListView();
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// Set current game

					Log.d(Integer.toString(auxilaryIds.get(position)),
							Integer.toString(auxilaryIds.size()));
					Intent i = new Intent(getApplicationContext(),
							CommentPageActivity.class);
					i.putExtra("thread_index", auxilaryIds.get(position));
					Log.d(Integer.toString(auxilaryIds.get(position)),
							Integer.toString(auxilaryIds.size()));

					startActivity(i);
				}
			});
		}
	};

	/**
	 * Recreate the adapter; gather the threads from the server; re-populate
	 * auxiliary structures; display the posts
	 */
	@Override
	public void onResume() {
		super.onResume();
		threadAdapter = new MyForumAdapter<ForumThreadTuple>(forumContext,
				R.layout.forum_row, R.id.threadInfo);

		forumContent.clear();
		auxilaryIds.clear();
		auxilaryData.clear();

		populateList.run();

		Iterator<Integer> forumIt = forumContent.keySet().iterator();
		while (forumIt.hasNext()) {
			int currentId = forumIt.next();
			auxilaryIds.add(currentId);
			auxilaryData.add(forumContent.get(currentId));
			threadAdapter.add(auxilaryData.get(auxilaryData.size() - 1));
		}

		showThreads.run();

	}

	/**
	 * This will change the color format of the Activity so that the background
	 * gradient will be very smooth. Without this it has noticeable
	 * color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	/**
	 * The JSON response is interpreted by this method. Here the 'forumContent'
	 * variable (see above) will be fed ids and ForumThreadTuples; these are
	 * objects that store post data.
	 * 
	 * @param json
	 *            the response from the server, containing posts and their data
	 */
	private void parseThreadResponse(String json) {
		JSONTokener tokenizer = new JSONTokener(json);
		Log.d("JSON", json);
		try {

			// Check that there is forum content to display
			tokenizer.nextTo(':');
			tokenizer.next();
			if (!tokenizer.nextTo(',').equals("true")) {
				return;
			}

			tokenizer.nextTo(':');
			tokenizer.next(2);
			if (!tokenizer.nextTo('"').equals("threads")) {
				return;
			}

			tokenizer.nextTo('{');
			tokenizer.next(2);

			// Begin retrieving forum content
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
				if (tokenizer.next() != ',') {
					return;
				}

			} while (true);

		} catch (JSONException ex) {
			Log.d("EXC", ex.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @param <ForumThreadTuple>
	 */
	private class MyForumAdapter<ForumThreadTuple> extends
			ArrayAdapter<ForumThreadTuple> {

		/**
		 * 
		 * @param context
		 *            the current context
		 * @param resource
		 *            the resource ID for a layout file containing a TextView to
		 *            use when instantiating views
		 * @param textViewResourceId
		 *            the id of the TextView within the layout resource to be
		 *            populated
		 */
		public MyForumAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}

		/**
		 * Get a View that displays the data at the specified position in the
		 * data set. You can either create a View manually or inflate it from an
		 * XML layout file. When the View is inflated, the parent View
		 * (GridView, ListView...) will apply default layout parameters unless
		 * you use inflate(int, android.view.ViewGroup, boolean) to specify a
		 * root view and to prevent attachment to the root.
		 * 
		 * @param position
		 *            the position of the item within the adapter's data set of
		 *            the item whose view we want
		 * @param convertView
		 *            The old view to reuse, if possible. Note: You should check
		 *            that this view is non-null and of an appropriate type
		 *            before using. If it is not possible to convert this view
		 *            to display the correct data, this method can create a new
		 *            view. Heterogeneous lists can specify their number of view
		 *            types, so that this View is always of the right type (see
		 *            getViewTypeCount() and getItemViewType(int)).
		 * @param parent
		 *            the parent that this view will eventually be attached to
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.forum_row, null);
			TextView threadInfo = (TextView) v.findViewById(R.id.threadInfo);
			threadInfo.setText(Html.fromHtml(auxilaryData.get(position)
					.toString()));
			return v;
		}

	}

	/**
	 * If the Create Post button is clicked launch a CreatePostDialog that
	 * handles the task
	 */
	@Override
	public void onClick(View v) {
		Log.d("Forum_Click", "Forum_Click");
		switch (v.getId()) {
		case R.id.create_post:
			Log.d("Forum_Click_Dialog", "Forum_Click_Dialog");
			Context mContext = this;
			CreatePostDialog dialog = new CreatePostDialog(mContext);
			dialog.show();
		}
	}

	/**
	 * Handles the task of creating a post. Comprised of a 'title' textbox,
	 * 'message' textbox, a submit button and a cancel button.
	 * 
	 */
	private class CreatePostDialog extends Dialog implements OnClickListener {

		/**
		 * Text box to enter the post title
		 */
		private EditText title;

		/**
		 * Text box to enter the post message
		 */
		private EditText message;

		/**
		 * Button that sends the post to the server
		 */
		private Button create;

		/**
		 * Button that cancels the post
		 */
		private Button cancel;

		/**
		 * This will need to be set to the context of ForumTabActivity
		 */
		private Context parentContext;

		/**
		 * Create the dialog and set the UI and listeners
		 * 
		 * @param context
		 *            the context from where this dialog will be launched
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
		 * The Create button sends the post to the server; the Cancel button
		 * dismisses the dialog.
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.create_post:
				publishPost(title.getText().toString(), message.getText()
						.toString());
				this.dismiss();
				onResume();
				break;
			case R.id.cancel_post:
				this.dismiss();
				break;
			}
		}

		/**
		 * Handles the task of sending a post to the server
		 * 
		 * @param title
		 *            the title of the post
		 * @param message
		 *            the post content
		 * @return true if the post was successfully published
		 * 
		 *         TODO: Verify the 'success' response from the server
		 */
		private boolean publishPost(String title, String message) {
			try {
				RestClient client = new RestClient(GeoGame.URL_FORUM
						+ "New/Thread/" + GeoGame.currentGameId);
				Log.d("FORUM URL", GeoGame.URL_FORUM + "New/Thread/"
						+ GeoGame.currentGameId);
				client.AddParam("title", title);
				client.AddParam("message", message);
				client.addCookie(GeoGame.sessionCookie);
				client.Execute(RequestMethod.POST);
				Log.d("Response", client.getResponse());
				Log.d("Response Code",
						Integer.toString(client.getResponseCode()));
				Log.d("Error", client.getErrorMessage());
				return true;
			} catch (Exception ex) {
				Toast.makeText(parentContext,
						"Error: your post was not created", 2);
				Log.d("Post", "False");
				return false;
			}
		}

	}

}

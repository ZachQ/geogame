package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Ben Elliott
 */
public class Menu extends Activity {
	Button joinGame, playGame, edit, about, logOut;
	TextView loggedIn;
	GeoGame game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		game = (GeoGame)getApplicationContext();
		
		// Show status
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
	    	String value = extras.getString("gameID");
	    	
	    	Toast.makeText(getApplicationContext(),
					"Joined Game ID:" + value, Toast.LENGTH_SHORT).show();
	    }
		
		// Get references
		joinGame = (Button) findViewById(R.id.buttonJoinGame);
		playGame = (Button) findViewById(R.id.buttonPlayGame);
		edit = (Button) findViewById(R.id.buttonEditAccout);
		about = (Button) findViewById(R.id.buttonAbout);
		logOut = (Button) findViewById(R.id.buttonLogOut);
		loggedIn = (TextView) findViewById(R.id.textViewloggedIn);
		
		// Set screen text
		loggedIn.setText("Logged in as: " + game.username);
		
		// Add ActionListeners to buttons
		joinGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), JoinGame.class);
                startActivityForResult(myIntent, 0);
			}
		});
		playGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), PlayGame.class);
                startActivityForResult(myIntent, 0);
			}
		});
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), null);
                startActivityForResult(myIntent, 0);
			}
		});
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), About.class);
                startActivityForResult(myIntent, 0);
			}
		});
		logOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/** THIS IS ZACH'S COMMENT:
			 	* When you get rid of this line, logout just exits to entire application
				* so if you have your information saved to the phone, you can never log
				* in as another user
				*/
				// Intent myIntent = new Intent(v.getContext(), Login.class);
                // startActivityForResult(myIntent, 0);
                
				finish();
			}
		});
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

		

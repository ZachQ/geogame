package edu.osu.geogame;

import edu.osu.geogame.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * This is basically the "splash screen" that comes up when the app is opened to
 * display GeoGame logo, Geo dept and Ohio State University
 * 
 * @author Zachary Quinn
 */
public class Splash extends Activity {
	Button start;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// Get references
		start = (Button) findViewById(R.id.buttonStart);

		// Add ActionListeners to buttons
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Login.class);
				startActivityForResult(myIntent, 0);
			}
		});
	}

	@Override
	protected void onPause() {
		// This will "pause" the activity aka kill itself so
		// the user can't press the "back" button and go back
		// to the splash screen.
		super.onPause();
		finish();
	}

	/*
	 * (non-Javadoc) This will change the color format of the Activity so that
	 * the background gradient will be very smooth. Without this it has
	 * noticeable color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}
package edu.osu.geogame;


import edu.osu.geogame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * This is basically the "splash screen" that comes up when the app
 * is opened to display GeoGame logo, Geo dept and Ohio State University
 * @author Zachary Quinn
 */
public class Splash extends Activity {
    /** Called when the activity is first created. */
	
	int counter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
		final int sleepTime = 6000;
		// timer to sleep for 5 seconds
		Thread timer = new Thread() {
			// wait for 5 seconds, then proceed to the next screen to login
			public void run() {
				try {
					sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally{
					// Proceed to the next "home" screen
					Intent openStartingPoint = new Intent("edu.osu.geogame.HOMESCREEN");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		// This will "pause" the activity aka kill itself so
		// the user can't press the "back" button and go back
		// to the splash screen.
		super.onPause();
		finish();
	}

}
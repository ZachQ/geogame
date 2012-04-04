package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * This is basically the "splash screen" that comes up when the app
 * is opened to display GeoGame logo, Geo dept and Ohio State University
 * @author Zach
 */
public class GeoGameActivity extends Activity {
    /** Called when the activity is first created. */
	
	int counter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
					// Proceed to the next screen to login
					//Intent openStartingPoint = new Intent("com.DeadManSwitch.REGISTER");
					//startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		// this will "pause" the activity
		super.onPause();
		finish();
	}

}
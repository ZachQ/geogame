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
 * This is basically the "splash screen" that comes up when the app
 * is opened to display GeoGame logo, Geo dept and Ohio State University
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
        String cookie = null;
        boolean firstLogin = false;
//        // Check to see if it is the first time opening the app
//        SharedPreferences sp2 = this.getSharedPreferences("First",0);
//        boolean firstLogin = sp2.getBoolean("isFirstLogin", true);
//        System.out.println("fewafw "+firstLogin);
//        
//        // Attempt to get the saved Cookie
//        SharedPreferences sp1 = this.getSharedPreferences("Login",0);
//        String cookie = sp1.getString("Cookie", null); 
        
        if (cookie == null || firstLogin == true) { 
        	// Cookie not found, continue to Login screen

        	
            // Add ActionListeners to buttons
            start.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				Intent myIntent = new Intent(v.getContext(), Login.class);
                    startActivityForResult(myIntent, 0);
     			}
     		});
        } else { 
        	// Cookie found, go to Menu screen   	
            // Add ActionListeners to buttons
            start.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				Intent myIntent = new Intent(v.getContext(), Menu.class);
                    startActivityForResult(myIntent, 0);
     			}
     		});
        }
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
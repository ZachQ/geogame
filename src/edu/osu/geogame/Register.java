package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Register extends Activity {

	Button register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		// Get register reference
		register = (Button) findViewById(R.id.buttonRegister);
		
		// Add ActionListeners to buttons
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO:: Try to register
				
				Intent myIntent = new Intent(v.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
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

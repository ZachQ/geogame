package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The Home Screen will hold the "Login", "Register" and "About/Rules" buttons.
 * @author Zachary Quinn
 */

public class HomeScreen extends Activity {

	Button login, register;
	EditText editLogin, editPassword;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		// Get references
		login = (Button) findViewById(R.id.buttonLogin);
		register = (Button) findViewById(R.id.buttonRegister);
		editLogin = (EditText) findViewById(R.id.editLogin);
		editPassword = (EditText) findViewById(R.id.editPassword);
		
		// Add ActionListeners to buttons
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO:: Try to login
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * This will change the color format of the Activity so that
	 * the background gradient will be very smooth.  Without this
	 * it has noticable color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

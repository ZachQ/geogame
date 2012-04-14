package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The Home Screen will hold the "Login", "Register" and "About/Rules" buttons.
 * @author Zachary Quinn, Ben Elliott
 */

public class Login extends Activity {

	Button login, register;
	EditText editLogin, editPassword;
	GeoGame game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		game = (GeoGame)getApplicationContext();
		
		// Get references
		login = (Button) findViewById(R.id.buttonLogin);
		register = (Button) findViewById(R.id.buttonRegister);
		editLogin = (EditText) findViewById(R.id.editLogin);
		editPassword = (EditText) findViewById(R.id.editPassword);
		
		// Add ActionListeners to buttons
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Attempt login
				RestClient client = new RestClient(game.URL_LOGON);
				client.AddParam("UserName", editLogin.getText().toString());
				client.AddParam("Password", editPassword.getText().toString());
				
				// Attempt login
				try {
					client.Execute(RequestMethod.POST);
				} catch (Exception e) {}
				
				// Check for success
				if (client.getResponseCode() == 302) {
					// Success
					// Store the login Cookie and UserName
					if (client.getCookies().size() > 0)
						game.sessionCookie = client.getCookies().get(0);
					else
						Toast.makeText(getApplicationContext(),
								"Unknown Error", Toast.LENGTH_SHORT).show();
					game.username = editLogin.getText().toString();
					
					// Next Screen
					Intent myIntent = new Intent(v.getContext(), edu.osu.geogame.Menu.class);
                	startActivityForResult(myIntent, 0);
				} else if (client.getResponseCode() == 200) {
					// Failure
					Toast.makeText(getApplicationContext(),
							"Incorrect Login or Password", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Unknown Error", Toast.LENGTH_SHORT).show();
				}
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
	 * it has noticeable color-stepping.
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}

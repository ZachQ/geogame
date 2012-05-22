package edu.osu.geogame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The Home Screen will hold the "Login", "Register" and "About/Rules" buttons.
 * @author Zachary Quinn, Ben Elliott
 */

public class Login extends Activity {

	Button login, register;
	EditText editLogin, editPassword;
	private Handler mHandler;
	private ProgressBar loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mHandler = new Handler();
		
		// Get references
		loading = (ProgressBar) findViewById(R.id.login_progress);
		login = (Button) findViewById(R.id.buttonLogin);
		register = (Button) findViewById(R.id.buttonRegister);
		editLogin = (EditText) findViewById(R.id.editLogin);
		editPassword = (EditText) findViewById(R.id.editPassword);
		
		// Add ActionListeners to buttons
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create the thread
				Thread attemptLogin = new Thread() {
					public void run() {
						// Attempt login
						RestClient client = new RestClient(GeoGame.URL_LOGON);
						client.AddParam("UserName", editLogin.getText().toString());
						client.AddParam("Password", editPassword.getText().toString());
						
						// Attempt login
						try {
							client.Execute(RequestMethod.POST);
						} catch (Exception e) {}
						
						// Check for success
						if (client.getResponseCode() == 302) {
							// Store the login Cookie and UserName
							if (client.getCookies().size() > 0)
								GeoGame.sessionCookie = client.getCookies().get(0);
							// Success
							mHandler.post(Success);
						} else if (client.getResponseCode() == 200) {
							// Failure
							mHandler.post(Failure);
						} else {
							Log.d(Integer.toString(client.getResponseCode()),"rcode");
							mHandler.post(Error);
						}
						
						// Re-enable the login button
						mHandler.post(endThread);
					}
				};
				// Disable login button to prevent multiple attempts
				login.setEnabled(false);
				loading.setVisibility(View.VISIBLE);
				// Run the thread
				attemptLogin.start();
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), Register.class);
				startActivity(myIntent);
			}
		});
	}
	
	private Runnable Success = new Runnable(){
        public void run(){
        	// Store username
        	
			GeoGame.username = editLogin.getText().toString();
			
			/*
			// -------------------------------------------
			// SAVE LOGIN INFORMATION TO PHONE
			SharedPreferences sp = getSharedPreferences("Login", 0);
			SharedPreferences.Editor Ed = sp.edit();
			Ed.putString("Cookie", GeoGame.sessionCookie.toString() );                
			Ed.commit();
			// -------------------------------------------
			// TESTING
			SharedPreferences sp2 = getSharedPreferences("First", 0);
			SharedPreferences.Editor Ed2 = sp2.edit();
			Ed2.putBoolean("isFirstLogin", false);              
			Ed2.commit();
			// -------------------------------------------
			*/
			// Next Screen
			Intent myIntent = new Intent(getApplicationContext(), edu.osu.geogame.Menu.class);
			Log.d("COOKIE",GeoGame.sessionCookie.toString());
			startActivity(myIntent);
        }
    };
    
    private Runnable Failure = new Runnable(){
        public void run(){
        	Toast.makeText(getApplicationContext(),
					"Incorrect Login or Password", Toast.LENGTH_SHORT).show();
        }
    };
    
    private Runnable Error = new Runnable(){
        public void run(){
        	Toast.makeText(getApplicationContext(),
					"Unknown Error", Toast.LENGTH_SHORT).show();
        }
    };
    
    private Runnable endThread = new Runnable(){
        public void run(){
        	login.setEnabled(true);
        	loading.setVisibility(View.INVISIBLE);
        }
    };
    
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

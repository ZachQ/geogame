package edu.osu.geogame;

import org.apache.http.cookie.Cookie;
import android.app.Application;

/**
 * @author Ben Elliott
 */
public class GeoGame extends Application {
	/* Constant URLs */
	public String URL_LOGON = "http://geogame.osu.edu/Account/LogOn";
	
	/* Login Cookie */
	public Cookie sessionCookie;
	
	/* Game Variables */
	public String username;
	
}

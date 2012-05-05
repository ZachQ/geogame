package edu.osu.geogame;

import org.apache.http.cookie.Cookie;
import android.app.Application;

/**
 * @author Ben Elliott
 */
public class GeoGame extends Application {
	/* Constant URLs */
	public static String URL_LOGON = "http://geogame.osu.edu/Account/LogOn";
	public static String URL_GAME = "http://geogame.osu.edu/Game/";
	public static String URL_MARKET = "http://geogame.osu.edu/Market/";
	
	/* Login Cookie */
	public static Cookie sessionCookie;
	
	/* Game Variables */
	public static String username;
	public static String currentGameId; 
	public static String familyName, money, adults, seedLR, seedHYC, fertilizer, grainLR, grainHYC, water, oxen, labor;
	
	
	// for testing
	public static String test;
	
}

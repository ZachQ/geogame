package edu.osu.geogame;

import org.apache.http.cookie.Cookie;
import android.app.Application;

/**
 * This class holds all of the URLs and global variables.
 * @author Ben Elliott
 */
public class GeoGame extends Application {
	/* Constant URLs */
	public static String URL_LOGON = "http://geogame.osu.edu/Account/LogOn";
	public static String URL_REGISTER = "http://geogame.osu.edu/Account/Register";
	public static String URL_GAME = "http://geogame.osu.edu/Game/";
	public static String URL_MARKET = "http://geogame.osu.edu/Market/";
	public static String URL_FORUM = "http://geogame.osu.edu/Forum/";
	public static String URL_TIME = "http://geogame.osu.edu/Game/Turn/";
	
	/* Login Cookie */
	public static Cookie sessionCookie;
	
	/* Game Variables */
	public static String username;
	public static String currentGameId; 
	public static String familyName, money, adults, seedLR, seedHYC, fertilizer, grainLR, grainHYC, water, oxen, labor;
	public static String timer, status, turn;
	/* Item Costs */
	public static int costSeedLR, costSeedHYC, costFertilizer, costWater, costOxen, costGrainLR, costGrainHYC;
	
	
	// for testing
	public static String test;
	
}

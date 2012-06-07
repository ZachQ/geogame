package edu.osu.geogame;

import org.apache.http.cookie.Cookie;
import android.app.Application;

/**
 * This class holds all of the URLs and global variables.
 * 
 * @author Ben Elliott
 */
public class GeoGame extends Application {

	/**
	 * Login URL
	 */
	public static String URL_LOGON = "http://geogame.osu.edu/Account/LogOn";

	/**
	 * Register URL
	 */
	public static String URL_REGISTER = "http://geogame.osu.edu/Account/Register";

	/**
	 * Game URL
	 */
	public static String URL_GAME = "http://geogame.osu.edu/Game/";

	/**
	 * Market URL
	 */
	public static String URL_MARKET = "http://geogame.osu.edu/Market/";

	/**
	 * Forum URL
	 */
	public static String URL_FORUM = "http://geogame.osu.edu/Forum/";

	/**
	 * Time URL
	 */
	public static String URL_TIME = "http://geogame.osu.edu/Game/Turn/";

	/**
	 * The player's cookie for this session
	 */
	public static Cookie sessionCookie;

	/**
	 * The player's username
	 */
	public static String username;

	/**
	 * The id of the current game
	 */
	public static String currentGameId;

	/**
	 * Various constants
	 */
	public static String familyName, money, adults, seedLR, seedHYC,
			fertilizer, grainLR, grainHYC, water, oxen, labor;
	public static String timer, status, turn;
	public static int costSeedLR, costSeedHYC, costFertilizer, costWater,
			costOxen, costGrainLR, costGrainHYC;

	// for testing
	public static String test;

}

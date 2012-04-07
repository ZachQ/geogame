package edu.osu.geogame;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Ben Elliott
 */
public class Map extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
}

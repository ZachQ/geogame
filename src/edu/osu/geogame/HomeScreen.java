package edu.osu.geogame;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The Home Screen will hold the "Login", "Register" and "About/Rules" buttons.
 * @author Zachary Quinn
 */

public class HomeScreen extends ListActivity {

	String classes[] = {"Login", "Register", "About", "text1", "text2", "text3"};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_list_item_1, classes));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String choice = classes[position];
		
		// reference the Login.java class
		Class tempClass = null;
		try {
			tempClass = Class.forName("edu.osu.geogame." + choice);
			Intent HomeScreenIntent = new Intent(HomeScreen.this, tempClass);
			startActivity(HomeScreenIntent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}

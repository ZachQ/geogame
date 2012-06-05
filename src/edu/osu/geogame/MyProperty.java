package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * This class is not used, but it was going to display a list
 * of all of the properties that the user owns. That way you
 * could see exactly how much land you owned and our initial
 * idea was to have the user be able to plant/seed/etc. all
 * from this screen.
 * @author Ben Elliott
 */
public class MyProperty extends ListActivity {
	private Vector<HashMap<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.property_list);
		data = new Vector<HashMap<String, String>>();
		
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				data,
				R.layout.property_row,
				new String[] {"name","one"},
				new int[] {R.id.text1,R.id.text2});
		
		// Create the list of properties
		populateList();
	    
		setListAdapter(adapter);
		
		// Add listeners
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, open a property activity with the property ID
				Intent i= new Intent(getApplicationContext(), SelectedProperty.class);
				i.putExtra("name",data.elementAt(position).get("name"));
				startActivity(i);
			}
		});
	}
	
	private void populateList() {
		
		// Test data
		for (int i = 0; i < 15; i++) {
			HashMap<String,String> temp = new HashMap<String,String>();
			temp.put("name","NAMETEST" + i);
			temp.put("one", "seed:0  water:0  fertilizer:0");
			data.add(temp);
		}
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

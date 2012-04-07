package edu.osu.geogame;

import java.util.HashMap;
import java.util.Vector;

import android.app.ListActivity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
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
				Toast.makeText(getApplicationContext(),
						"ID selected: " + id, Toast.LENGTH_SHORT).show();
				
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

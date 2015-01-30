package us.bmark.android.prefs;

import us.bmark.android.BookmarkListsActivity;
import us.bmark.android.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity  implements OnSharedPreferenceChangeListener{

	 SharedPreferences getPrefs;
	 SharedPreferences settings;
	 Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.settings);
        
        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        settings = getSharedPreferences("mypref", MODE_PRIVATE);
        editor = settings.edit();
        //As soon as it comes into SettingsActivity \, checks for last color, updates it.
        String colorid = settings.getString("mycolor", "1");
        if(colorid.equals("1"))
        {
        	getListView().setBackgroundColor(Color.BLACK);
        }
        else if(colorid.equals("2"))
        {
        	getListView().setBackgroundColor(Color.GRAY);
        }
        else if(colorid.equals("3"))
        {
        	getListView().setBackgroundColor(Color.TRANSPARENT);
        }
               
    }
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
    
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub 
		String bg_set = getPrefs
                        .getString("theme_preference",settings.getString("mycolor", "1"));
		Intent intent = new Intent(SettingsActivity.this, BookmarkListsActivity.class);
		intent.putExtra("caller","calling");
			
		// Writing data to SharedPreferences
		editor.putString("mycolor",bg_set);
	    editor.commit();
	    //As soon as it is chosen, SettingsActivity background changes.
	    if(bg_set.equals("1"))
	    {
	    	getListView().setBackgroundColor(Color.BLACK);
	    }
	    else if(bg_set.equals("2"))
	    {
	    	getListView().setBackgroundColor(Color.GRAY);
	    }	
	    else if(bg_set.equals("3"))
	    {
	    	getListView().setBackgroundColor(Color.TRANSPARENT);
	    }
	}
}

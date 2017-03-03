package com.papaya.scotthanberg.papaya;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sheolfire on 3/1/2017.
 */

public class PreferenceHandler {
    private SharedPreferences sharedPref;

    public PreferenceHandler(Context context) {
      //  Context context = getActivity();
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    /**
     * Stores a key value pair in the SharedPreferences file
     * @param key the key to be associated with the value
     * @param value the value key will point to
     * @return whether or not the write succeeded (whether or not key was null)
     */
    public boolean writeToSharedPref(String key, String value) {
        if(key == null)
            return false;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    /**
     * Obtains the value associated with the key
     * @param key the key to get the value of
     * @return
     */
    public String readFromSharedPref(String key) {
        return sharedPref.getString(key, "Value does not exist.");
    }
}

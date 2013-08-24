package com.trio.triotictactoe.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserPreferences implements Preferences {

	private SharedPreferences pref;
	public UserPreferences(Context context) {
		pref = context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
	}

	@Override
	public void setPreference(String key, String value) {
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
		System.out.println(pref.getString(key, null));
	}

	@Override
	public String getPreference(String key, String value) {
		return pref.getString(key, value);
	}

	@Override
	public void removePreference(String key) {
		Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}

}

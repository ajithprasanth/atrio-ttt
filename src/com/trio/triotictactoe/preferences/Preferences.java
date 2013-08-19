package com.trio.triotictactoe.preferences;

public interface Preferences {

	void setPreference(String key, String value);
	String getPreference(String key, String value);
	
}

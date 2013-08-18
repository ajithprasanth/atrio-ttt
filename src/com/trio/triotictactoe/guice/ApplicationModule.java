package com.trio.triotictactoe.guice;

import android.util.Log;

import com.google.inject.AbstractModule;
import com.trio.triotictactoe.preferences.Preferences;
import com.trio.triotictactoe.preferences.UserPreferences;

public class ApplicationModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(Preferences.class).to(UserPreferences.class).asEagerSingleton();
		Log.e("init", "init");
	}

}

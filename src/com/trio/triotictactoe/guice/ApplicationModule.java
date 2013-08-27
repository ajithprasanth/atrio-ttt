package com.trio.triotictactoe.guice;

import android.content.Context;
import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.trio.triotictactoe.preferences.Preferences;
import com.trio.triotictactoe.preferences.UserPreferences;
import com.trio.triotictactoe.utils.SoundUtils;

public class ApplicationModule extends AbstractModule{

	private Context cnt;
	public ApplicationModule(Context context) {
	   this.cnt = context;
	}
	@Override
	protected void configure() {
		bind(Preferences.class).to(UserPreferences.class).asEagerSingleton();
		 bind(SoundUtils.class).asEagerSingleton();
		 bind(Context.class).toInstance(cnt);
		Log.e("init", "init");
	}
	static public void Inject(Context context){
		Module mo = new ApplicationModule(context.getApplicationContext());
		Guice.createInjector(mo).injectMembers(context);
	}

}

package com.trio.triotictactoe.utils;

import com.google.inject.Inject;
import com.trio.triotictactoe.R;
import com.trio.triotictactoe.preferences.Preferences;
import com.trio.triotictactoe.preferences.UserPreferences;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class SoundUtils {

	private int click;
	private SoundPool pool;
	private Vibrator vibrator;
	private Preferences preferences;
	
	@Inject
	public SoundUtils(Context context) {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		click = pool.load(context.getApplicationContext(), R.raw.click, 1);
		preferences = new UserPreferences(context);
	}

	public void click() {
		if(!Boolean.parseBoolean(preferences.getPreference(TTTConstants.NO_SOUND, "false")))
			pool.play(click, 1, 1, 1, 0, 1);
		if(!Boolean.parseBoolean(preferences.getPreference(TTTConstants.NO_VIBRATOR, "false")))
			vibrator.vibrate(100);
	}
	public void viberate(){
		if(!Boolean.parseBoolean(preferences.getPreference(TTTConstants.NO_VIBRATOR, "false")))
			vibrator.vibrate(100);
	}
	public void sound(){
		if(!Boolean.parseBoolean(preferences.getPreference(TTTConstants.NO_SOUND, "false")))
			pool.play(click, 1, 1, 1, 0, 1);
	}
}

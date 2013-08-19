package com.trio.triotictactoe.utils;

import com.trio.triotictactoe.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

public class SoundUtils {

	private int click;
	private SoundPool pool;

	public SoundUtils(Context context) {
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		click = pool.load(context.getApplicationContext(), R.raw.click, 1);
	}

	public void click() {
		pool.play(click, 1, 1, 1, 0, 1);
	}

}

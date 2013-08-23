package com.trio.triotictactoe.activity;


import com.trio.triotictactoe.R;
import com.trio.triotictactoe.preferences.UserPreferences;
import com.trio.triotictactoe.utils.TTTConstants;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	
	View vibratorView, soundView, gamesettingsView, firstmoveView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadViews();
		update();
	}
	
	private void loadViews(){
		vibratorView = findViewById(R.id.vibrator);
		soundView = findViewById(R.id.sound);
		gamesettingsView = findViewById(R.id.gamesettings);
		firstmoveView = findViewById(R.id.firstmove);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		super.onWindowFocusChanged(hasFocus);
		
		if(hasFocus){
			
		}else{
			save();
		}
	}
	
	@Override
	public void finish() {
		save();
		super.finish();
	}
	
	private void update(){
		boolean gamesettings = Boolean.parseBoolean(new UserPreferences(this).getPreference(TTTConstants.NO_SHOW_TAKE_NAME, String.valueOf(false)));
		boolean sound = Boolean.parseBoolean(new UserPreferences(this).getPreference(TTTConstants.NO_SOUND, String.valueOf(false)));
		boolean vibrator = Boolean.parseBoolean(new UserPreferences(this).getPreference(TTTConstants.NO_VIBRATOR, String.valueOf(false)));
		int firstmove =Integer.parseInt(new UserPreferences(this).getPreference(TTTConstants.FIRST_MOVE, "0"));
		((CheckBox)vibratorView).setChecked(vibrator);
		((CheckBox)soundView).setChecked(sound);
		((CheckBox)gamesettingsView).setChecked(gamesettings);
		((Spinner)firstmoveView).setSelection(firstmove);

		new UserPreferences(this).setPreference(TTTConstants.NO_SOUND, String.valueOf(sound));
		new UserPreferences(this).setPreference(TTTConstants.NO_VIBRATOR, String.valueOf(vibrator));
		new UserPreferences(this).setPreference(TTTConstants.FIRST_MOVE, String.valueOf(firstmove));

	}
	private void save(){
		boolean vibrator = ((CheckBox)vibratorView).isChecked();
		boolean sound = ((CheckBox)soundView).isChecked();
		boolean gamesettings = ((CheckBox)gamesettingsView).isChecked();
		int firstmove = ((Spinner)firstmoveView).getSelectedItemPosition();
		new UserPreferences(this).setPreference(TTTConstants.NO_SHOW_TAKE_NAME, String.valueOf(gamesettings));
		new UserPreferences(this).setPreference(TTTConstants.NO_SOUND, String.valueOf(sound));
		new UserPreferences(this).setPreference(TTTConstants.NO_VIBRATOR, String.valueOf(vibrator));
		new UserPreferences(this).setPreference(TTTConstants.FIRST_MOVE, String.valueOf(firstmove));
		
	}
	
	
	
}

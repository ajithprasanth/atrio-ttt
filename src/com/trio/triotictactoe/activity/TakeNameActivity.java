package com.trio.triotictactoe.activity;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.preferences.Preferences;
import com.trio.triotictactoe.preferences.UserPreferences;
import com.trio.triotictactoe.utils.SoundUtils;
import com.trio.triotictactoe.utils.TTTConstants;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class TakeNameActivity extends Activity {

	
	int level, gameMode;
	String player1Name, player2Name;
	boolean showMe;
	View playView, clevelView, loginusingfbView, orloginusingfbView, beforeplayer2nameView, cplayer2nameView, levelView, player1NameView, player2NameView, showMeView;
	Preferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takename);
		preferences = new UserPreferences(this);
		getPreferences();
		if(!showMe){
			startMainActivity();
		}
		loadViews();
		updateViews();
	}
	
	private void startMainActivity(){
		Intent mainActivityIntent = new Intent(this, GameActivity.class);
		mainActivityIntent.putExtra(TTTConstants.GAME_MODE_KEY, gameMode);
		mainActivityIntent.putExtra(TTTConstants.PLAYER1_NAME_KEY, player1Name);
		mainActivityIntent.putExtra(TTTConstants.PLAYER2_NAME_KEY, player2Name);
		mainActivityIntent.putExtra(TTTConstants.GAME_LEVEL_KEY, level);
		startActivity(mainActivityIntent);
		finish();
	}
	private void getPreferences(){
		gameMode =  getIntent().getIntExtra(TTTConstants.GAME_MODE_KEY, TTTConstants.GAME_MODE_SINGLE_PLAYER);
		player1Name = preferences.getPreference(TTTConstants.PLAYER1_NAME_KEY, null	);
		if(level == TTTConstants.GAME_MODE_MULTI_PLAYER_LOCAL){
			player2Name = preferences.getPreference(TTTConstants.PLAYER2_NAME_KEY,null	);
		}
		level = Integer.parseInt(preferences.getPreference(TTTConstants.GAME_LEVEL_KEY, "0"));
		showMe = Boolean.parseBoolean(preferences.getPreference(gameMode + TTTConstants.NO_SHOW_TAKE_NAME, "true"));
		
	}
	
	private void loadViews(){
		levelView = findViewById(R.id.level);
		player1NameView = findViewById(R.id.player1name);
		
		player2NameView = findViewById(R.id.player2name);
		showMeView = findViewById(R.id.showMe);
		beforeplayer2nameView = findViewById(R.id.beforeplayer2name);
		loginusingfbView = findViewById(R.id.loginusingfb);
		orloginusingfbView = findViewById(R.id.orloginusingfb);
		cplayer2nameView = findViewById(R.id.cplayer2name);
		clevelView = findViewById(R.id.clevel);
		playView = findViewById(R.id.play	);
	}
	
	private void updateViews(){
		((EditText)player1NameView).addTextChangedListener(getTextWatcher());
		
		((EditText)player2NameView).addTextChangedListener(getTextWatcher());
		updatePlayButton();
		((Spinner)levelView).setSelection(level);
		if(player1Name != null){
			((EditText)player1NameView).setText(player1Name);
		}
		if(player2Name != null){
			((EditText)player2NameView).setText(player2Name);
		}
		if(gameMode == 0){
			beforeplayer2nameView.setVisibility(View.GONE);
			cplayer2nameView.setVisibility(View.GONE);
		}
		if(gameMode == 1){
			clevelView.setVisibility(View.GONE);
			loginusingfbView.setVisibility(View.GONE);
			orloginusingfbView.setVisibility(View.GONE);
		}
	}
	
	public void back(View v){
		finish();
	}
	
	public void play(View v){
		updatePreferences();
		new SoundUtils(this.getApplicationContext()).click();
		startMainActivity();
		
	}

	private TextWatcher getTextWatcher(){
		return new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				updatePlayButton();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
	}
	
	private void updatePlayButton(){
		if(((EditText)player1NameView).getText().toString() == null || "".equals(((EditText)player1NameView).getText().toString())){
			playView.setEnabled(false);
		}else if(gameMode == TTTConstants.GAME_MODE_MULTI_PLAYER_LOCAL && (((EditText)player2NameView).getText().toString() == null || "".equals(((EditText)player2NameView).getText().toString()))){
			playView.setEnabled(false);
		}else {
			playView.setEnabled(true);
		}

	}
	private void updatePreferences() {
		
		player1Name = ((EditText)player1NameView).getText().toString();
		player2Name = ((EditText)player2NameView).getText().toString();
		level = ((Spinner) levelView).getSelectedItemPosition();
		showMe = !((CheckBox)showMeView).isChecked();
		preferences.setPreference(TTTConstants.PLAYER1_NAME_KEY, player1Name);
		if(gameMode == TTTConstants.GAME_MODE_MULTI_PLAYER_LOCAL)
		preferences.setPreference(TTTConstants.PLAYER2_NAME_KEY, player2Name);
		preferences.setPreference(TTTConstants.GAME_LEVEL_KEY, String.valueOf(level));
		preferences.setPreference(gameMode + TTTConstants.NO_SHOW_TAKE_NAME, String.valueOf(showMe));
		
	}
	
}

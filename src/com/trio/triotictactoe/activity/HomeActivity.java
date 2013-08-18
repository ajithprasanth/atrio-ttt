package com.trio.triotictactoe.activity;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.spi.Dependency;
import com.trio.triotictactoe.R;
import com.trio.triotictactoe.guice.ApplicationModule;
import com.trio.triotictactoe.preferences.Preferences;
import com.trio.triotictactoe.preferences.UserPreferences;
import com.trio.triotictactoe.utils.TTTConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity {

	//View playerVsPlayer, exit, settings, 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			((AnimationDrawable)findViewById(R.id.textview).getBackground()).start();
		}else{
			((AnimationDrawable)findViewById(R.id.textview).getBackground()).stop();
		}
	}
	
	public void finish(View v){
		this.finish();
	}
	
	public void playerVsPlayer(View v){
		Intent nameAcivityIntent = new Intent(this, TakeNameActivity.class);
		nameAcivityIntent.putExtra(TTTConstants.GAME_MODE_KEY, TTTConstants.GAME_MODE_MULTI_PLAYER_LOCAL);
		startActivity(nameAcivityIntent);
	}

	public void playerVsComputer(View v){
		Intent nameAcivityIntent = new Intent(this, TakeNameActivity.class);
		nameAcivityIntent.putExtra(TTTConstants.GAME_MODE_KEY, TTTConstants.GAME_MODE_SINGLE_PLAYER);
		startActivity(nameAcivityIntent);
	}
	
}

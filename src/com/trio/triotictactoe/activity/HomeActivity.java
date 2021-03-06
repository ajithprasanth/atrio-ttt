package com.trio.triotictactoe.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.trio.triotictactoe.R;
import com.trio.triotictactoe.guice.ApplicationModule;
import com.trio.triotictactoe.utils.SoundUtils;
import com.trio.triotictactoe.utils.TTTConstants;

public class HomeActivity extends Activity {

	//View playerVsPlayer, exit, settings, 
	
	MediaPlayer player;
	
	@Inject
	private SoundUtils soundUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ApplicationModule.Inject(this);
		player = MediaPlayer.create(this.getApplicationContext(), R.raw.home);
		//player = MediaPlayer.create(this.getApplicationContext(), R.raw.home);
		findViewById(R.id.playervscomputer).setOnClickListener(layoutClickListener);
		findViewById(R.id.playervsplayer).setOnClickListener(layoutClickListener);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			player.start();
//			((AnimationDrawable)findViewById(R.id.textview).getBackground()).start();
		}else{
			player.pause();
//			((AnimationDrawable)findViewById(R.id.textview).getBackground()).stop();
		}
	}
	
	public void finish(View v){
		soundUtils.click();
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
	public void settingsActivity(View v){
		Intent settingsAcivityIntent = new Intent(this, SettingsActivity.class);
		startActivity(settingsAcivityIntent);
	}
	
	
	
	
	/**
	 * Click listener for individual TTT (Linear Layout)
	 */
	private View.OnClickListener layoutClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View clickedLinearLayout) {
			if(clickedLinearLayout.getId() == R.id.playervsplayer){
				soundUtils.click();
				playerVsPlayer(clickedLinearLayout);
				
			}else if (clickedLinearLayout.getId() == R.id.playervscomputer){
				soundUtils.click();
				playerVsComputer(clickedLinearLayout);
			}else if(clickedLinearLayout.getId() == R.id.settings){
				soundUtils.click();
				settingsActivity(clickedLinearLayout);
			}
				
			
		}
	};
	
}

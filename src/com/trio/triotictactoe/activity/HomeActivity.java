package com.trio.triotictactoe.activity;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.utils.TTTConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

	//View playerVsPlayer, exit, settings, 
	
	MediaPlayer player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		player = MediaPlayer.create(this.getApplicationContext(), R.raw.home);
		//player.prepareAsync();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			player.start();
			((AnimationDrawable)findViewById(R.id.textview).getBackground()).start();
		}else{
			player.pause();
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

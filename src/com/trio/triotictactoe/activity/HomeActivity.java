package com.trio.triotictactoe.activity;

import com.trio.triotictactoe.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			((AnimationDrawable)findViewById(R.id.textview).getBackground()).start();
		}else{
			((AnimationDrawable)findViewById(R.id.textview).getBackground()).stop();
		}
	}
}

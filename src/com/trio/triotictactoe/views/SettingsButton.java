package com.trio.triotictactoe.views;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.trio.triotictactoe.activity.SettingsActivity;
import com.trio.triotictactoe.guice.ApplicationModule;
import com.trio.triotictactoe.utils.SoundUtils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class SettingsButton extends ImageButton{

	@Inject
	private SoundUtils soundUtils;
	public SettingsButton(Context context) {
		super(context);
		setClickListener();
		Guice.createInjector(new ApplicationModule(context.getApplicationContext())).injectMembers(this);
	}
	public SettingsButton(Context context,AttributeSet attr){
		super(context,attr);
		setClickListener();
		Guice.createInjector(new ApplicationModule(context.getApplicationContext())).injectMembers(this);
		
	}
	
	private void setClickListener(){
			setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundUtils.click();
				Intent settingsAcivityIntent = new Intent(getContext(), SettingsActivity.class);
				getContext().startActivity(settingsAcivityIntent);
			}
		});
	}
	
	
	
	
	

}

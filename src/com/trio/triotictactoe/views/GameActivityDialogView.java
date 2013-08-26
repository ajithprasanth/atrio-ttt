package com.trio.triotictactoe.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.trio.triotictactoe.activity.GameActivity;

public class GameActivityDialogView extends Dialog {

	protected GameActivity gameActivity;

	public GameActivityDialogView(GameActivity activity) {
		super(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

		this.gameActivity = activity;

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.getWindow().getAttributes().dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		this.setCancelable(true);
	}

}

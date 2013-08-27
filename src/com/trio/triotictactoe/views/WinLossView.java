package com.trio.triotictactoe.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.activity.GameActivity;

public class WinLossView extends GameActivityDialogView {
	private boolean hasUserWon;

	public WinLossView(GameActivity activity, boolean hasUserWon) {
		super(activity);
		this.hasUserWon = hasUserWon;
		this.setCancelable(false);
		this.setContentView(R.layout.win_loss_view);
		initLayout();
	}

	private void initLayout() {
		if (!hasUserWon) {
			((ImageView) findViewById(R.id.win_image)).setImageDrawable(gameActivity.getResources().getDrawable(R.drawable.lost));
			findViewById(R.id.win_loss_fb_share_button).setVisibility(View.GONE);

		} else {
			// FB Share button
			findViewById(R.id.win_loss_fb_share_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundleData = new Bundle();
					bundleData.putString("name", "Trio Tic Tac Toe");
					bundleData.putString("caption", "Beat my score!");
					// TODO: Change with the original score
					bundleData.putString("description", "I have scored 1000 points. Beat me if you can!");
					// TODO: Use play store link here
					bundleData.putString("link", "https://www.facebook.com/pages/Atrio-Labs/373540002774689");
					bundleData.putString("picture", "http://ecx.images-amazon.com/images/I/61roGmJcZwL._SX150_.jpg");

					gameActivity.publishNewsFeed(bundleData, null);
				}
			});

		}

		// Retry game
		findViewById(R.id.win_loss_retry).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WinLossView.this.cancel();
				gameActivity.restartGame();
			}
		});

		// Exit game
		findViewById(R.id.win_loss_exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WinLossView.this.cancel();
				gameActivity.exit();
			}
		});
	}

}

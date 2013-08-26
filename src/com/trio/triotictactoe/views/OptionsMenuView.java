package com.trio.triotictactoe.views;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.activity.GameActivity;

public class OptionsMenuView extends GameActivityDialogView {

	private int[] menuItemImages = {R.drawable.help, R.drawable.settings, R.drawable.exit, R.drawable.exit};
	private int[] menuItemText = {R.string.restart, R.string.settings, R.string.exit_game, R.string.save_and_exit};

	public OptionsMenuView(GameActivity activity) {
		super(activity);
		this.setContentView(R.layout.options_menu);
		addItemsToLayout();
	}

	private void addItemsToLayout() {
		LayoutInflater inflater = (LayoutInflater) gameActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resources resources = gameActivity.getResources();

		LinearLayout rootLayout = (LinearLayout) findViewById(R.id.optoins_menu_root);

		for(int i=0; i< menuItemText.length; i++) {
			View menuItem = inflater.inflate(R.layout.options_menu_item, null);
			ImageView imageView = ((ImageView) menuItem.findViewById(R.id.menu_item_image));
			TextView textView = ((TextView) menuItem.findViewById(R.id.menu_item_text));

			// Set text and image
			imageView.setImageDrawable(resources.getDrawable(menuItemImages[i]));
			textView.setText(resources.getString(menuItemText[i]));

			// Set tags to follow up in click listener
			menuItem.setTag(menuItemText[i]);
			menuItem.setOnClickListener(menuItemClickListener);

			rootLayout.addView(menuItem);
		}
	}

	private final View.OnClickListener menuItemClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			int tagId = (Integer) view.getTag();
			switch (tagId) {
				case R.string.restart :
					gameActivity.restartGame();
					break;

				case R.string.settings :
					gameActivity.goToSettings();
					break;

				case R.string.exit_game :
					gameActivity.exit();
					break;

				case R.string.save_and_exit :
					gameActivity.saveAndExit();
					break;
			}

			OptionsMenuView.this.cancel();
		}
	};
}

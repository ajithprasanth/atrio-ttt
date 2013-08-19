package com.trio.triotictactoe.views;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.model.MiniTTTData;
import com.trio.triotictactoe.utils.CellState;

public class ZoomedView extends Dialog {
	private MiniTTTData data;

	private Activity activity;

	public ZoomedView(Activity activity, MiniTTTData data) {
		super(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.data = data;
		this.activity = activity;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount = .7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
		this.setCancelable(true);
		this.setContentView(R.layout.mega_ttt);

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "mega_b_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr,
						"id", activity.getPackageName());
				View view = findViewById(viewId);
				if (view != null) {
					view.setOnClickListener(layoutClickListener);
				}
			}
		}

		data.setHasUserSelectedData(false);

		draw();

		this.findViewById(R.id.mega_ttt_root).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						System.out.println("CLICKING !!!!");
						ZoomedView.this.cancel();
					}
				});
	}

	private void draw() {
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				drawAtIndex(i, j);
			}
		}
	}

	private void drawAtIndex(int i, int j) {
		String viewIdStr = "mega_b_" + i + "_" + j;
		int viewId = activity.getResources().getIdentifier(viewIdStr, "id",
				activity.getPackageName());
		Button cell = (Button) findViewById(viewId);
		cell.setTag(R.dimen.row_id, i);
		cell.setTag(R.dimen.col_id, j);
		CellState state = data.getCell(i, j);
		Drawable move;
		if (cell != null && state != null) {
			cell.setTag(R.dimen.cell_state, state);
			switch (state) {
			case EMPTY:
				break;

			case CROSS:
				move = activity.getResources().getDrawable(R.drawable.x_move);
				cell.setBackgroundDrawable(move);
				break;

			case NOUGHTS:
				move = activity.getResources().getDrawable(R.drawable.o_move);
				cell.setBackgroundDrawable(move);
				break;
			}
		}
	}

	private View.OnClickListener layoutClickListener = new View.OnClickListener() {

		@Override
		public void onClick(final View clickedLinearLayout) {
			int row = ((Integer) clickedLinearLayout.getTag(R.dimen.row_id));
			int col = ((Integer) clickedLinearLayout.getTag(R.dimen.col_id));
			data.populateData(row, col, CellState.CROSS);
			data.setUserClickedRowIndex(row);
			data.setUserClickedColumnIndex(col);
			drawAtIndex(row, col);
			data.setHasUserSelectedData(true);
			ZoomedView.this.cancel();
		}
	};

}

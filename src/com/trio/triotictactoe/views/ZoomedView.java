package com.trio.triotictactoe.views;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.activity.GameActivity;
import com.trio.triotictactoe.model.MiniTTTData;
import com.trio.triotictactoe.utils.CellState;
import com.trio.triotictactoe.utils.ViewUtils;

public class ZoomedView extends GameActivityDialogView {
	private MiniTTTData data;

	public ZoomedView(GameActivity activity, MiniTTTData data) {
		super(activity);
		this.data = data;
		data.setHasUserSelectedData(false);

		this.setContentView(R.layout.mega_ttt);
		draw();
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
		int viewId = gameActivity.getResources().getIdentifier(viewIdStr, "id", gameActivity.getPackageName());
		Button cell = (Button) findViewById(viewId);
		cell.setTag(R.dimen.row_id, i);
		cell.setTag(R.dimen.col_id, j);
		CellState state = data.getCell(i, j);
		Drawable move;
		if (cell != null && state != null) {
			cell.setTag(R.dimen.cell_state, state);
			switch (state) {
				case EMPTY :
					cell.setOnClickListener(layoutClickListener);
					break;

				case CROSS :
				case CROSS_LAST_SELECTED :
					move = gameActivity.getResources().getDrawable(R.drawable.x_move);
					move.setAlpha(ViewUtils.DISABLED_ALPHA);
					cell.setBackgroundDrawable(move);
					break;

				case NOUGHTS :
				case NOUGHTS_LAST_SELECTED :
					move = gameActivity.getResources().getDrawable(R.drawable.o_move);
					move.setAlpha(ViewUtils.DISABLED_ALPHA);
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

package com.trio.triotictactoe.activity;

import rules.Result;
import tictactoe.TTT;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.model.MiniTTTData;
import com.trio.triotictactoe.utils.CellState;
import com.trio.triotictactoe.views.ZoomedView;

public class MainActivity extends Activity {
	private MiniTTTData oneTTTdata = new MiniTTTData();
	private TTT nextStep = new TTT();
	private Result lastResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = getResources().getIdentifier(viewIdStr, "id",
						getPackageName());
				View view = findViewById(viewId);

				if (view != null) {
					view.setTag(R.dimen.row_id, i);
					view.setTag(R.dimen.col_id, j);
					view.setOnClickListener(layoutClickListener);
				}
			}
		}
	}

	private View.OnClickListener layoutClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View clickedLinearLayout) {
			fillCurrentLinearLayoutDataToOneTTT(clickedLinearLayout);
			ZoomedView megaTTT = new ZoomedView(MainActivity.this, oneTTTdata);
			megaTTT.show();
			megaTTT.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface canceledDialog) {
					handleUserClick(clickedLinearLayout);
				}
			});
		}
	};

	private void fillCurrentLinearLayoutDataToOneTTT(
			final View clickedLinearLayout) {
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "b_" + i + "_" + j;
				int viewId = getResources().getIdentifier(viewIdStr, "id",
						getPackageName());
				Button cell = (Button) clickedLinearLayout.findViewById(viewId);

				if (cell != null) {
					CharSequence text = cell.getText();
					if (text == null || "".equals(text)) {
						oneTTTdata.populateData(i, j, CellState.EMPTY);
					} else if (text.equals("X")) {
						oneTTTdata.populateData(i, j, CellState.CROSS);
					} else if (text.equals("O")) {
						oneTTTdata.populateData(i, j, CellState.NOUGHTS);
					}
				}
			}
		}
	}

	private void handleUserClick(final View clickedLinearLayout) {
		if (oneTTTdata.hasUserSelectedData()) {
			repaintWithData(clickedLinearLayout);
			int row = ((Integer) clickedLinearLayout.getTag(R.dimen.row_id))
					* 3 + oneTTTdata.getUserClickedRowIndex();
			int col = ((Integer) clickedLinearLayout.getTag(R.dimen.col_id))
					* 3 + oneTTTdata.getUserClickedColumnIndex();
			getNextSystemStep(row, col);
			applySystemResult();
		}
	}

	private void getNextSystemStep(int row, int col) {
		System.out.println("row: " + row + ", col: " + col);
		lastResult = nextStep.nextMove(row, col);
	}

	private void repaintWithData(View viewToRepaint) {
		String viewIdStr = "b_" + oneTTTdata.getUserClickedRowIndex() + "_"
				+ oneTTTdata.getUserClickedColumnIndex();
		int viewId = getResources().getIdentifier(viewIdStr, "id",
				getPackageName());
		Button cell = (Button) viewToRepaint.findViewById(viewId);

		System.out.println("Cell: " + cell);

		if (cell != null) {
			cell.setText("X");
		}
	}

	private void applySystemResult() {
		if (lastResult.win) {
			Toast.makeText(this, "You Won !!!", Toast.LENGTH_LONG).show();
		} else if (lastResult.loose) {
			Toast.makeText(this, "You Lose !!!", Toast.LENGTH_LONG).show();
		} else {
			System.out.println("lastResult.i: " + lastResult.i
					+ ", lastResult.j: " + lastResult.j);
			int llRow = lastResult.i / 3;
			int llCol = lastResult.j / 3;

			System.out.println("~~~old llRow: " + llRow + ", llCol: " + llCol);

			String viewIdStr = "ttt_" + llRow + "_" + llCol;
			int layoutViewId = getResources().getIdentifier(viewIdStr, "id",
					getPackageName());
			View linearLayout = findViewById(layoutViewId);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					if (lastResult.state[i][j] != 0) {
						String tttViewIdStr = "ttt_" + i + "_" + j;
						int tttViewId = getResources().getIdentifier(
								tttViewIdStr, "id", getPackageName());
						View tttView = findViewById(tttViewId);
						tttView.setClickable(false);
					}
				}

			}
			llRow = lastResult.i % 3;
			llCol = lastResult.j % 3;

			System.out.println("~~~new llRow: " + llRow + ", llCol: " + llCol);
			viewIdStr = "b_" + llRow + "_" + llCol;
			Button cell = (Button) linearLayout.findViewById(getResources()
					.getIdentifier(viewIdStr, "id", getPackageName()));

			if (cell != null) {
				cell.setText("O");
			}

		}
	}
}

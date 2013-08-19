package com.trio.triotictactoe.activity;

import java.util.HashMap;
import java.util.Map;

import rules.Result;
import tictactoe.TTT;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.model.MiniTTTData;
import com.trio.triotictactoe.utils.CellState;
import com.trio.triotictactoe.views.ZoomedView;

public class GameActivity extends Activity {
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer, MiniTTTData> miniTTTDataMap = new HashMap<Integer, MiniTTTData>();
	private TTT nextStep = new TTT();
	private Result lastResult;
	private View gameView;

	long startTime = System.currentTimeMillis();
	
	Handler timerHandler ;
	TextView userTimer;
	Runnable timer;
	boolean timerStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameView = findViewById(R.id.main_game_layout);
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = getResources().getIdentifier(viewIdStr, "id",
						getPackageName());
				View view = gameView.findViewById(viewId);
				miniTTTDataMap.put(viewId, new MiniTTTData());
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
			if(!timerStarted){
				startTime = System.currentTimeMillis();
				initiateUserTimer();
				timerStarted = true;
			}
			fillCurrentLinearLayoutDataToOneTTT(clickedLinearLayout);
			ZoomedView megaTTT = new ZoomedView(GameActivity.this, miniTTTDataMap.get(clickedLinearLayout.getId()));
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
		MiniTTTData oneTTTdata = miniTTTDataMap.get(clickedLinearLayout.getId());
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "b_" + i + "_" + j;
				int viewId = getResources().getIdentifier(viewIdStr, "id",
						getPackageName());
				Button cell = (Button) clickedLinearLayout.findViewById(viewId);

				if (cell != null) {
					CellState state = (CellState) cell
							.getTag(R.dimen.cell_state);
					if (state != null)
						switch (state) {
						case EMPTY:
							oneTTTdata.populateData(i, j, CellState.EMPTY);
							break;

						case CROSS:
							oneTTTdata.populateData(i, j, CellState.CROSS);
							break;

						case NOUGHTS:
							oneTTTdata.populateData(i, j, CellState.NOUGHTS);
							break;
						}
				}
			}
		}
	}

	private void handleUserClick(final View clickedLinearLayout) {
		MiniTTTData oneTTTdata = miniTTTDataMap.get(clickedLinearLayout.getId());
		if (oneTTTdata.hasUserSelectedData()) {
			repaintWithData(clickedLinearLayout);
			int row = ((Integer) clickedLinearLayout.getTag(R.dimen.row_id))
					* 3 + oneTTTdata.getUserClickedRowIndex();
			int col = ((Integer) clickedLinearLayout.getTag(R.dimen.col_id))
					* 3 + oneTTTdata.getUserClickedColumnIndex();
			getNextSystemStep(row, col);
			applySystemResult();
			setNextTurnComp();
			setNextTurnYou();
		}
	}

	private void getNextSystemStep(int row, int col) {
		System.out.println("row: " + row + ", col: " + col);
		lastResult = nextStep.nextMove(row, col);
	}

	private void repaintWithData(View viewToRepaint) {
		MiniTTTData oneTTTdata = miniTTTDataMap.get(viewToRepaint.getId());
		String viewIdStr = "b_" + oneTTTdata.getUserClickedRowIndex() + "_"
				+ oneTTTdata.getUserClickedColumnIndex();
		int viewId = getResources().getIdentifier(viewIdStr, "id",
				getPackageName());
		Button cell = (Button) viewToRepaint.findViewById(viewId);

		System.out.println("Cell: " + cell);

		if (cell != null) {
			Drawable xMove = getResources().getDrawable(R.drawable.x_move);
			cell.setBackgroundDrawable(xMove);
			cell.setTag(R.dimen.cell_state,CellState.CROSS);
		}
	}

	private void applySystemResult() {
		setNextTurnComp();
		System.out.println("lastResult.i: " + lastResult.i + ", lastResult.j: "
				+ lastResult.j);
		int llRow = lastResult.i / 3;
		int llCol = lastResult.j / 3;

		String viewIdStr = "ttt_" + llRow + "_" + llCol;
		int layoutViewId = getResources().getIdentifier(viewIdStr, "id",
				getPackageName());
		View linearLayout = gameView.findViewById(layoutViewId);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (lastResult.state[i][j] != 0) {
					String tttViewIdStr = "ttt_" + i + "_" + j;
					int tttViewId = getResources().getIdentifier(tttViewIdStr,
							"id", getPackageName());
					View tttView = findViewById(tttViewId);
					tttView.setClickable(false);
				}
			}

		}
		llRow = lastResult.i % 3;
		llCol = lastResult.j % 3;
		viewIdStr = "b_" + llRow + "_" + llCol;
		Button cell = (Button) linearLayout.findViewById(getResources()
				.getIdentifier(viewIdStr, "id", getPackageName()));

		if (cell != null) {
			Drawable oMove = getResources().getDrawable(R.drawable.o_move);
			cell.setBackgroundDrawable(oMove);
			cell.setTag(R.dimen.cell_state, CellState.NOUGHTS);
		}
		if (lastResult.win) {
			Toast.makeText(this, "You Won !!!", Toast.LENGTH_LONG).show();
		} else if (lastResult.loose) {
			Toast.makeText(this, "You Lose !!!", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Set next turn text to you
	 */
	private void setNextTurnYou() {
		// Set next turn text;
		TextView nextTurn = (TextView) findViewById(R.id.turn_view);
		nextTurn.setText(R.string.next_turn_you);

	}

	/**
	 * Set next turn text to comp
	 */
	private void setNextTurnComp() {
		// Set next turn text;
		TextView nextTurn = (TextView) findViewById(R.id.turn_view);
		nextTurn.setText(R.string.next_turn_comp);

	}

	/**
	 * Initiates the user timer
	 */
	private void initiateUserTimer() {
		timerHandler = new Handler();
		userTimer = (TextView) findViewById(R.id.timer_view);
		timer = new Runnable() {
			@Override
			public void run() {
				long millis = System.currentTimeMillis() - startTime;
				int seconds = (int) (millis / 1000);
				userTimer.setText(String.format("Time : %d secs", seconds));
				timerHandler.postDelayed(this, 500);
			}
		};
		timerHandler.postDelayed(timer, 0);
	}

}

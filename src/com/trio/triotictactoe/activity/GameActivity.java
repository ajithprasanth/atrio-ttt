package com.trio.triotictactoe.activity;

import rules.Result;
import tictactoe.TTT;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.model.GameDataManager;
import com.trio.triotictactoe.model.MiniTTTData;
import com.trio.triotictactoe.utils.CellState;
import com.trio.triotictactoe.views.ZoomedView;

public class GameActivity extends Activity {

	private SparseArray<MiniTTTData> miniTTTDataMap = new SparseArray<MiniTTTData>();
	private TTT nextStep = new TTT();
	private Result lastResult;
	private View gameView;
	private GameDataManager gameDataManager;
	private boolean hasUserMadeAtleastOneMove;

	private long startTime = System.currentTimeMillis();

	private Handler timerHandler;
	private TextView userTimer;
	private Runnable timer;
	boolean timerStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameView = findViewById(R.id.main_game_layout);
		gameDataManager = new GameDataManager(this, layoutClickListener);
		init();
	}

	public void init() {
		if (gameDataManager.isPreviousGameDataFound()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.continue_saved_game));
			builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					gameDataManager.restorePreviousGame();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					gameDataManager.createEmptyGameCells();
				}
			});

			builder.create().show();

		} else {
			gameDataManager.createEmptyGameCells();
		}
	}

	/**
	 * Click listener for individual TTT (Linear Layout)
	 */
	private View.OnClickListener layoutClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View clickedLinearLayout) {
			if(!timerStarted){
				startTime = System.currentTimeMillis();
				initiateUserTimer();
				timerStarted = true;
			}

			// Populate data for this mini TTT before calling zoom view
			gameDataManager.fillClickedLinearLayoutToOneTTTData(clickedLinearLayout);

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

	/**
	 * React to the user click in zoom view
	 * @param clickedLinearLayout
	 */
	private void handleUserClick(final View clickedLinearLayout) {
		MiniTTTData oneTTTdata = miniTTTDataMap.get(clickedLinearLayout.getId());
		if (oneTTTdata.hasUserSelectedData()) {
			hasUserMadeAtleastOneMove = true;

			applyUserSelection(clickedLinearLayout);

			setNextTurnComp();

			int row = ((Integer) clickedLinearLayout.getTag(R.dimen.row_id)) * 3 + oneTTTdata.getUserClickedRowIndex();
			int col = ((Integer) clickedLinearLayout.getTag(R.dimen.col_id)) * 3 + oneTTTdata.getUserClickedColumnIndex();
			getNextSystemStep(row, col);

			applySystemSelection();

			setNextTurnYou();
		}
	}

	/**
	 * Based on zoom view click position, populate the corresponding cell in the full view 
	 * @param viewToRepaint
	 */
	private void applyUserSelection(View viewToRepaint) {
		MiniTTTData oneTTTdata = miniTTTDataMap.get(viewToRepaint.getId());
		String viewIdStr = "b_" + oneTTTdata.getUserClickedRowIndex() + "_" + oneTTTdata.getUserClickedColumnIndex();
		int viewId = getResources().getIdentifier(viewIdStr, "id", getPackageName());
		Button cell = (Button) viewToRepaint.findViewById(viewId);

		System.out.println("Cell: " + cell);

		if (cell != null) {
			Drawable xMove = getResources().getDrawable(R.drawable.x_move);
			cell.setBackgroundDrawable(xMove);
			cell.setTag(R.dimen.cell_state,CellState.CROSS);
		}
	}

	/**
	 * Make the system move
	 * @param row
	 * @param col
	 */
	private void getNextSystemStep(int row, int col) {
		System.out.println("row: " + row + ", col: " + col);
		lastResult = nextStep.nextMove(row, col);
	}

	/**
	 * Apply the system move result
	 */
	private void applySystemSelection() {
		System.out.println("lastResult.i: " + lastResult.i + ", lastResult.j: " + lastResult.j);
		int llRow = lastResult.i / 3;
		int llCol = lastResult.j / 3;

		String viewIdStr = "ttt_" + llRow + "_" + llCol;
		int layoutViewId = getResources().getIdentifier(viewIdStr, "id", getPackageName());
		View linearLayout = gameView.findViewById(layoutViewId);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (lastResult.state[i][j] != 0) {
					String tttViewIdStr = "ttt_" + i + "_" + j;
					int tttViewId = getResources().getIdentifier(tttViewIdStr, "id", getPackageName());
					View tttView = findViewById(tttViewId);
					tttView.setClickable(false);
				}
			}

		}
		llRow = lastResult.i % 3;
		llCol = lastResult.j % 3;
		viewIdStr = "b_" + llRow + "_" + llCol;
		Button cell = (Button) linearLayout.findViewById(getResources().getIdentifier(viewIdStr, "id", getPackageName()));

		if (cell != null) {
			Drawable oMove = getResources().getDrawable(R.drawable.o_move);
			cell.setBackgroundDrawable(oMove);
			cell.setTag(R.dimen.cell_state, CellState.NOUGHTS);
		}

		// Keep the miniTTTDataMap in sync with the system selection
		gameDataManager.fillClickedLinearLayoutToOneTTTData(linearLayout);

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

	/**
	 * Catch user back press to avoid unintentional back press & show warning
	 */
	@Override
	public void onBackPressed() {
		if (hasUserMadeAtleastOneMove) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.save_current_game));
			builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					gameDataManager.storeThisGame();
					GameActivity.this.finish();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					gameDataManager.clearSavedGame();
					GameActivity.this.finish();
				}
			});
			builder.setNeutralButton(getResources().getString(R.string.cancel), null);

			builder.create().show();
		} else {
			super.onBackPressed();
			this.finish();
		}
	}

	public SparseArray<MiniTTTData> getMiniTTTDataMap() {
		return miniTTTDataMap;
	}

	public void setMiniTTTDataMap(SparseArray<MiniTTTData> miniTTTDataMap) {
		this.miniTTTDataMap = miniTTTDataMap;
	}

	public long getStartTime() {
		return startTime;
	}

	public void adjustStartTime(long timeAlreadySpent) {
		initiateUserTimer();
		timerStarted = true;
		startTime = startTime - timeAlreadySpent;
	}

}

package com.trio.triotictactoe.model;

import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.activity.GameActivity;
import com.trio.triotictactoe.preferences.UserPreferences;
import com.trio.triotictactoe.utils.CellState;
import com.trio.triotictactoe.utils.TTTConstants;
import com.trio.triotictactoe.views.GameButton;

/**
 * Static methods to save & restore game data
 * @author ajith
 *
 */
public class GameDataManager {
	private GameActivity activity;
	private OnClickListener layoutListener;
	private UserPreferences prefs;

	public GameDataManager(GameActivity gameActivity, OnClickListener listener) {
		this.activity = gameActivity;
		this.layoutListener = listener;
		this.prefs = new UserPreferences(activity);
	}

	public boolean isPreviousGameDataFound() {
		return prefs.getPreference(TTTConstants.SAVED_GAME_DATA_KEY, null) != null;
	}

	/**
	 * Remove stored game data
	 */
	public void clearSavedGame() {
		prefs.removePreference(TTTConstants.SAVED_GAME_TIME_KEY);
		prefs.removePreference(TTTConstants.SAVED_GAME_DATA_KEY);
	}

	/**
	 * Serialize the last game data
	 */
	public void storeThisGame() {
		prefs.setPreference(TTTConstants.SAVED_GAME_TIME_KEY, Long.toString(System.currentTimeMillis() - activity.getStartTime()));
		prefs.setPreference(TTTConstants.SAVED_GAME_DATA_KEY, getSerializedGameData());
	}

	/**
	 * Populate data from a previous game
	 */
	public void restorePreviousGame() {
		/*
		 * TODO: Restore library also at this point
		 */
		SparseArray<MiniTTTData> newMiniTTTDataMap = getDeserializedGameData();

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr, "id", activity.getPackageName());

				View view = activity.findViewById(R.id.main_game_layout).findViewById(viewId);
				view.setTag(R.dimen.row_id, i);
				view.setTag(R.dimen.col_id, j);
				view.setOnClickListener(layoutListener);

				MiniTTTData oneTTTData = newMiniTTTDataMap.get(viewId);

				for (int x = 0; x <= 2; x++) {
					for (int y = 0; y <= 2; y++) {
						String cellIdStr = "b_" + x + "_" + y;
						int cellId = activity.getResources().getIdentifier(cellIdStr, "id", activity.getPackageName());
						GameButton cell = (GameButton) view.findViewById(cellId);
						cell.setCellState(oneTTTData.getCell(x, y));
					}
				}
			}
		}

		activity.setMiniTTTDataMap(newMiniTTTDataMap);
		activity.adjustStartTime(Long.valueOf(prefs.getPreference(TTTConstants.SAVED_GAME_TIME_KEY, "0")));
	}

	/**
	 * Create a brand new game
	 */
	public void createEmptyGameCells() {
		/*
		 * TODO: Restore library also to be empty at this point
		 */
		View gameLayout = activity.findViewById(R.id.main_game_layout);
		SparseArray<MiniTTTData> newMiniTTTDataMap = new SparseArray<MiniTTTData>();

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr, "id", activity.getPackageName());
				View view = gameLayout.findViewById(viewId);

				view.setTag(R.dimen.row_id, i);
				view.setTag(R.dimen.col_id, j);
				view.setOnClickListener(layoutListener);

				newMiniTTTDataMap.put(viewId, new MiniTTTData());
			}
		}

		activity.setMiniTTTDataMap(newMiniTTTDataMap);
	}

	/**
	 * Before calling zoom view, fill the required data in MiniTTTData for that layout.
	 * This is also called after the system updates with its reply to the user.
	 * 
	 * @param clickedLayout
	 */
	public void fillClickedLayoutToOneTTTData(final View clickedLayout) {
		MiniTTTData oneTTTdata = activity.getMiniTTTDataMap().get(clickedLayout.getId());
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "b_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr, "id", activity.getPackageName());
				GameButton cell = (GameButton) clickedLayout.findViewById(viewId);

				if (cell != null) {
					CellState state = (CellState) cell.getCellState();
					if (state != null)
						switch (state) {
							case EMPTY :
								oneTTTdata.populateData(i, j, CellState.EMPTY);
								break;

							case CROSS :
							case CROSS_LAST_SELECTED :
								oneTTTdata.populateData(i, j, CellState.CROSS);
								break;

							case NOUGHTS :
							case NOUGHTS_LAST_SELECTED :
								oneTTTdata.populateData(i, j, CellState.NOUGHTS);
								break;
						}
				}
			}
		}
	}

	/**
	 * Return serialized data
	 * @return
	 */
	private String getSerializedGameData() {
		View gameLayout = activity.findViewById(R.id.main_game_layout);
		StringBuffer serializedData = new StringBuffer();

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr, "id", activity.getPackageName());
				View view = gameLayout.findViewById(viewId);

				if (view != null) {
					serializedData.append(getOneTTTDataAsString(view));

					// Do not append only for the last TTT
					if (!(i == 2 && j == 2)) {
						serializedData.append(TTTConstants.SERIALIZATION_TTT_DATA_SEPARATOR);
					}
				}
			}
		}

		return serializedData.toString();
	}

	/**
	 * Return one TTT data as string of @CellState enum values separated by comma.
	 * Used during serialization
	 * @param view
	 * @return
	 */
	private StringBuffer getOneTTTDataAsString(View view) {
		StringBuffer oneTTTData = new StringBuffer();
		MiniTTTData data = activity.getMiniTTTDataMap().get(view.getId());
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				oneTTTData.append(data.getCell(i, j));

				// Do not append only for the last cell
				if (!(i == 2 && j == 2)) {
					oneTTTData.append(TTTConstants.SERIALIZATION_CELL_DATA_SEPARATOR);
				}
			}
		}
		return oneTTTData;
	}

	/**
	 * De-serialize the last game data
	 */
	private SparseArray<MiniTTTData> getDeserializedGameData() {
		String oldData = prefs.getPreference(TTTConstants.SAVED_GAME_DATA_KEY, TTTConstants.INITIAL_GAME_STATE);
		SparseArray<MiniTTTData> miniTTTDataMap = new SparseArray<MiniTTTData>();

		String[] individualTTTData = oldData.split(TTTConstants.SERIALIZATION_TTT_DATA_SEPARATOR);

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				String viewIdStr = "ttt_" + i + "_" + j;
				int viewId = activity.getResources().getIdentifier(viewIdStr, "id", activity.getPackageName());

				int index = i * 3 + j;
				miniTTTDataMap.put(viewId, getStringAsOneTTTData(individualTTTData[index]));
			}
		}

		return miniTTTDataMap;
	}

	/**
	 * Return @MiniTTTData corresponding to one mini TTT game.
	 * Used during de-serialization
	 * @param tttData
	 * @return
	 */
	private MiniTTTData getStringAsOneTTTData(String tttData) {
		CellState[][] stateData = new CellState[3][3];
		String[] individualCellData = tttData.split(TTTConstants.SERIALIZATION_CELL_DATA_SEPARATOR);

		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				int index = i * 3 + j;
				stateData[i][j] = CellState.valueOf(individualCellData[index]);
			}
		}
		MiniTTTData miniTTTData = new MiniTTTData();
		miniTTTData.setData(stateData);
		return miniTTTData;
	}

}

package com.trio.triotictactoe.utils;

public class TTTConstants {
	public static final int GAME_MODE_SINGLE_PLAYER = 0;
	public static final int GAME_MODE_MULTI_PLAYER_LOCAL = 1;
	public static final String GAME_MODE_KEY = "mode";
	public static final String NO_SHOW_TAKE_NAME = "TakeNameActivity.noShow";
	public static final String PLAYER2_NAME_KEY = "player2namekey";
	public static final String PLAYER1_NAME_KEY = "player1namekey";
	public static final String GAME_LEVEL_KEY = "gamelevelkey";
	public static final String NO_SOUND = "appnosound";
	public static final String NO_VIBRATOR = "appnovibrator";
	public static final String FIRST_MOVE = "gamefirstmove";

	// Save game constants
	public static final String IS_GAME_RESTARTED = "isGameRestartedKey";
	public static final String SAVED_GAME_DATA_KEY = "savedGameDataKey";
	public static final String SAVED_GAME_TIME_KEY = "savedGameTimeKey";
	public static final String SERIALIZATION_CELL_DATA_SEPARATOR = ",";
	public static final String SERIALIZATION_TTT_DATA_SEPARATOR = ";";
	public static final String INITIAL_GAME_STATE = 
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 1
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 2
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 3
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 4
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 5
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 6
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 7
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY;" + // 8
			"EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY";   // 9
}

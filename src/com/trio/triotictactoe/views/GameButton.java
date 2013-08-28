package com.trio.triotictactoe.views;

import com.trio.triotictactoe.R;
import com.trio.triotictactoe.utils.CellState;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Button;

public class GameButton extends Button {

	private CellState cellState;

	public GameButton(Context context) {
		super(context);
	}

	public GameButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GameButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CellState getCellState() {
		return cellState;
	}

	public void setCellState(CellState gameState) {
		this.cellState = gameState;

		Resources res = getResources();

		switch (gameState) {
			case EMPTY :
				setBackgroundDrawable(res.getDrawable(R.drawable.cell_1));
				break;

			case NOUGHTS :
				setBackgroundDrawable(res.getDrawable(R.drawable.o_move));
				break;

			case NOUGHTS_LAST_SELECTED :
				setBackgroundDrawable(res.getDrawable(R.drawable.o_move_last_selected));
				break;

			case CROSS :
				setBackgroundDrawable(res.getDrawable(R.drawable.x_move));
				break;

			case CROSS_LAST_SELECTED :
				setBackgroundDrawable(res.getDrawable(R.drawable.x_move_last_selected));
				break;
		}
	}

}

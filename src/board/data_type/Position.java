package board.data_type;

import board.PositionParser;

public class Position {
	
	/*
	 * 00-63	board positions
	 * 64		en passant (row or -1)
	 * 65-68	castle (0 or 1)
	 * 69		color to move (0 or 1)
	 */
	
	private int[] board;
	
	public Position(String position) {
		board = PositionParser.StringToMap(position);
	}
	
	public Position(int[] position) {
		board = position;
	}
	
	public int[] getPosition() {
		return board;
	}
	
	/**
	 * @return 0 if whites turn, 1 if blacks turn
	 */
	public int getColorToMove() {
		return board[69];
	}
	
	/**
	 * @return piece at given index
	 */
	public int getPiece(int index) {
		if(index < 0 || index >= 64) return 0;
		return board[index];
	}
	
	/**
	 * @return row of pawn that enables en passant; -1 if no en passant possible
	 */
	public int getEnPassantRow() {
		return board[64];
	}
	
	public boolean getCastle(int i) {
		return board[65 + i] != 0;
	}
	
	/**
	 * @return the position with swapped colorToMove and no en passant
	 */
	public int[] getTemplate() {
		int[] tamplate = board.clone();
		tamplate[64] = -1;
		tamplate[69] = (tamplate[69] - 1) * -1;
		return tamplate;
	}
}

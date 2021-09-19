package board;

import board.data_type.Position;

public class PositionEditor {
	
	private Position original = null;;
	private int[] editPosition = null;
	
	public PositionEditor() {
	}
	
	public void loadPosition(Position position) {
		if(original == null) {
			original = position;
			resetEdit();
		} else {
			System.err.println("Must clear old position bevor loading next.");
		}
	}
	
	public void placePiece(int piece, int square) {
		editPosition[square] = piece;
	}
	
	public void placePiece(int piece, int color, int square) {
		editPosition[square] = piece + color * 8;
	}
	
	public void enableEnPassant(int row) {
		editPosition[64] = row;
	}
	
	public void revokeCastle(int color) {
		revokeCastle(color, true);
		revokeCastle(color, false);
	}
	
	public void revokeCastle(int color, boolean left) {
		int c = left ? 65 + color * 2 : 65 + color * 2 + 1;
		editPosition[c] = 0;
	}
	
	public void resetEdit() {
		editPosition = original.getPosition().clone();
		
		//prepare for next turn
		editPosition[64] = -1;							//reset en passant
		editPosition[69] = (editPosition[69] - 1) * -1; //change color
	}
	
	public void clear() {
		original = null;
		editPosition = null;
	}
	
	public Position getPosition() {
		if(editPosition[0] != 11) editPosition[67] = 0;
		if(editPosition[7] != 11) editPosition[68] = 0;
		if(editPosition[56] != 3) editPosition[65] = 0;
		if(editPosition[63] != 3) editPosition[66] = 0;
		Position position = new Position(editPosition);
		resetEdit();
		return position;
	}
}

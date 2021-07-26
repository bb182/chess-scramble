package board;

import board.data_type.Position;

public class PositionEditior {
	
	int[] position;
	
	public PositionEditior() {
		
	}
	
	public void loadPosition(Position position) {
		if(position.equals(null)) {
			this.position = position.getPosition();
		} else {
			System.err.println("Must clear old position bevor loading next.");
		}
	}
	
	public void placePiece(int piece, int square) {
		position[square] = piece;
	}
	
	public Position getPosition() {
		return new Position(position);
	}
}

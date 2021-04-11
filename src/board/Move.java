package board;

public class Move {
	
	private int piece;
	private int startingPos;
	private int targetPos;

	public Move(int piece, int startingPos, int targetPos) {
		this.piece = piece;
		this.startingPos = startingPos;
		this.targetPos = targetPos;
	}

	public int getPiece() {
		return piece;
	}

	public int getStartingPos() {
		return startingPos;
	}

	public int getTargetPos() {
		return targetPos;
	}
}

package board.data_type;

public class Move {

	private int startingSquare;
	private int targetSquare;
	private Position startPos;
	private Position endPos;

	public Move(int startingSquare, int targetSquare, Position startPos, Position endPos) {
		this.startingSquare = startingSquare;
		this.targetSquare = targetSquare;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public int getStart() {
		return startingSquare;
	}

	public int getTarget() {
		return targetSquare;
	}

	public Position getStartPos() {
		return startPos;
	}

	public Position getEndPos() {
		return endPos;
	}
}

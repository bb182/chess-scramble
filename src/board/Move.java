package board;

public class Move {
	
	private int piece;
	private int startingPos;
	private int targetPos;
	private boolean enPassant = false;
	private boolean enPassantPossible = false;

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
	
	public boolean getEnPassant() {
		return enPassant;
	}
	
	public void setEnPassant(boolean eP) {
		enPassant = eP;
	}
	
	public boolean getEnPassantPossible() {
		return enPassantPossible;
	}
	
	public void setEnPassantPossible(boolean ePP) {
		enPassantPossible = ePP;
	}
}

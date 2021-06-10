package board.data_type;

public class Piece {
	
	final public static int NONE = 0;
	final public static int KING = 1;
	final public static int QUEEN = 2;
	final public static int ROOK = 3;
	final public static int BISHOP = 4;
	final public static int KNIGHT = 5;
	final public static int PAWN = 6;
	final public static int WHITE = 0;
	final public static int BLACK = 1;
	
	public static boolean isColor(int p, int colour) {
		if(p == NONE) return false;
		return p/8 == colour;
	}
	
	public static boolean isPiece(int p, int piece) {
		return p%8 == piece;
	}
	
	public static boolean isSlidingPiece(int p) {
		return (p%8 < 5 && p%8 > 1);
	}
}

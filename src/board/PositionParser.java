package board;

import java.util.HashMap;
import java.util.Map;

public class PositionParser {
	
	/**
	 * Loads Position (ex: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR)
	 **/
	
	private static Map<Character, Integer> pieces = new HashMap<Character, Integer>(){
		private static final long serialVersionUID = 1L;
	{
		put('k', Piece.KING+8);
		put('q', Piece.QUEEN+8);
		put('r', Piece.ROOK+8);
		put('b', Piece.BISHOP+8);
		put('n', Piece.KNIGHT+8);
		put('p', Piece.PAWN+8);
		put('K', Piece.KING);
		put('Q', Piece.QUEEN);
		put('R', Piece.ROOK);
		put('B', Piece.BISHOP);
		put('N', Piece.KNIGHT);
		put('P', Piece.PAWN);
	}};
	
	public static int[] StringToMap(String position) {
		int[] board = new int[64];
		String[] row = position.split("/");
		for (int r = 0; r < 8; r++) {
			int x = 0;
			for(char c : row[r].toCharArray()) {
				    if(Character.isDigit(c)) {
				    	for(int i = 0; i < Character.getNumericValue(c); i++) {
				    		board[x+r*8] = 0;
				    		x++;
				    	}
				    	continue;
				    }
				    board[x+r*8] = pieces.get(c);
				    x++;
			}
		}
		return board;
	}
}

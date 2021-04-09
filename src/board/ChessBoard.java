package board;

public class ChessBoard {
	
	ChessBoard.Piece[][] board;
	
	public ChessBoard() {
		board = new ChessBoard.Piece[8][8];
		loadPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
	}
	
	public ChessBoard.Piece[][] getChessBoard() {
		return board;
	}
	
	
	public void requestPiece(int x, int y) {
	}
	
	/**
	 * Loads Position (ex: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR)
	 */
	public void loadPosition(String position) {
		String[] row = position.split("/");
		for (int r = 0; r < 8; r++) {
			int x = 0;
			for(char c : row[r].toCharArray()) {
				    if(Character.isDigit(c)) {
				    	x += Character.getNumericValue(c);
				    	continue;
				    }
				    switch (c) {
				    case 'k':
				    	board[x][r] = Piece.B_KING;
				    	break;
				    case 'q':
				    	board[x][r] = Piece.B_QUEEN;
				    	break;
				    case 'r':
				    	board[x][r] = Piece.B_ROOK;
				    	break;
				    case 'b':
				    	board[x][r] = Piece.B_BISHOP;
				    	break;
				    case 'n':
				    	board[x][r] = Piece.B_KNIGHT;
				    	break;
				    case 'p':
				    	board[x][r] = Piece.B_PAWN;
				    	break;
				    case 'K':
				    	board[x][r] = Piece.W_KING;
				    	break;
				    case 'Q':
				    	board[x][r] = Piece.W_QUEEN;
				    	break;
				    case 'R':
				    	board[x][r] = Piece.W_ROOK;
				    	break;
				    case 'B':
				    	board[x][r] = Piece.W_BISHOP;
				    	break;
				    case 'N':
				    	board[x][r] = Piece.W_KNIGHT;
				    	break;
				    case 'P':
				    	board[x][r] = Piece.W_PAWN;
				    	break;
				    }
				    x++;
			}
		}
	}
	
	public static enum Piece{
		B_KING, B_QUEEN, B_ROOK, B_BISHOP, B_KNIGHT, B_PAWN,
		W_KING, W_QUEEN, W_ROOK, W_BISHOP, W_KNIGHT, W_PAWN,
		EMPTY
	}
}

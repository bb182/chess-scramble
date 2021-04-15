package board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ui.Display;

public class ChessBoard {
	
	int[] board;
	List<Move> moves = new ArrayList<>();
	Display display;
	private MoveFinder moveFinder = new MoveFinder();
	private int turn = Piece.WHITE;
	
	Set<Move> validMoves = new HashSet<>();
	
	public ChessBoard(Display display) {
		//board = PositionParser.StringToMap("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		board = PositionParser.StringToMap("r2nb2k/2p2Ppp/N2P3Q/8/8/2N3n1/1PPPPPPP/R3K3");
		this.display = display;
		display.updateBoard(board);
	}
	
	public int[] getChessBoard() {
		return board;
	}
	
	
	public void requestPiece(int square) {
		validMoves.clear();
		validMoves.addAll(moveFinder.getValidMoves(turn, board, square));
		for(Move move : validMoves) {
			display.mark(move.getTargetPos());
		}
	}

	public void releasePiece(int x) {
		for(Move move : validMoves) {
			if(move.getTargetPos() == x) {
				makeMove(move);
				turn = (turn == Piece.WHITE) ? Piece.BLACK : Piece.WHITE;
				break;
			}
		}
		display.clearMarked();
	}
	
	private void makeMove(Move move) {
		moves.add(move);
		board[move.getStartingPos()] = Piece.NONE;
		board[move.getTargetPos()] = move.getPiece();
		//en passant
		if(Piece.isPiece(move.getPiece(), Piece.PAWN)) {
			//en passant
			board[64] = move.getEnPassantPossible() ? move.getTargetPos()%8 : -1;
			if(move.getEnPassant()) board[move.getStartingPos()/8*8+move.getTargetPos()%8] = Piece.NONE;
			//TODO promotion
			if(move.getTargetPos()/8%7 == 0) board[move.getTargetPos()] = Piece.QUEEN + turn * 8;
		}
	}
}

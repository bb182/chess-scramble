package board;

import java.util.HashSet;
import java.util.Set;

import ui.Display;

public class ChessBoard {
	
	int[] board;
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
	
	
	public void requestPiece(int x) {
		validMoves.clear();
		validMoves.addAll(moveFinder.getValidMoves(turn, board, x));
		for(Move move : validMoves) {
			display.mark(move.getTargetPos());
		}
	}
	
	private Set<Integer> determineValidMoves(int x) {
		Set<Integer> validMove = new HashSet<>();

		return validMove;
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
		board[move.getStartingPos()] = Piece.NONE;
		board[move.getTargetPos()] = move.getPiece();
		//TODO promotion
		if(Piece.isPiece(move.getPiece(), Piece.PAWN) && move.getTargetPos()/8%7 == 0) board[move.getTargetPos()] = Piece.QUEEN + turn * 8;
	}
}

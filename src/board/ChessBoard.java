package board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import board.data_type.Move;
import board.data_type.Position;
import ui.Display;
import ui.MouseHandler;

public class ChessBoard {
	
	Display display = new Display();
	
	Position position;
	List<Move> history = new ArrayList<>();
	
	
	
	public ChessBoard() {
		position = new Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR:-1/1/1/1/1/0");
		//position = new Position("r2nb2k/2p2Ppp/N2P3Q/8/8/2N3n1/1PPPPPPP/RN2K3:-1/0/0/1/0/0");
		display.updateBoard(position);
		display.addMouseListener(new MouseHandler(this));
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void makeMove(Move move) {
		history.add(move);
		position = move.getEndPos();
		display.updateBoard(position);
	}
	
	public void highlightPossibilities(int square, Set<Move> moves) {
		Set<Integer> targetSquares = new HashSet<>();
		for(Move move : moves) {
			targetSquares.add(move.getTarget());
		}
		display.showPlayerCoices(square, targetSquares);
	}
	
	public void clearPossibilities() {
		display.clearMarked();
	}
}

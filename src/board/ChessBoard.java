package board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
		//runTest();
	}
	
	private void runTest() {
		int[] time = {0,0,0,0,0};
		for(int j = 0; j < 4; j++) {
			for(int i = 1; i < 6; i++) {
				long t1 = System.currentTimeMillis();
				moveCounter(position, 1, i);
				long t2 = System.currentTimeMillis();
				time[i-1] += (t2 - t1);
			}
		}
		for(int i = 1; i < 6; i++) {
			System.out.printf("depth %d took %15d ms\n", i, time[i-1]/4);
		}
	}
	
	private int moveCounter(Position position, int currentDepth, int totalDepth) {
		int counter = 0;
		Set<Move> s = MoveFinder.getMoves(position);
		if(currentDepth < totalDepth) {
			for(Move move : s) {
				counter += moveCounter(move.getEndPos(), currentDepth + 1, totalDepth);
			}
			return counter;
		}
		return s.size();
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void makeMove(Move move) {
		history.add(move);
		position = move.getEndPos();
		
		//AI
		Set<Move> s = MoveFinder.getMoves(position);
		Random rand = new Random();
		int index = rand.nextInt(s.size());
		Iterator<Move> iter = s.iterator();
		for (int i = 0; i < index; i++) {
		    iter.next();
		}
		Move m = iter.next();
		history.add(m);
		position = m.getEndPos();
		
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

package board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.IPlayer;
import board.data_type.Move;
import board.data_type.Position;
import ui.Display;
import ui.MouseHandler;

public class ChessBoard {
	
	private static Display display = new Display();
	private static IPlayer whitePlayer;
	private static IPlayer blackPlayer;
	
	private static Position position;
	private static List<Move> history = new ArrayList<>();
	
	
	
	public static void initChessBoard(IPlayer white, IPlayer black, Position initPos) {
		whitePlayer = white;
		blackPlayer = black;
		position = initPos;
		display.updateBoard(position);
		display.addMouseListener(new MouseHandler());
		//runTest();
	}
	
	private void runTest() {
		int[] time = {0,0,0,0,0};
		for(int j = 0; j < 10; j++) {
			for(int i = 1; i < 6; i++) {
				long t1 = System.currentTimeMillis();
				moveCounter(position, 1, i);
				long t2 = System.currentTimeMillis();
				time[i-1] += (t2 - t1);
			}
		}
		for(int i = 1; i < 6; i++) {
			System.out.printf("depth %d took %15d ms\n", i, time[i-1]/10);
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
	
	public static Position getPosition() {
		return position;
	}
	
	public static void makeMove(Move move) {
		history.add(move);
		position = move.getEndPos();
		display.updateBoard(position);
		
		if(position.getColorToMove() == 0) {
			whitePlayer.playerTurn(position);
		}else {
			blackPlayer.playerTurn(position);
		}
	}
	
	public static void highlightPossibilities(int square, Set<Move> moves) {
		Set<Integer> targetSquares = new HashSet<>();
		for(Move move : moves) {
			targetSquares.add(move.getTarget());
		}
		display.showPlayerCoices(square, targetSquares);
	}
	
	public static void clearPossibilities() {
		display.clearMarked();
	}
}

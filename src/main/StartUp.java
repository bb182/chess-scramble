package main;

import board.ChessBoard;
import ui.Display;
import ui.MouseHandler;

public class StartUp {

	public static void main(String[] args) {
		ChessBoard board = new ChessBoard();
		Display display = new Display();
		display.updateBoard(board.getChessBoard());
		display.addMouseListener(new MouseHandler(board));
		System.out.println("finished");
	}
}

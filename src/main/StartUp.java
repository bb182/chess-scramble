package main;

import board.ChessBoard;
import ui.Display;
import ui.MouseHandler;

public class StartUp {

	public static void main(String[] args) {
		Display display = new Display();
		ChessBoard board = new ChessBoard(display);
		display.addMouseListener(new MouseHandler(board));
		System.out.println("finished");
	}
}

package main;

import ai.*;
import board.ChessBoard;
import board.data_type.Position;

public class StartUp {

	public static void main(String[] args) {
		Position pos = new Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR:-1/1/1/1/1/0");
		//Position pos = new Position("r2nb2k/2p2Ppp/N2P3Q/8/8/2N3n1/1PPPPPPP/RN2K3:-1/0/0/1/0/0");
		
		IPlayer w = new HumanPlayer();
		IPlayer b = new HumanPlayer();
		ChessBoard.initChessBoard(w, b, pos);
		System.out.println("Setup finished");
	}
}

package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import board.ChessBoard;

public class MouseHandler extends MouseAdapter{
	
	private ChessBoard board;
	
	public MouseHandler(ChessBoard chessBoard) {
		board = chessBoard;
	}
	
    public void mousePressed(MouseEvent me) { 
    	board.requestPiece(me.getX()/64 + me.getY()/64 * 8);
    }
    
    public void mouseReleased(MouseEvent me) {
    	board.releasePiece(me.getX()/64 + me.getY()/64 * 8);
    }
}

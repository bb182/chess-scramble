package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import board.ChessBoard;
import board.MoveFinder;
import board.data_type.Move;

public class MouseHandler extends MouseAdapter{
	
	ChessBoard chessBoard;
	Set<Move> validMoves;
	
	public MouseHandler(ChessBoard board) {
		chessBoard = board;
		validMoves = new HashSet<>();
	}
	
    public void mousePressed(MouseEvent me) {
    	int square = me.getX()/64 + me.getY()/64 * 8;
    	if(mouseOnBoard()) {
    		validMoves.addAll(MoveFinder.getMoves(square, chessBoard.getPosition()));
    		chessBoard.highlightPossibilities(square, validMoves);
    	}
    }
    
    public void mouseReleased(MouseEvent me) {
    	int square = me.getX()/64 + me.getY()/64 * 8;
    	if(mouseOnBoard()) {
    		for(Move move : validMoves) {
    			if(move.getTarget() == square) {
    				chessBoard.makeMove(move);
    				break;
    			}
    		}
    		validMoves.clear();
    		chessBoard.clearPossibilities();
    	}
    }
    
    //TODO
    private boolean mouseOnBoard() {
    	return true;
    }
}

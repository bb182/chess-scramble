package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import board.ChessBoard;
import board.MoveFinder;
import board.data_type.Move;

public class MouseHandler extends MouseAdapter{
	
	Set<Move> validMoves;
	
	public MouseHandler() {
		validMoves = new HashSet<>();
	}
	
    public void mousePressed(MouseEvent me) {
    	int square = me.getX()/64 + me.getY()/64 * 8;
    	if(mouseOnBoard()) {
    		validMoves.addAll(MoveFinder.getMovesForPiece(square, ChessBoard.getPosition()));
    		ChessBoard.highlightPossibilities(square, validMoves);
    	}
    }
    
    public void mouseReleased(MouseEvent me) {
    	int square = me.getX()/64 + me.getY()/64 * 8;
    	if(mouseOnBoard()) {
    		for(Move move : validMoves) {
    			if(move.getTarget() == square) {
    				System.out.println("mouse move");
    				ChessBoard.makeMove(move);
    				break;
    			}
    		}
    		validMoves.clear();
    		ChessBoard.clearPossibilities();
    	}
    }
    
    //TODO
    private boolean mouseOnBoard() {
    	return true;
    }
}

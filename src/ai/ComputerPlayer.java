package ai;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import board.ChessBoard;
import board.MoveFinder;
import board.data_type.Move;
import board.data_type.Position;

public class ComputerPlayer implements IPlayer{

	@Override
	public void playerTurn(Position position) {
		Set<Move> s = MoveFinder.getMoves(position);
		Random rand = new Random();
		int index = rand.nextInt(s.size());
		Iterator<Move> iter = s.iterator();
		for (int i = 0; i < index; i++) {
		    iter.next();
		}
		Move m = iter.next();
		ChessBoard.makeMove(m);
		System.out.println("computer move");
	}
}

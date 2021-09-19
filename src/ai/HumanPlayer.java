package ai;

import board.data_type.Move;
import board.data_type.Position;

public class HumanPlayer implements IPlayer{

	@Override
	public void playerTurn(Position position) {
		System.out.println("player move");
	}
}

package board;

import java.util.HashSet;
import java.util.Set;

import board.data_type.Move;
import board.data_type.Piece;
import board.data_type.Position;

public class MoveFinder {
	
	private static final int[] directionOffset = new int[] {-8,8,-1,1,-9,-7,7,9};
	private static final int[] knightOffset = new int[] {-17,-15,-10,-6,15,17,6,10};
	
	public static Set<Move> getMoves(Position position) {		
		Set<Move> validMoves = new HashSet<>(); 
		for (int index = 0; index < 64; index++) {
			validMoves.addAll(getMoves(index, position));
		}
		return validMoves;
	}

	public static Set<Move> getMoves(int pieceIndex, Position position) {
		int piece = position.getPiece(pieceIndex);
		if (piece == Piece.NONE || !Piece.isColor(piece, position.getColorToMove())) return new HashSet<>();
		if (Piece.isPiece(piece, Piece.KING)) return removeMovesInCheck(getAllKingMoves(pieceIndex, position));
		if (Piece.isPiece(piece, Piece.PAWN)) return removeMovesInCheck(getAllPawnMoves(pieceIndex, position));
		return removeMovesInCheck(getAllPieceMoves(pieceIndex, position));
	}
	
	private static Set<Move> getAllPawnMoves(int pieceIndex, Position position) {
		int[] directionDistance = edgeDistance(pieceIndex);
		Set<Move> pawnMoves = new HashSet<Move>();
		int direction = (position.getColorToMove() == Piece.WHITE) ? -1 : 1;
			//move straight
			int targetIndex = pieceIndex + 8 * direction;
			if(position.getPiece(targetIndex) == Piece.NONE) {
				int[] endPos8 = position.getTemplate();
				endPos8[pieceIndex] = Piece.NONE;
				boolean promote = targetIndex/8%7 == 0;
				endPos8[targetIndex] = promote ? Piece.QUEEN  + position.getColorToMove() * 8 : Piece.PAWN  + position.getColorToMove() * 8;
				pawnMoves.add(new Move(pieceIndex, targetIndex, position, new Position(endPos8)));
				boolean firstMove = (pieceIndex/8 == 3.5 - 2.5*direction);
				targetIndex = pieceIndex + 16 * direction;
				if(firstMove && position.getPiece(targetIndex) == Piece.NONE) {
					int[] endPos16 = position.getTemplate();
					endPos16[pieceIndex] = Piece.NONE;
					endPos16[targetIndex] = Piece.PAWN  + position.getColorToMove() * 8;
					endPos16[64] = pieceIndex%8;
					pawnMoves.add(new Move(pieceIndex, targetIndex, position, new Position(endPos16)));
				}
			}
			//move diagonal
			for(int d = 2; d < 4; d++) {
				if(directionDistance[d] > 0) {
					targetIndex = pieceIndex + 8 * direction + directionOffset[d];
					if(position.getPiece(targetIndex) != Piece.NONE && !Piece.isColor(position.getPiece(targetIndex), position.getColorToMove())) {
						int[] endPos = position.getTemplate();
						endPos[pieceIndex] = Piece.NONE;
						boolean promote = targetIndex/8%7 == 0;
						endPos[targetIndex] = promote ? Piece.QUEEN  + position.getColorToMove() * 8 : Piece.PAWN + position.getColorToMove() * 8;
						pawnMoves.add(new Move(pieceIndex, targetIndex, position, new Position(endPos)));
					}
				}
			}
			//move en passant
			int distanceToEdge = (direction+1)/2 * 7 - direction * pieceIndex/8;
			if(position.getEnPassantRow() != -1 && distanceToEdge == 3) {
				if(position.getEnPassantRow() == pieceIndex%8 - 1 || position.getEnPassantRow() == pieceIndex%8 + 1 ) {
					targetIndex = position.getEnPassantRow() + pieceIndex - pieceIndex%8 + direction*8;
					int[] endPos = position.getTemplate();
					endPos[pieceIndex] = Piece.NONE;
					endPos[targetIndex - (position.getColorToMove() * 2 - 1 ) * 8] = Piece.NONE;
					endPos[targetIndex] = Piece.PAWN  + position.getColorToMove() * 8;
					pawnMoves.add(new Move(pieceIndex, targetIndex, position, new Position(endPos)));
				}
			}
		return pawnMoves;
	}
	
	private static Set<Move> getAllPieceMoves(int pieceIndex, Position position){
		Set<Move> slidingMoves = new HashSet<Move>();
		int piece = position.getPiece(pieceIndex);
		Set<Integer> targets = Piece.isPiece(piece, Piece.KNIGHT) ? getAllKnightTargets(pieceIndex)
				: getSlidingTargets(pieceIndex, position, !Piece.isPiece(piece, Piece.BISHOP), !Piece.isPiece(piece, Piece.ROOK));
		for(int t : targets) {
			if(Piece.isColor(position.getPiece(t), position.getColorToMove())) continue;
			int[] endPos = position.getTemplate();
			endPos[pieceIndex] = Piece.NONE;
			endPos[t] = piece;
			slidingMoves.add(new Move(pieceIndex, t, position, new Position(endPos)));
		}
		return slidingMoves;
	}

	private static Set<Integer> getAllKnightTargets(int pieceIndex){
		Set<Integer> targets = new HashSet<>();
		int[] directionDistance = edgeDistance(pieceIndex);
		
		for(int i = 0; i < 8; i++) {
			if(directionDistance[i/4] > (i/2+1)%2 && directionDistance[i%2+2] > (i/2)%2){
				targets.add(pieceIndex + knightOffset[i]);
			}
		}
		return targets;
	}
	
	private static Set<Integer> getSlidingTargets(int pieceIndex, Position position, boolean straight, boolean diagonal){
		Set<Integer> targets = new HashSet<>();
		int[] directionDistance = edgeDistance(pieceIndex);
		
		int s = straight ? 0 : 4;
		int d = diagonal ? 8 : 4;
		for (int dir = s; dir < d; dir++) {
			for (int step = 1; step <= directionDistance[dir]; step++) {
				int target = pieceIndex + step * directionOffset[dir];
				if (position.getPiece(target) == Piece.NONE) {
					targets.add(target);
					continue;
				}
				targets.add(target);
				break;
			}
		}
		return targets;
	}

	private static Set<Move> getAllKingMoves(int pieceIndex, Position position) {
		Set<Move> kingMoves = new HashSet<Move>();
		for(int t : getKingTargets(pieceIndex)){
			if(Piece.isColor(position.getPiece(t), position.getColorToMove())) continue;
			int[] endPos = position.getTemplate();
			endPos[t] = position.getPiece(pieceIndex);
			endPos[pieceIndex] = Piece.NONE;
			endPos[68 - position.getColorToMove()] = 0;
			endPos[67 - position.getColorToMove()] = 0;
			kingMoves.add(new Move(pieceIndex, t, position, new Position(endPos)));
		}
		//castle
		for(int side = 0; side < 2; side++) {
			int color = position.getColorToMove() * 2 - 1;
			if(position.getCastle(side +2 - position.getColorToMove() * 2)){
				if(inCheck(pieceIndex + 0 * color, position, position.getColorToMove())) continue;
				if(inCheck(pieceIndex + 1 * color, position, position.getColorToMove())) continue;
				if(inCheck(pieceIndex + 2 * color, position, position.getColorToMove())) continue;
				int empty = 0;
				for(int i = pieceIndex%8 + color; i%7 > 0; i+=color) {
					empty += position.getPiece(pieceIndex/8*8 + i);
				}
				if(empty > 0) continue;
				int targetPosition = pieceIndex - 2 + side * 4;
				int[] endPos = position.getTemplate();
				endPos[targetPosition] = position.getPiece(pieceIndex);
				endPos[pieceIndex - 1 + side * 2] = Piece.ROOK  + position.getColorToMove() * 8;
				endPos[pieceIndex/8*8 + side*7] = Piece.NONE;
				endPos[pieceIndex] = Piece.NONE;
				endPos[68 - position.getColorToMove()] = 0;
				endPos[67 - position.getColorToMove()] = 0;
				kingMoves.add(new Move(pieceIndex, targetPosition, position, new Position(endPos)));
			}
		}
		return kingMoves;
	}
	
	private static Set<Integer> getKingTargets(int pieceIndex){
		Set<Integer> targets = new HashSet<>();
		int[] directionDistance = edgeDistance(pieceIndex);
		for (int dir = 0; dir < 8; dir++) {
			if(directionDistance[dir] > 0) {
				targets.add(pieceIndex + directionOffset[dir]);
			}
		}
		return targets;
	}
	
	private static Set<Move> removeMovesInCheck(Set<Move> moves) {
		Set<Move> clean = new HashSet<>();
		for(Move move : moves) {
			for (int i = 0; i < 64; i++) {
				int piece = move.getEndPos().getPiece(i);
				if(Piece.isPiece(piece, Piece.KING) && Piece.isColor(piece, move.getStartPos().getColorToMove())){
					if(!inCheck(i, move.getEndPos(), move.getStartPos().getColorToMove())) clean.add(move);
				}
			}
		}
		return clean;
	}
	
	private static boolean inCheck(int pieceIndex, Position position, int colourOfKing) {
		for(int t : getAllKnightTargets(pieceIndex)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if(Piece.isPiece(position.getPiece(t), Piece.KNIGHT)) return true;
		}
		
		for(int t : getSlidingTargets(pieceIndex, position, false, true)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if((Piece.isPiece(position.getPiece(t), Piece.BISHOP) || Piece.isPiece(position.getPiece(t), Piece.QUEEN))) return true;
		}
		
		for(int t : getSlidingTargets(pieceIndex, position, true, false)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if((Piece.isPiece(position.getPiece(t), Piece.ROOK) || Piece.isPiece(position.getPiece(t), Piece.QUEEN))) return true;
		}
		
		for(int t : getKingTargets(pieceIndex)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if(Piece.isPiece(position.getPiece(t), Piece.KING)) return true;
		}
		
		int t = pieceIndex + 7 * (colourOfKing * 2 - 1);
		if(Piece.isPiece(position.getPiece(t), Piece.PAWN) && !Piece.isColor(position.getPiece(t), colourOfKing)) return true;
		t = pieceIndex + 9 * (position.getColorToMove() * 2 - 1);
		if(Piece.isPiece(position.getPiece(t), Piece.PAWN) && !Piece.isColor(position.getPiece(t), colourOfKing)) return true;
		return false;
	}
	
	private static int[] edgeDistance(int i) {
		//TODO lookup for all squares
		int[] directionDistance = new int[8];
		directionDistance[0] = i/8; //N
		directionDistance[1] = 7-i/8; //S
		directionDistance[2] = i%8; //W
		directionDistance[3] = 7-i%8; //E
		
		directionDistance[4] = Math.min(directionDistance[0], directionDistance[2]); //NW
		directionDistance[5] = Math.min(directionDistance[0], directionDistance[3]); //NE
		directionDistance[6] = Math.min(directionDistance[1], directionDistance[2]); //SW
		directionDistance[7] = Math.min(directionDistance[1], directionDistance[3]); //SE
		
		return directionDistance;
	}
}

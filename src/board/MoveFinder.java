package board;

import java.util.HashSet;
import java.util.Set;

import board.data_type.Move;
import board.data_type.Piece;
import board.data_type.Position;

public class MoveFinder {
	
	private static final int[] directionOffset = new int[] {-8,8,-1,1,-9,-7,7,9};
	private static final int[] knightOffset = new int[] {-17,-15,-10,-6,15,17,6,10};
	private static final PositionEditor posEditor = new PositionEditor();
	private static final int[][] distanceDir = creatDistanceDir();
	
	public static Set<Move> getMoves(Position position) {		
		Set<Move> validMoves = new HashSet<>(); 
		
		// find king moves
		// find pins
		// if check/resolve
		// else all legal
		
		for (int index = 0; index < 64; index++) {
			validMoves.addAll(getMovesForPiece(index, position));
		}
		return validMoves;
	}

	public static Set<Move> getMovesForPiece(int pieceIndex, Position position) {
		posEditor.loadPosition(position);
		int piece = position.getPiece(pieceIndex);
		if (piece == Piece.NONE || !Piece.isColor(piece, position.getColorToMove())) {
			posEditor.clear();
			return new HashSet<>();
		}
		if (Piece.isPiece(piece, Piece.KING)) return removeMovesInCheck(getAllKingMoves(pieceIndex, position));
		if (Piece.isPiece(piece, Piece.PAWN)) return removeMovesInCheck(getAllPawnMoves(pieceIndex, position));
		return removeMovesInCheck(getAllPieceMoves(pieceIndex, position));
	}
	
	private static Set<Move> getAllPawnMoves(int pieceIndex, Position position) {
		Set<Move> pawnMoves = new HashSet<Move>();
		int direction = (position.getColorToMove() == Piece.WHITE) ? -1 : 1;
		//move straight
		int targetIndex = pieceIndex + 8 * direction;
		if(position.getPiece(targetIndex) == Piece.NONE) {
			posEditor.placePiece(Piece.NONE, pieceIndex);
			boolean promote = targetIndex/8%7 == 0;
			if(promote) {
				// TODO promotion
				posEditor.placePiece( Piece.QUEEN, position.getColorToMove(), targetIndex);
			} else {
				posEditor.placePiece( Piece.PAWN, position.getColorToMove(), targetIndex);
			}
			pawnMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
			boolean firstMove = (pieceIndex/8 == 3.5 - 2.5*direction);
			targetIndex = pieceIndex + 16 * direction;
			if(firstMove && position.getPiece(targetIndex) == Piece.NONE) {
				posEditor.placePiece(Piece.NONE, pieceIndex);
				posEditor.placePiece( Piece.PAWN, position.getColorToMove(), targetIndex);
				posEditor.enableEnPassant(pieceIndex%8);
				pawnMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
			}
		}
		//move diagonal
		for(int d = 2; d < 4; d++) {
			if(distanceDir[pieceIndex][d] > 0) {
				targetIndex = pieceIndex + 8 * direction + directionOffset[d];
				if(position.getPiece(targetIndex) != Piece.NONE && !Piece.isColor(position.getPiece(targetIndex), position.getColorToMove())) {
					posEditor.placePiece(Piece.NONE, pieceIndex);
					boolean promote = targetIndex/8%7 == 0;
					if(promote) {
						// TODO promotion
						posEditor.placePiece( Piece.QUEEN, position.getColorToMove(), targetIndex);
					} else {
						posEditor.placePiece( Piece.PAWN, position.getColorToMove(), targetIndex);
					}
					pawnMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
				}
			}
		}
		//move en passant
		int distanceToEdge = (direction+1)/2 * 7 - direction * pieceIndex/8;
		if(position.getEnPassantRow() != -1 && distanceToEdge == 3) {
			if(position.getEnPassantRow() == pieceIndex%8 - 1 || position.getEnPassantRow() == pieceIndex%8 + 1 ) {
				targetIndex = position.getEnPassantRow() + pieceIndex - pieceIndex%8 + direction*8;
				posEditor.placePiece(Piece.NONE, pieceIndex);
				posEditor.placePiece(Piece.NONE, targetIndex - (position.getColorToMove() * 2 - 1 ) * 8);
				posEditor.placePiece( Piece.PAWN, position.getColorToMove(), targetIndex);
				pawnMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
			}
		}
		posEditor.clear();
		return pawnMoves;
	}
	
	private static Set<Move> getAllPieceMoves(int pieceIndex, Position position){
		Set<Move> slidingMoves = new HashSet<Move>();
		int piece = position.getPiece(pieceIndex);
		Set<Integer> targets = Piece.isPiece(piece, Piece.KNIGHT) ? getAllKnightTargets(pieceIndex)
				: getSlidingTargets(pieceIndex, position, !Piece.isPiece(piece, Piece.BISHOP), !Piece.isPiece(piece, Piece.ROOK));
		for(int targetIndex : targets) {
			if(Piece.isColor(position.getPiece(targetIndex), position.getColorToMove())) continue;
			posEditor.placePiece(Piece.NONE, pieceIndex);
			posEditor.placePiece(piece, targetIndex);
			slidingMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
		}
		posEditor.clear();
		return slidingMoves;
	}

	private static Set<Integer> getAllKnightTargets(int pieceIndex){
		Set<Integer> targets = new HashSet<>();
		
		for(int i = 0; i < 8; i++) {
			if(distanceDir[pieceIndex][i/4] > (i/2+1)%2 && distanceDir[pieceIndex][i%2+2] > (i/2)%2){
				targets.add(pieceIndex + knightOffset[i]);
			}
		}
		return targets;
	}
	
	private static Set<Integer> getSlidingTargets(int pieceIndex, Position position, boolean straight, boolean diagonal){
		Set<Integer> targets = new HashSet<>();
		
		int s = straight ? 0 : 4;
		int d = diagonal ? 8 : 4;
		for (int dir = s; dir < d; dir++) {
			for (int step = 1; step <= distanceDir[pieceIndex][dir]; step++) {
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
		for(int targetIndex : getKingTargets(pieceIndex)){
			if(Piece.isColor(position.getPiece(targetIndex), position.getColorToMove())) continue;
			//int[] endPos = position.getTemplate();
			posEditor.placePiece(Piece.NONE, pieceIndex);
			posEditor.placePiece(position.getPiece(pieceIndex), targetIndex);
			posEditor.revokeCastle(position.getColorToMove());
			kingMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
		}
		//castle
		for(int side = 0; side < 2; side++) {
			int color = position.getColorToMove() * 2 - 1;
			
			// slide 0  /  1
			// color -1
			// pieceIndex 60 / 4
			
			if(position.getCastle(position.getColorToMove(), side == 0)){
				if(inCheck(pieceIndex + 0 * color, position, position.getColorToMove())) continue;
				if(inCheck(pieceIndex + 1 * color, position, position.getColorToMove())) continue;
				if(inCheck(pieceIndex + 2 * color, position, position.getColorToMove())) continue;
				int empty = 0;
				for(int i = pieceIndex%8 + (side * 2 -1); i%7 > 0; i+= (side * 2 -1)) {
					empty += position.getPiece(pieceIndex/8*8 + i);
				}
				if(empty > 0) continue;
				int targetIndex = pieceIndex - 2 + side * 4;
				posEditor.placePiece(Piece.NONE, pieceIndex);
				posEditor.placePiece(Piece.NONE, pieceIndex/8*8 + side*7);
				posEditor.placePiece(position.getPiece(pieceIndex), targetIndex);
				posEditor.placePiece(Piece.ROOK, position.getColorToMove(), pieceIndex - 1 + side * 2);
				posEditor.revokeCastle(position.getColorToMove());
				kingMoves.add(new Move(pieceIndex, targetIndex, position, posEditor.getPosition()));
			}
		}
		posEditor.clear();
		return kingMoves;
	}
	
	private static Set<Integer> getKingTargets(int pieceIndex){
		Set<Integer> targets = new HashSet<>();
		for (int dir = 0; dir < 8; dir++) {
			if(distanceDir[pieceIndex][dir] > 0) {
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
	
	private static boolean inCheck(int kingIndex, Position position, int colourOfKing) {
		for(int t : getAllKnightTargets(kingIndex)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if(Piece.isPiece(position.getPiece(t), Piece.KNIGHT)) return true;
		}
		
		for(int t : getSlidingTargets(kingIndex, position, false, true)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if((Piece.isPiece(position.getPiece(t), Piece.BISHOP) || Piece.isPiece(position.getPiece(t), Piece.QUEEN))) return true;
		}
		
		for(int t : getSlidingTargets(kingIndex, position, true, false)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if((Piece.isPiece(position.getPiece(t), Piece.ROOK) || Piece.isPiece(position.getPiece(t), Piece.QUEEN))) return true;
		}
		
		for(int t : getKingTargets(kingIndex)) {
			if(Piece.isColor(position.getPiece(t), colourOfKing)) continue;
			if(Piece.isPiece(position.getPiece(t), Piece.KING)) return true;
		}
		
		int t = kingIndex + 7 * (colourOfKing * 2 - 1);
		if(Piece.isPiece(position.getPiece(t), Piece.PAWN) && !Piece.isColor(position.getPiece(t), colourOfKing)) {
			return true;
		}
		t = kingIndex + 9 * (colourOfKing * 2 - 1);
		if(Piece.isPiece(position.getPiece(t), Piece.PAWN) && !Piece.isColor(position.getPiece(t), colourOfKing)) {
			return true;
		}
		return false;
	}
	
	private static int[][] creatDistanceDir() {
		int[][] dir = new int[64][];
		for(int i = 0; i < 64; i++) {
			int[] distance = new int[8];
			distance[0] = i/8; //N
			distance[1] = 7-i/8; //S
			distance[2] = i%8; //W
			distance[3] = 7-i%8; //E
			
			distance[4] = Math.min(distance[0], distance[2]); //NW
			distance[5] = Math.min(distance[0], distance[3]); //NE
			distance[6] = Math.min(distance[1], distance[2]); //SW
			distance[7] = Math.min(distance[1], distance[3]); //SE
			dir[i] = distance;
		}
		return dir;
	}
}

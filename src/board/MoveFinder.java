package board;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MoveFinder {
	
	private int[] directionDistance = new int[8];
	private boolean[] knightOnBoard = new boolean[8];
	private int[] directionOffset = new int[] {-8,8,-1,1,-9,-7,7,9};
	private int[] knightOffset = new int[] {-17,-15,-10,-6,15,17,6,10};
	
	private int colourTurn;
	private int[] position;
	
	public MoveFinder() {
	}
	
	public Set<Move> getValidMoves(int colourToMove, int[] board, int index){
		colourTurn = colourToMove;
		position = board;
		return getAllMovesForPiece(index, position[index]);
	}
	
	public Set<Move> getAllValidMoves(int colourToMove, int[] board) {
		colourTurn = colourToMove;
		position = board;
		
		Set<Move> validMoves = new HashSet<>(); 
		for (int index = 0; index < 64; index++) {
			int piece = position[index];
			validMoves.addAll(getAllMovesForPiece(index, piece));
		}
		return validMoves;
	}

	private Set<Move> getAllMovesForPiece(int pieceIndex, int piece) {
		if (piece == Piece.NONE || !Piece.isColour(piece, colourTurn)) return new HashSet<>();
		updateEdgeDistance(pieceIndex);
		if (Piece.isPiece(piece, Piece.KING)) return getAllKingMoves(pieceIndex, piece);
		else if (Piece.isPiece(piece, Piece.KNIGHT)) return getAllKnightMoves(pieceIndex, piece);
		else if (Piece.isPiece(piece, Piece.PAWN)) return getAllPawnMoves(pieceIndex, piece);
		else return getAllSlidingMoves(pieceIndex, piece);
	}
	
	private Set<Move> getAllPawnMoves(int pieceIndex, int piece) {
		Set<Move> pawnMoves = new HashSet<Move>();
		int direction = (colourTurn == Piece.WHITE) ? -1 : 1;
			//move straight
			if(position[pieceIndex + 8 * direction] == Piece.NONE) {
				pawnMoves.add(new Move(piece, pieceIndex, pieceIndex + 8 * direction));
				boolean firstMove = (pieceIndex/8 == 3.5 - 2.5*direction);
				if(firstMove && position[pieceIndex + 16 * direction] == Piece.NONE) {
					Move m = new Move(piece, pieceIndex, pieceIndex + 16 * direction);
					m.setEnPassantPossible(true);
					pawnMoves.add(m);
				}
			}
			//move diagonal
			for(int d = 2; d < 4; d++) {
				if(directionDistance[d] > 0) {
					int targetIndex = pieceIndex + 8 * direction + directionOffset[d];
					if(position[targetIndex] != Piece.NONE && !Piece.isColour(position[targetIndex], colourTurn)) {
						pawnMoves.add(new Move(piece, pieceIndex, targetIndex));
					}
				}
			}
			//move en passant
			int distanceToEdge = (direction+1)/2 * 7 - direction * pieceIndex/8;
			if(position[64] != -1 && distanceToEdge == 3) {
				if(position[64] == pieceIndex%8 - 1 || position[64] == pieceIndex%8 + 1 ) {
					Move m = new Move(piece, pieceIndex, position[64] + pieceIndex - pieceIndex%8 + direction*8);
					m.setEnPassant(true);
					pawnMoves.add(m);
				}
			}
		return pawnMoves;
	}

	private Set<Move> getAllKnightMoves(int pieceIndex, int piece) {
		Set<Move> knightMoves = new HashSet<Move>();
		for(int i = 0; i < 8; i++) {
			if(knightOnBoard[i] && !Piece.isColour(position[pieceIndex + knightOffset[i]], colourTurn)) {
				knightMoves.add(new Move(piece, pieceIndex, pieceIndex + knightOffset[i]));
			}
		}
		return knightMoves;
	}

	private Set<Move> getAllSlidingMoves(int piecePosition, int piece) {
		Set<Move> slidingMoves = new HashSet<Move>();
		int startDirIndex = Piece.isPiece(piece, Piece.BISHOP) ? 4 : 0;
		int endDirIndex = Piece.isPiece(piece, Piece.ROOK) ? 4 : 8;
		for (int directionIndex = startDirIndex; directionIndex < endDirIndex; directionIndex++) {
			for (int step = 1; step <= directionDistance[directionIndex]; step++) {
				int targetPosition = piecePosition + step * directionOffset[directionIndex];
				if (position[targetPosition] == Piece.NONE) {
					slidingMoves.add(new Move(piece, piecePosition, targetPosition));
					continue;
				}
				if (!Piece.isColour(position[targetPosition], colourTurn)) {
					slidingMoves.add(new Move(piece, piecePosition, targetPosition));
				}
				break;
			}
		}
		return slidingMoves;
	}

	private Set<Move> getAllKingMoves(int piecePosition, int piece) {
		Set<Move> kingMoves = new HashSet<Move>();
		for (int directionIndex = 0; directionIndex < 8; directionIndex++) {
			int targetPosition = piecePosition + directionOffset[directionIndex];
			if(directionDistance[directionIndex] > 0 && 
					(position[targetPosition] == Piece.NONE || !Piece.isColour(position[targetPosition], colourTurn))) {
				kingMoves.add(new Move(piece, piecePosition, targetPosition));
			}
		}
		return kingMoves;
	}
	
	private void updateEdgeDistance(int i) {
		//TODO lookup for all squares
		directionDistance[0] = i/8; //N
		directionDistance[1] = 7-i/8; //S
		directionDistance[2] = i%8; //W
		directionDistance[3] = 7-i%8; //E
		
		directionDistance[4] = Math.min(directionDistance[0], directionDistance[2]); //NW
		directionDistance[5] = Math.min(directionDistance[0], directionDistance[3]); //NE
		directionDistance[6] = Math.min(directionDistance[1], directionDistance[2]); //SW
		directionDistance[7] = Math.min(directionDistance[1], directionDistance[3]); //SE
		
		knightOnBoard[0] = directionDistance[0] > 1 && directionDistance[2] > 0;
		knightOnBoard[1] = directionDistance[0] > 1 && directionDistance[3] > 0;
		knightOnBoard[2] = directionDistance[0] > 0 && directionDistance[2] > 1;
		knightOnBoard[3] = directionDistance[0] > 0 && directionDistance[3] > 1;
		knightOnBoard[4] = directionDistance[1] > 1 && directionDistance[2] > 0;
		knightOnBoard[5] = directionDistance[1] > 1 && directionDistance[3] > 0;
		knightOnBoard[6] = directionDistance[1] > 0 && directionDistance[2] > 1;
		knightOnBoard[7] = directionDistance[1] > 0 && directionDistance[3] > 1;
	}
}

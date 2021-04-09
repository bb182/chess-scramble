package ui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import board.ChessBoard;
import board.ChessBoard.Piece;

public class Display extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private ChessBoard.Piece[][] board = new ChessBoard.Piece[8][8];
	private BufferedImage boardImg = null;
	private BufferedImage highlightImg = null;
	private Map<ChessBoard.Piece, BufferedImage> sprites = new HashMap<>();
	
	private int observedSquare = -1;
	
	public Display() {
		loadImgs();
		
		JFrame frame = buildFrame();

	    frame.add(this);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(boardImg, 0, 0, null);
        
        if(observedSquare >= 0) {
        	g.drawImage(highlightImg, observedSquare%8*64, observedSquare/8*64, null);
        }
        
        for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				ChessBoard.Piece piece = board[x][y];
				if(piece != null) {
					g.drawImage(sprites.get(piece), x*64, y*64, null);
				}
			}
		}
    }
	
	public void updateBoard(ChessBoard.Piece[][] ChessBoard) {
		board = ChessBoard;
	}
	
	private void loadImgs() {
		try {
		    boardImg = ImageIO.read(new File("res/sprites/Board.png"));
		    BufferedImage pieces = ImageIO.read(new File("res/sprites/Pieces.png"));
		    highlightImg = pieces.getSubimage(192, 0, 64, 64);
		    sprites.put(ChessBoard.Piece.B_KING, pieces.getSubimage(0, 0, 64, 64));
		    sprites.put(ChessBoard.Piece.B_QUEEN, pieces.getSubimage(64, 0, 64, 64));
		    sprites.put(ChessBoard.Piece.B_ROOK, pieces.getSubimage(128, 0, 64, 64));
		    sprites.put(ChessBoard.Piece.B_BISHOP, pieces.getSubimage(0, 64, 64, 64));
		    sprites.put(ChessBoard.Piece.B_KNIGHT, pieces.getSubimage(64, 64, 64, 64));
		    sprites.put(ChessBoard.Piece.B_PAWN, pieces.getSubimage(128, 64, 64, 64));
		    sprites.put(ChessBoard.Piece.W_KING, pieces.getSubimage(0, 128, 64, 64));
		    sprites.put(ChessBoard.Piece.W_QUEEN, pieces.getSubimage(64, 128, 64, 64));
		    sprites.put(ChessBoard.Piece.W_ROOK, pieces.getSubimage(128, 128, 64, 64));
		    sprites.put(ChessBoard.Piece.W_BISHOP, pieces.getSubimage(0, 192, 64, 64));
		    sprites.put(ChessBoard.Piece.W_KNIGHT, pieces.getSubimage(64, 192, 64, 64));
		    sprites.put(ChessBoard.Piece.W_PAWN, pieces.getSubimage(128, 192, 64, 64));
		} catch (IOException e) {
			System.err.println("Faild to read PNG!");
		}
	}


	private static JFrame buildFrame() {
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setSize(64*8, 64*8);
	    frame.setVisible(true);
	    return frame;
	}

}
